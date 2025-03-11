@file:Suppress("unused")

package com.patrickhoette.core.presentation.mvvm

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A specialized [MutableStateFlow] that holds an [Event] and ensures event consumption
 * semantics, meaning the event is only retrieved once.
 *
 * This is useful for handling UI events such as **navigation, toasts, and one-time messages**
 * that should not be re-emitted after configuration changes (e.g., screen rotation).
 *
 * @param T The type of data held by this event.
 */
@Stable
class MutableEventFlow<T : Any> private constructor(
    private val backingFlow: MutableStateFlow<Event<T>?>,
) : MutableStateFlow<Event<T>?> by backingFlow {

    constructor() : this(MutableStateFlow(null))

    /**
     * Sets a new event, wrapping the provided data inside an [Event].
     *
     * @param data The data to be emitted as an event.
     */
    fun setEvent(data: T) {
        value = Event(data)
    }

    /**
     * Converts this [MutableEventFlow] into a read-only [EventFlow].
     *
     * @return A [StateFlow] that can be observed for new events.
     */
    fun asEventFlow() = EventFlow(this)
}

/**
 * A read-only wrapper around [MutableEventFlow], exposing it as a [StateFlow] while
 * ensuring event consumption semantics.
 *
 * @param T The type of data held by the event.
 * @property mutable The underlying mutable event flow.
 */
@Stable
class EventFlow<out T : Any>(
    private val mutable: MutableEventFlow<T>,
) : StateFlow<Event<T>?> by mutable {

    /**
     * Collects the latest event only once per emission, ensuring each event is consumed
     * only once.
     *
     * This is useful for handling UI events, preventing multiple UI updates from a single event.
     *
     * @param collector The suspend function to handle the emitted event data.
     */
    suspend fun retrieveEach(collector: suspend (T?) -> Unit) {
        mutable.collectLatest { collector(it?.retrieve()) }
    }
}

/**
 * A wrapper for data that represents a one-time event, ensuring that it is only
 * consumed once.
 *
 * This is useful for UI events like toasts, navigation, or Snackbar messages,
 * where duplicate emissions should be avoided.
 *
 * @param T The type of data encapsulated in this event.
 * @property data The actual event data.
 */
@Immutable
data class Event<out T : Any>(
    private val data: T,
) {

    /**
     * Atomic boolean to track whether this event has been retrieved.
     */
    private val _isRetrieved = AtomicBoolean(false)

    /**
     * Returns whether this event has already been retrieved.
     */
    val isRetrieved: Boolean
        get() = _isRetrieved.get()

    /**
     * Returns the event data **without marking it as retrieved**.
     *
     * @return The underlying data.
     */
    fun peek(): T {
        return data
    }

    /**
     * Retrieves the event data **only if it has not been retrieved before**.
     *
     * - If this is the first time calling `retrieve()`, it returns the data.
     * - On subsequent calls, it returns `null`.
     *
     * @return The event data if not previously retrieved, otherwise `null`.
     */
    fun retrieve(): T? {
        return if (!_isRetrieved.getAndSet(true)) data else null
    }

    /**
     * Overrides `hashCode()` to consider the **retrieval state** of the event.
     */
    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + _isRetrieved.hashCode()
        return result
    }

    /**
     * Overrides `equals()` to compare both **data and retrieval state**.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        if (data != other.data) return false
        if (isRetrieved != other.isRetrieved) return false

        return true
    }
}
