package com.patrickhoette.core.ui.theme.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.patrickhoette.core.ui.theme.definition.ColorSchemes

val LocalThemeColors = staticCompositionLocalOf { ColorSchemes.Light }

@Immutable
data class ThemeColors(
    val primary: ColorVariations,
    val secondary: ColorVariations,
    val tertiary: ColorVariations,
    val background: ColorBackground,
    val error: ColorVariations,
    val success: ColorVariations,
    val warning: ColorVariations,
    val info: ColorVariations,
    val shimmer: ColorShimmer,
    val outline: Color,
)

@Immutable
data class ColorVariations(
    val base: Color,
    val onBase: Color,
    val container: Color,
    val onContainer: Color,
)

@Immutable
data class ColorBackground(
    val base: Color,
    val onBase: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
)

@Immutable
data class ColorShimmer(
    val background: Color,
    val foreground: Color,
)
