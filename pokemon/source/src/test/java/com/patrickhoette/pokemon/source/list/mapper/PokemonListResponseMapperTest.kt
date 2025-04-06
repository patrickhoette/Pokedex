package com.patrickhoette.pokemon.source.list.mapper

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.source.list.response.PokemonListResponse
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
class PokemonListResponseMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonListResponseMapper

    @Test
    fun `Given valid response, when mapping, then return mapped model`() {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = listOf(
                RemoteReferenceResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                RemoteReferenceResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon/2/",
                ),
                RemoteReferenceResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When
        val result = mapper.mapToPokemonList(response)

        // Then
        result assertEquals PokemonList(
            maxCount = 1304,
            hasNext = true,
            pokemon = listOf(
                Pokemon(
                    id = 1,
                    name = "bulbasaur",
                    types = emptyList(),
                    details = null,
                ),
                Pokemon(
                    id = 2,
                    name = "ivysaur",
                    types = emptyList(),
                    details = null,
                ),
                Pokemon(
                    id = 3,
                    name = "venusaur",
                    types = emptyList(),
                    details = null,
                ),
            ),
        )
    }

    @Test
    fun `Given valid response but no next page, when mapping, then return mapped model`() {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = null,
            previous = null,
            results = emptyList(),
        )

        // When
        val result = mapper.mapToPokemonList(response)

        // Then
        result assertEquals PokemonList(
            maxCount = 1304,
            hasNext = false,
            pokemon = emptyList(),
        )
    }

    @Test
    fun `Given id is not in the url, when mapping, then throw illegal argument exception`() {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = listOf(
                RemoteReferenceResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                RemoteReferenceResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon",
                ),
                RemoteReferenceResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When -> Then
        assertFailsWith<IllegalArgumentException> { mapper.mapToPokemonList(response) }
    }

    @Test
    fun `Given id has multiple digits, when mapping, then throw illegal argument exception`() {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = listOf(
                RemoteReferenceResponse(
                    name = "wigglytuff",
                    url = "https://pokeapi.co/api/v2/pokemon/40/",
                ),
            )
        )

        // When
        val result = mapper.mapToPokemonList(response)

        // Then
        result.pokemon.first().id assertEquals 40
    }

    @Test
    fun `Given id has no value, when mapping, then throw illegal argument exception`() {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = listOf(
                RemoteReferenceResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                RemoteReferenceResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon//",
                ),
                RemoteReferenceResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When -> Then
        assertFailsWith<IllegalArgumentException> { mapper.mapToPokemonList(response) }
    }

    @Test
    fun `Given id is not a number, when mapping, then throw illegal argument exception`() {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = listOf(
                RemoteReferenceResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                RemoteReferenceResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon/gg/",
                ),
                RemoteReferenceResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When -> Then
        assertFailsWith<IllegalArgumentException> { mapper.mapToPokemonList(response) }
    }
}
