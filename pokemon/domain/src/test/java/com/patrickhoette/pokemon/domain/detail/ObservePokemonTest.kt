package com.patrickhoette.pokemon.domain.detail

import app.cash.turbine.test
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.factory.pokemon.PokemonFactory
import com.patrickhoette.test.model.TestException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ObservePokemonTest {

    @MockK
    private lateinit var repository: PokemonDetailRepository

    @InjectMockKs
    private lateinit var observePokemon: ObservePokemon

    @Test
    fun `Given observing pokemon fails, when invoked, then throw error`() = runTest {
        // Given
        every { repository.observePokemon(any()) } throws TestException

        // When -> Then
        assertTestException { observePokemon(1).test { cancelAndIgnoreRemainingEvents() } }
    }

    @Test
    fun `Given observing pokemon emits error, when invoked, then emit error`() = runTest {
        // Given
        every { repository.observePokemon(any()) } returns flow { throw TestException }

        // When
        observePokemon(1).test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given observing pokemon emits, when invoked, then emit`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        every { observePokemon(pokemon.id) } returns flowOf(pokemon)

        // When
        observePokemon(pokemon.id).test {
            // Then
            awaitItem() assertEquals pokemon
            awaitComplete()
        }
    }
}
