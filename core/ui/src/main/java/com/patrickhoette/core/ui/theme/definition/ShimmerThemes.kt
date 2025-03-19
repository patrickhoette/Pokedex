@file:Suppress("FunctionName")

package com.patrickhoette.core.ui.theme.definition

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.animation.AnimationDefaults
import com.patrickhoette.core.ui.extension.shimmerItem
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.model.ThemeColors
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.shimmer
import kotlin.time.Duration.Companion.seconds

object ShimmerThemes {

    fun createShimmerTheme(colors: ThemeColors): ShimmerTheme {
        val foreground = colors.shimmer.foreground
        return ShimmerTheme(
            animationSpec = AnimationDefaults.repeatableTween(
                duration = 3.seconds,
                easing = EaseInOutCubic,
            ),
            blendMode = BlendMode.SrcAtop,
            rotation = 15F,
            shaderColors = listOf(
                foreground.copy(alpha = 0F),
                foreground.copy(alpha = 0.125F),
                foreground.copy(alpha = 0.75F),
                foreground.copy(alpha = 0.125F),
                foreground.copy(alpha = 0F),
            ),
            shaderColorStops = listOf(0F, 0.15F, 0.5F, 0.85F, 1F),
            shimmerWidth = 100.dp,
        )
    }
}

@Composable
@PreviewLightDark
private fun PreviewShimmerTheme() = AppTheme {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shimmer()
            .shimmerItem(),
    )
}
