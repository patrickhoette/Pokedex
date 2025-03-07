package com.patrickhoette.test

import org.junit.jupiter.api.assertAll

fun <T> assertAllWith(
    subject: T,
    vararg assertions: T.() -> Unit,
) = assertAll(*assertions.map { { it(subject) } }.toTypedArray())
