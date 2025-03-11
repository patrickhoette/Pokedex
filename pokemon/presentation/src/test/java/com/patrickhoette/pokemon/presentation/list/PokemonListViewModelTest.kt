package com.patrickhoette.pokemon.presentation.list

import app.cash.turbine.test
import com.patrickhoette.core.presentation.model.GenericError.Unknown
import com.patrickhoette.core.presentation.model.TypedUIState.*
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.domain.list.FetchNextPokemonPage
import com.patrickhoette.pokemon.domain.list.ObservePokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEvent.ShowError
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.test.android.ArchComponentsExtension
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.coroutine.TestDispatcherProvider
import com.patrickhoette.test.model.TestException
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
import org.junit.jupiter.api.RepeatedTest
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

    @RepeatedTest(200)
    fun `When starting to collect state, then state should be loading`() = runTest {
        // Given
        every { observePokemonList() } returns flowOf()

        // When
        viewModel.state.test {
            // Then
            awaitItem() assertEquals Loading
        }
    }

    @RepeatedTest(200)
    fun `When starting to collect state, then observe pokemon list`() = runTest {
        // Given
        every { observePokemonList() } returns flowOf()

        // When
        viewModel.state.test { cancelAndIgnoreRemainingEvents() }

        // Then
        verify { observePokemonList() }
    }

    @RepeatedTest(200)
    fun `Given observing pokemon list fails, when starting to collect state, then state should be error`() = runTest {
        // Given
        every { observePokemonList() } throws TestException

        // When
        viewModel.state.test {
            // Then
            awaitItem() assertEquals Error(Unknown)
        }
    }

    @RepeatedTest(200)
    fun `Given observing pokemon list succeeds, when starting to collect state, then state should be normal with mapped model`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = true,
                pokemon = emptyList(),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                pokemon = persistentListOf(),
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel

            // When
            viewModel.state.test {
                // Then
                awaitItem() assertEquals Normal(mappedModel)
            }
        }

    @RepeatedTest(200)
    fun `Given last pokemon list item is end, when get more pokemon has been selected, then do not update normal state`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = true,
                pokemon = emptyList(),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                pokemon = persistentListOf(
                    PokemonListEntryUIModel.Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                    ),
                    PokemonListEntryUIModel.End,
                ),
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel

            // When
            viewModel.state.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                expectNoEvents()
            }
        }

    @RepeatedTest(200)
    fun `Given last pokemon list item is loading, when get more pokemon has been selected, then do not update normal state`() =
        runTest {
            // Given
            val model = PokemonList(
                maxCount = 1304,
                hasNext = true,
                pokemon = emptyList(),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                pokemon = persistentListOf(
                    PokemonListEntryUIModel.Entry(
                        id = 1,
                        name = "Bulbasaur",
                        imageUrl = "",
                    ),
                    PokemonListEntryUIModel.Loading,
                ),
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel
            coEvery { fetchNextPokemonPage() } just awaits

            // When
            viewModel.state.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                expectNoEvents()
            }
        }

    @RepeatedTest(200)
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
                pokemon = persistentListOf(),
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel

            // When
            viewModel.state.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                expectNoEvents()
            }
        }

    @RepeatedTest(200)
    fun `Given list does have next, when get more pokemon has been selected, then state should be normal with previous pokemon list + 5 loading items`() =
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
                ),
                PokemonListEntryUIModel.Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                ),
                PokemonListEntryUIModel.Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                ),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                pokemon = entries,
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel
            coEvery { fetchNextPokemonPage() } just awaits

            // When
            viewModel.state.test {
                skipItems(1)
                viewModel.onGetMorePokemon()

                // Then
                awaitItem().normalDataOrNull()?.pokemon assertEquals entries + List(5) {
                    PokemonListEntryUIModel.Loading
                }
            }
        }

    @RepeatedTest(200)
    fun `Given fetching next page fails, when get more pokemon has been selected, then state should be normal without loading items`() =
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
                ),
                PokemonListEntryUIModel.Entry(
                    id = 2,
                    name = "Ivysaur",
                    imageUrl = "",
                ),
                PokemonListEntryUIModel.Entry(
                    id = 3,
                    name = "Venusaur",
                    imageUrl = "",
                ),
            )
            val mappedModel = PokemonListUIModel(
                hasNext = true,
                pokemon = entries,
            )
            every { observePokemonList() } returns flowOf(model)
            every { mapper.mapToUIModel(model) } returns mappedModel
            coEvery { fetchNextPokemonPage() } throws TestException

            // When
            viewModel.state.test {
                skipItems(1)
                viewModel.onGetMorePokemon()
                skipItems(1)

                // Then
                awaitItem().normalDataOrNull()?.pokemon assertEquals entries
            }
        }

    @RepeatedTest(200)
    fun `Given fetching next page fails, when get more pokemon has been selected, then show error`() = runTest {
        // Given
        val model = PokemonList(
            maxCount = 1304,
            hasNext = true,
            pokemon = emptyList(),
        )
        val mappedModel = PokemonListUIModel(
            hasNext = true,
            pokemon = persistentListOf(),
        )
        every { observePokemonList() } returns flowOf(model)
        every { mapper.mapToUIModel(model) } returns mappedModel
        coEvery { fetchNextPokemonPage() } throws TestException

        // When
        viewModel.state.test { cancelAndIgnoreRemainingEvents() }
        viewModel.events.test {
            skipItems(1)
            viewModel.onGetMorePokemon()

            // Then
            awaitItem()?.retrieve() assertEquals ShowError(Unknown)
        }
    }
}
