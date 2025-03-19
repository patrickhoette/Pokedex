package com.patrickhoette.pokedex.app.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.patrickhoette.core.ui.theme.AppTheme
import com.patrickhoette.core.ui.theme.Theme.colors
import com.patrickhoette.pokedex.app.utils.LocalFullscreenNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b),
            ),
            navigationBarStyle = SystemBarStyle.auto(lightScrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT),
        )
        setContent {
            AppTheme {
                val fullscreenNavController = rememberNavController()
                CompositionLocalProvider(
                    LocalFullscreenNavController provides fullscreenNavController,
                ) {
                    NavHost(
                        navController = fullscreenNavController,
                        startDestination = FullscreenNavGraph,
                        modifier = Modifier
                            .background(colors.background.base)
                            .fillMaxSize(),
                    ) {
                        fullscreenRoutes()
                    }
                }
            }
        }
    }
}
