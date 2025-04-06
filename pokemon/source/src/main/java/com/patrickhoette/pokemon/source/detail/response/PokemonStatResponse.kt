package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonStatResponse(

    @SerialName("base_stat")
    val baseStat: Int,

    @SerialName("effort")
    val effort: Int,

    @SerialName("stat")
    val stat: RemoteReferenceResponse,
)
