package com.patrickhoette.pokemon.store.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.patrickhoette.pokemon.store.database.pokemon.Pokemon as PokemonEntry

@ExtendWith(MockKExtension::class)
class PokemonListEntryMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonListEntryMapper

    @Test
    fun `When mapping to entry, then return properly mapped entry`() {
        // Given
        val model = Pokemon(
            id = 1,
            name = "bulbasaur",
        )

        // When
        val result = mapper.mapToEntry(model)

        // Then
        result.id assertEquals model.id.toLong()
        result.name assertEquals model.name
    }

    @Test
    fun `When mapping to list, then return properly mapped model`() = runTest {
        // Given
        val entries = listOf(
            PokemonEntry(
                id = 1,
                name = "bulbasaur",
                lastUpdate = 0,
            ),
            PokemonEntry(
                id = 2,
                name = "ivysaur",
                lastUpdate = 0,
            ),
            PokemonEntry(
                id = 3,
                name = "venusaur",
                lastUpdate = 0,
            ),
        )
        val maxCount = 1304L
        val hasNext = true

        // When
        val result = mapper.mapToList(entries, maxCount, hasNext)

        // Then
        result assertEquals PokemonList(
            maxCount = maxCount.toInt(),
            hasNext = hasNext,
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
}
