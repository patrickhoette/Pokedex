package com.patrickhoette.test.factory.pokemon

import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.pokemon.PokemonDetails
import com.patrickhoette.pokedex.entity.pokemon.PokemonStats
import com.patrickhoette.pokedex.entity.pokemon.Sprite
import com.patrickhoette.pokedex.entity.species.Species
import com.patrickhoette.test.factory.FactoryConstants.DefaultCollectionSize
import com.patrickhoette.test.factory.FactoryConstants.moduloGet
import com.patrickhoette.test.factory.ability.AbilityFactory
import com.patrickhoette.test.factory.move.MoveFactory
import com.patrickhoette.test.factory.species.SpeciesFactory

object PokemonDetailsFactory {

    fun createMultiple(
        amount: Int = DefaultCollectionSize,
        baseExperiences: List<Int> = emptyList(),
        heights: List<Length> = emptyList(),
        weights: List<Weight> = emptyList(),
        abilities: List<List<Ability>> = emptyList(),
        moves: List<List<Move>> = emptyList(),
        sprites: List<List<Sprite>> = emptyList(),
        cries: List<String> = emptyList(),
        species: List<Species> = emptyList(),
        stats: List<PokemonStats> = emptyList(),
    ) = List(amount) {
        create(
            baseExperience = baseExperiences.moduloGet(it),
            height = heights.moduloGet(it),
            weight = weights.moduloGet(it),
            abilities = abilities.moduloGet(it),
            moves = moves.moduloGet(it),
            sprites = sprites.moduloGet(it),
            cry = cries.moduloGet(it),
            species = species.moduloGet(it),
            stats = stats.moduloGet(it),
        )
    }

    fun create(
        baseExperience: Int? = null,
        height: Length? = null,
        weight: Weight? = null,
        abilities: List<Ability>? = null,
        moves: List<Move>? = null,
        sprites: List<Sprite>? = null,
        cry: String? = null,
        species: Species? = null,
        stats: PokemonStats? = null,
    ) = PokemonDetails(
        baseExperience = baseExperience ?: 100,
        height = height ?: Length.Meters(1.76),
        weight = weight ?: Weight.Kilograms(52.5),
        abilities = abilities ?: AbilityFactory.createMultiple(),
        moves = moves ?: MoveFactory.createMultiple(),
        sprites = sprites ?: SpriteFactory.createMultiple(),
        cry = cry ?: "https://www.google.com/",
        species = species ?: SpeciesFactory.create(),
        stats = stats ?: PokemonStatsFactory.create(),
    )
}
