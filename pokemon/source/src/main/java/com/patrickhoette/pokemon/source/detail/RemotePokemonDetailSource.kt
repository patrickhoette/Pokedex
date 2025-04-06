package com.patrickhoette.pokemon.source.detail

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokemon.data.detail.PokemonDetailSource
import com.patrickhoette.pokemon.source.detail.mapper.PokemonDetailResponseMapper
import com.patrickhoette.pokemon.source.detail.response.PokemonDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory

@Factory
class RemotePokemonDetailSource(
    private val client: HttpClient,
    private val mapper: PokemonDetailResponseMapper,
) : PokemonDetailSource {

    override suspend fun fetchPokemon(id: Int): Pokemon {
        val response = client.get("pokemon/$id").body<PokemonDetailResponse>()

        return mapper.mapToPokemon(response)
    }
}
