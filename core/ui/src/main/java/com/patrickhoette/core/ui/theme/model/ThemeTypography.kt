package com.patrickhoette.core.ui.theme.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.patrickhoette.core.ui.theme.definition.Typography

val LocalTypography = staticCompositionLocalOf { Typography.App }

@Immutable
data class ThemeTypography(
    val display: SizedTextStyles,
    val headings: SizedTextStyles,
    val body: SizedTextStyles,
    val label: SizedTextStyles,
    val mono: SizedTextStyles,
    val caption: TextStyle,
)

@Immutable
data class SizedTextStyles(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)
