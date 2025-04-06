package com.patrickhoette.pokemon.source.detail.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonCryResponse(

    @SerialName("latest")
    val latest: String?,

    @SerialName("legacy")
    val legacy: String?,
)
