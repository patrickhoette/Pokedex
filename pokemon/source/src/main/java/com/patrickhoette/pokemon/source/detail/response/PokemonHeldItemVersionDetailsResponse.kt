package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonHeldItemVersionDetailsResponse(

    @SerialName("rarity")
    val rarity: Int,

    @SerialName("version")
    val version: RemoteReferenceResponse,
)
