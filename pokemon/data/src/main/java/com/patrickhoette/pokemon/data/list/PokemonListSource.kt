package com.patrickhoette.pokemon.data.list

import com.patrickhoette.pokedex.entity.pokemon.PokemonList

interface PokemonListSource {

    suspend fun fetchPokemonPage(offset: Int, pageSize: Int): PokemonList
}
