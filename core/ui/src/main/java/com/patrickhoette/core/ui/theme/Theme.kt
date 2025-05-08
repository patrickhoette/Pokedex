package com.patrickhoette.core.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.patrickhoette.core.ui.theme.definition.ColorSchemes
import com.patrickhoette.core.ui.theme.definition.DarkThemeTypeColors
import com.patrickhoette.core.ui.theme.definition.LightThemeTypeColors
import com.patrickhoette.core.ui.theme.definition.ShimmerThemes
import com.patrickhoette.core.ui.theme.model.*
import com.valentinilk.shimmer.LocalShimmerTheme

@Suppress("ObjectPropertyNaming")
object Theme {

    val colors: ThemeColors
        @ReadOnlyComposable
        @Composable
        get() = LocalThemeColors.current

    val typeColors: ThemeTypeColors
        @ReadOnlyComposable
        @Composable
        get() = LocalThemeTypeColors.current

    val typography: ThemeTypography
        @ReadOnlyComposable
        @Composable
        get() = LocalTypography.current
}

@Composable
fun AppTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (isDark) ColorSchemes.Dark else ColorSchemes.Light
    CompositionLocalProvider(
        LocalThemeColors provides colors,
        LocalThemeTypeColors provides if (isDark) DarkThemeTypeColors else LightThemeTypeColors,
        LocalIndication provides ripple(),
        LocalContentColor provides colors.background.onBase,
        LocalShimmerTheme provides ShimmerThemes.createShimmerTheme(colors),
        content = content,
    )
}
