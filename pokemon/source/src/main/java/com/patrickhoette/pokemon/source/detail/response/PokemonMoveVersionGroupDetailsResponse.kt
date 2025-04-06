package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonMoveVersionGroupDetailsResponse(

    @SerialName("level_learned_at")
    val levelLearnedAt: Int,

    @SerialName("move_learn_method")
    val moveLearnMethod: RemoteReferenceResponse,

    @SerialName("version_group")
    val versionGroup: RemoteReferenceResponse,
)
