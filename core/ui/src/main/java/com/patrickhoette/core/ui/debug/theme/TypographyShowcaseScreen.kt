package com.patrickhoette.core.ui.debug.theme

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
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacings

@Composable
fun TypographyShowcaseScreen(
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .background(colors.background.base)
        .verticalScroll(rememberScrollState())
        .statusBarsPadding()
        .padding(Spacings.x2),
    verticalArrangement = Arrangement.spacedBy(Spacings.x2),
) {
    Text(
        text = stringResource(R.string.debug_typography_showcase_title),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_display_large),
        color = colors.background.onBase,
        style = typography.display.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_display_medium),
        color = colors.background.onBase,
        style = typography.display.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_display_small),
        color = colors.background.onBase,
        style = typography.display.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_headings_large),
        color = colors.background.onBase,
        style = typography.headings.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_headings_medium),
        color = colors.background.onBase,
        style = typography.headings.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_headings_small),
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_body_large),
        color = colors.background.onBase,
        style = typography.body.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_body_medium),
        color = colors.background.onBase,
        style = typography.body.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_body_small),
        color = colors.background.onBase,
        style = typography.body.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_label_large),
        color = colors.background.onBase,
        style = typography.label.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_label_medium),
        color = colors.background.onBase,
        style = typography.label.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_label_small),
        color = colors.background.onBase,
        style = typography.label.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_mono_large),
        color = colors.background.onBase,
        style = typography.mono.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_mono_medium),
        color = colors.background.onBase,
        style = typography.mono.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_mono_small),
        color = colors.background.onBase,
        style = typography.mono.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_caption),
        color = colors.background.onBase,
        style = typography.caption,
    )
}

@Preview
@Composable
private fun PreviewTypographyShowcaseScreen() = AppTheme {
    TypographyShowcaseScreen(modifier = Modifier.fillMaxSize())
}
