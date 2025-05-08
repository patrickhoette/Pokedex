package com.patrickhoette.core.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.patrickhoette.core.presentation.extension.toGenericError
import com.patrickhoette.core.presentation.model.TypedUIState.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update

/**
 * Represents the state of a UI component.
 */
@Stable
sealed interface TypedUIState<out D, out E> {

    /**
     * Returns either the [Normal.data] or `null`, if `this` is not [Normal].
     */
    fun normalDataOrNull(): D? = (this as? Normal<D>)?.data

    /**
     * Returns either the [Error.data] or `null`, if `this` is not [Error].
     */
    fun errorDataOrNull(): E? = (this as? Error<E>)?.data

    /**
     * Represents a normal UI state where no data is being loaded and no errors have occurred.
     */
    @Immutable
    data class Normal<out T>(val data: T) : TypedUIState<T, Nothing>

    /**
     * Represents a UI state where data is currently in the process of being retrieved/work is being preformed.
     */
    @Immutable
    data object Loading : TypedUIState<Nothing, Nothing>

    /**
     * Represents a UI state where the loading of data/work preformed failed.
     */
    @Immutable
    data class Error<out T>(val data: T) : TypedUIState<Nothing, T>
}

/**
 * Map [Error.data] from [E] to [R] using the given [mapper] if `this` is [Error], otherwise returns a cast version
 * of `this`.
 */
inline fun <D, E, R> TypedUIState<D, E>.mapError(mapper: (E) -> R): TypedUIState<D, R> = when (this) {
    is Error -> Error(mapper(data))
    is Loading -> this
    is Normal -> this
}

/**
 * Map [Normal.data] from [D] to [R] using the given [mapper] if `this` is [Normal], otherwise returns a cast
 * version of `this`.
 */
inline fun <D, E, R> TypedUIState<D, E>.mapNormal(mapper: (D) -> R): TypedUIState<R, E> = when (this) {
    is Error -> this
    is Loading -> this
    is Normal -> Normal(mapper(data))
}

// Type Aliases

/**
 * A [TypedUIState] with [Unit] as both the data type and error type.
 */
typealias SimpleUIState = TypedUIState<Unit, Unit>

/**
 * A [TypedUIState] with `T` as the data type and [GenericError] as the error type.
 */
typealias GenericUIState<T> = TypedUIState<T, GenericError>

// Factories

/**
 * Factory function for creating a [TypedUIState] with [E] for the error type and [Unit] for the data type.
 */
fun <E> Normal(): TypedUIState<Unit, E> = Normal(Unit)

/**
 * Factory function for creating a [TypedUIState] with [D] for the data type and [Unit] for the error type.
 */
fun <D> Error(): TypedUIState<D, Unit> = Error(Unit)

fun <T> Error(error: Throwable): GenericUIState<T> = Error(error.toGenericError())

// Utility Functions

/**
 * Set [MutableStateFlow.value] to [Normal] with given [data].
 */
fun <D, E> MutableStateFlow<TypedUIState<D, E>>.setNormal(data: D) {
    value = Normal(data)
}

/**
 * Utility function to allow you to call [setNormal] without giving a parameter if `D` is [Unit].
 */
fun <E> MutableStateFlow<TypedUIState<Unit, E>>.setNormal() = setNormal(Unit)

/**
 * Set [MutableStateFlow.value] to [Loading].
 */
fun <D, E> MutableStateFlow<TypedUIState<D, E>>.setLoading() {
    value = Loading
}

/**
 * Set [MutableStateFlow.value] to [Error] with given [data].
 */
fun <D, E> MutableStateFlow<TypedUIState<D, E>>.setError(data: E) {
    value = Error(data)
}

/**
 * Utility function to allow you to call [setError] without giving a parameter if `E` is [Unit].
 */
fun <D> MutableStateFlow<TypedUIState<D, Unit>>.setError() = setError(Unit)

fun <T> MutableStateFlow<GenericUIState<T>>.setError(error: Throwable) = setError(error.toGenericError())

/**
 * [Flow.mapLatest] every emit of `this` and calls [TypedUIState.mapNormal] on it with given [mapper].
 */
inline fun <D, E, R> Flow<TypedUIState<D, E>>.mapNormal(
    crossinline mapper: suspend (D) -> R,
): Flow<TypedUIState<R, E>> = mapLatest { uiState ->
    uiState.mapNormal { mapper(it) }
}

/**
 * [Flow.mapLatest] every emit of `this` and calls [TypedUIState.mapError] on it with given [mapper].
 */
inline fun <D, E, R> Flow<TypedUIState<D, E>>.mapError(
    crossinline mapper: suspend (E) -> R,
): Flow<TypedUIState<D, R>> = mapLatest { uiState ->
    uiState.mapError { mapper(it) }
}

/**
 * [MutableStateFlow.update] the [Normal.data] if current value is [Normal], if not and [enforce] is set to `false`
 * (default) then it will just be ignored. If [enforce] is set to `true`, an [IllegalStateException] will be thrown.
 */
inline fun <D, E> MutableStateFlow<TypedUIState<D, E>>.updateIfNormal(
    enforce: Boolean = false,
    updater: (D) -> D,
) = update { state ->
    (state as? Normal<D>)
        ?.mapNormal(updater)
        ?: if (enforce) {
            error("Value passed to the update function was not Normal!")
        } else {
            state
        }
}

/**
 * [MutableStateFlow.update] the [Error.data] if current value is [Error], if not and [enforce] is set to `false`
 * (default) then it will just be ignored. If [enforce] is set to `true`, an [IllegalStateException] will be thrown.
 */
inline fun <D, E> MutableStateFlow<TypedUIState<D, E>>.updateIfError(
    enforce: Boolean = false,
    updater: (E) -> E,
) = update { state ->
    (state as? Error<E>)
        ?.mapError(updater)
        ?: if (enforce) {
            error("Value passed to the update function was not Error!")
        } else {
            state
        }
}
