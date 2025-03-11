package com.patrickhoette.core.presentation.mvvm

import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertNull
import kotlin.test.Test

class EventTest {

    @Test
    fun `When retrieve is called once, then return data`() {
        // Given
        val event = Event("Hello")

        // When
        val result = event.retrieve()

        // Then
        result assertEquals "Hello"
    }

    @Test
    fun `Given event retrieved once, when retrieve is called again, then return null`() {
        // Given
        val event = Event("Hello")
        event.retrieve()

        // When
        val result = event.retrieve()

        // Then
        result.assertNull()
    }

    @Test
    fun `When peek is called multiple times, then return data without marking it as retrieved`() {
        // Given
        val event = Event("Hello")

        // When
        val firstPeek = event.peek()
        val secondPeek = event.peek()

        // Then
        firstPeek assertEquals "Hello"
        secondPeek assertEquals "Hello"
    }

    @Test
    fun `When isRetrieved is checked before retrieval, then return false`() {
        // Given
        val event = Event("Hello")

        // When
        val result = event.isRetrieved

        // Then
        result assertEquals false
    }

    @Test
    fun `When retrieve is called, then isRetrieved should be true`() {
        // Given
        val event = Event("Hello")
        event.retrieve()

        // When
        val result = event.isRetrieved

        // Then
        result assertEquals true
    }
}
