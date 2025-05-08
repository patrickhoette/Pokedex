package com.patrickhoette.test.factory.pokemon

import com.patrickhoette.pokedex.entity.pokemon.PokemonStats
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet

object PokemonStatsFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        hps: List<Int> = emptyList(),
        attacks: List<Int> = emptyList(),
        defenses: List<Int> = emptyList(),
        specialAttacks: List<Int> = emptyList(),
        specialDefenses: List<Int> = emptyList(),
        speeds: List<Int> = emptyList(),
    ) = List(amount) {
        create(
            hp = hps.moduloGet(it),
            attack = attacks.moduloGet(it),
            defense = defenses.moduloGet(it),
            specialAttack = specialAttacks.moduloGet(it),
            specialDefense = specialDefenses.moduloGet(it),
            speed = speeds.moduloGet(it),
        )
    }

    fun create(
        hp: Int? = null,
        attack: Int? = null,
        defense: Int? = null,
        specialAttack: Int? = null,
        specialDefense: Int? = null,
        speed: Int? = null,
    ) = PokemonStats(
        hp = hp ?: 20,
        attack = attack ?: 34,
        defense = defense ?: 56,
        specialAttack = specialAttack ?: 20,
        specialDefense = specialDefense ?: 45,
        speed = speed ?: 30,
    )
}
