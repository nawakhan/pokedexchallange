package com.challange.pokedex.pojo;

/**
 * This class holds Pokemon Object along with a Response status Code
 * 
 * @author nawaz
 *
 */
public class PokeMonResult {

	Pokemon pokemon;

	public Pokemon getPokemon() {
		return pokemon;
	}

	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	Integer responseCode;
}
