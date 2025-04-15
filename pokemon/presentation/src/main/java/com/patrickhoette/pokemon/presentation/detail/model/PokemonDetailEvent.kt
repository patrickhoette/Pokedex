package com.patrickhoette.pokemon.presentation.detail.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class PokemonDetailEvent {

    @Immutable
    data object Close : PokemonDetailEvent()
}
