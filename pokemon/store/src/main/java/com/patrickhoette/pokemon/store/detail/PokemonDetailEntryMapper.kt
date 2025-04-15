package com.patrickhoette.pokemon.store.detail

import com.patrickhoette.core.store.extension.toBoolean
import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.pokedex.entity.pokemon.*
import com.patrickhoette.pokedex.entity.species.Species
import org.koin.core.annotation.Factory
import com.patrickhoette.pokedex.database.Pokemon_full_view as PokemonFullEntry
import com.patrickhoette.pokedex.database.Sprite as SpriteEntry
import com.patrickhoette.pokedex.database.pokemon.SelectAbilitiesById as AbilityEntry
import com.patrickhoette.pokedex.database.pokemon.SelectMovesById as MoveEntry

@Factory
class PokemonDetailEntryMapper {

    fun mapToModel(
        full: PokemonFullEntry,
        abilities: List<AbilityEntry>,
        moves: List<MoveEntry>,
        sprites: List<SpriteEntry>,
    ) = Pokemon(
        id = full.id.toInt(),
        name = full.name,
        types = listOfNotNull(
            full.primaryType?.let(Type::valueOf),
            full.secondaryType?.let(Type::valueOf),
        ),
        detail = mapPokemonDetails(
            full = full,
            abilities = abilities,
            sprites = sprites,
            moves = moves,
        ),
    )

    private fun mapPokemonDetails(
        full: PokemonFullEntry,
        abilities: List<AbilityEntry>,
        moves: List<MoveEntry>,
        sprites: List<SpriteEntry>,
    ): PokemonDetail? {
        val baseExperience = full.baseExperience ?: return null
        val height = full.heightCm?.let(Length::Centimeters) ?: return null
        val weight = full.weightG?.let(Weight::Grams) ?: return null
        val speciesId = full.speciesId ?: return null
        val speciesName = full.speciesName ?: return null
        val hp = full.hp ?: return null
        val attack = full.attack ?: return null
        val defense = full.defense ?: return null
        val specialAttack = full.specialAttack ?: return null
        val specialDefense = full.specialDefense ?: return null
        val speed = full.speed ?: return null

        return PokemonDetail(
            baseExperience = baseExperience.toInt(),
            height = height,
            weight = weight,
            abilities = mapAbilities(abilities),
            moves = mapMoves(moves),
            sprites = mapSpriteGroups(sprites),
            cry = full.cry,
            species = Species(
                id = speciesId.toInt(),
                name = speciesName,
            ),
            stats = PokemonStats(
                hp = hp.toInt(),
                attack = attack.toInt(),
                defense = defense.toInt(),
                specialAttack = specialAttack.toInt(),
                specialDefense = specialDefense.toInt(),
                speed = speed.toInt(),
            ),
        )
    }

    private fun mapAbilities(abilities: List<AbilityEntry>) = abilities.map {
        Ability(
            id = it.id.toInt(),
            name = it.name,
            isHidden = it.isHidden.toBoolean(),
        )
    }

    private fun mapMoves(moves: List<MoveEntry>) = moves.map {
        Move(
            id = it.id.toInt(),
            name = it.name,
            method = MoveMethod.valueOf(it.method),
            level = it.level.toInt(),
        )
    }

    private fun mapSpriteGroups(groups: List<SpriteEntry>) = groups
        .groupBy { it.groupName }
        .map { (name, sprites) ->
            SpriteGroup(
                name = name,
                sprites = mapSprites(sprites),
            )
        }

    private fun mapSprites(sprites: List<SpriteEntry>) = sprites.map {
        Sprite(
            name = it.name,
            url = it.url,
        )
    }
}
