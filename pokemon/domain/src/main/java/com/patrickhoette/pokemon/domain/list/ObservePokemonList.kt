package com.patrickhoette.pokemon.domain.list

import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObservePokemonList(
    private val repository: PokemonListRepository,
) {

    operator fun invoke(): Flow<PokemonList> = repository.observePokemonList()
}
