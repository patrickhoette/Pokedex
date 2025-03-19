package com.patrickhoette.pokemon.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrickhoette.core.presentation.extension.launchCatching
import com.patrickhoette.core.presentation.mvvm.MutableEventFlow
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import com.patrickhoette.core.utils.coroutine.SerialJob
import com.patrickhoette.core.utils.extension.launchCatching
import com.patrickhoette.pokemon.domain.list.FetchNextPokemonPage
import com.patrickhoette.pokemon.domain.list.ObservePokemonList
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEvent
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEvent.OpenPokemonDetails
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PokemonListViewModel(
    private val observePokemonList: ObservePokemonList,
    private val fetchNextPokemonPage: FetchNextPokemonPage,
    private val mapper: PokemonListUIMapper,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private val _list = MutableStateFlow(mapper.createLoading())
    val list by lazy {
        startObservingPokemonList()
        _list.asStateFlow()
    }

    private val _events = MutableEventFlow<PokemonListEvent>()
    val events = _events.asEventFlow()

    private val observingSerialJob = SerialJob()

    private fun startObservingPokemonList() = observingSerialJob.launchCatching(
        context = dispatchers.IO,
        onError = ::handleObservingError
    ) {
        observePokemonList()
            .filterNotNull()
            .mapLatest(mapper::mapToUIModel)
            .collectLatest {
                Napier.d("!!! entries=${it.entries.size}")
                _list.value = it
            }
    }

    private fun handleObservingError(error: Throwable) {
        Napier.e("Failed to observe pokemon list", error)
        _list.update { mapper.addErrorEntry(it, error) }
    }

    fun onGetMorePokemon() = viewModelScope.launchCatching(
        context = dispatchers.IO,
        onError = ::handleFetchError,
    ) {
        val currentList = _list.value
        val isLoading = currentList.entries
            .asReversed()
            .any { it is PokemonListEntryUIModel.Loading }
        if (currentList.hasNext && !isLoading) {
            _list.update(mapper::addLoadingEntries)
            fetchNextPokemonPage()
        }
    }

    private fun handleFetchError(error: Throwable) {
        Napier.e("Failed to fetch more pokemon", error)
        // Remove loading items if they exist
        _list.update { mapper.addErrorEntry(it, error) }
    }

    fun onPokemon(entry: PokemonListEntryUIModel.Entry) = _events.setEvent(OpenPokemonDetails(entry.id))
}
