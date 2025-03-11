package com.patrickhoette.pokemon.presentation.list.model

import com.patrickhoette.core.presentation.model.GenericError

sealed class PokemonListEvent {

    data class ShowError(val type: GenericError) : PokemonListEvent()
}
