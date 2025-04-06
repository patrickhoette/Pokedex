package com.patrickhoette.test.factory.pokemon

import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonDetail
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object PokemonFactory {

    private val DefaultNames = listOf(
        "Bulbasaur",
        "Ivysaur",
        "Venusaur",
        "Charmander",
        "Charmeleon",
        "Charizard",
        "Squirtle",
        "Wartortle",
        "Blastoise",
        "Caterpie",
    )

    private val DefaultTypes = listOf(
        listOf(Grass, Poison),
        listOf(Grass, Poison),
        listOf(Grass, Poison),
        listOf(Fire),
        listOf(Fire),
        listOf(Fire, Flying),
        listOf(Water),
        listOf(Water),
        listOf(Water),
        listOf(Bug),
    )

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        names: List<String> = DefaultNames,
        types: List<List<Type>> = DefaultTypes,
        details: List<PokemonDetail> = PokemonDetailsFactory.createMultiple(),
    ) = List(amount) {
        create(
            id = it + 1,
            name = names.moduloGet(it),
            types = types.moduloGet(it),
            details = details.moduloGet(it),
        )
    }

    fun create(
        id: Int? = null,
        name: String? = null,
        types: List<Type>? = null,
        details: PokemonDetail? = null,
    ) = Pokemon(
        id = id ?: 1,
        name = name ?: "Bulbasaur",
        types = types ?: listOf(Grass, Poison),
        detail = details ?: PokemonDetailsFactory.create(),
    )
}
