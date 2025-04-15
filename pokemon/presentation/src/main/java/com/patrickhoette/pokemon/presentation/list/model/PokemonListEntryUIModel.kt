package com.patrickhoette.pokemon.presentation.list.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel

@Stable
sealed class PokemonListEntryUIModel {

    @Immutable
    data class Entry(
        val id: Int,
        val name: StringUIModel,
        val imageUrl: String,
        val type: PokemonTypeUIModel,
    ) : PokemonListEntryUIModel()

    @Immutable
    data object Loading : PokemonListEntryUIModel()

    @Immutable
    data class Error(val cause: GenericError) : PokemonListEntryUIModel()

    @Immutable
    data object End : PokemonListEntryUIModel()
}
