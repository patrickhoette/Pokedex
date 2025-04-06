package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonAbilityResponse(

    @SerialName("ability")
    val ability: RemoteReferenceResponse,

    @SerialName("is_hidden")
    val isHidden: Boolean,

    @SerialName("slot")
    val slot: Int,
)
