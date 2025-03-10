package com.patrickhoette.core.source.error

import com.patrickhoette.pokedex.entity.error.NetworkException
import com.patrickhoette.pokedex.entity.error.ServerException
import com.patrickhoette.test.assertFailsWith
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.model.TestException
import io.ktor.client.plugins.ResponseException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
class ApiErrorMapperTest {

    @InjectMockKs
    private lateinit var mapper: APIErrorMapper

    @ParameterizedTest(name = "[{index}] Given code is {0}, when mapping error, then throw server exception")
    @MethodSource("provideArgsServerException")
    fun `Server exception mapping`(
        code: Int,
    ) = runTest {
        // Given
        val exception = mockk<ResponseException> {
            every { response } returns mockk {
                every { status } returns mockk {
                    every { value } returns code
                }
            }
        }

        // When -> Then
        assertFailsWith<ServerException> { mapper.mapError(exception) }
    }

    @ParameterizedTest(name = "[{index}] Given code is {0}, when mapping error, then throw cause")
    @MethodSource("provideArgsNotServerException")
    fun `Non server exception mapping`(
        code: Int,
    ) = runTest {
        // Given
        val exception = mockk<ResponseException> {
            every { response } returns mockk {
                every { status } returns mockk {
                    every { value } returns code
                }
            }
        }

        // When -> Then
        assertFailsWith(exception) { mapper.mapError(exception) }
    }

    @Test
    fun `Given cause is unknown host exception, when mapping error, then throw network exception`() = runTest {
        // When -> Then
        assertFailsWith<NetworkException> { mapper.mapError(UnknownHostException()) }
    }

    @Test
    fun `Given cause is connect exception, when mapping error, then throw network exception`() = runTest {
        // When -> Then
        assertFailsWith<NetworkException> { mapper.mapError(ConnectException()) }
    }

    @Test
    fun `Given cause is socket timeout exception, when mapping error, then throw network exception`() = runTest {
        // When -> Then
        assertFailsWith<NetworkException> { mapper.mapError(mockk<SocketTimeoutException>()) }
    }

    @Test
    fun `Given cause is some other exception, when mapping error, then throw cause`() = runTest {
        // When -> Then
        assertTestException { mapper.mapError(TestException) }
    }

    companion object {

        @JvmStatic
        fun provideArgsServerException() = (500 until 600).map { Arguments.of(it) }

        @JvmStatic
        fun provideArgsNotServerException() = ((0 until 500) + (600 until 1000)).map { Arguments.of(it) }
    }
}
