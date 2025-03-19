package com.patrickhoette.pokedex.app.molecule.nav.item

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.semantics.Role.Companion.Tab
import androidx.compose.ui.text.drawText
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutTokens

@Composable
fun NavItem(
    entry: NavEntry,
    measurePolicy: MeasurePolicy,
    state: NavItemState,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(state, entry.isSelected) { state.updateSelected(entry.isSelected) }

    val indicatorColor = NavLayoutTokens.Indicator
    val painter = rememberVectorPainter(entry.icon)

    Layout(
        modifier = modifier
            .drawBehind {
                val radius = state.indicatorGradientRadius
                if (radius > 0) {
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

                painter.run {
                    translate(
                        left = state.iconTopLeft.x,
                        top = state.iconTopLeft.y,
                    ) {
                        draw(
                            size = state.iconSize,
                            colorFilter = ColorFilter.tint(color = state.foreground),
                        )
                    }
                }
                state.textLayoutResult?.let {
                    drawText(
                        textLayoutResult = it,
                        color = state.foreground,
                        topLeft = state.textTopLeft,
                    )
                }
            }
            .selectable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                selected = entry.isSelected,
                role = Tab,
                onClick = entry.onClick,
            ),
        measurePolicy = measurePolicy,
    )
}
