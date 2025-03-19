package com.patrickhoette.pokedex.app.debug

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
import com.patrickhoette.core.ui.atom.button.PrimaryButton
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokedex.app.R
import com.patrickhoette.core.ui.R as CoreR

@Composable
internal fun DebugScreen(
    onOpenShowcase: () -> Unit,
    onOpenPlayground: () -> Unit,
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
        text = stringResource(R.string.debug_title),
        modifier = Modifier.semantics { heading() },
        style = typography.headings.large,
    )

    PrimaryButton(
        text = stringResource(CoreR.string.debug_showcase_title),
        onClick = onOpenShowcase,
        modifier = Modifier.fillMaxWidth(),
    )

    PrimaryButton(
        text = stringResource(CoreR.string.debug_playground_title),
        onClick = onOpenPlayground,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewDebugScreen() = AppTheme {
    DebugScreen(
        onOpenShowcase = {},
        onOpenPlayground = {},
        modifier = Modifier.fillMaxSize(),
    )
}
