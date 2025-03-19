package com.patrickhoette.pokedex.app.molecule.nav.item

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState.Companion.SelectedIconScaleFactor
import com.patrickhoette.pokedex.app.molecule.nav.item.NavItemState.Companion.UnselectedIconScaleFactor
import kotlin.math.roundToInt

@Stable
abstract class SmallNavItemMeasurePolicy : MeasurePolicy {

    abstract val textMeasurer: TextMeasurer
    abstract val entry: NavEntry
    abstract val state: NavItemState

    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        state.textLayoutResult = textMeasurer.measure(
            style = state.textStyle,
            constraints = Constraints(
                minWidth = 0,
                minHeight = 0,
                maxWidth = constraints.maxWidth.coerceAtLeast(0),
                maxHeight = constraints.maxHeight.coerceAtLeast(0),
            ),
        )

        state.calculatedIconSize = Size(
            width = entry.icon.defaultWidth.toPx(),
            height = entry.icon.defaultHeight.toPx(),
        )

        val width = constraints.constrainWidth(
            maxOf(
                state.iconSize.width.roundToInt(),
                state.textLayoutResult?.size?.width ?: 0,
            )
        )
        val contentHeight = (state.iconSize.height + (state.textLayoutResult?.size?.height ?: 0)).roundToInt()
        val height = constraints.constrainHeight(contentHeight)

        measureRadius(width = width, height = height)

        return layout(width = width, height = height) {
            val remainingHeight = height - contentHeight
            val vertSpacing = remainingHeight / 2F
            state.iconTopLeft = Offset(
                x = (width - state.iconSize.width) / 2F,
                y = vertSpacing,
            )
            state.textTopLeft = Offset(
                x = (width - (state.textLayoutResult?.size?.width ?: 0)) / 2F,
                y = vertSpacing + state.iconSize.height,
            )
        }
    }

    protected abstract fun MeasureScope.measureRadius(width: Int, height: Int)

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int {
        val iconWidth = (entry.icon.defaultWidth.toPx() * SelectedIconScaleFactor).roundToInt()
        val textWidth = textMeasurer.measure(
            constraints = Constraints.fixedHeight(height),
            style = state.stopTextStyle,
        ).size.width
        return maxOf(iconWidth, textWidth)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
        val iconHeight = (entry.icon.defaultHeight.toPx() * SelectedIconScaleFactor).roundToInt()
        val textHeight = textMeasurer.measure(
            constraints = Constraints.fixedWidth(width),
            style = state.stopTextStyle,
        ).size.height
        return iconHeight + textHeight
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int {
        val iconWidth = (entry.icon.defaultWidth.toPx() * UnselectedIconScaleFactor).roundToInt()
        val textWidth = textMeasurer.measure(
            constraints = Constraints.fixedHeight(height),
            style = state.startTextStyle,
        ).size.width
        return maxOf(iconWidth, textWidth)
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
        val iconHeight = (entry.icon.defaultHeight.toPx() * UnselectedIconScaleFactor).roundToInt()
        val textHeight = textMeasurer.measure(
            constraints = Constraints.fixedWidth(width),
            style = state.startTextStyle,
        ).size.height
        return iconHeight + textHeight
    }

    context(IntrinsicMeasureScope)
    protected abstract fun TextMeasurer.measure(
        constraints: Constraints,
        style: TextStyle,
    ): TextLayoutResult
}
