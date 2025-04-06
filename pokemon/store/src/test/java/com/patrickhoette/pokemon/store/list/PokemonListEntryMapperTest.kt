package com.patrickhoette.pokemon.store.list

import com.patrickhoette.pokedex.entity.generic.Type.Grass
import com.patrickhoette.pokedex.entity.generic.Type.Poison
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.patrickhoette.pokedex.database.Pokemon_list_entry as PokemonListEntryEntity

@ExtendWith(MockKExtension::class)
class PokemonListEntryMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonListEntryMapper

    @Test
    fun `When mapping to list, then return properly mapped model`() = runTest {
        // Given
        val entries = listOf(
            PokemonListEntryEntity(
                id = 1,
                name = "bulbasaur",
                lastUpdate = 0,
                primaryType = Grass.name,
                secondaryType = Poison.name,
            ),
            PokemonListEntryEntity(
                id = 2,
                name = "ivysaur",
                lastUpdate = 0,
                primaryType = Grass.name,
                secondaryType = null,
            ),
            PokemonListEntryEntity(
                id = 3,
                name = "venusaur",
                lastUpdate = 0,
                primaryType = null,
                secondaryType = null,
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
                    types = listOf(Grass, Poison),
                    detail = null,
                ),
                Pokemon(
                    id = 2,
                    name = "ivysaur",
                    types = listOf(Grass),
                    detail = null,
                ),
                Pokemon(
                    id = 3,
                    name = "venusaur",
                    types = emptyList(),
                    detail = null,
                ),
            ),
        )
    }
}
