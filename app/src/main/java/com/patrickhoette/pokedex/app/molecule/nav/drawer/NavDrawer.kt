package com.patrickhoette.pokedex.app.molecule.nav.drawer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokedex.app.molecule.nav.MultiNavParameterProvider
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.AnimationDuration
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.AnimationEasing
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.CollapsedWidth
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.ExpandedWidth
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.ItemSpacing
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState.Companion.SelectedIconScaleFactor
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState.Companion.UnselectedIconScaleFactor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sv.lib.squircleshape.SquircleShape
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private object NavDrawerTokens {

    val CollapsedWidth = 48.dp
    val ExpandedWidth = 240.dp

    val ItemSpacing = Spacing.x1
}

@Composable
fun NavDrawer(
    entries: ImmutableList<NavEntry>,
    modifier: Modifier = Modifier,
    state: NavDrawerState = rememberNavDrawerState(),
) {
    Layout(
        content = {
            ExpandIcon(state)

            for (entry in entries) {
                NavDrawerItem(
                    entry = entry,
                    state = rememberNavDrawerItemState(entry = entry, navDrawerState = state),
                )
            }
        },
        modifier = modifier.padding(Spacing.x0_5),
    ) { measurables, constraints ->
        val expandIconMeasurable = measurables.first()
        val entryMeasurables = measurables.drop(1)

        val expandIconPlaceable = expandIconMeasurable.measure(constraints)

        val height = constraints.maxHeight

        val collapsedWidth = CollapsedWidth.toPx()
        val expandedWidth = ExpandedWidth.toPx()
        val width = constraints.constrainWidth(
            (collapsedWidth + (expandedWidth - collapsedWidth) * state.progress).roundToInt()
        )

        val entryPlaceables = entryMeasurables.map {
            it.measure(
                constraints.copy(
                    minWidth = minOf(width, collapsedWidth.roundToInt()),
                    maxWidth = width,
                ),
            )
        }

        val itemSpacing = ItemSpacing.roundToPx()

        layout(width = width, height = height) {
            expandIconPlaceable.placeRelative(
                x = width - expandIconPlaceable.width,
                y = 0,
            )

            var currentY = expandIconPlaceable.height + itemSpacing
            for (placeable in entryPlaceables) {
                placeable.placeRelative(x = 0, y = currentY)
                currentY += placeable.height + itemSpacing
            }
        }
    }
}

@Composable
private fun NavDrawerItem(
    entry: NavEntry,
    modifier: Modifier = Modifier,
    state: NavDrawerItemState,
) {
    LaunchedEffect(entry.isSelected) { state.updateSelected(entry.isSelected) }

    val textMeasurer = rememberTextMeasurer()
    val painter = rememberVectorPainter(entry.icon)
    Layout(
        modifier = modifier
            .drawBehind {
                painter.run {
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
            )
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

        layout(width = constraints.maxWidth, height = height) {
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

@Composable
fun rememberNavDrawerState(initialIsExpanded: Boolean = false): NavDrawerState {
    return remember {
        NavDrawerState(
            initialIsExpanded = initialIsExpanded,
            progressAnimationSpec = tween(duration = 600.milliseconds, easing = AnimationEasing),
        )
    }
}

@Stable
class NavDrawerState(
    initialIsExpanded: Boolean,
    val progressAnimationSpec: AnimationSpec<Float>,
) {

    var isExpanded by mutableStateOf(initialIsExpanded)

    private val _progress = Animatable(if (initialIsExpanded) 1F else 0F)
    val progress by _progress.asState()

    suspend fun toggle() = coroutineScope {
        val newState = !isExpanded
        isExpanded = newState
        awaitAll(
            async {
                _progress.animateTo(
                    targetValue = if (newState) 1F else 0F,
                    animationSpec = progressAnimationSpec,
                )
            },
        )
    }
}

@Composable
private fun ExpandIcon(
    state: NavDrawerState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .clip(SquircleShape())
            .background(colors.primary.container)
            .clickable(
                role = Role.Button,
                onClick = {
                    scope.launch { state.toggle() }
                },
            )
            .size(CollapsedWidth),
        contentAlignment = Alignment.Center
    ) {
        val iconRotation by animateFloatAsState(
            targetValue = if (state.isExpanded) 270F else 90F,
            animationSpec = tween(duration = AnimationDuration, easing = AnimationEasing),
            label = "Icon Rotation",
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer { rotationZ = iconRotation },
        )
    }
}

@Preview(name = "Light", device = DESKTOP)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, device = DESKTOP)
@Composable
private fun PreviewNavDrawer(
    @PreviewParameter(MultiNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .systemBarsPadding()
            .padding(Spacing.x2),
        horizontalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        repeat(entries.size.coerceAtLeast(1)) { outerIndex ->
            var items by remember(entries) {
                mutableStateOf(
                    entries.mapIndexed { innerIndex, navItem ->
                        navItem.copy(isSelected = outerIndex == innerIndex)
                    }
                )
            }
            val state = rememberNavDrawerState(outerIndex == 0)
            NavDrawer(
                entries = items
                    .mapIndexed { innerIndex, navItem ->
                        navItem.copy(
                            onClick = {
                                items = items.mapIndexed { index, item ->
                                    item.copy(isSelected = index == innerIndex)
                                }
                            },
                        )
                    }
                    .toImmutableList(),
                modifier = Modifier
                    .background(colors.background.surface)
                    .clip(RectangleShape),
                state = state,
            )
        }
    }
}

@Preview(device = DESKTOP)
@Composable
private fun PreviewNavDrawerDev(
    @PreviewParameter(MultiNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .systemBarsPadding()
            .padding(Spacing.x2),
    ) {
        NavDrawer(
            entries = entries.toImmutableList(),
            modifier = Modifier
                .background(colors.background.surface)
                .clip(RectangleShape),
        )
    }
}
