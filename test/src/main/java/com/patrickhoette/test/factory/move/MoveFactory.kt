package com.patrickhoette.test.factory.move

import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object MoveFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        names: List<String> = emptyList(),
        methods: List<MoveMethod> = emptyList(),
        levels: List<Int> = emptyList(),
    ) = List(amount) {
        create(
            id = it,
            name = names.moduloGet(it),
            method = methods.moduloGet(it),
            level = levels.moduloGet(it),
        )
    }

    fun create(
        id: Int? = null,
        name: String? = null,
        method: MoveMethod? = null,
        level: Int? = null,
    ) = Move(
        id = id ?: 1,
        name = name ?: "Tackle",
        method = method ?: MoveMethod.LevelUp,
        level = level ?: 1,
    )
}
