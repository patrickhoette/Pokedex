package com.patrickhoette.pokemon.store.list

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import com.patrickhoette.pokedex.database.pokemon.PokemonListQueries
import com.patrickhoette.pokedex.database.pokemon.PokemonQueries
import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.data.generic.model.CacheStatus
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import com.patrickhoette.pokemon.data.list.PokemonListStore
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.days
import com.patrickhoette.pokedex.database.Pokemon_list_entry as PokemonListEntryEntity

@Single
class DatabasePokemonListStore(
    private val pokemonQueries: PokemonQueries,
    private val pokemonListQueries: PokemonListQueries,
    private val mapper: PokemonListEntryMapper,
    private val dispatchers: DispatcherProvider,
    private val clock: Clock,
) : PokemonListStore {

    private val _currentPage = MutableStateFlow(0)
    override val currentPage
        get() = _currentPage.value

    override suspend fun storePokemonList(list: PokemonList) {
        pokemonQueries.transaction {
            for (entry in list.pokemon) {
                pokemonQueries.upsertPokemon(
                    name = entry.name,
                    lastUpdate = clock.now().epochSeconds,
                    id = entry.id.toLong(),
                )
            }
        }
        pokemonListQueries.transaction {
            pokemonListQueries.upsert(list.maxCount.toLong())
        }
    }

    override suspend fun getPageStatus(page: Int, pageSize: Int): CacheStatus {
        val offset = (pageSize * (page)).toLong()
        val exists = pokemonQueries.isFullPageInDatabase(offset = offset, pageSize = pageSize.toLong())
            .awaitAsOneOrNull()
        return if (exists == true) {
            val oldestUpdate = pokemonQueries.getOldestUpdated(offset = offset, pageSize = pageSize.toLong())
                .awaitAsOneOrNull()
                ?.MIN
                ?: return Stale

            val now = clock.now()
            val updateTime = Instant.fromEpochSeconds(oldestUpdate)
            if (now - updateTime > CacheUpdateInterval) Stale else Available
        } else {
            Missing
        }
    }

    override fun observePokemonList(pages: Int, pageSize: Int): Flow<PokemonList?> = combine(
        observePokemonPage(pages = pages, pageSize = pageSize),
        observeMaxCount(),
    ) { entries, maxCount ->
        if (maxCount != null && entries.isNotEmpty()) {
            mapper.mapToList(entries = entries, maxCount = maxCount, hasNext = pages * pageSize < maxCount)
        } else {
            null
        }
    }

    private fun observePokemonPage(pages: Int, pageSize: Int): Flow<List<PokemonListEntryEntity>> {
        return pokemonQueries
            .selectAllInPages(pageCount = pages.toLong(), pageSize = pageSize.toLong())
            .asFlow()
            .mapToList(dispatchers.Default)
    }

    private fun observeMaxCount() = pokemonListQueries.select()
        .asFlow()
        .mapToOneOrNull(dispatchers.Default)
        .map { it?.maxPokemonCount }

    override fun observeCurrentPage(): Flow<Int> = _currentPage.asStateFlow()

    override suspend fun incrementCurrentPage() = _currentPage.update { it + 1 }

    companion object {

        private val CacheUpdateInterval = 1.days
    }
}
