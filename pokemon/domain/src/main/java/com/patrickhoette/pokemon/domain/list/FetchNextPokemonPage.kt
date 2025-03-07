package com.patrickhoette.pokemon.domain.list

import org.koin.core.annotation.Factory

@Factory
class FetchNextPokemonPage(
    private val repository: PokemonListRepository,
) {

    suspend operator fun invoke() = repository.fetchNextPokemonPage()
}
