package com.patrickhoette.core.presentation.mvvm

import app.cash.turbine.test
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertNotNull
import com.patrickhoette.test.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class MutableEventFlowTest {

    @Test
    fun `When checking initial value, then return null`() = runTest {
        // Given
        val flow = MutableEventFlow<String>()

        // When
        val result = flow.value

        // Then
        result.assertNull()
    }

    @Test
    fun `When setEvent is called, then new event should be emitted`() = runTest {
        // Given
        val flow = MutableEventFlow<String>()

        // When
        flow.setEvent("Hello")

        // Then
        flow.value.assertNotNull()
        flow.value?.peek() assertEquals "Hello"
    }

    @Test
    fun `Given MutableEventFlow with event, when setEvent is called again, then previous event is replaced`() =
        runTest {
            // Given
            val flow = MutableEventFlow<String>()
            flow.setEvent("First")

            // When
            flow.setEvent("Second")

            // Then
            flow.value?.peek() assertEquals "Second"
        }

    @Test
    fun `When converting to EventFlow, then emitted event should match`() = runTest {
        // Given
        val flow = MutableEventFlow<String>()
        val eventFlow = flow.asEventFlow()

        // When
        flow.setEvent("Hello")

        // Then
        eventFlow.first()?.peek() assertEquals "Hello"
    }

    @Test
    fun `Given EventFlow, when setting multiple events, then only latest event should be emitted`() = runTest {
        // Given
        val flow = MutableEventFlow<String>()
        val eventFlow = flow.asEventFlow()

        // When
        flow.setEvent("First")
        flow.setEvent("Second")

        // Then
        eventFlow.test {
            awaitItem()?.retrieve() assertEquals "Second"
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Given EventFlow with no events, when retrieveEach is called, then emit null`() = runTest {
        // Given
        val flow = MutableEventFlow<String>()
        val eventFlow = flow.asEventFlow()

        // When -> Then
        eventFlow.test {
            awaitItem()?.retrieve().assertNull()
            cancelAndConsumeRemainingEvents()
        }
    }
}
