package com.patrickhoette.core.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class NamedEnumUIModel<out T : Enum<out T>>(
    val name: StringUIModel,
    val value: T,
)
