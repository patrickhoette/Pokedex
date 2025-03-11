@file:Suppress("Unused")

package com.patrickhoette.core.presentation.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

fun CoroutineScope.cancelChildren() = coroutineContext.cancelChildren()
