package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeResponse(

    @SerialName("slot")
    val slot: Int,

    @SerialName("type")
    val type: RemoteReferenceResponse,
)
