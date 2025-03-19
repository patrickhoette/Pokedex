package com.patrickhoette.pokemon.presentation.list

import com.patrickhoette.core.presentation.extension.toGenericError
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
class PokemonListUIMapper {

    fun createLoading() = PokemonListUIModel(
        hasNext = true,
        entries = List(NumberOfLoadingEntries) { Loading }.toImmutableList(),
    )

    fun mapToUIModel(model: PokemonList) = PokemonListUIModel(
        hasNext = model.hasNext,
        entries = buildList {
            addAll(model.pokemon.map(::mapToEntry))
            if (!model.hasNext) add(End)
            Napier.d("!!! hasNext=${model.hasNext}")
        }.toImmutableList(),
    )

    private fun mapToEntry(model: Pokemon) = Entry(
        id = model.id,
        name = model.name,
        imageUrl = buildString {
            append("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/")
            append(model.id)
            append(".png")
        },
        type = PokemonTypeUIModel.MonoType(Type.Unknown),
    )

    fun addLoadingEntries(list: PokemonListUIModel): PokemonListUIModel {
        // Only add it if its not end (just to be safe) or loading (don't need it twice)
        val lastPokemon = list.entries.lastOrNull()
        return if (lastPokemon is Entry || lastPokemon is Error) {
            list.copy(
                entries = (list.entries + List(NumberOfLoadingEntries) { Loading })
                    .filterNot { it is Error }
                    .toImmutableList()
            )
        } else {
            list
        }
    }

    fun addErrorEntry(list: PokemonListUIModel, error: Throwable): PokemonListUIModel {
        return list.copy(
            entries = (list.entries.filterNot { it is Loading } + Error(error.toGenericError())).toImmutableList()
        )
    }

    companion object {

        private const val NumberOfLoadingEntries = 10
    }
}
