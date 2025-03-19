package com.patrickhoette.pokemon.presentation.list.model

sealed class PokemonListEvent {

    data class OpenPokemonDetails(val id: Int) : PokemonListEvent()
}
