package com.patrickhoette.pokedex.app.molecule.nav.drawer

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.AnimationDuration
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.AnimationEasing
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.CollapsedWidth
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.ExpandedWidth
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState.Companion.SelectedIconScaleFactor
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState.Companion.UnselectedIconScaleFactor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt

@Composable
fun NavDrawerItem(
    entry: NavEntry,
    modifier: Modifier = Modifier,
    state: NavDrawerItemState,
) {
    LaunchedEffect(entry.isSelected) { state.updateSelected(entry.isSelected) }

    val indicatorColor = NavLayoutTokens.Indicator
    val textMeasurer = rememberTextMeasurer()
    val painter = rememberVectorPainter(entry.icon)
    Layout(
        modifier = modifier
            .drawBehind {
                painter.run {
                    val radius = state.indicatorGradientRadius
                    if (radius > 0) {
                        scale(
                            scaleX = state.indicatorScaleX,
                            scaleY = 1F,
                            pivot = state.indicatorGradientCenter,
                        ) {
                            drawRect(
                                brush = Brush.radialGradient(
                                    colorStops = arrayOf(
                                        0.5F to indicatorColor,
                                        1F to Color.Transparent,
                                    ),
                                    center = state.indicatorGradientCenter,
                                    radius = radius,
                                ),
                                topLeft = state.indicatorTopLeft,
                                size = state.indicatorSize,
                            )
                        }
                    }

                    translate(
                        left = state.iconTopLeft.x,
                        top = state.iconTopLeft.y,
                    ) {
                        draw(
                            size = state.iconSize,
                            colorFilter = ColorFilter.tint(state.foreground)
                        )
                    }

                    state.textLayoutResult?.let {
                        drawText(
                            textLayoutResult = it,
                            color = state.foreground,
                            topLeft = state.textTopLeft,
                            alpha = state.navDrawerState.progress.coerceIn(minimumValue = 0F, maximumValue = 1F)
                        )
                    }
                }
            }
            .selectable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                selected = entry.isSelected,
                role = Role.Button,
                onClick = entry.onClick,
            ),
    ) { _, constraints ->
        val iconWidth = entry.icon.defaultWidth.toPx()
        val iconHeight = entry.icon.defaultHeight.toPx()
        val iconSpaceSize = CollapsedWidth.toPx()
        state.calculatedIconSize = Size(width = iconWidth, height = iconHeight)

        state.textLayoutResult = textMeasurer.measure(
            text = entry.name,
            style = state.textStyle,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            constraints = Constraints.fixedWidth((ExpandedWidth.roundToPx() - state.iconSize.width).roundToInt()),
        )
        val textHeight = state.textLayoutResult?.size?.height?.toFloat() ?: 0F

        val height = constraints.constrainHeight(iconSpaceSize.roundToInt())
        val width = constraints.maxWidth

        state.indicatorGradientRadius = height / 1.5F * state.indicatorProgress
        state.indicatorGradientCenter = Offset(x = width.toFloat(), y = height / 2F)
        state.indicatorTopLeft = Offset(
            x = -(width / 2F),
            y = -(height / 2F),
        )
        state.indicatorSize = Size(
            width = width * 2F,
            height = height * 2F,
        )

        layout(width = width, height = height) {
            state.iconTopLeft = Offset(
                x = (iconSpaceSize - state.iconSize.width) / 2F,
                y = (iconSpaceSize - state.iconSize.height) / 2F,
            )
            state.textTopLeft = Offset(
                x = iconSpaceSize,
                y = (iconSpaceSize - textHeight) / 2F,
            )
        }
    }
}

