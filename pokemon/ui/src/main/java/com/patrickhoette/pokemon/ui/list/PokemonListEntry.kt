package com.patrickhoette.pokemon.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.patrickhoette.core.ui.atom.card.AppCardTokens
import com.patrickhoette.core.ui.atom.image.StateAwareAsyncImage
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.core.ui.theme.Theme.typeColors
import com.patrickhoette.core.ui.theme.Theme.typography
import com.patrickhoette.core.ui.theme.definition.Spacing
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.DualType
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.MonoType
import com.patrickhoette.pokemon.ui.R

@Composable
fun PokemonListEntry(
    onClick: () -> Unit,
    model: PokemonListEntryUIModel.Entry,
    modifier: Modifier = Modifier,
) {
    val typeColors = typeColors
    val type = model.type
    val imageBackgroundBrush = remember(type) {
        when (type) {
            is DualType -> Brush.verticalGradient(
                colors = listOf(
                    typeColors[type.primary].container,
                    typeColors[type.secondary].container,
                )
            )
            is MonoType -> SolidColor(typeColors[type.value].container)
        }
    }
    Column(
        modifier = modifier
            .shadow(
                elevation = AppCardTokens.Elevation,
                shape = AppCardTokens.Shape,
            )
            .background(colors.background.surface)
            .clickable(
                onClickLabel = stringResource(R.string.pokemon_list_accessibility_open_details, model.name),
                role = Role.Button,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StateAwareAsyncImage(
            url = model.imageUrl,
            contentDescription = stringResource(R.string.pokemon_list_accessibility_label_image, model.name),
            modifier = Modifier
                .background(imageBackgroundBrush)
                .fillMaxWidth()
                .aspectRatio(1F),
            contentScale = ContentScale.Fit,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.x1),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(
                    R.string.pokemon_list_dex_number,
                    model.id.toString().padStart(length = 4, padChar = '0')
                ),
                color = colors.background.onSurface.copy(0.5F),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                style = typography.mono.small,
            )

            Text(
                text = model.name.capitalize(Locale.current),
                color = colors.background.onSurface,
                textAlign = TextAlign.Center,
                style = typography.body.medium,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewPokemonListEntry() = AppTheme {
    val pokemon = remember { PokemonListEntryFactory.createList(30) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.background(colors.background.base),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(Spacing.x2),
        horizontalArrangement = Arrangement.spacedBy(Spacing.x2),
    ) {
        items(pokemon) {
            PokemonListEntry(
                onClick = {},
                model = it,
            )
        }
    }
}
