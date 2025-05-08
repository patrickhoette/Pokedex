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
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultEnterAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultExit
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultExitAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultPopEnterAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultPopExitAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.tween
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.pokedex.app.utils.isSameRootAs

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) = NavHost(
    navController = navController,
    startDestination = MainNavGraph,
    modifier = modifier.background(colors.background.base),
    enterTransition = {
        if (initialState.destination isSameRootAs targetState.destination) {
            DefaultEnterAnimation
        } else {
            fadeIn(tween())
        }
    },
    exitTransition = {
        if (initialState.destination isSameRootAs targetState.destination) {
            DefaultExitAnimation
        } else {
            ExitTransition.None
        }
    },
    popEnterTransition = {
        if (initialState.destination isSameRootAs targetState.destination) {
            DefaultPopEnterAnimation
        } else {
            EnterTransition.None
        }
    },
    popExitTransition = {
        if (initialState.destination isSameRootAs targetState.destination) {
            DefaultPopExitAnimation
        } else {
            fadeOut(tween(DefaultExit))
        }
    },
) {
    mainRoutes(navController)
}
