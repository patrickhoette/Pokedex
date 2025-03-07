package com.patrickhoette.core.source.error

import com.patrickhoette.pokedex.entity.error.NetworkException
import com.patrickhoette.pokedex.entity.error.ServerException
import io.ktor.client.plugins.ResponseException
import org.koin.core.annotation.Factory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Factory
class APIErrorMapper {

    fun mapError(cause: Throwable) {
        throw when (cause) {
            is ResponseException -> mapResponseException(cause)
            is UnknownHostException, is ConnectException, is SocketTimeoutException -> NetworkException()
            else -> cause
        }
    }

    private fun mapResponseException(cause: ResponseException): Throwable = when (cause.response.status.value) {
        in 500 until 600 -> ServerException()
        else -> cause
    }
}
