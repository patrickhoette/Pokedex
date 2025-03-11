package com.patrickhoette.pokemon.presentation.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
class PokemonListUIMapper {

    fun mapToUIModel(model: PokemonList) = PokemonListUIModel(
        hasNext = model.hasNext,
        pokemon = buildList {
            addAll(model.pokemon.map(::mapToEntry))
            if (!model.hasNext) {
                add(PokemonListEntryUIModel.End)
            }
        }.toImmutableList(),
    )

    private fun mapToEntry(model: Pokemon) = PokemonListEntryUIModel.Entry(
        id = model.id,
        name = model.name,
        imageUrl = buildString {
            append("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/")
            append(model.id)
            append(".png")
        }
    )
}
