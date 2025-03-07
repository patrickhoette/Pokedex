package com.patrickhoette.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    CompositionLocalProvider(
        LocalPokeTypeColors provides if (isDark) DarkPokeTypeColors else LightPokeTypeColors,
        content = content,
    )
}
