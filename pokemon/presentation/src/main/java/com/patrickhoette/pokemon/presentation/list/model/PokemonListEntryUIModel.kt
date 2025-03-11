package com.patrickhoette.pokemon.presentation.list.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class PokemonListEntryUIModel {

    @Immutable
    data class Entry(
        val id: Int,
        val name: String,
        val imageUrl: String,
    ) : PokemonListEntryUIModel()

    @Immutable
    data object Loading : PokemonListEntryUIModel()

    @Immutable
    data object End : PokemonListEntryUIModel()
}
