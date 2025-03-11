package com.patrickhoette.core.presentation.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrickhoette.core.utils.coroutines.SerialJob
import com.patrickhoette.core.utils.extension.launchCatching
import kotlinx.coroutines.CoroutineScope
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
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatching(onError) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchOnIO(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchOnIO(context) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchOnMain(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchOnMain(context) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatchingOnIO(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatchingOnIO(onError) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatchingOnMain(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatchingOnMain(onError) {
        invoke { block() }
    }
}

