package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonMoveResponse(

    @SerialName("move")
    val move: RemoteReferenceResponse,

    @SerialName("version_group_details")
    val versionGroupDetails: List<PokemonMoveVersionGroupDetailsResponse>,
)
