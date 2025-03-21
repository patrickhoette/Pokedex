package com.patrickhoette.pokedex.app.molecule.nav.drawer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokedex.app.molecule.nav.MultiNavParameterProvider
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.AnimationDuration
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.AnimationEasing
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.CollapsedWidth
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.ExpandedWidth
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawerTokens.ItemSpacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sv.lib.squircleshape.SquircleShape
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

object NavDrawerTokens {

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
