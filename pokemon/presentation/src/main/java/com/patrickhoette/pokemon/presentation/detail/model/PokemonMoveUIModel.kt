package com.patrickhoette.pokemon.presentation.detail.model

import androidx.compose.runtime.Immutable
import com.patrickhoette.core.presentation.model.NamedEnumUIModel
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.pokedex.entity.move.MoveMethod

@Immutable
data class PokemonMoveUIModel(
    val id: Int,
    val name: StringUIModel,
    val method: NamedEnumUIModel<MoveMethod>,
    val level: Int,
)
