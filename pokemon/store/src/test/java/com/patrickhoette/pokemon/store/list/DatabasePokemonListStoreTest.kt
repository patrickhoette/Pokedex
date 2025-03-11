package com.patrickhoette.pokemon.store.list

import app.cash.sqldelight.SuspendingTransactionWithoutReturn
import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.turbine.test
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import com.patrickhoette.pokemon.store.database.pokemon.PokemonListQueries
import com.patrickhoette.pokemon.store.database.pokemon.PokemonQueries
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertNull
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.coroutine.TestDispatcherProvider
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
import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import com.patrickhoette.pokemon.store.database.pokemon.Pokemon as PokemonEntry

@ExtendWith(MockKExtension::class)
class DatabasePokemonListStoreTest {

    @MockK
    private lateinit var pokemonQueries: PokemonQueries

    @MockK
    private lateinit var pokemonListQueries: PokemonListQueries

    @MockK
    private lateinit var mapper: PokemonListEntryMapper

    private val dispatchers = TestDispatcherProvider

    @InjectMockKs
    private lateinit var store: DatabasePokemonListStore

    @Test
    fun `Given page has not been incremented, when checking current page, then return 0`() = runTest {
        // When
        val result = store.currentPage

        // Then
        result assertEquals 0
    }

    @Test
    fun `Given page has been incremented, when checking current page, then return 1`() = runTest {
        // Given
        store.incrementCurrentPage()

        // When
        val result = store.currentPage

        // Then
        result assertEquals 1
    }

    @Test
    fun `Given page has been multiple times, when checking current page, then return correct page number`() = runTest {
        // Given
        val increments = 5
        repeat(increments) { store.incrementCurrentPage() }

        // When
        val result = store.currentPage

        // Then
        result assertEquals increments
    }

    @Test
    fun `Given page has not been incremented, when observing current page, then emit 0`() = runTest {
        // When
        store.observeCurrentPage().test {
            // Then
            awaitItem() assertEquals 0
        }
    }

    @Test
    fun `Given page has been incremented, when observing current page, then emit 1`() = runTest {
        // Given
        store.incrementCurrentPage()

        // When
        store.observeCurrentPage().test {
            // Then
            awaitItem() assertEquals 1
        }
    }

    @Test
    fun `Given page has been multiple times, when observing current page, then emit correct page number`() = runTest {
        // Given
        val increments = 5
        repeat(increments) { store.incrementCurrentPage() }

        // When
        store.observeCurrentPage().test {
            // Then
            awaitItem() assertEquals increments
        }
    }

    @Test
    fun `Given page is being incremented while observed, when observing current page, then emit new page after every increment`() =
        runTest {
            // When -> Then
            store.observeCurrentPage().test {
                awaitItem() assertEquals 0
                store.incrementCurrentPage()
                awaitItem() assertEquals 1
                store.incrementCurrentPage()
                awaitItem() assertEquals 2
            }
        }

    @Test
    fun `Given transaction on pokemon queries fails, when inserting pokemon list, then throw error`() = runTest {
        // Given
        coEvery { pokemonQueries.transaction(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.storePokemonList(mockk()) }
    }

    @Test
    fun `Given inserting pokemon fails, when inserting pokemon list, then throw error`() = runTest {
        // Given
        val pokemon = Pokemon(
            id = 1,
            name = "bulbasaur",
        )
        val pokemonList = PokemonList(
            maxCount = 1304,
            hasNext = true,
            pokemon = listOf(pokemon),
        )
        val entry = PokemonEntry(
            id = 1,
            name = "bulbasaur",
            lastUpdate = 0,
        )
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.insert(entry) } throws TestException
        every { mapper.mapToEntry(pokemon) } returns entry

        // When -> Then
        assertTestException { store.storePokemonList(pokemonList) }
    }

