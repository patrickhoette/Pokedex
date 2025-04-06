package com.patrickhoette.pokedex.entity.pokemon

import com.patrickhoette.pokedex.entity.generic.Type

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val detail: PokemonDetail?,
)
