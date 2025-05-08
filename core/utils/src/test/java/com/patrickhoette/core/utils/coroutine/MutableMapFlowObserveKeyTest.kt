package com.patrickhoette.core.utils.coroutine

import app.cash.turbine.test
import com.patrickhoette.core.utils.collection.MapDiffer
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertNull
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MutableMapFlowObserveKeyTest {

    private val initialKey = "initial key"
    private val initialValue = "initial value"

    private val initialMap = mapOf(initialKey to initialValue)

    @MockK
    private lateinit var differ: MapDiffer<String, String>

    @InjectMockKs
    private lateinit var mapFlow: MutableMapFlow<String, String>

    @Test
    fun `Given initial value contains key, when observing key, then emit value`() = runTest {
        // When
        mapFlow[initialKey].test {
            // Then
            awaitItem() assertEquals initialValue
        }
    }

    @Test
    fun `Given initial value does not contain key, when observing key, then emit null`() = runTest {
        // When
        mapFlow["some other key"].test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given no initial value, when observing key, then emit null`() = runTest {
        // Given
        val mapFlow = MutableMapFlow<String, String>()

        // When
        mapFlow[initialKey].test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given key exists in old value but not in new, when updating, then emit null after update`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.update { emptyMap() }

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given key doesn't exist in old value but does in new value, when updating, then emit new value`() = runTest {
        // Given
        val newKey = "new key"
        val newValue = "new value"

        // When
        mapFlow[newKey].test {
            skipItems(1)
            mapFlow.update { mapOf(newKey to newValue) }

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given key exists in both new and old value but the value is different, when updating, then emit new value`() =
        runTest {
            // Given
            val newValue = "new value"

            // When
            mapFlow[initialKey].test {
                skipItems(1)
                mapFlow.update { mapOf(initialKey to newValue) }

                // Then
                awaitItem() assertEquals newValue
            }
        }

    @Test
    fun `Given new value is null, when updating, then emit null`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.update { null }

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given new value is the same as old, when updating entry, then do not emit`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.updateEntry(initialKey) { initialValue }

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new value is different from old, when updating entry, then emit new value`() = runTest {
        // Given
        val newValue = "new value"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.updateEntry(initialKey) { newValue }

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given new value is null, when updating entry, then emit null`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.updateEntry(initialKey) { null }

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given different key, when updating entry, then do not emit`() = runTest {
        // Given
        val newKey = "new key"
        val newValue = "new value"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.updateEntry(newKey) { newValue }

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given key didn't exists but gets new value, when updating entry, then emit new value`() = runTest {
        // Given
        val newKey = "new key"
        val newValue = "new value"

        // When
        mapFlow[newKey].test {
            skipItems(1)
            mapFlow.updateEntry(newKey) { newValue }

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given new entry, when adding entry, then emit value`() = runTest {
        // Given
        val newKey = "new key"
        val newValue = "new value"

        // When
        mapFlow[newKey].test {
            skipItems(1)
            mapFlow += newKey to newValue

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given existing entry with new value, when adding entry, then emit new value`() = runTest {
        // Given
        val newValue = "new value"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow += initialKey to newValue

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given existing entry with the same value, when adding entry, then do not emit`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow += initialKey to initialValue

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given existing entry with different key, when adding entry, then do not emit`() = runTest {
        // Given
        val newKey = "new key"
        val newValue = "new value"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow += newKey to newValue

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new entry, when adding entries, then emit value`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newValueOne = "new value one"
        val newKeyTwo = "new key two"
        val newValueTwo = "new value two"
        val newKeyThree = "new key three"
        val newValueThree = "new value three"
        val newEntries = mapOf(
            newKeyOne to newValueOne,
            newKeyTwo to newValueTwo,
            newKeyThree to newValueThree,
        )

        // When
        mapFlow[newKeyTwo].test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            awaitItem() assertEquals newValueTwo
        }
    }

    @Test
    fun `Given existing entry with new value, when adding entries, then emit new value`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newValueOne = "new value one"
        val newKeyTwo = "new key two"
        val newValueTwo = "new value two"
        val newKeyThree = "new key three"
        val newValueThree = "new value three"
        val newValue = "new value"
        val newEntries = mapOf(
            newKeyOne to newValueOne,
            newKeyTwo to newValueTwo,
            newKeyThree to newValueThree,
            initialKey to newValue,
        )

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given existing entry with the same value, when adding entries, then do not emit`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newValueOne = "new value one"
        val newKeyTwo = "new key two"
        val newValueTwo = "new value two"
        val newKeyThree = "new key three"
        val newValueThree = "new value three"
        val newEntries = mapOf(
            newKeyOne to newValueOne,
            newKeyTwo to newValueTwo,
            newKeyThree to newValueThree,
            initialKey to initialValue,
        )

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given existing entry with different key, when adding entries, then do not emit`() = runTest {
        // Given
        val newKeyOne = "new key one"
        val newValueOne = "new value one"
        val newKeyTwo = "new key two"
        val newValueTwo = "new value two"
        val newKeyThree = "new key three"
        val newValueThree = "new value three"
        val newEntries = mapOf(
            newKeyOne to newValueOne,
            newKeyTwo to newValueTwo,
            newKeyThree to newValueThree,
        )

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow += newEntries

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given key that is observed, when removing entry, then emit null`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow -= initialKey

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given a different key, when removing entry, then do not emit`() = runTest {
        // Given
        val newKey = "new key"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow -= newKey

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given key that is observed but key does not exist, when removing entry, then do not emit`() = runTest {
        // Given
        val newKey = "new key"

        // When
        mapFlow[newKey].test {
            skipItems(1)
            mapFlow -= newKey

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given key that is observed, when removing entries, then emit null`() = runTest {
        // Given
        val oldEntries = setOf(
            initialKey,
            "new key one",
            "new key two",
        )

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow -= oldEntries

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given a different key, when removing entries, then do not emit`() = runTest {
        // Given
        val oldEntries = setOf(
            "new key one",
            "new key two",
        )

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow -= oldEntries

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given key that is observed but key does not exist, when removing entries, then do not emit`() = runTest {
        // Given
        val newKey = "new key"
        val oldEntries = setOf(
            newKey,
            "new key one",
            "new key two",
        )

        // When
        mapFlow[newKey].test {
            skipItems(1)
            mapFlow -= oldEntries

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new value, when setting value, then emit new value`() = runTest {
        // Given
        val newValue = "new value"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow[initialKey] = newValue

            // Then
            awaitItem() assertEquals newValue
        }
    }

    @Test
    fun `Given same value, when setting value, then do not emit value`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow[initialKey] = initialValue

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `Given new key and value, when setting value, then emit value`() = runTest {
        // Given
        val newKey = "new key"
        val newValue = "new value"

        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow[newKey] = newValue

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `When cleared, then emit null`() = runTest {
        // When
        mapFlow[initialKey].test {
            skipItems(1)
            mapFlow.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given key doesn't exist, when cleared, then do not emit`() = runTest {
        // Given
        val newKey = "new key"

        // When
        mapFlow[newKey].test {
            skipItems(1)
            mapFlow.clear()

            // Then
            expectNoEvents()
        }
    }
}
