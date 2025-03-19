@file:OptIn(ExperimentalStdlibApi::class)

package com.patrickhoette.core.ui.showcase.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

internal object ColorUtils {

    private val ColorHexFormat = HexFormat {
        upperCase = true
        number {
            prefix = "#"
        }
    }

    @Stable
    fun convertToHexString(color: Color) = color.value.toHexString(ColorHexFormat).substring(0, 9)
}
