package com.patrickhoette.pokemon.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.core.ui.R
import com.patrickhoette.core.ui.atom.button.TextButton
import com.patrickhoette.core.ui.atom.card.AppCard
import com.patrickhoette.core.ui.extension.bodyRes
import com.patrickhoette.core.ui.extension.titleRes
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing

@Composable
fun PokemonListError(
    onRetry: () -> Unit,
    cause: GenericError,
    modifier: Modifier = Modifier,
) = AppCard(modifier) {
    Column(
        modifier = Modifier.padding(horizontal = Spacing.x2),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = null,
            modifier = Modifier
                .rotate(45F)
                .size(32.dp),
            tint = colors.error.base,
        )

        Text(
            text = stringResource(cause.titleRes),
            modifier = Modifier.padding(vertical = Spacing.x1),
            style = typography.headings.small,
            textAlign = TextAlign.Center,
            color = colors.error.base,
        )

        Text(
            text = stringResource(cause.bodyRes),
            style = typography.body.medium,
            textAlign = TextAlign.Center,
            color = colors.background.onSurface,
        )

        TextButton(
            onClick = onRetry,
            text = stringResource(R.string.retry),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.x1),
            leadingIcon = Icons.Filled.Refresh,
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewPokemonListError() = AppTheme {
    Column(
        modifier = Modifier
            .background(colors.background.base)
            .padding(Spacing.x2),
        verticalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        PokemonListError(
            onRetry = {},
            cause = GenericError.Server,
            modifier = Modifier.fillMaxWidth(),
        )

        PokemonListError(
            onRetry = {},
            cause = GenericError.Network,
            modifier = Modifier.fillMaxWidth(),
        )

        PokemonListError(
            onRetry = {},
            cause = GenericError.Unknown,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
