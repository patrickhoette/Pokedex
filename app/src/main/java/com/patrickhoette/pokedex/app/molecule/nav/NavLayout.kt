package com.patrickhoette.pokedex.app.molecule.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.animation.AnimationDefaults.BounceEasing
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens.Background
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutType.*
import com.patrickhoette.pokedex.app.molecule.nav.bar.NavBar
import com.patrickhoette.pokedex.app.molecule.nav.drawer.NavDrawer
import com.patrickhoette.pokedex.app.molecule.nav.rail.NavRail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.time.Duration.Companion.milliseconds

object NavLayoutTokens {

    val ForegroundSelected
        @Composable
        get() = colors.primary.base

    val ForegroundUnselected
        @Composable
        get() = colors.secondary.base

    val Background
        @Composable
        get() = colors.background.surface

    val Indicator
        @Composable
        get() = colors.primary.container

    val AnimationDuration = 400.milliseconds
    val AnimationEasing = BounceEasing
}

@Composable
fun NavLayout(
    type: NavLayoutType,
    entries: ImmutableList<NavEntry>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val consumedWindowInsets = when (type) {
        Bar -> WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
        Rail -> WindowInsets.displayCutout.only(WindowInsetsSides.Start)
        Drawer -> WindowInsets.displayCutout.only(WindowInsetsSides.Start)
    }
    val paddingWindowInsets = if (type == Bar) {
        consumedWindowInsets
    } else {
        consumedWindowInsets.add(
            WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
        )
    }

    Layout(
        content = {
            Box(
                Modifier
                    .shadow(elevation = 20.dp)
                    .background(Background)
                    .windowInsetsPadding(paddingWindowInsets)
                    .layoutId(NavLayoutId.Nav),
            ) {
                NavSection(
                    entries = entries,
                    type = type,
                )
            }
            Box(
                modifier = Modifier
                    .layoutId(NavLayoutId.Content)
                    .consumeWindowInsets(consumedWindowInsets)
            ) {
                content()
            }
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val navMeasurable = measurables.first { it.layoutId == NavLayoutId.Nav }
        val contentMeasurable = measurables.first { it.layoutId == NavLayoutId.Content }

        val navPlaceable = navMeasurable.measure(looseConstraints)

        val height = constraints.maxHeight
        val width = constraints.maxWidth

        val contentPlaceable = contentMeasurable.measure(
            if (type == Bar) {
                constraints.copy(
                    minHeight = height - navPlaceable.height,
                    maxHeight = height - navPlaceable.height,
                )
            } else {
                constraints.copy(
                    minWidth = width - navPlaceable.width,
                    maxWidth = width - navPlaceable.width,
                )
            }
        )

        layout(width, height) {
            if (type == Bar) {
                contentPlaceable.placeRelative(0, 0)
                navPlaceable.placeRelative(0, height - navPlaceable.height)
            } else {
                contentPlaceable.placeRelative(navPlaceable.width, 0)
                navPlaceable.placeRelative(0, 0)
            }
        }
    }
}

@Composable
private fun NavSection(
    entries: ImmutableList<NavEntry>,
    type: NavLayoutType,
    modifier: Modifier = Modifier,
) = when (type) {
    Bar -> NavBar(
        entries = entries,
        modifier = modifier,
    )
    Rail -> NavRail(
        entries = entries,
        modifier = modifier,
    )
    Drawer -> NavDrawer(
        entries = entries,
        modifier = modifier,
    )
}

enum class NavLayoutType {

    Bar,
    Rail,
    Drawer,
}

private enum class NavLayoutId {

    Nav,
    Content,
}

@Immutable
data class NavEntry(
    val icon: ImageVector,
    val name: String,
    val isSelected: Boolean,
    val onClick: () -> Unit,
)

@Preview(device = Devices.PIXEL_7_PRO, showSystemUi = true)
@Composable
private fun PreviewNavLayoutBar(
    @PreviewParameter(SingleNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    var items by remember(entries) { mutableStateOf(entries) }
    NavLayout(
        type = Bar,
        entries = items
            .mapIndexed { outerIndex, outerItem ->
                outerItem.copy(
                    onClick = {
                        items = items.mapIndexed { innerIndex, innerItem ->
                            innerItem.copy(isSelected = outerIndex == innerIndex)
                        }
                    },
                )
            }
            .toImmutableList(),
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background.base),
    ) {}
}

@Preview(device = "spec:width=891dp,height=411dp,dpi=420", showSystemUi = true)
@Composable
private fun PreviewNavLayoutRailPhone(
    @PreviewParameter(SingleNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    var items by remember(entries) { mutableStateOf(entries) }
    NavLayout(
        type = Rail,
        entries = items
            .mapIndexed { outerIndex, outerItem ->
                outerItem.copy(
                    onClick = {
                        items = items.mapIndexed { innerIndex, innerItem ->
                            innerItem.copy(isSelected = outerIndex == innerIndex)
                        }
                    },
                )
            }
            .toImmutableList(),
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background.base),
    ) {}
}

@Preview(device = Devices.TABLET, showSystemUi = true)
@Composable
private fun PreviewNavLayoutRailTablet(
    @PreviewParameter(SingleNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    var items by remember(entries) { mutableStateOf(entries) }
    NavLayout(
        type = Rail,
        entries = items
            .mapIndexed { outerIndex, outerItem ->
                outerItem.copy(
                    onClick = {
                        items = items.mapIndexed { innerIndex, innerItem ->
                            innerItem.copy(isSelected = outerIndex == innerIndex)
                        }
                    },
                )
            }
            .toImmutableList(),
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background.base),
    ) {}
}

@Preview(device = Devices.DESKTOP, showSystemUi = true)
@Composable
private fun PreviewNavLayoutDrawer(
    @PreviewParameter(SingleNavParameterProvider::class) entries: List<NavEntry>,
) = AppTheme {
    var items by remember(entries) { mutableStateOf(entries) }
    NavLayout(
        type = Drawer,
        entries = items
            .mapIndexed { outerIndex, outerItem ->
                outerItem.copy(
                    onClick = {
                        items = items.mapIndexed { innerIndex, innerItem ->
                            innerItem.copy(isSelected = outerIndex == innerIndex)
                        }
                    },
                )
            }
            .toImmutableList(),
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background.base),
    ) {}
}
