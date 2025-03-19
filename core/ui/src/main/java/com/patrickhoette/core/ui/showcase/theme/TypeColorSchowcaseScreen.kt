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
import com.patrickhoette.core.ui.extension.nameRes
import com.patrickhoette.core.ui.showcase.utils.ColorSquare
import com.patrickhoette.core.ui.showcase.utils.LightDarkSideBySide
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typeColors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokedex.entity.generic.Type

@Composable
fun TypeColorShowcaseScreen(
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
        text = stringResource(R.string.debug_type_color_showcase_title),
        modifier = Modifier.semantics { heading() },
        style = typography.headings.large,
    )

    for (type in Type.entries) {
        ColorSection(
            type = type,
            modifier = Modifier.padding(top = Spacing.x1),
        )
    }
}

@Composable
private fun ColorSection(
    type: Type,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(Spacing.x2),
) {
    Text(
        text = stringResource(type.nameRes),
        modifier = Modifier.semantics { heading() },
        style = typography.headings.small,
    )

    Text(
        text = stringResource(R.string.debug_type_color_showcase_subheading_base),
        modifier = Modifier.semantics { heading() },
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
        text = stringResource(R.string.debug_type_color_showcase_subheading_container),
        modifier = Modifier.semantics { heading() },
        style = typography.label.large,
    )

    LightDarkSideBySide(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val typeColors = typeColors[type]
        ColorSquare(
            background = typeColors.container,
            foreground = typeColors.onContainer,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewTypeColorShowcaseScreen() = AppTheme {
    TypeColorShowcaseScreen(modifier = Modifier.fillMaxSize())
}
