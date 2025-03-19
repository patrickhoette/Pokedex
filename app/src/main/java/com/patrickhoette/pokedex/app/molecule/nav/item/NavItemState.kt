package com.patrickhoette.pokedex.app.molecule.nav.item

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextMotion
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Composable
fun rememberNavItemState(
    startTextStyle: TextStyle,
    stopTextStyle: TextStyle,
    entry: NavEntry,
): NavItemState {
    val startForeground = NavLayoutTokens.ForegroundSelected
    val stopForeground = NavLayoutTokens.ForegroundUnselected

    // isSelected isn't a key on purpose as we only need it for the start value and don't want to recreate the state
    // when it changes, as that would break animation.
    return remember(startTextStyle, stopTextStyle, startForeground, stopForeground) {
        NavItemState(
            startTextStyle = startTextStyle,
            stopTextStyle = stopTextStyle,
            startForeground = startForeground,
            stopForeground = stopForeground,
            textStyleAnimationSpec = tween(
                duration = NavLayoutTokens.AnimationDuration,
                easing = NavLayoutTokens.AnimationEasing,
            ),
            foregroundAnimationSpec = tween(
                duration = NavLayoutTokens.AnimationDuration,
                easing = NavLayoutTokens.AnimationEasing,
            ),
            radiusAnimationSpec = tween(
                duration = NavLayoutTokens.AnimationDuration,
                easing = NavLayoutTokens.AnimationEasing,
            ),
            iconScaleAnimationSpec = tween(
                duration = NavLayoutTokens.AnimationDuration,
                easing = NavLayoutTokens.AnimationEasing,
            ),
            initialIsSelected = entry.isSelected,
        )
    }
}

@Stable
class NavItemState(
    val startTextStyle: TextStyle,
    val stopTextStyle: TextStyle,
    private val startForeground: Color,
    private val stopForeground: Color,
    private val textStyleAnimationSpec: AnimationSpec<Float>,
    private val foregroundAnimationSpec: AnimationSpec<Color>,
    private val radiusAnimationSpec: AnimationSpec<Float>,
    private val iconScaleAnimationSpec: AnimationSpec<Float>,
    initialIsSelected: Boolean,
) {

    var textLayoutResult by mutableStateOf<TextLayoutResult?>(null)
    var textTopLeft by mutableStateOf(Offset.Zero)

    private val iconScaleFactor = Animatable(
        if (initialIsSelected) SelectedIconScaleFactor else UnselectedIconScaleFactor
    )
    var calculatedIconSize by mutableStateOf(Size.Zero)
    val iconSize by derivedStateOf { calculatedIconSize * iconScaleFactor.value }
    var iconTopLeft by mutableStateOf(Offset.Zero)

    private val textStyleFraction = Animatable(
        if (initialIsSelected) SelectedTextStyleProgress else UnselectedTextStyleProgress
    )
    val textStyle by derivedStateOf {
        lerp(
            start = startTextStyle,
            stop = stopTextStyle,
            fraction = textStyleFraction.value,
        ).copy(
            textMotion = TextMotion.Animated,
        )
    }

    private val _foreground = Animatable(if (initialIsSelected) startForeground else stopForeground)
    val foreground by _foreground.asState()

    private val _indicatorProgress = Animatable(
        if (initialIsSelected) SelectedIndicatorProgress else UnselectedIndicatorProgress
    )
    val indicatorProgress by _indicatorProgress.asState()

    var indicatorGradientCenter by mutableStateOf(Offset.Zero)
    var indicatorGradientRadius by mutableFloatStateOf(0F)
    var indicatorTopLeft by mutableStateOf(Offset.Zero)
    var indicatorSize by mutableStateOf(Size.Zero)

    suspend fun updateSelected(isSelected: Boolean) = coroutineScope {
        awaitAll(
            async {
                textStyleFraction.animateTo(
                    targetValue = if (isSelected) SelectedTextStyleProgress else UnselectedTextStyleProgress,
                    animationSpec = textStyleAnimationSpec,
                )
            },
            async {
                _foreground.animateTo(
                    targetValue = if (isSelected) startForeground else stopForeground,
                    animationSpec = foregroundAnimationSpec,
                )
            },
            async {
                _indicatorProgress.animateTo(
                    targetValue = if (isSelected) SelectedIndicatorProgress else UnselectedIndicatorProgress,
                    animationSpec = radiusAnimationSpec,
                )
            },
            async {
                iconScaleFactor.animateTo(
                    targetValue = if (isSelected) SelectedIconScaleFactor else UnselectedIconScaleFactor,
                    animationSpec = iconScaleAnimationSpec,
                )
            },
        )
    }

    companion object {

        private const val SelectedTextStyleProgress = 1F
        private const val UnselectedTextStyleProgress = 0F

        private const val SelectedIndicatorProgress = 1F
        private const val UnselectedIndicatorProgress = 0F

        const val SelectedIconScaleFactor = 1.25F
        const val UnselectedIconScaleFactor = 1F
    }
}
