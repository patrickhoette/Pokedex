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
import com.patrickhoette.core.ui.debug.utils.ColorSquare
import com.patrickhoette.core.ui.debug.utils.ColorUtils.convertToHexString
import com.patrickhoette.core.ui.debug.utils.LightDarkSideBySide
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacings
import com.patrickhoette.core.ui.theme.model.ColorVariations

@Composable
fun ColorShowcaseScreen(
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
        text = stringResource(R.string.debug_color_showcase_title),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.large,
    )

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_primary),
        colorVariations = { colors.primary },
    )

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_secondary),
        colorVariations = { colors.secondary },
    )

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_tertiary),
        colorVariations = { colors.tertiary },
    )

    Text(
        text = stringResource(R.string.debug_color_showcase_heading_background),
        modifier = Modifier
            .padding(top = Spacings.x1)
            .semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ColorSquare(
            background = colors.background.base,
            foreground = colors.background.onBase,
        )
    }

    Text(
        text = stringResource(R.string.debug_color_showcase_heading_surface),
        modifier = Modifier
            .padding(top = Spacings.x1)
            .semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ColorSquare(
            background = colors.background.surface,
            foreground = colors.background.onSurface,
        )
    }

    Text(
        text = stringResource(R.string.debug_color_showcase_heading_surface_variant),
        modifier = Modifier
            .padding(top = Spacings.x1)
            .semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ColorSquare(
            background = colors.background.surfaceVariant,
            foreground = colors.background.onSurfaceVariant,
        )
    }

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_error),
        colorVariations = { colors.error },
    )

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_success),
        colorVariations = { colors.success },
    )

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_warning),
        colorVariations = { colors.warning },
    )

    ColorVariationsSection(
        title = stringResource(R.string.debug_color_showcase_heading_info),
        colorVariations = { colors.info },
    )

    Text(
        text = stringResource(R.string.debug_color_showcase_heading_outline),
        modifier = Modifier
            .padding(top = Spacings.x1)
            .semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val colorHex = convertToHexString(colors.outline)
        Text(
            text = colorHex,
            modifier = Modifier
                .background(colors.outline)
                .padding(Spacings.x2)
                .fillMaxWidth(),
            color = colors.background.onBase,
            style = typography.mono.medium,
        )
    }
}

@Composable
private fun ColorVariationsSection(
    title: String,
    colorVariations: @Composable () -> ColorVariations,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.padding(top = Spacings.x1),
    verticalArrangement = Arrangement.spacedBy(Spacings.x2),
) {
    Text(
        text = title,
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    Text(
        text = stringResource(R.string.debug_color_showcase_subheading_main),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.label.large,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ColorSquare(
            background = colorVariations().base,
            foreground = colorVariations().onBase,
        )
    }

    Text(
        text = stringResource(R.string.debug_color_showcase_subheading_container),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.label.large,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ColorSquare(
            background = colorVariations().container,
            foreground = colorVariations().onContainer,
        )
    }
}

@Preview
@Composable
private fun PreviewColorShowcaseScreen() = AppTheme {
    ColorShowcaseScreen(modifier = Modifier.fillMaxSize())
}
