package com.patrickhoette.pokedex.app.molecule.nav.rail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokedex.app.molecule.nav.MultiNavParameterProvider
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.rail.NavRailTokens.MaxWidth
import com.patrickhoette.pokedex.app.molecule.nav.rail.NavRailTokens.MinItemHeight
import com.patrickhoette.pokedex.app.molecule.nav.rail.NavRailTokens.MinWidth
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object NavRailTokens {

    val MinWidth = 54.dp
    val MaxWidth = 72.dp
    val MinItemHeight = 48.dp
}

@Composable
fun NavRail(
    entries: ImmutableList<NavEntry>,
    modifier: Modifier = Modifier,
) = Layout(
    content = { for (entry in entries) NavRailItem(entry) },
    modifier = modifier.selectableGroup(),
) { measurables, constraints ->
    val itemCount = measurables.size

    val maxIntrinsicWidth = measurables.maxOfOrNull { it.maxIntrinsicWidth(constraints.maxHeight) } ?: 0
    val itemWidth = (maxIntrinsicWidth + 8.dp.roundToPx())
        .coerceIn(minimumValue = MinWidth.roundToPx(), maximumValue = MaxWidth.roundToPx())

    val maxItemHeight = maxOf(
        measurables.maxOfOrNull { it.maxIntrinsicHeight(itemWidth) } ?: 0,
        MinItemHeight.roundToPx(),
    )
    val itemHeight = if (maxItemHeight * itemCount <= constraints.maxHeight) {
        maxItemHeight
    } else {
        constraints.maxHeight / itemCount
    }

    val width = constraints.constrainWidth(itemWidth)

    val placeables = measurables.map {
        it.measure(
            Constraints.fixed(
                width = width,
                height = itemHeight,
            )
        )
    }

    val height = constraints.maxHeight

    val spacing = (height - itemHeight * itemCount) / (itemCount + 1)

    layout(width = width, height = height) {
        for ((index, placeable) in placeables.withIndex()) {
            placeable.placeRelative(
                x = 0,
                y = spacing * (index + 1) + itemHeight * index,
            )
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewNavRail(
    @PreviewParameter(MultiNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    Row(
        modifier = Modifier
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
            NavRail(
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
            )
        }
    }
}
