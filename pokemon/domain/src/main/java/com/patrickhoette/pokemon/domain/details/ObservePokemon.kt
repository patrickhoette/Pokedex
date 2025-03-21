package com.patrickhoette.pokemon.domain.details

import com.patrickhoette.pokemon.domain.list.PokemonListRepository
import org.koin.core.annotation.Factory

@Factory
class ObservePokemon(
    private val repository: PokemonListRepository,
) {

    // operator fun invoke(id: Int) = repository.observePokemon(id)
}