@Composable
fun rememberNavDrawerItemState(
    entry: NavEntry,
    navDrawerState: NavDrawerState,
): NavDrawerItemState {
    val startTextStyle = typography.label.medium.copy(textMotion = TextMotion.Animated)
    val stopTextStyle = typography.label.large.copy(textMotion = TextMotion.Animated)
    val startForeground = NavLayoutTokens.ForegroundSelected
    val stopForeground = NavLayoutTokens.ForegroundUnselected

    return remember(startTextStyle, stopTextStyle, startForeground, stopForeground, navDrawerState) {
        NavDrawerItemState(
            startTextStyle = startTextStyle,
            stopTextStyle = stopTextStyle,
            startForeground = startForeground,
            stopForeground = stopForeground,
            navDrawerState = navDrawerState,
            textStyleAnimationSpec = tween(
                duration = AnimationDuration,
                easing = AnimationEasing,
            ),
            foregroundAnimationSpec = tween(
                duration = AnimationDuration,
                easing = AnimationEasing,
            ),
            radiusAnimationSpec = tween(
                duration = AnimationDuration,
                easing = AnimationEasing,
            ),
            iconScaleAnimationSpec = tween(
                duration = AnimationDuration,
                easing = AnimationEasing,
            ),
            initialIsSelected = entry.isSelected,
        )
    }
}

@Stable
class NavDrawerItemState(
    private val startTextStyle: TextStyle,
    private val stopTextStyle: TextStyle,
    private val startForeground: Color,
    private val stopForeground: Color,
    private val textStyleAnimationSpec: AnimationSpec<Float>,
    private val foregroundAnimationSpec: AnimationSpec<Color>,
    private val radiusAnimationSpec: AnimationSpec<Float>,
    private val iconScaleAnimationSpec: AnimationSpec<Float>,
    val navDrawerState: NavDrawerState,
    initialIsSelected: Boolean,
) {

    var textLayoutResult by mutableStateOf<TextLayoutResult?>(null)
    var textTopLeft by mutableStateOf(Offset.Zero)

    private val textStyleFraction = Animatable(if (initialIsSelected) 1F else 0F)
    val textStyle by derivedStateOf {
        lerp(
            start = startTextStyle,
            stop = stopTextStyle,
            fraction = textStyleFraction.value,
        )
    }

    private val iconScaleFactor = Animatable(
        if (initialIsSelected) SelectedIconScaleFactor else UnselectedIconScaleFactor
    )
    var calculatedIconSize by mutableStateOf(Size.Zero)
    val iconSize by derivedStateOf { calculatedIconSize * iconScaleFactor.value }
    var iconTopLeft by mutableStateOf(Offset.Zero)

    private val _foreground = Animatable(if (initialIsSelected) startForeground else stopForeground)
    val foreground by _foreground.asState()

    private val _indicatorProgress = Animatable(if (initialIsSelected) 1F else 0F)
    val indicatorProgress by _indicatorProgress.asState()

    var indicatorGradientCenter by mutableStateOf(Offset.Zero)
    var indicatorGradientRadius by mutableFloatStateOf(0F)
    var indicatorTopLeft by mutableStateOf(Offset.Zero)
    var indicatorSize by mutableStateOf(Size.Zero)
    val indicatorScaleX by derivedStateOf { (1F + 6F * navDrawerState.progress).coerceAtLeast(1F) }

    suspend fun updateSelected(selected: Boolean) = coroutineScope {
        awaitAll(
            async {
                textStyleFraction.animateTo(
                    targetValue = if (selected) 1F else 0F,
                    animationSpec = textStyleAnimationSpec,
                )
            },
            async {
                _foreground.animateTo(
                    targetValue = if (selected) startForeground else stopForeground,
                    animationSpec = foregroundAnimationSpec,
                )
            },
            async {
                _indicatorProgress.animateTo(
                    targetValue = if (selected) 1F else 0F,
                    animationSpec = radiusAnimationSpec,
                )
            },
            async {
                iconScaleFactor.animateTo(
                    targetValue = if (selected) SelectedIconScaleFactor else UnselectedIconScaleFactor,
                    animationSpec = iconScaleAnimationSpec,
                )
            },
        )
    }
}
