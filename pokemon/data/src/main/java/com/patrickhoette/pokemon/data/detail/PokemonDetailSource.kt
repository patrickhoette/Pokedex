package com.patrickhoette.pokemon.data.detail

import com.patrickhoette.pokedex.entity.pokemon.Pokemon

interface PokemonDetailSource {

    suspend fun fetchPokemon(id: Int): Pokemon
}
