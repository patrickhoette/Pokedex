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
        val page = 1
        val size = 20
        coEvery { repository.fetchNextPokemonPage(page, size) } throws TestException

        // When -> Then
        assertTestException { fetchNextPokemonPage(page, size) }
    }

    @Test
    fun `Given fetching succeeds, when invoked, then succeed`() = runTest {
        // Given
        val page = 2
        val size = 30
        coEvery { repository.fetchNextPokemonPage(page, size) } just runs

        // When -> Then
        assertSucceeds { fetchNextPokemonPage(page, size) }
    }
}
