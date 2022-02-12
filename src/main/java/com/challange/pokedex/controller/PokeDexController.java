package com.challange.pokedex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.challange.pokedex.pojo.PokeMonResult;
import com.challange.pokedex.pojo.Pokemon;
import com.challange.pokedex.service.PokeDexService;

/**
 * This Controller handles all the requests related to Pokedex application
 * 
 * @author nawaz
 *
 */
@RestController
@CrossOrigin
public class PokeDexController {

	Logger logger = LoggerFactory.getLogger(PokeDexController.class);

	@Autowired
	PokeDexService pokeDexService;

	/**
	 * This endpoint is called to fetch the Pokemon information
	 * 
	 * @param name - Name of the Pokemon e.g. Pikachu
	 * @return - A ResponseEntity<Pokemon> Object
	 */
	@GetMapping("/pokemon/{name}")
	public ResponseEntity<Pokemon> getPokemonInformationByName(@PathVariable("name") String name) {
		// TODO:Call Pokemon APIs to get the required information

		PokeMonResult pokemonResult = pokeDexService.getBasicPokeMonInformation(name);
		int status = 200;
		if(pokemonResult != null && pokemonResult.getResponseCode() != null)
		{
			status = pokemonResult.getResponseCode();
		}
		return new ResponseEntity<>(pokemonResult.getPokemon(), HttpStatus.valueOf(status));

	}

	/**
	 * This end point will be called to fetch the Pokemon Information along with
	 * Yoda or Shakespeare translation
	 * 
	 * @param name - Name of the Pokemon e.g. Pikachu
	 * @return - A ResponseEntity<Pokemon> Object
	 */
	@GetMapping("/pokemon/translated/{name}")
	public ResponseEntity<Pokemon> getPokemonInformationFunTrans(@PathVariable("name") String name) {
		// TODO:Call Pokemon APIs to get the required information and translate them
		PokeMonResult pokemonResult = pokeDexService.getPokeMonInformationWithFunTranslation(name);
		int status = 200;
		if(pokemonResult != null && pokemonResult.getResponseCode() != null)
		{
			status = pokemonResult.getResponseCode();
		}
		return new ResponseEntity<>(pokemonResult.getPokemon(), HttpStatus.valueOf(status));

	}
}
