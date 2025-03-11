package com.patrickhoette.pokemon.store.list

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import java.util.Date
import com.patrickhoette.pokemon.store.database.pokemon.Pokemon as PokemonEntry

class PokemonListEntryMapper {

    fun mapToList(entries: List<PokemonEntry>, maxCount: Long, hasNext: Boolean) = PokemonList(
        maxCount = maxCount.toInt(),
        hasNext = hasNext,
        pokemon = entries.map { mapToModel(it) },
    )

    private fun mapToModel(entry: PokemonEntry) = Pokemon(
        id = entry.id.toInt(),
        name = entry.name,
    )

    fun mapToEntry(model: Pokemon) = PokemonEntry(
        id = model.id.toLong(),
        name = model.name,
        lastUpdate = Date().time,
    )
}
