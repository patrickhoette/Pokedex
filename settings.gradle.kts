pluginManagement {
    repositories {
        mavenLocal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "Pokédex"

include(":app")
include(":entity")

// Core
include(":core")
include(":core:domain")
include(":core:ui")
include(":core:data")
include(":core:presentation")
include(":core:store")
include(":core:source")

// Feature: Pokemon
include(":pokemon")
include(":pokemon:domain")
include(":pokemon:ui")
include(":pokemon:data")
include(":pokemon:presentation")
include(":pokemon:store")
include(":pokemon:source")

include(":test")
