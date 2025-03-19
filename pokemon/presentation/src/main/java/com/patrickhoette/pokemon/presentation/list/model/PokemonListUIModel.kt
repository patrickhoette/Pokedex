package com.patrickhoette.pokemon.presentation.list.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PokemonListUIModel(
    val hasNext: Boolean,
    val entries: ImmutableList<PokemonListEntryUIModel>,
)
