package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonDetail
import com.patrickhoette.pokedex.entity.pokemon.PokemonStats
import com.patrickhoette.pokedex.entity.species.Species
import com.patrickhoette.pokemon.source.detail.response.PokemonAbilityResponse
import com.patrickhoette.pokemon.source.detail.response.PokemonDetailResponse
import com.patrickhoette.pokemon.source.detail.response.PokemonMoveResponse
import org.koin.core.annotation.Factory

@Factory
class PokemonDetailResponseMapper(
    private val typeMapper: PokemonTypeResponseMapper,
    private val moveTypeMapper: PokemonMoveTypeResponseMapper,
    private val spriteMapper: PokemonSpriteResponseMapper,
) {

    fun mapToPokemon(response: PokemonDetailResponse) = Pokemon(
        id = response.id,
        name = response.name,
        types = mapTypes(response),
        detail = mapDetail(response),
    )

    private fun mapTypes(response: PokemonDetailResponse) = response.types
        .asSequence()
        .sortedBy { it.slot }
        .map { it.type.name }
        .map(typeMapper::mapType)
        .toList()

    private fun mapDetail(response: PokemonDetailResponse) = PokemonDetail(
        baseExperience = response.baseExperience,
        height = Length.Decimeters(response.height),
        weight = Weight.Hectograms(response.weight),
        abilities = mapAbilities(response),
        moves = mapMoves(response),
        sprites = spriteMapper.mapSpriteGroups(response),
        cry = response.cries.latest,
        species = mapSpecies(response),
        stats = mapStats(response),
    )

    private fun mapAbilities(response: PokemonDetailResponse) = response.abilities
        .asSequence()
        .sortedBy { it.slot }
        .map(::mapAbility)
        .toList()

    private fun mapAbility(response: PokemonAbilityResponse) = Ability(
        id = response.ability.parseId(),
        name = response.ability.name,
        isHidden = response.isHidden,
    )

    private fun mapMoves(response: PokemonDetailResponse) = response.moves
        .asSequence()
        .sortedBy { it.versionGroupDetails.lastOrNull()?.levelLearnedAt ?: 0 }
        .map(::mapMove)
        .toList()

    private fun mapMove(response: PokemonMoveResponse) = Move(
        id = response.move.parseId(),
        name = response.move.name,
        method = response.versionGroupDetails.lastOrNull()
            ?.moveLearnMethod
            ?.name
            ?.let(moveTypeMapper::mapMoveMethod)
            ?: MoveMethod.Unknown,
        level = response.versionGroupDetails.lastOrNull()?.levelLearnedAt ?: 0,
    )

    private fun mapSpecies(response: PokemonDetailResponse) = Species(
        id = response.species.parseId(),
        name = response.name,
    )

    private fun mapStats(response: PokemonDetailResponse): PokemonStats {
        val mappedStats = response.stats.associateBy { it.stat.parseId() }
        return PokemonStats(
            hp = mappedStats.getValue(IdStatHp).baseStat,
            attack = mappedStats.getValue(IdStatAttack).baseStat,
            defense = mappedStats.getValue(IdStatDefense).baseStat,
            specialAttack = mappedStats.getValue(IdStatSpecialAttack).baseStat,
            specialDefense = mappedStats.getValue(IdStatSpecialDefense).baseStat,
            speed = mappedStats.getValue(IdStatSpeed).baseStat,
        )
    }

    companion object {

        private const val IdStatHp = 1
        private const val IdStatAttack = 2
        private const val IdStatDefense = 3
        private const val IdStatSpecialAttack = 4
        private const val IdStatSpecialDefense = 5
        private const val IdStatSpeed = 6
    }
}
