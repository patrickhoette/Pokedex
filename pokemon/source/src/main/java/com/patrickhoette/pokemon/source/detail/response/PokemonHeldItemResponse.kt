package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonHeldItemResponse(

    @SerialName("item")
    val item: RemoteReferenceResponse,

    @SerialName("version_details")
    val versionDetails: List<PokemonHeldItemVersionDetailsResponse>,
)
