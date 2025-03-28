package com.patrickhoette.pokemon.domain.list

import app.cash.turbine.test
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.factory.pokemon.PokemonListFactory
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
class ObservePokemonListTest {

    @MockK
    private lateinit var repository: PokemonListRepository

    @InjectMockKs
    private lateinit var observePokemonList: ObservePokemonList

    @Test
    fun `Given observing fails, when invoked, then throw error`() = runTest {
        // Given
        every { repository.observePokemonList() } throws TestException

        // When -> Then
        assertTestException {
            observePokemonList().test { cancelAndIgnoreRemainingEvents() }
        }
    }

    @Test
    fun `Given error is emitted, when invoked, then emit error`() = runTest {
        // Given
        every { repository.observePokemonList() } returns flow { throw TestException }

        // When
        observePokemonList().test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given list is emitted, when invoked, then emit list`() = runTest {
        // Given
        val list = PokemonListFactory.create()
        every { repository.observePokemonList() } returns flowOf(list)

        // When
        observePokemonList().test {
            // Then
            awaitItem() assertEquals list
            awaitComplete()
        }
    }
}
