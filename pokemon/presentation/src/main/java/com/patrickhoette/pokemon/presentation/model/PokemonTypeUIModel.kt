package com.patrickhoette.pokemon.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.patrickhoette.pokedex.entity.generic.Type

@Stable
sealed class PokemonTypeUIModel {

    @Immutable
    data class MonoType(val value: Type) : PokemonTypeUIModel()

    @Immutable
    data class DualType(
        val primary: Type,
        val secondary: Type,
    ) : PokemonTypeUIModel()
}
