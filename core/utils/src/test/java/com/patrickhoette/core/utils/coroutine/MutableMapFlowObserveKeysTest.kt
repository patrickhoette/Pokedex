package com.patrickhoette.core.utils.coroutine

import app.cash.turbine.test
import com.patrickhoette.core.utils.collection.MapDiffer
import com.patrickhoette.test.assertEmpty
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MutableMapFlowObserveKeysTest {

    private val initialKey = "initial key"
    private val initialValue = "initial value"

    private val initialMap = mapOf(initialKey to initialValue)

    @MockK
    private lateinit var differ: MapDiffer<String, String>

    @InjectMockKs
    private lateinit var mapFlow: MutableMapFlow<String, String>

    @Test
    fun `Given initial value, when observing keys, then emit set of initial keys`() = runTest {
        // When
        mapFlow.keys().test {
            // Then
            awaitItem() assertEquals setOf(initialKey)
        }
    }

    @Test
    fun `Given no initial value, when observing keys, then emit empty set`() = runTest {
        // Given
        val mapFlow = MutableMapFlow<String, String>()

        // When
        mapFlow.keys().test {
            // Then
            awaitItem().assertEmpty()
        }
    }

    @Test
    fun `Given new value is the same, when updating, then do not emit`() = runTest {
        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { it }

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new value is null, when updating, then emit empty set`() = runTest {
        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { null }

            // Then
            awaitItem().assertEmpty()
        }
    }

    @Test
    fun `Given new value is completely different, when updating, then emit set of new keys`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newKeyTwo = "new key two"
        val newMap = mapOf(
            newKeyOne to "new value one",
            newKeyTwo to "new value two",
        )

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { newMap }

            // Then
            awaitItem() assertEquals setOf(newKeyOne, newKeyTwo)
        }
    }

    @Test
    fun `Given new value has some new keys, when updating, then emit set of remaining and new keys`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newKeyTwo = "new key two"
        val newMap = mapOf(
            initialKey to initialValue,
            newKeyOne to "new value one",
            newKeyTwo to "new value two",
        )

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { newMap }

            // Then
            awaitItem() assertEquals setOf(initialKey, newKeyOne, newKeyTwo)
        }
    }

    @Test
    fun `Given new value is same, when updating, then do not emit`() = runTest {
        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { initialMap }

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new value is null for existing key, when updating, then emit set with key removed`() = runTest {
        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { null }

            // Then
            awaitItem().assertEmpty()
        }
    }

    @Test
    fun `Given new value is null for non existing key, when updating, then do not emit`() = runTest {
        // Given
        val mapFlow = MutableMapFlow<String, String>()

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { null }

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new value is different, when updating, then do not emit`() = runTest {
        // Given
        val newMap = mapOf(initialKey to "new value")

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow.update { newMap }

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given existing key, when adding new value, then do not emit`() = runTest {
        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow += initialKey to "new value"

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new key, when adding new entry, then emit set with new key`() = runTest {
        // Given
        val newKey = "new key"

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow += newKey to "new value"

            // Then
            awaitItem() assertEquals setOf(initialKey, newKey)
        }
    }

    @Test
    fun `Given all new keys, when adding new entries, then emit set with new keys`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newKeyTwo = "new key two"
        val newKeyThree = "new key three"
        val newEntries = mapOf(
            newKeyOne to "new value one",
            newKeyTwo to "new value two",
            newKeyThree to "new value three",
        )

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            awaitItem() assertEquals setOf(initialKey, newKeyOne, newKeyTwo, newKeyThree)
        }
    }

    @Test
    fun `Given some new and some old keys, when adding new entries, then emit set with new keys`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newKeyTwo = "new key two"
        val newEntries = mapOf(
            initialKey to "new initial value",
            newKeyOne to "new value one",
            newKeyTwo to "new value two",
        )

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            awaitItem() assertEquals setOf(initialKey, newKeyOne, newKeyTwo)
        }
    }

    @Test
    fun `Given only existing keys, when adding new entries, then do not emit`() = runTest {
        // Given
        val newEntries = mapOf(initialKey to "new initial value")

        // When
        mapFlow.keys().test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given existing key, when removing entry, then emit set with entry removed`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `Given non existing key, when removing entry, then do not emit`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `Given only existing keys, when removing entries, then emit set with entries removed`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `Given some existing and some non existing keys, when removing entries, then emit set with existing entries removed`() =
        runTest {
            // Given

            // When

            // Then

        }

    @Test
    fun `Given only non existing keys, when removing entries, then do not emit`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `Given existing key, when setting, then do not emit`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `Given non existing key, when setting, then emit set with new key`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `When cleared, then emit empty set`() = runTest {
        // Given

        // When

        // Then

    }

    @Test
    fun `Given already empty, when cleared, then do not emit`() = runTest {
        // Given

        // When

        // Then

    }
}
