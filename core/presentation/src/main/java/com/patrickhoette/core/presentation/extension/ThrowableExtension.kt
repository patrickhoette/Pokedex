package com.patrickhoette.core.presentation.extension

import com.patrickhoette.core.presentation.model.GenericError.*
import com.patrickhoette.pokedex.entity.error.NetworkException
import com.patrickhoette.pokedex.entity.error.ServerException

fun Throwable.toGenericError() = when (this) {
    is NetworkException -> Network
    is ServerException -> Server
    else -> Unknown
}
