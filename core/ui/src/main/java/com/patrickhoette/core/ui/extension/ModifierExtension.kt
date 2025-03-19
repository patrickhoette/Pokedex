package com.patrickhoette.core.ui.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.theme.Theme.colors

@Stable
@Composable
fun Modifier.shimmerItem(shape: Shape = RoundedCornerShape(4.dp)) = composed {
    then(
        Modifier
            .clip(shape)
            .background(colors.shimmer.background),
    )
}
