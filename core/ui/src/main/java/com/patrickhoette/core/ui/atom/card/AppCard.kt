package com.patrickhoette.core.ui.atom.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.atom.card.AppCardTokens.Elevation
import com.patrickhoette.core.ui.atom.card.AppCardTokens.Shape
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing

object AppCardTokens {

    val Shape
        get() = RoundedCornerShape(20.dp)

    val Elevation = 20.dp
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Box(
    modifier = modifier
        .shadow(elevation = Elevation, shape = Shape)
        .background(colors.background.surface)
        .padding(Spacing.x2),
    contentAlignment = Alignment.Center,
) {
    CompositionLocalProvider(
        LocalContentColor provides colors.background.onSurface,
        content = content,
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppCard() = AppTheme {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background.base)
            .padding(Spacing.x2)
    ) {
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
        ) {}
    }
}
