package com.patrickhoette.pokemon.presentation.list

import androidx.lifecycle.ViewModel
import com.patrickhoette.core.presentation.extension.launchCatching
import com.patrickhoette.core.presentation.extension.toGenericError
import com.patrickhoette.core.presentation.model.*
import com.patrickhoette.core.presentation.model.TypedUIState.Loading
import com.patrickhoette.core.presentation.mvvm.MutableEventFlow
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import com.patrickhoette.core.utils.coroutine.SerialJob
import com.patrickhoette.pokemon.domain.list.FetchNextPokemonPage
import com.patrickhoette.pokemon.domain.list.ObservePokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEvent
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEvent.ShowError
import com.patrickhoette.pokemon.presentation.list.model.PokemonListUIModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PokemonListViewModel(
    private val observePokemonList: ObservePokemonList,
    private val fetchNextPokemonPage: FetchNextPokemonPage,
    private val mapper: PokemonListUIMapper,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private val _state = MutableStateFlow<GenericUIState<PokemonListUIModel>>(Loading)
    val state by lazy {
        startObservingPokemonList()
        _state.asStateFlow()
    }

    private val _events = MutableEventFlow<PokemonListEvent>()
    val events = _events.asEventFlow()

    private val observingSerialJob = SerialJob()
    private val fetchSerialJob = SerialJob()

    private fun startObservingPokemonList() = observingSerialJob.launchCatching(
        context = dispatchers.IO,
        onError = ::handleObservingError
    ) {
        _state.setLoading()
        observePokemonList()
            .filterNotNull()
            .mapLatest(mapper::mapToUIModel)
            .collectLatest(_state::setNormal)
    }

    private fun handleObservingError(error: Throwable) {
        Napier.e("Failed to observe pokemon list", error)
        _state.setError(error)
    }

    fun onGetMorePokemon() = fetchSerialJob.launchCatching(
        context = dispatchers.IO,
        onError = ::handleFetchError,
    ) {
        if (_state.value.normalDataOrNull()?.hasNext == true) {
            addLoadingEntries()
            fetchNextPokemonPage()
        }
    }

    private fun addLoadingEntries() {
        _state.updateIfNormal {
            // Only add it if its not end (just to be safe) or loading (don't need it twice)
            if (it.pokemon.last() is PokemonListEntryUIModel.Entry) {
                it.copy(
                    pokemon = (it.pokemon + List(NumberOfLoadingEntries) { PokemonListEntryUIModel.Loading })
                        .toImmutableList()
                )
            } else {
                it
            }
        }
    }

    private fun handleFetchError(error: Throwable) {
        Napier.e("Failed to fetch more pokemon", error)
        // Remove loading items if they exist
        _state.updateIfNormal { list ->
            list.copy(
                pokemon = list.pokemon
                    .filterNot { it is PokemonListEntryUIModel.Loading }
                    .toImmutableList()
            )
        }
        _events.setEvent(ShowError(error.toGenericError()))
    }

    companion object {

        private const val NumberOfLoadingEntries = 5
    }
}
