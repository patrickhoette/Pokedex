package com.patrickhoette.test.factory.ability

import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object AbilityFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        names: List<String> = emptyList(),
        isHiddens: List<Boolean> = emptyList(),
    ) = List(amount) {
        create(
            id = it,
            name = names.moduloGet(it),
            isHidden = isHiddens.moduloGet(it),
        )
    }

    fun create(
        id: Int = 1,
        name: String? = null,
        isHidden: Boolean? = null,
    ) = Ability(
        id = id,
        name = name ?: "Thick Fat",
        isHidden = isHidden ?: false,
    )
}
