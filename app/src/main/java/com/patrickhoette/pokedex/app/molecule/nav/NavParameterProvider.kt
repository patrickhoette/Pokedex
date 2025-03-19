package com.patrickhoette.pokedex.app.molecule.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.patrickhoette.pokedex.app.molecule.nav.SingleNavParameterProvider.Companion.NavIcons

class SingleNavParameterProvider : PreviewParameterProvider<List<NavEntry>> {

    override val values: Sequence<List<NavEntry>>
        get() = sequenceOf(NavIcons)

    companion object {

        val NavIcons = listOf(
            NavEntry(
                icon = Icons.Filled.Build,
                name = "Debug",
                isSelected = true,
                onClick = {},
            ),
            NavEntry(
                icon = Icons.Filled.Email,
                name = "Email",
                isSelected = false,
                onClick = {},
            ),
            NavEntry(
                icon = Icons.Filled.Home,
                name = "Home",
                isSelected = false,
                onClick = {},
            ),
            NavEntry(
                icon = Icons.Filled.FavoriteBorder,
                name = "Pokémon",
                isSelected = false,
                onClick = {},
            ),
            NavEntry(
                icon = Icons.Filled.Settings,
                name = "Settings",
                isSelected = false,
                onClick = {},
            ),
        )
    }
}

class MultiNavParameterProvider : PreviewParameterProvider<List<NavEntry>> {

    override val values: Sequence<List<NavEntry>>
        get() = sequenceOf(
            emptyList(),
            NavIcons.take(1),
            NavIcons.take(2),
            NavIcons.take(3),
            NavIcons.take(4),
            NavIcons.take(5),
        )
}
