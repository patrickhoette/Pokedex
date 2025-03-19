package com.patrickhoette.pokedex.app.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalFullscreenNavController = staticCompositionLocalOf<NavHostController> {
    throw IllegalStateException("Fullscreen nav controller has not been set!")
}
