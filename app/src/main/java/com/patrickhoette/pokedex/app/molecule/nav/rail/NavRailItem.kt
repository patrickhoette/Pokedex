package com.patrickhoette.pokedex.app.molecule.nav.rail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItem
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState
import com.patrickhoette.pokedex.app.molecule.nav.item.SmallNavItemMeasurePolicy
import com.patrickhoette.pokedex.app.molecule.nav.item.rememberNavItemState

@Composable
fun NavRailItem(
    entry: NavEntry,
    modifier: Modifier = Modifier,
    state: NavItemState = rememberNavItemState(
        startTextStyle = typography.label.small.copy(
            textAlign = TextAlign.Center,
            textMotion = TextMotion.Animated,
        ),
        stopTextStyle = typography.label.medium.copy(
            textAlign = TextAlign.Center,
            textMotion = TextMotion.Animated,
        ),
        entry = entry,
    ),
) = NavItem(
    entry = entry,
    measurePolicy = rememberNavRailItemMeasurePolicy(entry = entry, state = state),
    state = state,
    modifier = modifier,
)

@Composable
private fun rememberNavRailItemMeasurePolicy(entry: NavEntry, state: NavItemState): NavRailItemMeasurePolicy {
    val textMeasurer = rememberTextMeasurer()
    return remember(entry, textMeasurer, state) {
        NavRailItemMeasurePolicy(textMeasurer = textMeasurer, entry = entry, state = state)
    }
}

@Stable
private class NavRailItemMeasurePolicy(
    override val textMeasurer: TextMeasurer,
    override val entry: NavEntry,
    override val state: NavItemState,
) : SmallNavItemMeasurePolicy() {

    override fun MeasureScope.measureRadius(width: Int, height: Int) {
        state.indicatorGradientRadius = width / 1.5F * state.indicatorProgress
        state.indicatorGradientCenter = Offset(x = width.toFloat(), y = height / 2F)
        state.indicatorTopLeft = Offset(
            x = -(width / 2F),
            y = -(height / 2F),
        )
        state.indicatorSize = Size(
            width = width * 2F,
            height = height * 2F,
        )
    }

    context(IntrinsicMeasureScope)
    override fun TextMeasurer.measure(
        constraints: Constraints,
        style: TextStyle,
    ) = measure(
        text = entry.name,
        style = style,
        overflow = TextOverflow.Ellipsis,
        constraints = constraints,
        layoutDirection = layoutDirection,
        density = Density(density),
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun PreviewNavRailItemSelected() = AppTheme {
    NavRailItem(
        entry = NavEntry(
            icon = Icons.Filled.AccountBox,
            name = "Account",
            isSelected = true,
            onClick = {},
        )
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun PreviewNavRailItemUnselected() = AppTheme {
    NavRailItem(
        entry = NavEntry(
            icon = Icons.Filled.AccountBox,
            name = "Account",
            isSelected = false,
            onClick = {},
        )
    )
}
