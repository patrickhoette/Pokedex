package com.patrickhoette.pokedex.entity.pokemon

data class PokemonList(
    val maxCount: Int,
    val hasNext: Boolean,
    val pokemon: List<Pokemon>,
)
