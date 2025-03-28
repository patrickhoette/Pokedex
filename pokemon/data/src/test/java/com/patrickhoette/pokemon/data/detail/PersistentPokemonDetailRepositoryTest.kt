package com.patrickhoette.pokemon.data.detail

import app.cash.turbine.test
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.coVerifyNever
import com.patrickhoette.test.factory.pokemon.PokemonFactory
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
class PersistentPokemonDetailRepositoryTest {

    @MockK
    private lateinit var source: PokemonDetailSource

    @MockK
    private lateinit var store: PokemonDetailStore

    @InjectMockKs
    private lateinit var repository: PersistentPokemonDetailRepository

    @Test
    fun `Given observing pokemon with the store fails, when observing pokemon, then throw error`() = runTest {
        // Given
        every { store.observePokemon(any()) } throws TestException

        // When -> Then
        assertTestException { repository.observePokemon(1).test { cancelAndIgnoreRemainingEvents() } }
    }

    @Test
    fun `Given observing pokemon with store emits error, when observing pokemon, then emit error`() = runTest {
        // Given
        coEvery { store.getPokemonStatus(any()) } returns Available
        every { store.observePokemon(any()) } returns flow { throw TestException }

        // When
        repository.observePokemon(1).test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given observing pokemon with store emits item, when observing pokemon, then emit`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { store.getPokemonStatus(pokemon.id) } returns Available
        every { store.observePokemon(pokemon.id) } returns flowOf(pokemon)

        // When
        repository.observePokemon(pokemon.id).test {
            // Then
            awaitItem() assertEquals pokemon
            awaitComplete()
        }
    }

    @Test
    fun `Given checking pokemon status fails, when observing pokemon, then emit error`() = runTest {
        // Given
        every { store.observePokemon(any()) } returns flow {}
        coEvery { store.getPokemonStatus(any()) } throws TestException

        // When
        repository.observePokemon(1).test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given pokemon is available in the cache, when observing pokemon, then don't fetch pokemon`() = runTest {
        // Given
        val id = 1
        every { store.observePokemon(any()) } returns flow {}
        coEvery { store.getPokemonStatus(any()) } returns Available

        // When
        repository.observePokemon(id).test { cancelAndIgnoreRemainingEvents() }

        // Then
        coVerify { store.getPokemonStatus(id) }
        coVerifyNever { store.storePokemon(any()) }
    }

    @Test
    fun `Given pokemon is stale in the cache but fetching fails, when observing pokemon, then do not store pokemon`() =
        runTest {
            // Given
            val id = 1
            every { store.observePokemon(any()) } returns flow {}
            coEvery { store.getPokemonStatus(any()) } returns Stale
            coEvery { source.fetchPokemon(id) } throws TestException

            // When
            repository.observePokemon(id).test {
                awaitComplete()
            }

            // Then
            coVerify { store.getPokemonStatus(id) }
            coVerify { source.fetchPokemon(id) }
            coVerifyNever { store.storePokemon(any()) }
        }

    @Test
    fun `Given pokemon is stale in the cache, when observing pokemon, then store the new pokemon`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        every { store.observePokemon(any()) } returns flow {}
        coEvery { store.getPokemonStatus(any()) } returns Stale
        coEvery { source.fetchPokemon(pokemon.id) } returns pokemon
        coEvery { store.storePokemon(pokemon) } just runs

        // When
        repository.observePokemon(pokemon.id).test { cancelAndIgnoreRemainingEvents() }

        // Then
        coVerify { store.getPokemonStatus(pokemon.id) }
        coVerify { source.fetchPokemon(pokemon.id) }
        coVerify { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given pokemon is stale in the cache but storing fails, when observing pokemon, then emit error`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        every { store.observePokemon(any()) } returns flow {}
        coEvery { store.getPokemonStatus(any()) } returns Stale
        coEvery { source.fetchPokemon(pokemon.id) } returns pokemon
        coEvery { store.storePokemon(pokemon) } throws TestException

        // When
        repository.observePokemon(pokemon.id).test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given pokemon is missing in the cache but fetching fails, when observing pokemon, then emit error and don't store anything`() =
        runTest {
            // Given
            val id = 1
            every { store.observePokemon(any()) } returns flow {}
            coEvery { store.getPokemonStatus(any()) } returns Missing
            coEvery { source.fetchPokemon(id) } throws TestException

            // When
            repository.observePokemon(id).test {
                awaitError() assertEquals TestException
            }

            // Then
            coVerify { store.getPokemonStatus(id) }
            coVerify { source.fetchPokemon(id) }
            coVerifyNever { store.storePokemon(any()) }
        }

    @Test
    fun `Given pokemon is missing in the cache, when observing pokemon, then store the new pokemon`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        every { store.observePokemon(any()) } returns flow {}
        coEvery { store.getPokemonStatus(any()) } returns Missing
        coEvery { source.fetchPokemon(pokemon.id) } returns pokemon
        coEvery { store.storePokemon(pokemon) } just runs

        // When
        repository.observePokemon(pokemon.id).test { cancelAndIgnoreRemainingEvents() }

        // Then
        coVerify { store.getPokemonStatus(pokemon.id) }
        coVerify { source.fetchPokemon(pokemon.id) }
        coVerify { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given pokemon is missing in the cache but storing fails, when observing pokemon, then emit error`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        every { store.observePokemon(any()) } returns flow {}
        coEvery { store.getPokemonStatus(any()) } returns Missing
        coEvery { source.fetchPokemon(pokemon.id) } returns pokemon
        coEvery { store.storePokemon(pokemon) } throws TestException

        // When
        repository.observePokemon(pokemon.id).test {
            // Then
            awaitError() assertEquals TestException
        }
    }
}
