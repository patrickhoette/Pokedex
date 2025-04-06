package com.patrickhoette.pokemon.source.detail.response

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class PokemonDetailResponse(

    @SerialName("abilities")
    val abilities: List<PokemonAbilityResponse>,

    @SerialName("base_experience")
    val baseExperience: Int,

    @SerialName("cries")
    val cries: PokemonCryResponse,

    @SerialName("forms")
    val forms: List<RemoteReferenceResponse>,

    @SerialName("game_indices")
    val gameIndices: List<PokemonGameIndexResponse>,

    @SerialName("height")
    val height: Int,

    @SerialName("held_items")
    val heldItems: List<PokemonHeldItemResponse>,

    @SerialName("id")
    val id: Int,

    @SerialName("is_default")
    val isDefault: Boolean,

    @SerialName("location_area_encounters")
    val locationAreaEncounters: String,

    @SerialName("moves")
    val moves: List<PokemonMoveResponse>,

    @SerialName("name")
    val name: String,

    @SerialName("order")
    val order: Int,

    @SerialName("past_abilities")
    val pastAbilities: List<PokemonAbilityResponse>,

    @SerialName("past_types")
    val pastTypes: List<PokemonTypeResponse>,

    @SerialName("species")
    val species: RemoteReferenceResponse,

    @SerialName("sprites")
    val sprites: JsonObject,

    @SerialName("stats")
    val stats: List<PokemonStatResponse>,

    @SerialName("types")
    val types: List<PokemonTypeResponse>,

    @SerialName("weight")
    val weight: Int,
)
