package com.patrickhoette.core.utils.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.runningFold

/* Credit: https://stackoverflow.com/questions/72626286/get-current-and-previous-value-in-flows-collect */
data class History<T>(val previous: T?, val current: T)

fun <T> Flow<T>.runningHistory() = runningFold(null as (History<T>?)) { accumulator, new ->
    History(accumulator?.current, new)
}.filterNotNull()
