package com.patrickhoette.pokemon.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.patrickhoette.core.ui.atom.card.AppCard
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokemon.ui.R

@Composable
fun PokemonListEnd(
    modifier: Modifier = Modifier,
) = AppCard(modifier) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.x0_5),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.pokemon_list_end_of_list_title),
            color = colors.primary.base,
            style = typography.label.large,
        )

        Text(
            text = stringResource(R.string.pokemon_list_end_of_list_body),
            color = colors.secondary.base,
            style = typography.body.small,
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewPokemonListEnd() = AppTheme {
    Box(
        modifier = Modifier
            .background(colors.background.base)
            .padding(Spacing.x2),
    ) {
        PokemonListEnd(modifier = Modifier.fillMaxWidth())
    }
}
