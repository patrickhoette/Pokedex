package com.patrickhoette.pokemon.presentation.detail.model

import androidx.compose.runtime.Immutable
import com.patrickhoette.core.presentation.model.StringUIModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class SpriteGroupUIModel(
    val name: StringUIModel,
    val sprites: ImmutableList<SpriteUIModel>,
)
