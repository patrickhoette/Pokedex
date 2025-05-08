package com.patrickhoette.pokedex.app.main.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.patrickhoette.core.ui.icon.AppIcons
import com.patrickhoette.core.ui.icon.Pokeball
import com.patrickhoette.pokedex.app.R
import com.patrickhoette.pokedex.app.debug.DebugNavGraph
import com.patrickhoette.pokedex.app.pokemon.PokemonNavGraph

@Immutable
enum class RootScreen(
    val icon: ImageVector,
    @StringRes val label: Int,
    val destination: Any,
) {

    Pokemon(
        icon = AppIcons.Pokeball,
        label = R.string.main_nav_item_pokemon,
        destination = PokemonNavGraph,
    ),
    Debug(
        icon = Icons.Filled.Build,
        label = R.string.main_nav_item_debug,
        destination = DebugNavGraph,
    );
}
