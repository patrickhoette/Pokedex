package com.patrickhoette.pokemon.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.definition.Spacing

@Composable
fun PokemonListError(
    cause: GenericError,
    modifier: Modifier = Modifier,
) {
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
            cause = GenericError.Server,
            modifier = Modifier.fillMaxWidth(),
        )

        PokemonListError(
            cause = GenericError.Network,
            modifier = Modifier.fillMaxWidth(),
        )

        PokemonListError(
            cause = GenericError.Unknown,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
