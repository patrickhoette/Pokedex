package com.patrickhoette.pokemon.data.detail

import com.patrickhoette.pokedex.entity.pokemon.Pokemon
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.Available
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.Stale
import com.patrickhoette.pokemon.domain.detail.PokemonDetailRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory

@Factory
class PersistentPokemonDetailRepository(
    private val source: PokemonDetailSource,
    private val store: PokemonDetailStore,
) : PokemonDetailRepository {

    override fun observePokemon(id: Int): Flow<Pokemon?> = store.observePokemon(id)
        .onStart { checkCache(id) }

    private suspend fun checkCache(id: Int) {
        val status = store.getPokemonStatus(id)
        if (status != Available) {
            val newModel = try {
                source.fetchPokemon(id)
            } catch (error: Throwable) {
                if (status == Stale) {
                    Napier.e("Failed to fetch pokemon with id=$id")
                    null
                } else {
                    throw error
                }
            }
            newModel?.let { store.storePokemon(it) }
        }
    }
}
