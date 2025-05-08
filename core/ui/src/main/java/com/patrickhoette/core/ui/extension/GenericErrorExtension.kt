package com.patrickhoette.core.ui.extension

import androidx.annotation.StringRes
import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.core.presentation.model.GenericError.*
import com.patrickhoette.core.ui.R

@get:StringRes
val GenericError.titleRes
    get() = when (this) {
        Network -> R.string.error_title_network
        Server -> R.string.error_title_server
        Unknown -> R.string.error_title_unknown
    }

@get:StringRes
val GenericError.bodyRes
    get() = when (this) {
        Network -> R.string.error_body_network
        Server -> R.string.error_body_server
        Unknown -> R.string.error_body_unknown
    }
