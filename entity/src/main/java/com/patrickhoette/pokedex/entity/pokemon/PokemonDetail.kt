package com.patrickhoette.pokedex.entity.pokemon

import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.species.Species

data class PokemonDetail(
    val baseExperience: Int,
    val height: Length,
    val weight: Weight,
    val abilities: List<Ability>,
    val moves: List<Move>,
    val sprites: List<SpriteGroup>,
    val cry: String?,
    val species: Species,
    val stats: PokemonStats,
)
