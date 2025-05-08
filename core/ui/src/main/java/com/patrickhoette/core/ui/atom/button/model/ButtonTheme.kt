package com.patrickhoette.core.ui.atom.button.model

import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ButtonTheme(
    val background: ButtonColors?,
    val foreground: ButtonColors,
    val border: ButtonColors?,
    val ripple: RippleConfiguration,
)

@Immutable
data class ButtonColors(
    val enabled: Color,
    val disabled: Color = enabled,
    val pressed: Color = enabled,
    val focussed: Color = enabled,
    val hovered: Color = enabled,
    val loading: Color = enabled,
)
