package com.patrickhoette.pokemon.presentation.detail.model

import androidx.compose.runtime.Immutable
import com.patrickhoette.core.presentation.model.StringUIModel

@Immutable
data class SpriteUIModel(
    val name: StringUIModel,
    val url: String,
)
