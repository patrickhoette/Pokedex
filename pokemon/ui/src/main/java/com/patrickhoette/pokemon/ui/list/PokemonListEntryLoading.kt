package com.patrickhoette.pokemon.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.atom.card.AppCardTokens
import com.patrickhoette.core.ui.extension.shimmerItem
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokemon.ui.R
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun PokemonListEntryLoading(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = AppCardTokens.Elevation,
                shape = AppCardTokens.Shape,
            )
            .background(colors.background.surface)
            .shimmer(shimmer),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F)
                .shimmerItem(RectangleShape),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.x2)
                .shimmerItem(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.x0_5, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.pokemon_list_dex_number, 1),
                modifier = Modifier.alpha(0F),
                style = typography.mono.medium,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewPokemonListEntryLoading() = AppTheme {
    val shimmer = rememberShimmer(ShimmerBounds.View)
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.background(colors.background.base),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(Spacing.x2),
        horizontalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        items(10) { PokemonListEntryLoading(shimmer) }
    }
}
