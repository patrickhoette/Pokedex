package com.patrickhoette.pokemon.source.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.source.list.response.PokemonListEntryResponse
import com.patrickhoette.pokemon.source.list.response.PokemonListResponse
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
class PokemonListResponseMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonListResponseMapper

    @Test
    fun `Given valid response, when mapping, then return mapped model`() = runTest {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = mapOf(
                0 to PokemonListEntryResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                1 to PokemonListEntryResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon/2/",
                ),
                2 to PokemonListEntryResponse(
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
                ),
                Pokemon(
                    id = 2,
                    name = "ivysaur",
                ),
                Pokemon(
                    id = 3,
                    name = "venusaur",
                ),
            ),
        )
    }

    @Test
    fun `Given valid response but no next page, when mapping, then return mapped model`() = runTest {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = null,
            previous = null,
            results = emptyMap(),
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
    fun `Given id is not in the url, when mapping, then throw illegal argument exception`() = runTest {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = mapOf(
                0 to PokemonListEntryResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                1 to PokemonListEntryResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon",
                ),
                2 to PokemonListEntryResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When -> Then
        assertFailsWith<IllegalArgumentException> { mapper.mapToPokemonList(response) }
    }

    @Test
    fun `Given id has no value, when mapping, then throw illegal argument exception`() = runTest {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = mapOf(
                0 to PokemonListEntryResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                1 to PokemonListEntryResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon//",
                ),
                2 to PokemonListEntryResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When -> Then
        assertFailsWith<IllegalArgumentException> { mapper.mapToPokemonList(response) }
    }

    @Test
    fun `Given id is not a number, when mapping, then throw illegal argument exception`() = runTest {
        // Given
        val response = PokemonListResponse(
            count = 1304,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = mapOf(
                0 to PokemonListEntryResponse(
                    name = "bulbasaur",
                    url = "https://pokeapi.co/api/v2/pokemon/1/",
                ),
                1 to PokemonListEntryResponse(
                    name = "ivysaur",
                    url = "https://pokeapi.co/api/v2/pokemon/gg/",
                ),
                2 to PokemonListEntryResponse(
                    name = "venusaur",
                    url = "https://pokeapi.co/api/v2/pokemon/3/",
                ),
            )
        )

        // When -> Then
        assertFailsWith<IllegalArgumentException> { mapper.mapToPokemonList(response) }
    }
}
