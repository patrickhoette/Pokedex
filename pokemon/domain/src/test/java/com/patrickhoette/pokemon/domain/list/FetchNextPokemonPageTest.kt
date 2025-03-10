package com.patrickhoette.pokemon.domain.list

import com.patrickhoette.test.assertSucceeds
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.model.TestException
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FetchNextPokemonPageTest {

    @MockK
    private lateinit var repository: PokemonListRepository

    @InjectMockKs
    private lateinit var fetchNextPokemonPage: FetchNextPokemonPage

    @Test
    fun `Given fetching fails, when invoked, then throw error`() = runTest {
        // Given
        coEvery { repository.fetchNextPokemonPage() } throws TestException

        // When -> Then
        assertTestException { fetchNextPokemonPage() }
    }

    @Test
    fun `Given fetching succeeds, when invoked, then succeed`() = runTest {
        // Given
        coEvery { repository.fetchNextPokemonPage() } just runs

        // When -> Then
        assertSucceeds { fetchNextPokemonPage() }
    }
}
