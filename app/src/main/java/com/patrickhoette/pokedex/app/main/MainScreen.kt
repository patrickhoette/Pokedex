package com.patrickhoette.pokedex.app.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.patrickhoette.core.ui.icon.AppIcons
import com.patrickhoette.core.ui.icon.Pokeball
import com.patrickhoette.pokedex.app.R
import com.patrickhoette.pokedex.app.main.MainScreenTokens.RootScreens
import com.patrickhoette.pokedex.app.molecule.nav.NavEntry
import com.patrickhoette.pokedex.app.molecule.nav.NavLayout
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutType
import com.patrickhoette.pokedex.app.molecule.nav.NavLayoutType.*
import kotlinx.collections.immutable.toImmutableList

private object MainScreenTokens {

    val RootScreens = buildList {
        add(
            RootScreen(
                icon = AppIcons.Pokeball,
                label = R.string.main_nav_item_pokemon,
                destination = MainNavGraph.Pokemon,
            )
        )
        // if (BuildConfig.DEBUG) {
        add(
            RootScreen(
                icon = Icons.Filled.Build,
                label = R.string.main_nav_item_debug,
                destination = MainNavGraph.Debug,
            )
        )
        // }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()

    NavLayout(
        type = calculateType(),
        entries = RootScreens
            .map {
                NavEntry(
                    icon = it.icon,
                    name = stringResource(it.label),
                    isSelected = currentDestination?.destination?.hasRoute(it.destination::class) == true,
                    onClick = {
                        navController.navigate(it.destination) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                )
            }
            .toImmutableList(),
        modifier = modifier,
    ) {
        MainNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun calculateType(): NavLayoutType {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val widthSizeClass = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass
    val heightSizeClass = windowAdaptiveInfo.windowSizeClass.windowHeightSizeClass
    return when {
        widthSizeClass == WindowWidthSizeClass.COMPACT -> Bar
        widthSizeClass == WindowWidthSizeClass.MEDIUM -> Rail
        heightSizeClass == WindowHeightSizeClass.COMPACT -> Rail
        else -> Drawer
    }
}

@Immutable
private data class RootScreen(
    val icon: ImageVector,
    @StringRes val label: Int,
    val destination: Any,
)
