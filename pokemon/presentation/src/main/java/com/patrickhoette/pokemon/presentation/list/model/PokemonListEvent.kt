package com.patrickhoette.pokemon.presentation.list.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class PokemonListEvent {

    @Immutable
    data class OpenPokemonDetails(val id: Int) : PokemonListEvent()
}
