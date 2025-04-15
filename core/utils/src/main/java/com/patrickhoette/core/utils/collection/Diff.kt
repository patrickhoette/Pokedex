package com.patrickhoette.core.utils.collection

sealed interface Diff<out T> {

    data class Addition<out T>(val data: T) : Diff<T>

    data class Removal<out T>(val data: T) : Diff<T>

    data class Change<out T>(
        val old: T,
        val new: T,
    ) : Diff<T>
}
