package com.patrickhoette.pokemon.presentation.detail.model

import androidx.compose.runtime.Immutable
import com.patrickhoette.core.presentation.model.StringUIModel

@Immutable
data class PokemonAbilityUIModel(
    val id: Int,
    val name: StringUIModel,
    val isHidden: Boolean,
)
