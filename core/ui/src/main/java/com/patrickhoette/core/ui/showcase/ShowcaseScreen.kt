package com.patrickhoette.core.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.patrickhoette.core.ui.R
import com.patrickhoette.core.ui.atom.button.PrimaryButton
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing

@Composable
fun ShowcaseScreen(
    onOpenColors: () -> Unit,
    onOpenTypeColors: () -> Unit,
    onOpenTypography: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .background(colors.background.base)
        .verticalScroll(rememberScrollState())
        .padding(Spacing.x2)
        .systemBarsPadding()
        .displayCutoutPadding(),
    verticalArrangement = Arrangement.spacedBy(Spacing.x2),
) {
    Text(
        text = stringResource(R.string.debug_showcase_title),
        modifier = Modifier.semantics { heading() },
        style = typography.headings.large,
    )

    PrimaryButton(
        text = stringResource(R.string.debug_color_showcase_title),
        onClick = onOpenColors,
        modifier = Modifier.fillMaxWidth(),
    )

    PrimaryButton(
        text = stringResource(R.string.debug_type_color_showcase_title),
        onClick = onOpenTypeColors,
        modifier = Modifier.fillMaxWidth(),
    )

    PrimaryButton(
        text = stringResource(R.string.debug_typography_showcase_title),
        onClick = onOpenTypography,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewShowcaseScreen() = AppTheme {
    ShowcaseScreen(
        onOpenColors = {},
        onOpenTypeColors = {},
        onOpenTypography = {},
        modifier = Modifier.fillMaxSize(),
    )
}
