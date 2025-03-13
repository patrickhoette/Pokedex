package com.patrickhoette.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.patrickhoette.core.ui.theme.definition.ColorSchemes
import com.patrickhoette.core.ui.theme.definition.DarkThemeTypeColors
import com.patrickhoette.core.ui.theme.definition.LightThemeTypeColors
import com.patrickhoette.core.ui.theme.model.*

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
    CompositionLocalProvider(
        LocalThemeColors provides if (isDark) ColorSchemes.dark else ColorSchemes.light,
        LocalThemeTypeColors provides if (isDark) DarkThemeTypeColors else LightThemeTypeColors,
        content = content,
    )
}
