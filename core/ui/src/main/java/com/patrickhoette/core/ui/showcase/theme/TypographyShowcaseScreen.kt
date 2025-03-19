package com.patrickhoette.core.ui.showcase.theme

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
import com.patrickhoette.core.ui.theme.definition.Spacing

@Composable
fun TypographyShowcaseScreen(
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .background(colors.background.base)
        .verticalScroll(rememberScrollState())
        .padding(Spacing.x2)
        .statusBarsPadding()
        .displayCutoutPadding(),
    verticalArrangement = Arrangement.spacedBy(Spacing.x2),
) {
    Text(
        text = stringResource(R.string.debug_typography_showcase_title),
        modifier = Modifier.semantics { heading() },
        style = typography.headings.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_display_large),
        style = typography.display.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_display_medium),
        style = typography.display.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_display_small),
        style = typography.display.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_headings_large),
        style = typography.headings.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_headings_medium),
        style = typography.headings.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_headings_small),
        style = typography.headings.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_body_large),
        style = typography.body.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_body_medium),
        style = typography.body.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_body_small),
        style = typography.body.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_label_large),
        style = typography.label.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_label_medium),
        style = typography.label.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_label_small),
        style = typography.label.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_mono_large),
        style = typography.mono.large,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_mono_medium),
        style = typography.mono.medium,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_mono_small),
        style = typography.mono.small,
    )

    Text(
        text = stringResource(R.string.debug_typography_showcase_caption),
        style = typography.caption,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewTypographyShowcaseScreen() = AppTheme {
    TypographyShowcaseScreen(modifier = Modifier.fillMaxSize())
}
