package com.patrickhoette.pokemon.domain.list

import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    fun observePokemonList(): Flow<PokemonList?>

    suspend fun fetchNextPokemonPage()
}
