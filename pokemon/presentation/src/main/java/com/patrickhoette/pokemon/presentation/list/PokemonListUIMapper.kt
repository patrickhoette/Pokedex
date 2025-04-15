package com.patrickhoette.pokemon.presentation.list

import com.patrickhoette.core.presentation.extension.toGenericError
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.presentation.PokemonTypeUIMapper
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
class PokemonListUIMapper(
    private val typeUIMapper: PokemonTypeUIMapper,
) {

    fun createLoading() = PokemonListUIModel(
        hasNext = true,
        entries = List(NumberOfLoadingEntries) { Loading }.toImmutableList(),
    )

    fun mapToUIModel(model: PokemonList) = PokemonListUIModel(
        hasNext = model.hasNext,
        entries = buildList {
            addAll(model.pokemon.map(::mapToEntry))
            if (!model.hasNext) add(End)
        }.toImmutableList(),
    )

    private fun mapToEntry(model: Pokemon) = Entry(
        id = model.id,
        name = StringUIModel.Raw(model.name),
        imageUrl = buildString {
            append("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/")
            append(model.id)
            append(".png")
        },
        type = typeUIMapper.mapToUIModel(model.types),
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