    @Test
    fun `Given transaction on pokemon list queries fails, when inserting pokemon list, then throw error`() = runTest {
        // Given
        coEvery { pokemonQueries.transaction(any(), any()) } just runs
        coEvery { pokemonListQueries.transaction(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.storePokemonList(mockk()) }
    }

    @Test
    fun `Given inserting max count fails, when inserting pokemon list, then throw error`() = runTest {
        // Given
        val maxCount = 1304
        val pokemonList = PokemonList(
            maxCount = maxCount,
            hasNext = true,
            pokemon = emptyList(),
        )
        coEvery { pokemonQueries.transaction(any(), any()) } just runs
        coEvery { pokemonListQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonListQueries.insert(any()) } throws TestException

        // When -> Then
        assertTestException { store.storePokemonList(pokemonList) }
    }

    @Test
    fun `When inserting pokemon list, then insert all pokemon and max count`() = runTest {
        // Given
        val maxCount = 1304
        val pokemonOne = Pokemon(
            id = 1,
            name = "bulbasaur",
        )
        val pokemonTwo = Pokemon(
            id = 2,
            name = "ivysaur",
        )
        val pokemonThree = Pokemon(
            id = 3,
            name = "venusaur",
        )
        val list = PokemonList(
            maxCount = maxCount,
            hasNext = true,
            pokemon = listOf(pokemonOne, pokemonTwo, pokemonThree),
        )
        val entryOne = PokemonEntry(
            id = 1,
            name = "bulbasaur",
            lastUpdate = 0,
        )
        val entryTwo = PokemonEntry(
            id = 2,
            name = "ivysaur",
            lastUpdate = 0,
        )
        val entryThree = PokemonEntry(
            id = 3,
            name = "venusaur",
            lastUpdate = 0,
        )
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonListQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        every { mapper.mapToEntry(pokemonOne) } returns entryOne
        every { mapper.mapToEntry(pokemonTwo) } returns entryTwo
        every { mapper.mapToEntry(pokemonThree) } returns entryThree
        coEvery { pokemonQueries.insert(any()) } just runs
        coEvery { pokemonListQueries.insert(any()) } just runs

        // When
        store.storePokemonList(list)

        // Then
        coVerify { pokemonQueries.insert(entryOne) }
        coVerify { pokemonQueries.insert(entryTwo) }
        coVerify { pokemonQueries.insert(entryThree) }
        coVerify { pokemonListQueries.insert(maxCount.toLong()) }
    }

    @Test
    fun `Given checking if full page in database fails, when getting page status, then throw error`() = runTest {
        // Given
        val page = 1
        val pageSize = 20
        coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.getPageStatus(page, pageSize) }
    }

