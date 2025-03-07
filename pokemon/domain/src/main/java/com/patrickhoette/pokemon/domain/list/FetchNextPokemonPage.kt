package com.patrickhoette.pokemon.domain.list

import org.koin.core.annotation.Factory

@Factory
class FetchNextPokemonPage(
    private val repository: PokemonListRepository,
) {

    suspend operator fun invoke(currentPage: Int, pageSize: Int) =
        repository.fetchNextPokemonPage(currentPage, pageSize)
}
