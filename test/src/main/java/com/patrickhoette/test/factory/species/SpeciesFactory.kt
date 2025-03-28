package com.patrickhoette.test.factory.species

import com.patrickhoette.pokedex.entity.species.Species
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object SpeciesFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        names: List<String> = emptyList(),
    ) = List(amount) {
        create(
            id = it,
            name = names.moduloGet(it),
        )
    }

    fun create(
        id: Int? = null,
        name: String? = null,
    ) = Species(
        id = id ?: 1,
        name = name ?: "Ivysaur",
    )
}
