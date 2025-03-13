package com.patrickhoette.core.ui.debug.utils

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.definition.Spacings

@Composable
internal fun LightDarkSideBySide(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) = Row(
    modifier = modifier.height(IntrinsicSize.Min),
    horizontalArrangement = Arrangement.spacedBy(Spacings.x1),
) {
    AppTheme(isDark = false) {
        Box(
            modifier = Modifier.weight(1F),
            content = content,
        )
    }
    AppTheme(isDark = true) {
        Box(
            modifier = Modifier.weight(1F),
            content = content,
        )
    }
}
