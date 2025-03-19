package com.patrickhoette.core.ui.grid

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AdaptiveGridWithMax(
    private val minSize: Dp,
    private val maxColumns: Int,
) : GridCells {

    init {
        require(minSize > 0.dp) { "Provided min size $minSize should be larger than zero" }
        require(maxColumns > 0) { "Provided max columns $maxColumns should be larger than zero" }
    }

    override fun Density.calculateCrossAxisCellSizes(
        availableSize: Int,
        spacing: Int,
    ): List<Int> {
        val count = ((availableSize + spacing) / (minSize.roundToPx() + spacing))
            .coerceIn(minimumValue = 1, maximumValue = maxColumns)
        return calculateCellsCrossAxisSizeImpl(availableSize, count, spacing)
    }

    private fun calculateCellsCrossAxisSizeImpl(
        gridSize: Int,
        slotCount: Int,
        spacing: Int,
    ): List<Int> {
        val gridSizeWithoutSpacing = gridSize - spacing * (slotCount - 1)
        val slotSize = gridSizeWithoutSpacing / slotCount
        val remainingPixels = gridSizeWithoutSpacing % slotCount
        return List(slotCount) {
            slotSize + if (it < remainingPixels) 1 else 0
        }
    }
}
