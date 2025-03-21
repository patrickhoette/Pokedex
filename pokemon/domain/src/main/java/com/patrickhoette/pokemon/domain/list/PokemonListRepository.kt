package com.patrickhoette.pokemon.domain.list

import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    fun observePokemonList(): Flow<PokemonList?>
    // fun observePokemon(id: Int): Flow<Pokemon>

    suspend fun fetchNextPokemonPage()
}
