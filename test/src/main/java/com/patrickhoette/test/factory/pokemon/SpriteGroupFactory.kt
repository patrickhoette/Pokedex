package com.patrickhoette.test.factory.pokemon

import com.patrickhoette.pokedex.entity.pokemon.Sprite
import com.patrickhoette.pokedex.entity.pokemon.SpriteGroup
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object SpriteGroupFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        names: List<String> = emptyList(),
        sprites: List<List<Sprite>> = emptyList(),
    ) = List(amount) {
        create(
            name = names.moduloGet(it),
            sprites = sprites.moduloGet(it),
        )
    }

    fun create(
        name: String? = null,
        sprites: List<Sprite>? = null,
    ) = SpriteGroup(
        name = name ?: "Default",
        sprites = sprites ?: SpriteFactory.createMultiple(),
    )
}
