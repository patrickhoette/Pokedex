package com.patrickhoette.pokemon.data.detail

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokemon.data.generic.model.CacheStatus
import kotlinx.coroutines.flow.Flow

interface PokemonDetailStore {

    suspend fun storePokemon(pokemon: Pokemon)
    suspend fun getPokemonStatus(id: Int): CacheStatus
    fun observePokemon(id: Int): Flow<Pokemon?>
}
