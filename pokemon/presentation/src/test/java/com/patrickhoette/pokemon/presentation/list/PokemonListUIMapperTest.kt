package com.patrickhoette.pokemon.presentation.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.End
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.Entry
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PokemonListUIMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonListUIMapper

    @Test
    fun `Given list has next, when mapping, then return mapped list without end`() = runTest {
        // Given
        val model = PokemonList(
            maxCount = 1304,
            hasNext = true,
            pokemon = listOf(
                Pokemon(
                    id = 1,
                    name = "Bulbasaur",
                ),
                Pokemon(
                    id = 2,
                    name = "Ivysaur",
                ),
                Pokemon(
                    id = 3,
                    name = "Venusaur",
                ),
            ),
        )

        // When
        val result = mapper.mapToUIModel(model)

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = true,
            pokemon = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png"
                ),
            ),
        )
    }

    @Test
    fun `Given list does not have next, when mapping, then return mapped list with end`() = runTest {
        // Given
        val model = PokemonList(
            maxCount = 1304,
            hasNext = false,
            pokemon = listOf(
                Pokemon(
                    id = 1,
                    name = "Bulbasaur",
                ),
                Pokemon(
                    id = 2,
                    name = "Ivysaur",
                ),
                Pokemon(
                    id = 3,
                    name = "Venusaur",
                ),
            ),
        )

        // When
        val result = mapper.mapToUIModel(model)

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = false,
            pokemon = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png"
                ),
                End,
            ),
        )
    }
}
