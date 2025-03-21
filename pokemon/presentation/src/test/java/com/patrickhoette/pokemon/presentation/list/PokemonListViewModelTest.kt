package com.patrickhoette.pokemon.presentation.list

import app.cash.turbine.test
import com.patrickhoette.core.presentation.model.GenericError.Unknown
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.domain.list.FetchNextPokemonPage
import com.patrickhoette.pokemon.domain.list.ObservePokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEvent.OpenPokemonDetails
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.MonoType
import com.patrickhoette.test.android.ArchComponentsExtension
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.coroutine.TestDispatcherProvider
import com.patrickhoette.test.model.TestException
import com.patrickhoette.test.verifyNever
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, ArchComponentsExtension::class)
class PokemonListViewModelTest {

    @MockK
    private lateinit var observePokemonList: ObservePokemonList

    @MockK
    private lateinit var fetchNextPokemonPage: FetchNextPokemonPage

    @MockK
    private lateinit var mapper: PokemonListUIMapper

    private val dispatchers = TestDispatcherProvider()

    @InjectMockKs
    private lateinit var viewModel: PokemonListViewModel

    @BeforeEach
    fun setupAll() {
        Dispatchers.setMain(dispatchers.Main)
    }

    @AfterEach
    fun tearDownAll() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When starting to collect state, then state should be loading list`() = runTest {
        // Given
        val loadingPokemonList = PokemonListUIModel(
            hasNext = false,
            entries = persistentListOf(),
        )
        every { observePokemonList() } returns flowOf()
        every { mapper.createLoading() } returns loadingPokemonList

        // When
        viewModel.list.test {
            // Then
            awaitItem() assertEquals loadingPokemonList
        }
    }

    @Test
    fun `When starting to collect state, then observe pokemon list`() = runTest {
        // Given
        val loadingPokemonList = PokemonListUIModel(
            hasNext = false,
            entries = persistentListOf(),
        )
        every { mapper.createLoading() } returns loadingPokemonList
        every { observePokemonList() } returns flowOf()

        // When
        viewModel.list.test { cancelAndIgnoreRemainingEvents() }

        // Then
        verify { observePokemonList() }
    }

    @Test
    fun `Given observing pokemon list fails, when starting to collect state, then state should be error`() = runTest {
        // Given
        val loadingPokemonList = PokemonListUIModel(
            hasNext = false,
            entries = persistentListOf(),
        )
        val errorPokemonList = PokemonListUIModel(
            hasNext = false,
            entries = persistentListOf(PokemonListEntryUIModel.Error(Unknown)),
        )
        every { mapper.createLoading() } returns loadingPokemonList
        every { mapper.addErrorEntry(loadingPokemonList, TestException) } returns errorPokemonList
        every { observePokemonList() } throws TestException

        // When
        viewModel.list.test {
            // Then
            awaitItem() assertEquals errorPokemonList
        }
    }

    @Test
    fun `Given observing pokemon list succeeds, when starting to collect state, then state should be mapped model`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = true,
                pokemon = emptyList(),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(),
            )
            val loadingPokemonList = PokemonListUIModel(
                hasNext = false,
                entries = persistentListOf(),
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.createLoading() } returns loadingPokemonList
            every { mapper.mapToUIModel(model) } returns mappedModel

            // When
            viewModel.list.test {
                // Then
                awaitItem() assertEquals mappedModel
            }
        }

    @Test
    fun `Given last pokemon list item is loading, when get more pokemon has been selected, then do not add loading entries`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = true,
                pokemon = emptyList(),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(
                    PokemonListEntryUIModel.Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                        type = MonoType(Type.Unknown),
                    ),
                    PokemonListEntryUIModel.Loading,
                ),
            )
            val loadingPokemonList = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(PokemonListEntryUIModel.Loading),
            )
            every { mapper.createLoading() } returns loadingPokemonList
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel
            coEvery { fetchNextPokemonPage() } just awaits

            // When
            viewModel.list.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                expectNoEvents()
            }
            verifyNever { mapper.addLoadingEntries(any()) }
        }

    @Test
    fun `Given list does not have next, when get more pokemon has been selected, then do not update normal state and do not fetch next page`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = false,
                pokemon = emptyList(),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = false,
                entries = persistentListOf(),
            )
            val loadingPokemonList = PokemonListUIModel(
                hasNext = false,
                entries = persistentListOf(),
            )
            every { mapper.createLoading() } returns loadingPokemonList
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel

            // When
            viewModel.list.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                expectNoEvents()
            }
        }

    @Test
    fun `Given list does have next, when get more pokemon has been selected, then state should be add loading entries`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = true,
                pokemon = emptyList(),
            )
            val entries = persistentListOf(
                PokemonListEntryUIModel.Entry(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = "",
                    type = MonoType(Type.Unknown),
                ),
                PokemonListEntryUIModel.Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                    type = MonoType(Type.Unknown),
                ),
                PokemonListEntryUIModel.Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                    type = MonoType(Type.Unknown),
                ),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                entries = entries,
            )
            val loadingPokemonList = PokemonListUIModel(
                hasNext = true,
                entries = persistentListOf(PokemonListEntryUIModel.Loading),
            )
            every { mapper.createLoading() } returns loadingPokemonList
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel
            coEvery { fetchNextPokemonPage() } just awaits
            every { mapper.addLoadingEntries(mappedModel) } returns loadingPokemonList

            // When
            viewModel.list.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                awaitItem() assertEquals loadingPokemonList
            }
        }

    @Test
    fun `Given fetching next page fails, when get more pokemon has been selected, then add error entry`() = runTest {
        // Given
        val model = PokemonList(
            maxCount = 1304,
            hasNext = true,
            pokemon = emptyList(),
        )
        val mappedModel = PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(),
        )
        val errorPokemonList = PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(PokemonListEntryUIModel.Error(Unknown)),
        )
        val loadingPokemonList = PokemonListUIModel(
            hasNext = true,
            entries = persistentListOf(PokemonListEntryUIModel.Loading),
        )
        every { mapper.createLoading() } returns loadingPokemonList
        every { observePokemonList() } returns flowOf(model)
        every { mapper.mapToUIModel(model) } returns mappedModel
        coEvery { fetchNextPokemonPage() } throws TestException
        every { mapper.addErrorEntry(loadingPokemonList, TestException) } returns errorPokemonList
        every { mapper.addLoadingEntries(mappedModel) } returns loadingPokemonList

        // When
        viewModel.list.test {
            skipItems(1)
            viewModel.onGetMorePokemon()

            // Then
            awaitItem() assertEquals errorPokemonList
        }
    }

    @Test
    fun `When pokemon has been selected, then open pokemon details`() = runTest {
        // Given
        val selectedEntry = PokemonListEntryUIModel.Entry(
            id = 1,
            name = "Bulbasaur",
            imageUrl = "",
            type = MonoType(Type.Unknown),
        )
        val loadingPokemonList = PokemonListUIModel(
            hasNext = false,
            entries = persistentListOf(),
        )
        every { mapper.createLoading() } returns loadingPokemonList

        // When
        viewModel.events.test {
            skipItems(1)
            viewModel.onPokemon(selectedEntry)

            // Then
            awaitItem()?.retrieve() assertEquals OpenPokemonDetails(selectedEntry.id)
        }
    }
}
