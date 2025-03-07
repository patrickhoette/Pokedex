package com.patrickhoette.test

import io.mockk.MockKVerificationScope
import io.mockk.Ordering
import io.mockk.coVerify
import io.mockk.verify

fun verifyOnce(
    ordering: Ordering = Ordering.UNORDERED,
    timeout: Long = 0,
    verifyBlock: MockKVerificationScope.() -> Unit,
) {
    verify(
        ordering = ordering,
        timeout = timeout,
        verifyBlock = verifyBlock,
        exactly = 1,
    )
}

fun coVerifyOnce(
    ordering: Ordering = Ordering.UNORDERED,
    timeout: Long = 0,
    verifyBlock: suspend MockKVerificationScope.() -> Unit,
) {
    coVerify(
        ordering = ordering,
        timeout = timeout,
        verifyBlock = verifyBlock,
        exactly = 1,
    )
}

fun verifyNever(verifyBlock: MockKVerificationScope.() -> Unit) = verify(inverse = true, verifyBlock = verifyBlock)

fun coVerifyNever(
    verifyBlock: suspend MockKVerificationScope.() -> Unit,
) = coVerify(inverse = true, verifyBlock = verifyBlock)
