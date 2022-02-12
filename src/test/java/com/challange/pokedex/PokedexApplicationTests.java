package com.challange.pokedex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.challange.pokedex.pojo.PokeMonResult;
import com.challange.pokedex.pojo.Pokemon;
import com.challange.pokedex.service.PokeDexService;

/**
 * Class cotaining Junits
 * 
 * @author nawaz
 *
 */
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
