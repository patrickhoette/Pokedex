package com.patrickhoette.core.utils.collection

import com.patrickhoette.core.utils.collection.Diff.*
import com.patrickhoette.test.assertEmpty
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MapDifferTest {

    @InjectMockKs
    private lateinit var differ: MapDiffer<String, String>

    @Test
    fun `Given old and new are empty, when invoked, then return empty set`() = runTest {
        // Given
        val old = emptyMap<String, String>()
        val new = emptyMap<String, String>()

        // When
        val result = differ.diff(old, new)

        // Then
        result.assertEmpty()
    }

    @Test
    fun `Given old is empty, when invoked, then return a set of additions`() = runTest {
        // Given
        val newPairOne = "new key one" to "new value one"
        val newPairTwo = "new key two" to "new value two"
        val newPairThree = "new key three" to "new value three"
        val old = emptyMap<String, String>()
        val new = mapOf(newPairOne, newPairTwo, newPairThree)

        // When
        val result = differ.diff(old, new)

        // Then
        result assertEquals setOf(
            Addition(newPairOne),
            Addition(newPairTwo),
            Addition(newPairThree),
        )
    }

    @Test
    fun `Given new is empty, when invoked, then return a set of removals`() = runTest {
        // Given
        val oldPairOne = "old key one" to "old value one"
        val oldPairTwo = "old key two" to "old value two"
        val oldPairThree = "old key three" to "old value three"
        val old = mapOf(oldPairOne, oldPairTwo, oldPairThree)
        val new = emptyMap<String, String>()

        // When
        val result = differ.diff(old, new)

        // Then
        result assertEquals setOf(
            Removal(oldPairOne),
            Removal(oldPairTwo),
            Removal(oldPairThree),
        )
    }

    @Test
    fun `Map diffing test`() = runTest {
        // Given
        val old = mapOf(
            "removed key one" to "removed value one",
            "changed key one" to "old value one",
            "changed key two" to "old value two",
            "unchanged key one" to "unchanged value two",
            "removed key three" to "removed value three",
            "removed key four" to "removed value four",
            "unchanged key two" to "unchanged value two",
            "removed key five" to "removed value five",
            "changed key three" to "old value three",
        )
        val new = mapOf(
            "changed key one" to "new value one",
            "added key one" to "added value one",
            "changed key two" to "new value two",
            "unchanged key one" to "unchanged value two",
            "unchanged key two" to "unchanged value two",
            "added key two" to "added value two",
            "changed key three" to "new value three",
        )

        // When
        val result = differ.diff(old, new)

        // Then
        result assertEquals setOf(
            Removal("removed key one" to "removed value one"),
            Change(old = "changed key one" to "old value one", new = "changed key one" to "new value one"),
            Change(old = "changed key two" to "old value two", new = "changed key two" to "new value two"),
            Removal("removed key three" to "removed value three"),
            Removal("removed key four" to "removed value four"),
            Removal("removed key five" to "removed value five"),
            Change(old = "changed key three" to "old value three", new = "changed key three" to "new value three"),
            Addition("added key one" to "added value one"),
            Addition("added key two" to "added value two"),
        )
    }
}
