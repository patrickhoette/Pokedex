package com.patrickhoette.core.utils.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.properties.Delegates

/**
 * Container that will cancel the old job when a new one is set.
 */
class SerialJob(
    private val onCancel: ((CancellationException) -> Unit)? = null,
) {

    private var job: Job? by Delegates.observable(null) { _, old, _ ->
        old?.cancel()
    }

    fun cancel(cause: CancellationException? = null) = job?.cancel(cause)

    suspend operator fun <R> invoke(runnable: suspend () -> R): R? {
        return coroutineScope {
            val newJob = async { runnable() }
            job = newJob
            try {
                newJob.await()
            } catch (error: CancellationException) {
                onCancel?.invoke(error)
                null
            }
        }
    }
}
