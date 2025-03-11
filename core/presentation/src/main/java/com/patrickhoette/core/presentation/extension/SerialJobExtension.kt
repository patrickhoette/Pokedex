package com.patrickhoette.core.presentation.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrickhoette.core.utils.coroutine.SerialJob
import com.patrickhoette.core.utils.extension.launchCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

context(ViewModel)
fun SerialJob.launch(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) = viewModelScope.launchCatching(
    context = context,
    start = start,
    onError = onError,
) {
    invoke { block() }
}
