package com.patrickhoette.pokedex.app.molecule.nav.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokedex.app.molecule.nav.MultiNavParameterProvider
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.bar.NavBarTokens.MinHeight
import com.patrickhoette.pokedex.app.molecule.nav.bar.NavBarTokens.MinItemWidth
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object NavBarTokens {

    val MinHeight = 54.dp
    val MinItemWidth = 48.dp
}

@Composable
fun NavBar(
    entries: ImmutableList<NavEntry>,
    modifier: Modifier = Modifier,
) = Layout(
    content = { for (entry in entries) NavBarItem(entry = entry) },
    modifier = modifier.selectableGroup(),
) { measurables, constraints ->
    val itemCount = measurables.size

    val maxIntrinsicWidth = measurables.maxOfOrNull { it.maxIntrinsicWidth(constraints.maxHeight) } ?: 0
    val maxItemWidth = maxOf(
        maxIntrinsicWidth + 8.dp.roundToPx(),
        MinItemWidth.roundToPx(),
    )
    val itemWidth = if (maxItemWidth * itemCount <= constraints.maxWidth) {
        maxItemWidth
    } else {
        constraints.maxWidth / itemCount
    }

    val itemHeight = (measurables.maxOfOrNull { it.maxIntrinsicHeight(itemWidth) } ?: 0)
        .coerceAtLeast(MinHeight.roundToPx())

    val height = constraints.constrainHeight(itemHeight)

    val placeables = measurables.map {
        it.measure(
            Constraints.fixed(
                width = itemWidth,
                height = height,
            )
        )
    }

    val width = constraints.maxWidth

    val spacing = (width - itemWidth * itemCount) / (itemCount + 1)

    layout(width = width, height = height) {
        for ((index, placeable) in placeables.withIndex()) {
            placeable.placeRelative(
                x = spacing * (index + 1) + itemWidth * index,
                y = 0,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNavBar(
    @PreviewParameter(MultiNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    Column(
        modifier = Modifier
            .background(Color.Gray)
            .systemBarsPadding()
            .padding(Spacing.x2),
        verticalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        repeat(entries.size.coerceAtLeast(1)) { outerIndex ->
            var items by remember(entries) {
                mutableStateOf(
                    entries.mapIndexed { innerIndex, navItem ->
                        navItem.copy(isSelected = outerIndex == innerIndex)
                    }
                )
            }
            NavBar(
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
                modifier = Modifier.background(colors.background.surface),
            )
        }
    }
}
