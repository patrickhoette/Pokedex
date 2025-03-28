package com.patrickhoette.pokemon.domain.detail

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonDetailRepository {

    fun observePokemon(id: Int): Flow<Pokemon>
}
