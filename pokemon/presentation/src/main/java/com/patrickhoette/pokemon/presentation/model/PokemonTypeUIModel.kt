package com.patrickhoette.pokemon.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.patrickhoette.core.presentation.model.NamedEnumUIModel
import com.patrickhoette.pokedex.entity.generic.Type

@Stable
sealed class PokemonTypeUIModel {

    @Immutable
    data class MonoType(
        val value: NamedEnumUIModel<Type>,
    ) : PokemonTypeUIModel()

    @Immutable
    data class DualType(
        val primary: NamedEnumUIModel<Type>,
        val secondary: NamedEnumUIModel<Type>,
    ) : PokemonTypeUIModel()
}
