package com.patrickhoette.core.store.extension

fun Boolean.toLong() = if (this) 1L else 0L

fun Long.toBoolean() = this == 1L
