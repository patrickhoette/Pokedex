package com.patrickhoette.pokemon.source.list.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(

    @SerialName("count")
    val count: Int,

    @SerialName("next")
    val next: String?,

    @SerialName("previous")
    val previous: String?,

    @SerialName("results")
    val results: List<RemoteReferenceResponse>,
)
