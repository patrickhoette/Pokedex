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
import com.patrickhoette.core.ui.debug.utils.LightDarkSideBySide
import com.patrickhoette.core.ui.extension.nameRes
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typeColors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacings
import com.patrickhoette.pokedex.entity.generic.Type

@Composable
fun TypeColorShowcaseScreen(
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
        text = stringResource(R.string.debug_type_color_showcase_title),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.large,
    )

    for (type in Type.entries) {
        ColorSection(
            type = type,
            modifier = Modifier.padding(top = Spacings.x1),
        )
    }
}

@Composable
private fun ColorSection(
    type: Type,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(Spacings.x2),
) {
    Text(
        text = stringResource(type.nameRes),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.headings.small,
    )

    Text(
        text = stringResource(R.string.debug_type_color_showcase_subheading_light),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.label.large,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val typeColors = typeColors[type]
        ColorSquare(
            background = typeColors.light,
            foreground = typeColors.onLight,
        )
    }

    Text(
        text = stringResource(R.string.debug_type_color_showcase_subheading_base),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.label.large,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val typeColors = typeColors[type]
        ColorSquare(
            background = typeColors.base,
            foreground = typeColors.onBase,
        )
    }

    Text(
        text = stringResource(R.string.debug_type_color_showcase_subheading_dark),
        modifier = Modifier.semantics { heading() },
        color = colors.background.onBase,
        style = typography.label.large,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val typeColors = typeColors[type]
        ColorSquare(
            background = typeColors.dark,
            foreground = typeColors.onDark,
        )
    }
}

@Preview
@Composable
private fun PreviewTypeColorShowcaseScreen() = AppTheme {
    TypeColorShowcaseScreen(modifier = Modifier.fillMaxSize())
}
