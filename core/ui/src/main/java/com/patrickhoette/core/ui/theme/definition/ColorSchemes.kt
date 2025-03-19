package com.patrickhoette.core.ui.theme.definition

import androidx.compose.ui.graphics.Color
import com.patrickhoette.core.ui.theme.model.ColorBackground
import com.patrickhoette.core.ui.theme.model.ColorShimmer
import com.patrickhoette.core.ui.theme.model.ColorVariations
import com.patrickhoette.core.ui.theme.model.ThemeColors

object ColorSchemes {

    val Light = ThemeColors(
        primary = ColorVariations(
            base = Color(0xFFD43E2A),
            onBase = Color.White,
            container = Color(0xFFFFE1D6),
            onContainer = Color(0xFF5A1B16),
        ),
        secondary = ColorVariations(
            base = Color(0xFF00629B),
            onBase = Color.White,
            container = Color(0xFFC9E7FF),
            onContainer = Color(0xFF003355),
        ),
        tertiary = ColorVariations(
            base = Color(0xFFF7C143),
            onBase = Color(0xFF5A3B00),
            container = Color(0xFFFFF3C1),
            onContainer = Color(0xFF3D2800),
        ),
        background = ColorBackground(
            base = Color(0xFFFDFDFD),
            onBase = Color(0xFF1C1C1C),
            surface = Color(0xFFFAFAFA),
            onSurface = Color(0xFF1C1C1C),
            surfaceVariant = Color(0xFFE1E3E6),
            onSurfaceVariant = Color(0xFF43474E),
        ),
        error = ColorVariations(
            base = Color(0xFFBA1A1A),
            onBase = Color.White,
            container = Color(0xFFFFDAD6),
            onContainer = Color(0xFF410002),
        ),
        success = ColorVariations(
            base = Color(0xFF2E7D32),
            onBase = Color.White,
            container = Color(0xFFA5D6A7),
            onContainer = Color(0xFF003D00),
        ),
        warning = ColorVariations(
            base = Color(0xFFE65100),
            onBase = Color.White,
            container = Color(0xFFFFCC80),
            onContainer = Color(0xFF612100),
        ),
        info = ColorVariations(
            base = Color(0xFF0288D1),
            onBase = Color.White,
            container = Color(0xFF81D4FA),
            onContainer = Color(0xFF002C51),
        ),
        shimmer = ColorShimmer(
            background = Color(0xFFBFC1C5),
            foreground = Color(0xFFF4F5F7),
        ),
        outline = Color(0xFF7A7D82),
    )

    val Dark = ThemeColors(
        primary = ColorVariations(
            base = Color(0xFFFF7665),
            onBase = Color(0xFF5A1B16),
            container = Color(0xFF5A1B16),
            onContainer = Color(0xFFFFDAD6),
        ),
        secondary = ColorVariations(
            base = Color(0xFF58A8FF),
            onBase = Color(0xFF003355),
            container = Color(0xFF00477C),
            onContainer = Color(0xFFC9E7FF),
        ),
        tertiary = ColorVariations(
            base = Color(0xFFF7C143),
            onBase = Color(0xFF3D2800),
            container = Color(0xFF594100),
            onContainer = Color(0xFFFFF3C1),
        ),
        background = ColorBackground(
            base = Color(0xFF161618),
            onBase = Color(0xFFE2E2E2),
            surface = Color(0xFF1E1E1F),
            onSurface = Color(0xFFE2E2E2),
            surfaceVariant = Color(0xFF3A3B40),
            onSurfaceVariant = Color(0xFFC7C8D0),
        ),
        error = ColorVariations(
            base = Color(0xFFFFB4A9),
            onBase = Color(0xFF5A1B16),
            container = Color(0xFF5A1B16),
            onContainer = Color(0xFFFFDAD6),
        ),
        success = ColorVariations(
            base = Color(0xFF81C784),
            onBase = Color(0xFF003D00),
            container = Color(0xFF2E7D32),
            onContainer = Color(0xFFA5D6A7),
        ),
        warning = ColorVariations(
            base = Color(0xFFFF9800),
            onBase = Color(0xFF612100),
            container = Color(0xFFE65100),
            onContainer = Color(0xFFFFCC80),
        ),
        info = ColorVariations(
            base = Color(0xFF81D4FA),
            onBase = Color(0xFF002C51),
            container = Color(0xFF0288D1),
            onContainer = Color(0xFF81D4FA),
        ),
        shimmer = ColorShimmer(
            background = Color(0xFF2A2B30),
            foreground = Color(0xFF4A4D55)
        ),
        outline = Color(0xFF8C8E91),
    )
}
