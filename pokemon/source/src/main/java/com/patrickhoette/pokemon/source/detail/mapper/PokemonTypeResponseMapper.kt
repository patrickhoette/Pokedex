package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.pokedex.entity.generic.Type.*
import org.koin.core.annotation.Factory

@Factory
class PokemonTypeResponseMapper {

    fun mapType(name: String) = when (name) {
        "normal" -> Normal
        "fighting" -> Fighting
        "flying" -> Flying
        "poison" -> Poison
        "ground" -> Ground
        "rock" -> Rock
        "bug" -> Bug
        "ghost" -> Ghost
        "steel" -> Steel
        "fire" -> Fire
        "water" -> Water
        "grass" -> Grass
        "electric" -> Electric
        "psychic" -> Psychic
        "ice" -> Ice
        "dragon" -> Dragon
        "dark" -> Dark
        "fairy" -> Fairy
        "stellar" -> Stellar
        else -> Unknown
    }
}
