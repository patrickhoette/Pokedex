package com.patrickhoette.pokedex.entity.move

data class Move(
    val id: Int,
    val name: String,
    val method: MoveMethod,
    val level: Int,
)
