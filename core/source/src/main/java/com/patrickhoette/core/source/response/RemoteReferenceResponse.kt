package com.patrickhoette.core.source.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteReferenceResponse(

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String,
) {

    fun parseId(): Int {
        val id = IdRegex.find(url)?.groups?.get(1)?.value?.toIntOrNull()
        return requireNotNull(id) { "Failed to parse id from url: $url" }
    }

    companion object {

        private val IdRegex = Regex("^https://pokeapi\\.co/api/v2/(?:[a-z]|-)*/(\\d+)/$")
    }
}
