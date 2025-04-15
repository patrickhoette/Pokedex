package com.patrickhoette.core.presentation.text

import com.patrickhoette.core.presentation.model.StringUIModel

interface NamedEnumTextProvider {

    fun <T : Enum<out T>> getName(enum: T): StringUIModel
}