    @Test
    fun `Given first page and page size of 20, when getting page status, then check full page in database with offset 0 and page size of 20`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val page = 1
            val pageSize = 20
            coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
                coEvery { awaitAsOne() } returns false
            }

            // When
            store.getPageStatus(page, pageSize)

            // Then
            coVerify { pokemonQueries.isFullPageInDatabase(20, 0) }
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given second page and page size of 20, when getting page status, then check full page in database with offset 20 and page size 20`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val page = 2
            val pageSize = 20
            coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
                coEvery { awaitAsOne() } returns false
            }

            // When
            store.getPageStatus(page, pageSize)

            // Then
            coVerify { pokemonQueries.isFullPageInDatabase(20, 20) }
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given full page is not in the database, when getting page status, then return missing`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        val page = 1
        val pageSize = 20
        coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
            coEvery { awaitAsOne() } returns false
        }

        // When
        val result = store.getPageStatus(page, pageSize)

        // Then
        result assertEquals Missing
        unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
    }

    @Test
    fun `Given full page is in the database but getting oldest updated fails, when getting page status, then throw error`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val page = 0
            val pageSize = 20
            coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
                coEvery { awaitAsOne() } returns true
            }
            coEvery { pokemonQueries.getOldestUpdated(any(), any()) } throws TestException

            // When
            assertTestException { store.getPageStatus(page, pageSize) }

            // Then
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given full page is in the database but oldest update is null, when getting page status, then return stale`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val page = 0
            val pageSize = 20
            coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
                coEvery { awaitAsOne() } returns true
            }
            coEvery { pokemonQueries.getOldestUpdated(any(), any()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns null
            }

            // When
            val result = store.getPageStatus(page, pageSize)

            // Then
            result assertEquals Stale
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given full page is in the database but oldest update is more than 1 day ago, when getting page status, then return stale`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val page = 0
            val pageSize = 20
            coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
                coEvery { awaitAsOne() } returns true
            }
            coEvery { pokemonQueries.getOldestUpdated(any(), any()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns mockk {
                    every { MIN } returns Date().time - 2.days.inWholeMilliseconds
                }
            }

            // When
            val result = store.getPageStatus(page, pageSize)

            // Then
            result assertEquals Stale
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given full page is in the database but oldest update is less than 1 day ago, when getting page status, then return available`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val page = 0
            val pageSize = 20
            coEvery { pokemonQueries.isFullPageInDatabase(any(), any()) } returns mockk {
                coEvery { awaitAsOne() } returns true
            }
            coEvery { pokemonQueries.getOldestUpdated(any(), any()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns mockk {
                    every { MIN } returns Date().time - 12.hours.inWholeMilliseconds
                }
            }

            // When
            val result = store.getPageStatus(page, pageSize)

            // Then
            result assertEquals Available
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given select all in pages fails, when observing pokemon list, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        coEvery { pokemonQueries.selectAllInPages(any(), any()) } throws TestException
        coEvery { pokemonListQueries.select() } returns mockk {
            coEvery { asFlow() } returns flowOf()
        }

        // When -> Then
        assertTestException { store.observePokemonList(pages, pageSize) }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given select all in pages emits error, when observing pokemon list, then emit error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        coEvery { pokemonQueries.selectAllInPages(any(), any()) } returns mockk {
            coEvery { asFlow() } returns flow { throw TestException }
        }
        coEvery { pokemonListQueries.select() } returns mockk {
            coEvery { asFlow() } returns flowOf()
        }

        // When
        store.observePokemonList(pages, pageSize).test {
            // Then
            awaitError() assertEquals TestException
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given select pokemon list fails, when observing pokemon list, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        coEvery { pokemonQueries.selectAllInPages(any(), any()) } returns mockk {
            coEvery { asFlow() } returns flowOf()
        }
        coEvery { pokemonListQueries.select() } throws TestException

        // When -> Then
        assertTestException { store.observePokemonList(pages, pageSize) }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given select pokemon list emits error, when observing pokemon list, then emit error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        coEvery { pokemonQueries.selectAllInPages(any(), any()) } returns mockk {
            coEvery { asFlow() } returns flowOf()
        }
        coEvery { pokemonListQueries.select() } returns mockk {
            coEvery { asFlow() } returns flow { throw TestException }
        }

        // When
        store.observePokemonList(pages, pageSize).test {
            // Then
            awaitError() assertEquals TestException
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given max count is null, when observing pokemon list, then emit null`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        coEvery { pokemonQueries.selectAllInPages(pages.toLong(), pageSize.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(listOf(mockk()))
            }
        }
        coEvery { pokemonListQueries.select() } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToOneOrNull(any()) } returns flowOf(null)
            }
        }

        // When
        store.observePokemonList(pages, pageSize).test {
            // Then
            awaitItem().assertNull()
            awaitComplete()
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given entries is empty, when observing pokemon list, then emit null`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        coEvery { pokemonQueries.selectAllInPages(pages.toLong(), pageSize.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(emptyList())
            }
        }
        coEvery { pokemonListQueries.select() } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToOneOrNull(any()) } returns flowOf(
                    mockk {
                        every { maxPokemonCount } returns 1304
                    }
                )
            }
        }

        // When
        store.observePokemonList(pages, pageSize).test {
            // Then
            awaitItem().assertNull()
            awaitComplete()
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `When observing pokemon list, then emit mapped list`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val pages = 1
        val pageSize = 20
        val maxCount = 1304
        val entryOne = PokemonEntry(
            id = 1,
            name = "bulbasaur",
            lastUpdate = 0,
        )
        val entryTwo = PokemonEntry(
            id = 2,
            name = "ivysaur",
            lastUpdate = 0,
        )
        val entryThree = PokemonEntry(
            id = 3,
            name = "venusaur",
            lastUpdate = 0,
        )
        val pokemonList = PokemonList(
            maxCount = maxCount,
            hasNext = true,
            pokemon = listOf(
                Pokemon(
                    id = 1,
                    name = "bulbasaur",
                ),
                Pokemon(
                    id = 2,
                    name = "ivysaur",
                ),
                Pokemon(
                    id = 3,
                    name = "venusaur",
                ),
            ),
        )
        coEvery { pokemonQueries.selectAllInPages(pages.toLong(), pageSize.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(listOf(entryOne, entryTwo, entryThree))
            }
        }
        coEvery { pokemonListQueries.select() } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToOneOrNull(any()) } returns flowOf(
                    mockk {
                        every { maxPokemonCount } returns maxCount.toLong()
                    }
                )
            }
        }
        every { mapper.mapToList(listOf(entryOne, entryTwo, entryThree), maxCount.toLong(), true) } returns pokemonList

        // When
        store.observePokemonList(pages, pageSize).test {
            // Then
            awaitItem() assertEquals pokemonList
            awaitComplete()
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }
}
