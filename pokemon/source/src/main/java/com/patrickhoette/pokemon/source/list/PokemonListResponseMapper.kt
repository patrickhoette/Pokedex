package com.patrickhoette.pokemon.source.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.source.list.response.PokemonListEntryResponse
import com.patrickhoette.pokemon.source.list.response.PokemonListResponse
import org.koin.core.annotation.Factory

@Factory
class PokemonListResponseMapper {

    fun mapToPokemonList(response: PokemonListResponse) = PokemonList(
        maxCount = response.count,
        hasNext = !response.next.isNullOrBlank(),
        pokemon = mapToModels(response.results.values),
    )

    private fun mapToModels(responses: Collection<PokemonListEntryResponse>) = responses.map(::mapToPokemon)

    private fun mapToPokemon(response: PokemonListEntryResponse) = Pokemon(
        id = parseId(response.url),
        name = response.name,
    )

    private fun parseId(url: String): Int {
        val id = IdRegex.find(url)?.groups?.get(1)?.value?.toIntOrNull()
        return requireNotNull(id) { "Failed to parse id from url: $url" }
    }

    companion object {

        private val IdRegex = Regex("^https://pokeapi\\.co/api/v2/pokemon/(\\d)/$")
    }
}
