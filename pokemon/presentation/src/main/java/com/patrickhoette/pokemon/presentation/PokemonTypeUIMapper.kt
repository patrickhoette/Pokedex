package com.patrickhoette.pokemon.presentation

import com.patrickhoette.core.presentation.mapper.NamedEnumUIMapper
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Type.Unknown
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel
import org.koin.core.annotation.Factory

@Factory
class PokemonTypeUIMapper(
    private val mapper: NamedEnumUIMapper,
) {

    fun mapToUIModel(types: List<Type>) = when {
        types.isEmpty() -> PokemonTypeUIModel.MonoType(mapper.mapToUIModel(Unknown))
        types.size == 1 -> PokemonTypeUIModel.MonoType(mapper.mapToUIModel(types.single()))
        else -> PokemonTypeUIModel.DualType(
            primary = mapper.mapToUIModel(types[0]),
            secondary = mapper.mapToUIModel(types[1]),
        )
    }
}
