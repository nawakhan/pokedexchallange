package com.challange.pokedex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.challange.pokedex.pojo.Pokemon;

/**
 * This Controller handles all the requests related to Pokedex application
 * 
 * @author nawaz
 *
 */
@RestController
@CrossOrigin
public class PokeDexController {

	/**
	 * 
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/pokemon/{name}")
	public ResponseEntity<Pokemon> getPokemonInformationByName(@PathVariable("name") String name) {
		// TODO:Call Pokemon APIs to get the required information
		
		return null;

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/pokemon/translated/{name}")
	public ResponseEntity<Pokemon> getPokemonInformationFunTrans(@PathVariable("name") String name) {
		// TODO:Call Pokemon APIs to get the required information and translate them
		return null;

	}
}
