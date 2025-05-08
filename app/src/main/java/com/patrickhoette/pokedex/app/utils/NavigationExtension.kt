package com.patrickhoette.pokedex.app.utils

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.patrickhoette.pokedex.app.main.model.RootScreen
import kotlin.reflect.KClass

fun <T : Any> NavDestination.hasRouteInHierarchy(kClass: KClass<T>): Boolean = hierarchy.any { it.hasRoute(kClass) }

infix fun NavDestination.isSameRootAs(other: NavDestination): Boolean {
    val rootOne = findRootRoute()
    val rootTwo = other.findRootRoute()
    return rootOne == rootTwo
}

private fun NavDestination.findRootRoute(): String? {
    return hierarchy
        .find { dest ->
            RootScreen.entries.any { dest.hasRoute(it.destination::class) }
        }
        ?.route
}
