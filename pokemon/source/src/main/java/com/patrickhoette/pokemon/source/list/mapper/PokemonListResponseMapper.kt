package com.patrickhoette.pokemon.source.list.mapper

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.source.list.response.PokemonListResponse
import org.koin.core.annotation.Factory

@Factory
class PokemonListResponseMapper {

    fun mapToPokemonList(response: PokemonListResponse) = PokemonList(
        maxCount = response.count,
        hasNext = !response.next.isNullOrBlank(),
        pokemon = mapToModels(response.results),
    )

    private fun mapToModels(responses: Collection<RemoteReferenceResponse>) = responses.map(::mapToPokemon)

    private fun mapToPokemon(response: RemoteReferenceResponse) = Pokemon(
        id = response.parseId(),
        name = response.name,
        types = emptyList(),
        details = null,
    )
}
