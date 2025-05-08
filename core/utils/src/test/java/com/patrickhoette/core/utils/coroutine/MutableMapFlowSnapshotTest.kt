package com.patrickhoette.core.utils.coroutine

import com.patrickhoette.core.utils.collection.MapDiffer
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MutableMapFlowSnapshotTest {

    private val initialKey = "initial key"
    private val initialValue = "initial value"

    private val initialMap = mapOf(initialKey to initialValue)

    @MockK
    private lateinit var differ: MapDiffer<String, String>

    @InjectMockKs
    private lateinit var mapFlow: MutableMapFlow<String, String>

    @Test
    fun `Given initial value, when checking snapshot, then return initial map`() = runTest {
        // When
        val result = mapFlow.snapshot

        // Then
        result assertEquals initialMap
    }

    @Test
    fun `Given no initial value, when checking snapshot, then return empty map`() = runTest {
        // Given
        val mapFlow = MutableMapFlow(differ = differ)

        // When
        val result = mapFlow.snapshot

        // Then
        result assertEquals emptyMap()
    }

    @Test
    fun `When updating, then snapshot should return new map`() = runTest {
        // Given
        val newMap = mapOf(
            "new key one" to "new value one",
            "new key two" to "new value two",
        )

        // When
        val initial = mapFlow.snapshot
        mapFlow.update { newMap }
        val result = mapFlow.snapshot

        // Then
        initial assertEquals initialMap
        result assertEquals newMap
    }

    @Test
    fun `Given existing key, When updating entry, then snapshot should return existing map with updated value for key`() =
        runTest {
            // Given
            val newValue = "new value"

            // When
            val initial = mapFlow.snapshot
            mapFlow.updateEntry(initialKey) { newValue }
            val result = mapFlow.snapshot

            // Then
            initial assertEquals initialMap
            result assertEquals mapOf(initialKey to newValue)
        }

    @Test
    fun `Given non existing key, When updating entry, then snapshot should return existing map with new entry`() =
        runTest {
            // Given
            val newKey = "new key"
            val newValue = "new value"

            // When
            val initial = mapFlow.snapshot
            mapFlow.updateEntry(newKey) { newValue }
            val result = mapFlow.snapshot

            // Then
            initial assertEquals initialMap
            result assertEquals mapOf(
                initialKey to initialValue,
                newKey to newValue
            )
        }

    @Test
    fun `When adding new entry, then snapshot should return existing map with new entry added`() = runTest {
        // Given
        val newEntry = "new key" to "new value"

        // When
        val initial = mapFlow.snapshot
        mapFlow += newEntry
        val result = mapFlow.snapshot

        // Then
        initial assertEquals initialMap
        result assertEquals mapOf(
            initialKey to initialValue,
            newEntry,
        )
    }

    @Test
    fun `When adding map, then snapshot should return existing map with all entries added`() = runTest {
        // Given
        val newEntryOne = "new key one" to "new value one"
        val newEntryTwo = "new key two" to "new value two"
        val newEntryThree = "new key three" to "new value three"
        val newEntryFour = "new key four" to "new value four"
        val newEntries = mapOf(newEntryOne, newEntryTwo, newEntryThree, newEntryFour)

        // When
        val initial = mapFlow.snapshot
        mapFlow += newEntries
        val result = mapFlow.snapshot

        // Then
        initial assertEquals initialMap
        result assertEquals mapOf(
            initialKey to initialValue,
            newEntryOne,
            newEntryTwo,
            newEntryThree,
            newEntryFour,
        )
    }

    @Test
    fun `When removing entry, then snapshot should return existing map with entry removed`() = runTest {
        // Given
        val entryToRemove = "remove me key" to "remove me value"

        // When
        mapFlow += entryToRemove
        val initial = mapFlow.snapshot
        mapFlow -= entryToRemove.first
        val result = mapFlow.snapshot

        // Then
        initial assertEquals mapOf(
            initialKey to initialValue,
            entryToRemove
        )
        result assertEquals initialMap
    }

    @Test
    fun `When removing multiple entries, then snapshot should return existing map with all entries removed`() =
        runTest {
            // Given
            val entryToRemoveOne = "key to remove one" to "value to remove one"
            val entryToRemoveTwo = "key to remove two" to "value to remove two"
            val entryToRemoveThree = "key to remove three" to "value to remove three"
            val entriesToRemove = mapOf(entryToRemoveOne, entryToRemoveTwo, entryToRemoveThree)

            // When
            mapFlow += entriesToRemove
            val initial = mapFlow.snapshot
            mapFlow -= entriesToRemove.keys
            val result = mapFlow.snapshot

            // Then
            initial assertEquals mapOf(
                initialKey to initialValue,
                entryToRemoveOne,
                entryToRemoveTwo,
                entryToRemoveThree,
            )
            result assertEquals initialMap
        }

    @Test
    fun `Given key exists, when setting value, then snapshot should return existing map with changed value`() =
        runTest {
            // Given
            val newValue = "new value"

            // When
            val initial = mapFlow.snapshot
            mapFlow[initialKey] = newValue
            val result = mapFlow.snapshot

            // Then
            initial assertEquals initialMap
            result assertEquals mapOf(
                initialKey to newValue,
            )
        }

    @Test
    fun `Given key does not exist, when setting value, then snapshot should return existing map with added entry`() =
        runTest {
            // Given
            val newKey = "new key"
            val newValue = "new value"

            // When
            val initial = mapFlow.snapshot
            mapFlow[newKey] = newValue
            val result = mapFlow.snapshot

            // Then
            initial assertEquals initialMap
            result assertEquals mapOf(
                initialKey to initialValue,
                newKey to newValue,
            )
        }

    @Test
    fun `When clearing, then snapshot should return empty map`() = runTest {
        // When
        val initial = mapFlow.snapshot
        mapFlow.clear()
        val result = mapFlow.snapshot

        // Then
        initial assertEquals initialMap
        result assertEquals emptyMap()
    }
}
