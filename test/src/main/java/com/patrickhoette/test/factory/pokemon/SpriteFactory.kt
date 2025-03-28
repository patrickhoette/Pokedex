package com.patrickhoette.test.factory.pokemon

import com.patrickhoette.pokedex.entity.pokemon.Sprite
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object SpriteFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        names: List<String> = emptyList(),
        urls: List<String> = emptyList(),
    ) = List(amount) {
        create(
            name = names.moduloGet(it),
            url = urls.moduloGet(it),
        )
    }

    fun create(
        name: String? = null,
        url: String? = null,
    ) = Sprite(
        name = name ?: "Default Front",
        url = url ?: "https://www.google.com/",
    )
}
