package com.patrickhoette.pokemon.source.list.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListEntryResponse(

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String,
)
