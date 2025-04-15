package com.patrickhoette.pokemon.presentation.detail

import com.patrickhoette.core.presentation.mapper.NamedEnumUIMapper
import com.patrickhoette.core.presentation.mapper.UnitMapper
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.UnitSystem
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.Sprite
import com.patrickhoette.pokedex.entity.pokemon.SpriteGroup
import com.patrickhoette.pokemon.presentation.PokemonTypeUIMapper
import com.patrickhoette.pokemon.presentation.detail.model.*
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
class PokemonDetailUIMapper(
    private val namedEnumMapper: NamedEnumUIMapper,
    private val typeMapper: PokemonTypeUIMapper,
    private val unitMapper: UnitMapper,
) {

    fun mapToUIModel(model: Pokemon, unitSystem: UnitSystem): PokemonDetailUIModel? = model.detail?.let { detail ->
        PokemonDetailUIModel(
            id = model.id,
            name = StringUIModel.Raw(model.name),
            types = typeMapper.mapToUIModel(model.types),
            baseExperience = detail.baseExperience,
            height = unitMapper.mapToUIModel(detail.height, unitSystem),
            weight = unitMapper.mapToUIModel(detail.weight, unitSystem),
            abilities = detail.abilities.map(::mapAbility).toImmutableList(),
            moves = detail.moves.map(::mapMove).toImmutableList(),
            sprites = detail.sprites.map(::mapSpriteGroups).toImmutableList(),
            cry = detail.cry,
            species = StringUIModel.Raw(detail.species.name),
            hp = detail.stats.hp,
            attack = detail.stats.attack,
            defense = detail.stats.defense,
            specialAttack = detail.stats.specialAttack,
            specialDefense = detail.stats.specialDefense,
            speed = detail.stats.speed,
        )
    }

    private fun mapAbility(model: Ability) = PokemonAbilityUIModel(
        id = model.id,
        name = StringUIModel.Raw(model.name),
        isHidden = model.isHidden,
    )

    private fun mapMove(model: Move) = PokemonMoveUIModel(
        id = model.id,
        name = StringUIModel.Raw(model.name),
        method = namedEnumMapper.mapToUIModel(model.method),
        level = model.level,
    )

    private fun mapSpriteGroups(model: SpriteGroup) = SpriteGroupUIModel(
        name = StringUIModel.Raw(model.name),
        sprites = model.sprites.map(::mapSprite).toImmutableList(),
    )

    private fun mapSprite(model: Sprite) = SpriteUIModel(
        name = StringUIModel.Raw(model.name),
        url = model.url,
    )
}
