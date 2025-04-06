package com.patrickhoette.pokemon.store.list

import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import org.koin.core.annotation.Factory
import com.patrickhoette.pokedex.database.Pokemon_list_entry as PokemonListEntryEntity

@Factory
class PokemonListEntryMapper {

    fun mapToList(entries: List<PokemonListEntryEntity>, maxCount: Long, hasNext: Boolean) = PokemonList(
        maxCount = maxCount.toInt(),
        hasNext = hasNext,
        pokemon = entries.map(::mapToModel),
    )

    private fun mapToModel(entry: PokemonListEntryEntity) = Pokemon(
        id = entry.id.toInt(),
        name = entry.name,
        types = listOfNotNull(
            entry.primaryType?.let(Type::valueOf),
            entry.secondaryType?.let(Type::valueOf),
        ),
        detail = null,
    )
}
