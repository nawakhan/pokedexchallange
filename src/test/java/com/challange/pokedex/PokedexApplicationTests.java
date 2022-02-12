package com.challange.pokedex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challange.pokedex.pojo.PokeMonResult;
import com.challange.pokedex.pojo.Pokemon;
import com.challange.pokedex.service.PokeDexService;

@SpringBootTest
class PokedexApplicationTests {
	@Autowired
	PokeDexService pokeDexService;

	@Test
	void contextLoads() {
	}

	@Test
	void testInvalidPokemon() {
		PokeMonResult pokeMonResult = pokeDexService.getBasicPokeMonInformation("Nawazish");
		Pokemon pokemon = pokeMonResult.getPokemon();
		String name = pokemon.getName();
		assertEquals(name, null);
	}

	@Test
	void testResponseCodForInvalidPokemon() {
		PokeMonResult pokeMonResult = pokeDexService.getBasicPokeMonInformation("Nawazish");
		Integer responseCode = pokeMonResult.getResponseCode();
		assertEquals(responseCode, 404);
	}

	@Test
	void testValidPokemon() {
		PokeMonResult pokeMonResult = pokeDexService.getBasicPokeMonInformation("pikachu");
		Pokemon pokemon = pokeMonResult.getPokemon();
		String name = pokemon.getName();
		assertEquals(name, "pikachu");
	}

	@Test
	void testValidPokemonWithTranslation() {
		PokeMonResult pokeMonResult = pokeDexService.getPokeMonInformationWithFunTranslation("pikachu");
		Pokemon pokemon = pokeMonResult.getPokemon();
		String name = pokemon.getName();
		assertEquals(name, "pikachu");
	}

	@Test
	void testInvalidPokemonWithTranslation() {
		PokeMonResult pokeMonResult = pokeDexService.getPokeMonInformationWithFunTranslation("nawazish");
		Pokemon pokemon = pokeMonResult.getPokemon();
		String name = pokemon.getName();
		assertEquals(name, null);
	}

}
