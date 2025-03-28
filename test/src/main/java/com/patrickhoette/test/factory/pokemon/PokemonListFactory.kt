package com.patrickhoette.test.factory.pokemon

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object PokemonListFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        maxCounts: List<Int> = emptyList(),
        hasNexts: List<Boolean> = emptyList(),
        pokemon: List<List<Pokemon>>,
    ) = List(amount) {
        create(
            maxCount = maxCounts.moduloGet(it),
            hasNext = hasNexts.moduloGet(it),
            pokemon = pokemon.moduloGet(it),
        )
    }

    fun create(
        maxCount: Int? = null,
        hasNext: Boolean? = null,
        pokemon: List<Pokemon>? = null,
    ) = PokemonList(
        maxCount = maxCount ?: 1304,
        hasNext = hasNext ?: true,
        pokemon = pokemon ?: PokemonFactory.createMultiple(),
    )
}
