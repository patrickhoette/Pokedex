package com.patrickhoette.pokemon.data.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.data.generic.model.CacheStatus
import kotlinx.coroutines.flow.Flow

interface PokemonListStore {

    val currentPage: Int

    suspend fun storePokemon(pokemon: List<Pokemon>)
    suspend fun getPageStatus(page: Int, pageSize: Int): CacheStatus
    fun observePokemonList(pages: Int, pageSize: Int): Flow<PokemonList>

    fun observeCurrentPage(): Flow<Int>
    suspend fun incrementCurrentPage()
}
