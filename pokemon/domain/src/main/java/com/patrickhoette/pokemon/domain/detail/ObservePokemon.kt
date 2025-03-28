package com.patrickhoette.pokemon.domain.detail

import org.koin.core.annotation.Factory

@Factory
class ObservePokemon(
    private val repository: PokemonDetailRepository,
) {

    operator fun invoke(id: Int) = repository.observePokemon(id)
}
