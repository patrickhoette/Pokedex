package com.patrickhoette.pokemon.presentation.list

import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.DualType
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.MonoType
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.model.TestException
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
                    types = emptyList(),
                    details = null,
                ),
                Pokemon(
                    id = 2,
                    name = "Ivysaur",
                    types = emptyList(),
                    details = null,
                ),
                Pokemon(
                    id = 3,
                    name = "Venusaur",
                    types = emptyList(),
                    details = null,
                ),
            ),
        )

        // When
        val result = mapper.mapToUIModel(model)

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    type = MonoType(Unknown),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
                    type = MonoType(Unknown),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
                    type = MonoType(Unknown),
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
                    types = emptyList(),
                    details = null,
                ),
                Pokemon(
                    id = 2,
                    name = "Ivysaur",
                    types = emptyList(),
                    details = null,
                ),
                Pokemon(
                    id = 3,
                    name = "Venusaur",
                    types = emptyList(),
                    details = null,
                ),
            ),
        )

        // When
        val result = mapper.mapToUIModel(model)

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = false,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    type = MonoType(Unknown),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
                    type = MonoType(Unknown),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
                    type = MonoType(Unknown),
                ),
                End,
            ),
        )
    }

    @Test
    fun `When creating loading, then return a UI model with the correct number of loading items`() = runTest {
        // When
        val result = mapper.createLoading()

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading,
            ),
        )
    }

    @Test
    fun `Given last item in list is end, when adding loading entries, then do not add loading items`() = runTest {
        // Given
        val list = PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                End,
            ),
        )

        // When
        val result = mapper.addLoadingEntries(list)

        // Then
        result assertEquals list
    }

    @Test
    fun `Given last item in list is loading item, when adding loading entries, then do not add loading items`() =
        runTest {
            // Given
            val list = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(
                    Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 2,
                        name = "Ivysaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 3,
                        name = "Venusaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Loading,
                ),
            )

            // When
            val result = mapper.addLoadingEntries(list)

            // Then
            result assertEquals list
        }

    @Test
    fun `Given last item in list is error, when adding loading entries, then add loading entries and remove error entry`() =
        runTest {
            // Given
            val list = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(
                    Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 2,
                        name = "Ivysaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 3,
                        name = "Venusaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Error(GenericError.Unknown),
                ),
            )

            // When
            val result = mapper.addLoadingEntries(list)

            // Then
            result assertEquals PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(
                    Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 2,
                        name = "Ivysaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 3,
                        name = "Venusaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading,
                ),
            )
        }

    @Test
    fun `Given last item in list is entry, when adding loading entries, then add loading entries`() = runTest {
        // Given
        val list = PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
            ),
        )

        // When
        val result = mapper.addLoadingEntries(list)

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading, Loading,
            ),
        )
    }

    @Test
    fun `Given list contains no loading items, when adding error entry, then add error entry`() = runTest {
        // Given
        val list = PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
            ),
        )

        // When
        val result = mapper.addErrorEntry(list, TestException)

        // Then
        result assertEquals PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(
                Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                    type = DualType(Grass, Poison),
                ),
                Error(GenericError.Unknown),
            ),
        )
    }

    @Test
    fun `Given list contains loading items, when adding error entry, then remove loading entries and add error entry`() =
        runTest {
            // Given
            val list = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(
                    Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 2,
                        name = "Ivysaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 3,
                        name = "Venusaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Loading,
                ),
            )

            // When
            val result = mapper.addErrorEntry(list, TestException)

            // Then
            result assertEquals PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(
                    Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 2,
                        name = "Ivysaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Entry(
                        id = 3,
                        name = "Venusaur",
                        imageUrl = "",
                        type = DualType(Grass, Poison),
                    ),
                    Error(GenericError.Unknown),
                ),
            )
        }
}
