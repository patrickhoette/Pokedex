package com.patrickhoette.core.presentation.mapper

import com.patrickhoette.core.presentation.model.NamedEnumUIModel
import com.patrickhoette.core.presentation.text.NamedEnumTextProvider
import org.koin.core.annotation.Factory

@Factory
class NamedEnumUIMapper(
    private val textProvider: NamedEnumTextProvider,
) {

    fun <T : Enum<out T>> mapToUIModel(enum: T) = NamedEnumUIModel(
        name = textProvider.getName(enum),
        value = enum,
    )
}
