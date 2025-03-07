package com.patrickhoette.core.ui.theme.entity

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class VariantColor(
    val light: Color,
    val onLight: Color,
    val base: Color,
    val onBase: Color,
    val dark: Color,
    val onDark: Color,
)
