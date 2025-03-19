package com.patrickhoette.core.ui.showcase.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.R
import com.patrickhoette.core.ui.showcase.utils.ColorUtils.convertToHexString
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing

@Composable
internal fun ColorSquare(
    background: Color,
    foreground: Color,
    modifier: Modifier = Modifier,
) = Row(modifier.border(width = 2.dp, color = colors.outline)) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .weight(9F)
            .background(background)
            .padding(Spacing.x2),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides foreground
        ) {
            Text(
                text = stringResource(R.string.debug_color_showcase_background),
                style = typography.label.medium,
            )

            val backgroundHex = convertToHexString(background)
            Text(
                text = backgroundHex,
                style = typography.mono.medium,
            )

            Text(
                text = stringResource(R.string.debug_color_showcase_foreground),
                modifier = Modifier.padding(top = Spacing.x0_5),
                style = typography.label.medium,
            )

            val foregroundHex = convertToHexString(foreground)
            Text(
                text = foregroundHex,
                style = typography.mono.medium,
            )
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1F)
            .background(foreground),
    )
}
