package com.patrickhoette.core.presentation.extension

import com.patrickhoette.core.presentation.model.GenericError.*
import com.patrickhoette.pokedex.entity.error.NetworkException
import com.patrickhoette.pokedex.entity.error.ServerException
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.model.TestException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ThrowableExtensionTest {

    @Test
    fun `Given error is network exception, when mapping error, then return network`() = runTest {
        // When
        val result = NetworkException().toGenericError()

        // Then
        result assertEquals Network
    }

    @Test
    fun `Given error is server exception, when mapping error, then return server`() = runTest {
        // When
        val result = ServerException().toGenericError()

        // Then
        result assertEquals Server
    }

    @Test
    fun `Given error is something else, when mapping error, then return unknown`() = runTest {
        // When
        val result = TestException.toGenericError()

        // Then
        result assertEquals Unknown
    }
}
