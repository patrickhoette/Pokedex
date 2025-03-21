# Modules

## General Structure

This project has the following top-level modules (i.e., modules without submodules and whose parent is only the root
project):

- `:app`
- `:database`
- `:entity`
- `:test`
- `:test-android`

In addition to top-level modules, we have `:core:*` and feature-specific modules. These often follow a standardized
submodule structure (where applicable):

- `domain`: Contains use-cases and repository interfaces. This module defines the business logic layer.
- `data`: Implements repository interfaces and manages interactions between the source (e.g., network) and store (e.g., database).
- `presentation`: Handles presentation logic, such as ViewModels. It uses use cases to fetch data and transforms it into UI-ready models. It also processes events from the UI.
- `store`: Manages persistent storage, including the database, shared preferences, or filesystem.
- `source`: Manages data sourcing, typically through network calls.
- `ui`: Compose, this module deals with Compose.

## Noteworthy Modules

### :entity

This module defines the core data classes used across the application. By isolating them, feature modules can share data
models without introducing unnecessary dependencies. For example, a `Pokemon` might reference an `Ability`—but this
setup avoids creating a tight dependency between the two feature modules. Keeping this module lean and independent, also
improves compilation speed.

### :database

A module that solely deals with the database schema and queries for it. This has been split off into its own module for
the same reason as the entity module. The database will need to be to store and retrieve the data from all the entities
and therefore its better (and way more easy to understand) if it is 1 module. This also helps get rid of a very annoying
interaction between Koin and SQLDelight where Koin annotations fails to generate modules that contain any reference to
SQLDelight generated code if the SQLDelight plugin is applied to the module. For example now the `@Single` annotation on
`DatabasePokemonListStore` properly works but if you have the SQLDelight plugin applied to the `:pokemon:store` module
it would not properly generate and you would have to get rid of the annotation and write the binding manually.

### :app

This module is incharge of "gluing" the entire application together, therefore it knows about all other modules (well
except the test modules). For this reason the app module is incharge of putting together all the Koin modules into 1
graph and handles the navigation graphs, as well as some other small bits of generic code like the debug screens.

### :test

Utilities for writing unit tests, kotlin only.

### :test-android

Utilities for writing unit tests for android.

### :core

The core submodules are meant for generic code/utilities that will be used throughout the application. Some of the
submodules (like the `:core:source` module) are also in charge of setting up certain core libraries for their layer.

#### :core:ui

Shared utils and UI components. This module contains all of the shared atoms, molecules, and organisms as well as the
Compose theme.

#### :core:utils

This module is meant for utilities that will be used in multiple layers within the application. This prevents for
example `:pokemon:data` from needing to know about `:core:domain` just to get access to some utilities.

### :pokemon

The submodules are the ones needed for the pokemon list and pokemon details screens.

## Graph

![Graph showing the dependencies between project modules](/docs/resources/module-dependencies-graph.png)
