package com.patrickhoette.pokedex.app.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.patrickhoette.core.ui.animation.AnimationDefaults
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultExit
import com.patrickhoette.core.ui.theme.Theme.colors

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) = NavHost(
    navController = navController,
    startDestination = MainNavGraph,
    modifier = modifier.background(colors.background.base),
    enterTransition = { fadeIn(AnimationDefaults.tween()) },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { fadeOut(AnimationDefaults.tween(DefaultExit)) },
) {
    mainRoutes()
}
