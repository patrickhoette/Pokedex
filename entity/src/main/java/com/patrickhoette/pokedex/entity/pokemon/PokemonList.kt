package com.patrickhoette.pokedex.entity.pokemon

data class PokemonList(
    val maxCount: Int,
    val currentPage: Int,
    val pageSize: Int,
    val pokemon: List<Pokemon>,
)
