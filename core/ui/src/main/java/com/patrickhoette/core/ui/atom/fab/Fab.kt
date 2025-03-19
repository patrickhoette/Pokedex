package com.patrickhoette.core.ui.atom.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing
import sv.lib.squircleshape.SquircleShape

@Composable
fun Fab(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
) = FloatingActionButton(
    onClick = onClick,
    modifier = modifier
        .displayCutoutPadding()
        .padding(Spacing.x3),
    shape = SquircleShape(),
    containerColor = colors.primary.base,
    contentColor = colors.primary.onBase,
    elevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 30.dp,
        pressedElevation = 20.dp,
        focusedElevation = 40.dp,
        hoveredElevation = 45.dp,
    )
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = Modifier.size(32.dp),
    )
}

@PreviewLightDark
@Composable
private fun PreviewFab() = AppTheme {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background.base),
    ) {
        Fab(
            onClick = {},
            icon = Icons.Filled.Add,
            contentDescription = "",
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}
