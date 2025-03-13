package com.patrickhoette.core.ui.theme.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.patrickhoette.core.ui.theme.definition.LightThemeTypeColors
import com.patrickhoette.core.ui.theme.entity.VariantColor
import com.patrickhoette.pokedex.entity.generic.Type

val LocalThemeTypeColors = staticCompositionLocalOf<ThemeTypeColors> { LightThemeTypeColors }

@Stable
interface ThemeTypeColors {

    operator fun get(type: Type): VariantColor
}
