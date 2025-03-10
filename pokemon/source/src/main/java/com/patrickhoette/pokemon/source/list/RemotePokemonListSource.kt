package com.patrickhoette.pokemon.source.list

import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.data.list.PokemonListSource
import com.patrickhoette.pokemon.source.list.response.PokemonListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory

@Factory
class RemotePokemonListSource(
    private val client: HttpClient,
    private val mapper: PokemonListResponseMapper,
) : PokemonListSource {

    override suspend fun fetchPokemonPage(offset: Int, pageSize: Int): PokemonList {
        val response = client.get("pokemon") {
            parameter("offset", offset)
            parameter("limit", pageSize)
        }.body<PokemonListResponse>()

        return mapper.mapToPokemonList(response)
    }
}
