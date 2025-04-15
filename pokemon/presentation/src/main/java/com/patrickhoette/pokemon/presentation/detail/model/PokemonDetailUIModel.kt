package com.patrickhoette.pokemon.presentation.detail.model

import androidx.compose.runtime.Immutable
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PokemonDetailUIModel(
    val id: Int,
    val name: StringUIModel,
    val types: PokemonTypeUIModel,
    val baseExperience: Int,
    val height: StringUIModel,
    val weight: StringUIModel,
    val abilities: ImmutableList<PokemonAbilityUIModel>,
    val moves: ImmutableList<PokemonMoveUIModel>,
    val sprites: ImmutableList<SpriteGroupUIModel>,
    val cry: String?,
    val species: StringUIModel,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
)
