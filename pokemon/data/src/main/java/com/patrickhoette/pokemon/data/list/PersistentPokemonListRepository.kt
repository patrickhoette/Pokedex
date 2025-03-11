@file:Suppress("ConstPropertyName")

package com.patrickhoette.pokemon.data.list

import com.patrickhoette.pokedex.entity.pokemon.PokemonList
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import com.patrickhoette.pokemon.domain.list.PokemonListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory

@Factory
class PersistentPokemonListRepository(
    private val source: PokemonListSource,
    private val store: PokemonListStore,
) : PokemonListRepository {

    override fun observePokemonList(): Flow<PokemonList?> = store.observeCurrentPage()
        .flatMapLatest { store.observePokemonList(pages = it + 1, pageSize = PageSize) }
        .onStart { checkIfHasAllCurrentPages() }

    override suspend fun fetchNextPokemonPage() {
        val nextPage = store.currentPage + 1
        val status = store.getPageStatus(page = nextPage, pageSize = PageSize)
        when (status) {
            Available -> store.incrementCurrentPage()
            Stale -> {
                store.incrementCurrentPage()
                fetchPage(offset = nextPage * PageSize, size = PageSize)
            }
            Missing -> {
                fetchPage(offset = nextPage * PageSize, size = PageSize)
                store.incrementCurrentPage()
            }
        }
    }

    private suspend fun checkIfHasAllCurrentPages() {
        val rangesToFetch = getStaleOrMissingRanges()

        for (range in rangesToFetch) {
            val offset = range.first - 1
            val size = range.last - range.first + 1
            fetchPage(offset = offset, size = size)
        }
    }

    private suspend fun fetchPage(offset: Int, size: Int) {
        val list = source.fetchPokemonPage(offset, size)
        store.storePokemonList(list)
    }

    private suspend fun getStaleOrMissingRanges(): List<IntRange> {
        val pagesToFetch = mutableListOf<IntRange>()
        var currentRange: IntRange? = null
        for (page in 0..store.currentPage) {
            val status = store.getPageStatus(page = page + 1, pageSize = PageSize)

            if (status == Available) continue

            val pageStart = PageSize * page + 1
            val nextStart = pageStart + PageSize
            val newRange = pageStart until nextStart

            when {
                currentRange == null -> currentRange = newRange
                currentRange.last + 1 == newRange.first -> currentRange = currentRange.first..newRange.last
                else -> {
                    pagesToFetch += currentRange
                    currentRange = newRange
                }
            }
        }

        currentRange?.let { pagesToFetch += it }

        return pagesToFetch
    }

    companion object {

        private const val PageSize = 20
    }
}
