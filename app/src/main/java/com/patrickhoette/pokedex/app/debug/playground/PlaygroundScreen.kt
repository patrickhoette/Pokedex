package com.patrickhoette.pokedex.app.debug.playground

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.patrickhoette.core.presentation.model.GenericError
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.pokemon.ui.list.PokemonListEntryFactory
import com.patrickhoette.pokemon.ui.list.PokemonListScreen
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PlaygroundScreen(modifier: Modifier = Modifier) {
    val list = remember {
        PokemonListUIModel(
            hasNext = true,
            entries = buildList {
                addAll(PokemonListEntryFactory.createList(30))
                repeat(10) { add(Loading) }
                add(Error(GenericError.Network))
                add(Error(GenericError.Server))
                add(Error(GenericError.Unknown))
                add(End)
            }.toImmutableList()
        )
    }

    PokemonListScreen(
        onEntry = {},
        onLoadMore = {},
        list = list,
        modifier = modifier.fillMaxSize(),
    )
}
