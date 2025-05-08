package com.patrickhoette.pokedex.app.debug

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.patrickhoette.core.ui.showcase.ShowcaseScreen
import com.patrickhoette.core.ui.showcase.theme.ColorShowcaseScreen
import com.patrickhoette.core.ui.showcase.theme.TypeColorShowcaseScreen
import com.patrickhoette.core.ui.showcase.theme.TypographyShowcaseScreen
import kotlinx.serialization.Serializable

@Serializable
data object ShowcaseNavGraph {

    @Serializable
    data object Landing

    @Serializable
    data object Colors

    @Serializable
    data object TypeColors

    @Serializable
    data object Typography
}

@Stable
fun NavGraphBuilder.showcaseRoutes(navController: NavController) = navigation<ShowcaseNavGraph>(
    startDestination = ShowcaseNavGraph.Landing,
) {
    composable<ShowcaseNavGraph.Landing> {
        ShowcaseScreen(
            onOpenColors = { navController.navigate(ShowcaseNavGraph.Colors) },
            onOpenTypeColors = { navController.navigate(ShowcaseNavGraph.TypeColors) },
            onOpenTypography = { navController.navigate(ShowcaseNavGraph.Typography) },
            modifier = Modifier.fillMaxSize(),
        )
    }

    composable<ShowcaseNavGraph.Colors> { ColorShowcaseScreen(Modifier.fillMaxSize()) }

    composable<ShowcaseNavGraph.TypeColors> { TypeColorShowcaseScreen(Modifier.fillMaxSize()) }

    composable<ShowcaseNavGraph.Typography> { TypographyShowcaseScreen(Modifier.fillMaxSize()) }
}
