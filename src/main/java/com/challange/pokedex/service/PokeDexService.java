package com.challange.pokedex.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.challange.pokedex.pojo.PokeMonResult;
import com.challange.pokedex.pojo.Pokemon;

/**
 * This service contains the Business logic required for this application
 * 
 * @author nawaz
 *
 */
@Service
public class PokeDexService {

	Logger logger = LoggerFactory.getLogger(PokeDexService.class);

	// one instance, reuse
	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	/**
	 * Public URI to get the Pokemon information
	 */
	@Value("${pokemon.uri}")
	String uri;

	/**
	 * Public URI to get the yoda translation
	 */
	@Value("${yoda.translate.uri}")
	String yodaTranslateUri;
	/**
	 * Public URI to get the shakespeare translation
	 */
	@Value("${shakespeare.translate.uri}")
	String shakespeareTranslateUri;
	
	/**
	 * Public URI to get the shakespeare translation
	 */
	@Value("${endpoint.pokemonspecies}")
	String endpointPokemonSpecies;

	/**
	 * This method is responsible for fetching the required details from the
	 * pokeapi.
	 * 
	 * URI to be called is configured in the application.properties file using the
	 * property pokemon.uri
	 * 
	 * an HTTP connection is made to the URL.
	 * 
	 * Data will be read from the response of the API call and set to an object of
	 * the Pokemon class
	 * 
	 * 
	 * @param name - Name of the Pokemon
	 * @return - an Object of the class {@link PokeMonResult}
	 */
	public PokeMonResult getBasicPokeMonInformation(String name) { 
		PokeMonResult pokeMonResult = new PokeMonResult();
		Pokemon pokemon = new Pokemon();
		StringBuffer bufferName = new StringBuffer(uri);
		bufferName.append(endpointPokemonSpecies);
		bufferName.append(name);

		try {

			URL url = new URL(bufferName.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			// Getting the response code
			int responsecode = conn.getResponseCode();

			if (responsecode != 200) {
				logger.error("Response Code is: " + responsecode);
				pokeMonResult.setResponseCode(responsecode);
			} else {

				String inline = "";
				Scanner scanner = new Scanner(url.openStream());

				// Write all the JSON data into a string using a scanner
				while (scanner.hasNext()) {
					inline += scanner.nextLine();
				}

				// Close the scanner
				scanner.close();

				// Using the JSON simple library parse the string into a json object
				JSONParser parse = new JSONParser();
				JSONObject data_obj = (JSONObject) parse.parse(inline);

				pokemon.setName(name);

				// Get the required object from the above created object
				Object objId = data_obj.get("id");

				Long id = null;
				if (objId != null) {
					id = (Long) objId;
				}
				logger.info("Id: " + id);
				
				Object objIsLegendary = data_obj.get("is_legendary");

				Boolean isLegendary = false;
				if (objIsLegendary != null) {
					isLegendary = (Boolean) objIsLegendary;
				}
				pokemon.setIsLegendary(isLegendary);

				Object objHabitat = data_obj.get("habitat");
				JSONObject habitat = null;
				if (objHabitat != null) {
					habitat = (JSONObject) objHabitat;
				}
				if (habitat != null && habitat.containsKey("name") && habitat.get("name") != null)
					pokemon.setHabitat((String) habitat.get("name"));

				JSONArray arr = (JSONArray) data_obj.get("flavor_text_entries");

				for (int i = 0; i < arr.size(); i++) {

					JSONObject new_obj = (JSONObject) arr.get(i);

					if (new_obj != null && new_obj.get("language") != null) {
						JSONObject languageObj = (JSONObject) new_obj.get("language");
						Object languageName = languageObj.get("name");
						String strLanguage = null;
						if (languageName != null) {
							strLanguage = (String) languageName;
							logger.info(strLanguage);
							if ("en".equals(strLanguage)) {
								if (new_obj.get("flavor_text") != null) {
									String strDesc = (String) new_obj.get("flavor_text");
									logger.info("Description: " + strDesc);
									pokemon.setStandardDescription(strDesc);
								}
								break;
							}
						}

					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		pokeMonResult.setPokemon(pokemon);
		return pokeMonResult;
	}

	/**
	 * This method calls the method getBasicPokeMonInformation method to get the
	 * Pokemon Information then calls the getTranslatedDescription to get the
	 * required translation
	 * 
	 * @param name - Name of the Pokemon for which the information along with
	 *             translation should be fetched
	 * @return - an Object of the {@link PokeMonResult} class
	 */
	public PokeMonResult getPokeMonInformationWithFunTranslation(String name) {
		PokeMonResult pokemonResult = getBasicPokeMonInformation(name);
		Pokemon pokemon = pokemonResult.getPokemon();
		if ("cave".equals(pokemon.getHabitat()) || pokemon.getIsLegendary() != null && pokemon.getIsLegendary()) {
			getTranslatedDescription(pokemon, "yoda", pokemonResult);

		} else if (pokemon.getIsLegendary() != null) {
			getTranslatedDescription(pokemon, "shakespeare",pokemonResult);

		}
		pokemonResult.setPokemon(pokemon);
		return pokemonResult;
	}

	/**
	 * This method calls Yoda Transaction endpoint if type passed is yoda otherwise
	 * Shakespeare endpoint and overrides the value of the Standard Description in
	 * the passed pokemon object
	 * 
	 * 
	 * @param pokemon - {@link Pokemon} Object with already fetched details
	 * @param type    - yoda or shakespeare
	 * @param pokemonResult    - an Object of the PokeMonResult
	 */
	private void getTranslatedDescription(Pokemon pokemon, String type, PokeMonResult pokemonResult) {

		String uri;

		if ("yoda".equals(type)) {
			uri = yodaTranslateUri;
		} else {
			uri = shakespeareTranslateUri;

		}
		// Apply Yoda Transaction
		Map<Object, Object> data = new HashMap<>();
		data.put("text", pokemon.getStandardDescription());

		HttpRequest request = HttpRequest.newBuilder().POST(buildFormDataFromMap(data)).uri(URI.create(uri))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
				.header("Content-Type", "application/x-www-form-urlencoded").build();

		HttpResponse<String> response;
		try {

			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			// print status code
			logger.info("Response Code: " + response.statusCode());
			pokemonResult.setResponseCode(response.statusCode());
			// print response body
			logger.info("Response Body: " + response.body());
			JSONParser parse = new JSONParser();
			try {
				JSONObject data_obj = (JSONObject) parse.parse(response.body());
				if (data_obj != null) {
					if (data_obj.containsKey("contents")) {
						JSONObject jsonObj = (JSONObject) data_obj.get("contents");
						if (jsonObj.containsKey("translated")) {
							String translated = (String) jsonObj.get("translated");
							pokemon.setStandardDescription(translated);
							pokemonResult.setPokemon(pokemon);
						}
					}

				}
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}

		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
		var builder = new StringBuilder();
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			if (builder.length() > 0) {
				builder.append("&");
			}
			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}
}
