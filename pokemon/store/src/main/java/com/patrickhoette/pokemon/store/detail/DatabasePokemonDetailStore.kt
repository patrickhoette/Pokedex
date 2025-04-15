package com.patrickhoette.pokemon.store.detail

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.patrickhoette.core.store.extension.toLong
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import com.patrickhoette.pokedex.database.ability.AbilityQueries
import com.patrickhoette.pokedex.database.move.MoveQueries
import com.patrickhoette.pokedex.database.pokemon.PokemonQueries
import com.patrickhoette.pokedex.database.species.SpeciesQueries
import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokedex.entity.pokemon.PokemonDetail
import com.patrickhoette.pokemon.data.detail.PokemonDetailStore
import com.patrickhoette.pokemon.data.generic.model.CacheStatus
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory
import kotlin.time.Duration.Companion.hours

@Factory
class DatabasePokemonDetailStore(
    private val pokemonQueries: PokemonQueries,
    private val abilityQueries: AbilityQueries,
    private val moveQueries: MoveQueries,
    private val speciesQueries: SpeciesQueries,
    private val mapper: PokemonDetailEntryMapper,
    private val dispatchers: DispatcherProvider,
    private val clock: Clock,
) : PokemonDetailStore {

    override suspend fun storePokemon(pokemon: Pokemon) {
        pokemon.detail?.let { detail ->
            abilityQueries.transaction { storeAbilities(detail) }
            moveQueries.transaction { storeMoves(detail) }
            speciesQueries.transaction { storeSpecies(detail) }
        }

        pokemonQueries.transaction {
            storePokemonEntry(pokemon)

            pokemon.detail?.let { storePokemonDetail(pokemon = pokemon, detail = it) }
        }
    }

    private suspend fun storeAbilities(detail: PokemonDetail) {
        for (ability in detail.abilities) {
            abilityQueries.upsert(
                name = ability.name,
                id = ability.id.toLong(),
            )
        }
    }

    private suspend fun storeMoves(detail: PokemonDetail) {
        for (move in detail.moves) {
            moveQueries.upsert(
                name = move.name,
                id = move.id.toLong(),
            )
        }
    }

    private suspend fun storeSpecies(detail: PokemonDetail) {
        speciesQueries.upsert(
            name = detail.species.name,
            id = detail.species.id.toLong(),
        )
    }

    private suspend fun storePokemonEntry(pokemon: Pokemon) {
        pokemonQueries.upsertPokemon(
            name = pokemon.name,
            lastUpdate = clock.now().epochSeconds,
            id = pokemon.id.toLong(),
        )
    }

    private suspend fun storePokemonDetail(pokemon: Pokemon, detail: PokemonDetail) {
        pokemonQueries.upsertPokemonDetails(
            baseExperience = detail.baseExperience.toLong(),
            heightCm = detail.height.centimeters,
            weightG = detail.weight.grams,
            cry = detail.cry,
            speciesId = detail.species.id.toLong(),
            hp = detail.stats.hp.toLong(),
            attack = detail.stats.attack.toLong(),
            defense = detail.stats.defense.toLong(),
            specialAttack = detail.stats.specialAttack.toLong(),
            specialDefense = detail.stats.specialDefense.toLong(),
            speed = detail.stats.speed.toLong(),
            primaryType = pokemon.types[0].name,
            secondaryType = pokemon.types.getOrNull(1)?.name,
            pokemonId = pokemon.id.toLong(),
            lastUpdate = clock.now().epochSeconds,
        )

        storePokemonAbilities(pokemon = pokemon, detail = detail)
        storePokemonMoves(pokemon = pokemon, detail = detail)
        storePokemonSprites(pokemon = pokemon, detail = detail)
    }

    private suspend fun storePokemonAbilities(pokemon: Pokemon, detail: PokemonDetail) {
        for (ability in detail.abilities) {
            pokemonQueries.upsertPokemonAbility(
                abilityId = ability.id.toLong(),
                isHidden = ability.isHidden.toLong(),
                pokemonId = pokemon.id.toLong(),
            )
        }
    }

    private suspend fun storePokemonMoves(pokemon: Pokemon, detail: PokemonDetail) {
        for (move in detail.moves) {
            pokemonQueries.upsertPokemonMove(
                moveId = move.id.toLong(),
                method = move.method.name,
                level = move.level.toLong(),
                pokemonId = pokemon.id.toLong(),
            )
        }
    }

    private suspend fun storePokemonSprites(pokemon: Pokemon, detail: PokemonDetail) {
        for (group in detail.sprites) {
            for (sprite in group.sprites) {
                pokemonQueries.upsertSprites(
                    name = sprite.name,
                    url = sprite.url,
                    groupName = group.name,
                    pokemonId = pokemon.id.toLong(),
                )
            }
        }
    }

    override suspend fun getPokemonStatus(id: Int): CacheStatus {
        val exists = pokemonQueries.isDetailInDatabase(id.toLong()).awaitAsOneOrNull()

        return if (exists == true) {
            val updated = pokemonQueries.getLastDetailUpdated(id.toLong()).awaitAsOneOrNull() ?: return Stale

            val now = clock.now()
            val updateTime = Instant.fromEpochSeconds(updated)
            if (now - updateTime > CacheUpdateInterval) Stale else Available
        } else {
            Missing
        }
    }

    override fun observePokemon(id: Int): Flow<Pokemon?> = combine(
        observeFull(id),
        observePokemonAbilities(id),
        observePokemonMoves(id),
        observePokemonSprites(id),
    ) { full, abilities, moves, sprites ->
        if (full != null) {
            mapper.mapToModel(full, abilities, moves, sprites)
        } else {
            null
        }
    }

    private fun observeFull(id: Int) = pokemonQueries.selectFullById(id.toLong())
        .asFlow()
        .mapToOneOrNull(dispatchers.IO)

    private fun observePokemonAbilities(id: Int) = pokemonQueries.selectAbilitiesById(id.toLong())
        .asFlow()
        .mapToList(dispatchers.IO)

    private fun observePokemonMoves(id: Int) = pokemonQueries.selectMovesById(id.toLong())
        .asFlow()
        .mapToList(dispatchers.IO)

    private fun observePokemonSprites(id: Int) = pokemonQueries.selectSpritesById(id.toLong())
        .asFlow()
        .mapToList(dispatchers.IO)

    companion object {

        private val CacheUpdateInterval = 6.hours
    }
}
