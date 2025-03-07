package com.patrickhoette.pokemon.data.list

import app.cash.turbine.test
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.coVerifyNever
import com.patrickhoette.test.model.TestException
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PersistentPokemonListRepositoryTest {

    @MockK
    private lateinit var source: PokemonListSource

    @MockK
    private lateinit var store: PokemonListStore

    @InjectMockKs
    private lateinit var repository: PersistentPokemonListRepository

    @Test
    fun `Given observing current page fails, when observing pokemon list, then throw error`() = runTest {
        // Given
        every { store.observeCurrentPage() } throws TestException

        // When -> Then
        assertTestException { repository.observePokemonList() }
    }

    @Test
    fun `Given current page emits error, when observing pokemon list, then emit error`() = runTest {
        // Given
        every { store.observeCurrentPage() } returns flow { throw TestException }
        every { store.currentPage } returns 0
        coEvery { store.getPageStatus(any(), any()) } returns Available

        // When
        repository.observePokemonList().test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given observing pokemon list fails, when observing pokemon list, then emit error`() = runTest {
        // Given
        every { store.observePokemonList(any(), any()) } throws TestException
        every { store.observeCurrentPage() } returns flowOf(0)
        every { store.currentPage } returns 0
        coEvery { store.getPageStatus(any(), any()) } returns Available

        // When
        repository.observePokemonList().test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given store emits error, when observing pokemon list, then emit error`() = runTest {
        // Given
        every { store.observePokemonList(any(), any()) } returns flow { throw TestException }
        every { store.observeCurrentPage() } returns flowOf(0)
        every { store.currentPage } returns 0
        coEvery { store.getPageStatus(any(), any()) } returns Available

        // When
        repository.observePokemonList().test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given page is available in cache, when fetching next page, then increment page`() = runTest {
        // Given
        coEvery { store.getPageStatus(1, 20) } returns Available
        every { store.currentPage } returns 0
        coEvery { store.incrementCurrentPage() } just runs

        // When
        repository.fetchNextPokemonPage()

        // Then
        coVerify { store.incrementCurrentPage() }
    }

    @Test
    fun `Given page is 0 and fetching fails, when fetching next page, then throw error and do not increment current page`() =
        runTest {
            // Given
            every { store.currentPage } returns 0
            coEvery { store.getPageStatus(1, 20) } returns Missing
            coEvery { source.fetchPokemonPage(any(), any()) } throws TestException

            // When -> Then
            assertTestException { repository.fetchNextPokemonPage() }
            coVerifyNever { store.incrementCurrentPage() }
        }

    @Test
    fun `Given page is 0 and fetching succeeds, when fetching next page, then store pokemon and increment current page`() =
        runTest {
            // Given
            val pokemon = listOf(
                Pokemon(id = 1, name = "Bulbasaur"),
                Pokemon(id = 2, name = "Ivysaur"),
                Pokemon(id = 3, name = "Venusaur"),
            )
            every { store.currentPage } returns 0
            coEvery { store.getPageStatus(1, 20) } returns Stale
            coEvery { source.fetchPokemonPage(20, 20) } returns PokemonList(
                maxCount = 1304,
                pokemon = pokemon,
            )
            coEvery { store.storePokemon(pokemon) } just runs
            coEvery { store.incrementCurrentPage() } just runs

            // When
            repository.fetchNextPokemonPage()

            // Then
            coVerify { store.storePokemon(pokemon) }
            coVerify { store.incrementCurrentPage() }
        }

    @Test
    fun `Given page is 1 but fetching fails, when fetching next page, then throw error and do not increment current page`() =
        runTest {
            // Given
            every { store.currentPage } returns 1
            coEvery { store.getPageStatus(2, 20) } returns Missing
            coEvery { source.fetchPokemonPage(any(), any()) } throws TestException

            // When -> Then
            assertTestException { repository.fetchNextPokemonPage() }
            coVerifyNever { store.incrementCurrentPage() }
        }

    @Test
    fun `Given page is 1, when fetching next page, then fetch and store the next page`() = runTest {
        // Given
        val pokemon = listOf(
            Pokemon(id = 1, name = "Bulbasaur"),
            Pokemon(id = 2, name = "Ivysaur"),
            Pokemon(id = 3, name = "Venusaur"),
        )
        every { store.currentPage } returns 1
        coEvery { store.getPageStatus(2, 20) } returns Stale
        coEvery { source.fetchPokemonPage(40, 20) } returns PokemonList(
            maxCount = 1304,
            pokemon = pokemon,
        )
        coEvery { store.storePokemon(pokemon) } just runs
        coEvery { store.incrementCurrentPage() } just runs

        // When
        repository.fetchNextPokemonPage()

        // Then
        coVerify { store.storePokemon(pokemon) }
        coVerify { store.incrementCurrentPage() }
    }

    @Test
    fun `Given page is 0, when observing pokemon list, then observe a single page from the data store`() =
        runTest {
            // Given
            every { store.observeCurrentPage() } returns flowOf(0)
            every { store.observePokemonList(1, 20) } returns flowOf()
            every { store.currentPage } returns 0
            coEvery { store.getPageStatus(1, 20) } returns Available

            // When
            repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

            // Then
            coVerify { store.observePokemonList(1, 20) }
        }

    @Test
    fun `Given page is 0 and page is not available in cache, when observing pokemon list, then fetch and store the first page`() =
        runTest {
            // Given
            val pokemon = listOf(
                Pokemon(id = 1, name = "Bulbasaur"),
                Pokemon(id = 2, name = "Ivysaur"),
                Pokemon(id = 3, name = "Venusaur"),
            )
            every { store.observeCurrentPage() } returns flowOf(0)
            every { store.observePokemonList(1, 20) } returns flowOf()
            every { store.currentPage } returns 0
            coEvery { store.getPageStatus(1, 20) } returns Missing
            coEvery { source.fetchPokemonPage(0, 20) } returns PokemonList(
                maxCount = 1304,
                pokemon = pokemon,
            )
            coEvery { store.storePokemon(pokemon) }

            // When
            repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

            // Then
            coVerify { source.fetchPokemonPage(0, 20) }
            coVerify { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given observed before, when observing again, then fetch and store the first page`() = runTest {
        // Given
        val pokemon = listOf(
            Pokemon(id = 1, name = "Bulbasaur"),
            Pokemon(id = 2, name = "Ivysaur"),
            Pokemon(id = 3, name = "Venusaur"),
        )
        every { store.observeCurrentPage() } returns flowOf(0)
        every { store.observePokemonList(1, 20) } returns flowOf()
        every { store.currentPage } returns 0
        coEvery { store.getPageStatus(1, 20) } returns Missing
        coEvery { source.fetchPokemonPage(0, 20) } returns PokemonList(
            maxCount = 1304,
            pokemon = pokemon,
        )
        coEvery { store.storePokemon(pokemon) } just runs

        // When
        repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }
        repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

        // Then
        coVerify(exactly = 2) { source.fetchPokemonPage(0, 20) }
        coVerify(exactly = 2) { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given first page is available in the cache, when observing again, then do not fetch and store the first page`() =
        runTest {
            // Given
            every { store.observeCurrentPage() } returns flowOf(0)
            every { store.observePokemonList(1, 20) } returns flowOf()
            every { store.currentPage } returns 0
            coEvery { store.getPageStatus(1, 20) } returns Available

            // When
            repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

            // Then
            coVerifyNever { source.fetchPokemonPage(0, 20) }
            coVerifyNever { store.storePokemon(any()) }
        }

    @Test
    fun `Given page is 3 but all pages are available in cache, when observing list, then do not fetch or store anything`() =
        runTest {
            // Given
            every { store.observeCurrentPage() } returns flowOf(2)
            every { store.observePokemonList(3, 20) } returns flowOf()
            every { store.currentPage } returns 2
            coEvery { store.getPageStatus(1, 20) } returns Available
            coEvery { store.getPageStatus(2, 20) } returns Available
            coEvery { store.getPageStatus(3, 20) } returns Available

            // When
            repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

            // Then
            coVerifyNever { source.fetchPokemonPage(any(), any()) }
            coVerifyNever { store.storePokemon(any()) }
        }

    @Test
    fun `Given page is 3 but all pages are stale or missing, when observing list, then make 1 call for all the pages and store them`() =
        runTest {
            // Given
            val pokemon = listOf(
                Pokemon(id = 1, name = "Bulbasaur"),
                Pokemon(id = 2, name = "Ivysaur"),
                Pokemon(id = 3, name = "Venusaur"),
            )
            every { store.observeCurrentPage() } returns flowOf(2)
            every { store.observePokemonList(3, 20) } returns flowOf()
            every { store.currentPage } returns 2
            coEvery { store.getPageStatus(1, 20) } returns Missing
            coEvery { store.getPageStatus(2, 20) } returns Stale
            coEvery { store.getPageStatus(3, 20) } returns Missing
            coEvery { source.fetchPokemonPage(0, 60) } returns PokemonList(
                maxCount = 1304,
                pokemon = pokemon,
            )

            // When
            repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

            // Then
            coVerify { source.fetchPokemonPage(0, 60) }
            coVerify { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given page is 10 and some pages are stale or missing, when observing list, then make optimized calls for pages and store them`() =
        runTest {
            // Given
            val pokemonOne = listOf(
                Pokemon(id = 1, name = "Bulbasaur"),
                Pokemon(id = 2, name = "Ivysaur"),
                Pokemon(id = 3, name = "Venusaur"),
            )
            val pokemonTwo = listOf(
                Pokemon(id = 4, name = "Charmander"),
                Pokemon(id = 5, name = "Charmeleon"),
                Pokemon(id = 6, name = "Charizard"),
            )
            val pokemonThree = listOf(
                Pokemon(id = 7, name = "Squirtle"),
                Pokemon(id = 8, name = "Wartortle"),
                Pokemon(id = 9, name = "Blastoise"),
            )
            every { store.observeCurrentPage() } returns flowOf(9)
            every { store.observePokemonList(10, 20) } returns flowOf()
            every { store.currentPage } returns 9
            coEvery { store.getPageStatus(1, 20) } returns Missing
            coEvery { store.getPageStatus(2, 20) } returns Available
            coEvery { store.getPageStatus(3, 20) } returns Available
            coEvery { store.getPageStatus(4, 20) } returns Missing
            coEvery { store.getPageStatus(5, 20) } returns Stale
            coEvery { store.getPageStatus(6, 20) } returns Available
            coEvery { store.getPageStatus(7, 20) } returns Stale
            coEvery { store.getPageStatus(8, 20) } returns Stale
            coEvery { store.getPageStatus(9, 20) } returns Missing
            coEvery { store.getPageStatus(10, 20) } returns Available
            coEvery { source.fetchPokemonPage(0, 20) } returns PokemonList(
                maxCount = 1304,
                pokemon = pokemonOne,
            )
            coEvery { source.fetchPokemonPage(60, 40) } returns PokemonList(
                maxCount = 1304,
                pokemon = pokemonTwo,
            )
            coEvery { source.fetchPokemonPage(120, 60) } returns PokemonList(
                maxCount = 1304,
                pokemon = pokemonThree,
            )
            coEvery { store.storePokemon(pokemonOne) } just runs
            coEvery { store.storePokemon(pokemonTwo) } just runs
            coEvery { store.storePokemon(pokemonThree) } just runs

            // When
            repository.observePokemonList().test { cancelAndIgnoreRemainingEvents() }

            // Then
            coVerify { source.fetchPokemonPage(0, 20) }
            coVerify { source.fetchPokemonPage(60, 40) }
            coVerify { source.fetchPokemonPage(120, 60) }
            coVerify { store.storePokemon(pokemonOne) }
            coVerify { store.storePokemon(pokemonTwo) }
            coVerify { store.storePokemon(pokemonThree) }
        }
}
