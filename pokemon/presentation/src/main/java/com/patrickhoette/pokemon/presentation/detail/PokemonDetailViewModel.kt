package com.patrickhoette.pokemon.presentation.detail

import androidx.lifecycle.ViewModel
import com.patrickhoette.core.domain.config.ObserveUserSetting
import com.patrickhoette.core.presentation.extension.launchCatching
import com.patrickhoette.core.presentation.model.GenericUIState
import com.patrickhoette.core.presentation.model.TypedUIState.Loading
import com.patrickhoette.core.presentation.model.setError
import com.patrickhoette.core.presentation.model.setLoading
import com.patrickhoette.core.presentation.model.setNormal
import com.patrickhoette.core.presentation.mvvm.MutableEventFlow
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import com.patrickhoette.core.utils.coroutine.SerialJob
import com.patrickhoette.pokedex.entity.config.UserSetting.UnitSystem
import com.patrickhoette.pokemon.domain.detail.ObservePokemon
import com.patrickhoette.pokemon.presentation.detail.model.PokemonDetailArgs
import com.patrickhoette.pokemon.presentation.detail.model.PokemonDetailEvent
import com.patrickhoette.pokemon.presentation.detail.model.PokemonDetailEvent.Close
import com.patrickhoette.pokemon.presentation.detail.model.PokemonDetailUIModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class PokemonDetailViewModel(
    private val observePokemon: ObservePokemon,
    private val observeUserSetting: ObserveUserSetting,
    private val mapper: PokemonDetailUIMapper,
    private val dispatchers: DispatcherProvider,
    @InjectedParam private val args: PokemonDetailArgs,
) : ViewModel() {

    private val _state = MutableStateFlow<GenericUIState<PokemonDetailUIModel?>>(Loading)
    val state by lazy {
        startObservingPokemon()
        _state.asStateFlow()
    }

    private val _event = MutableEventFlow<PokemonDetailEvent>()
    val event = _event.asEventFlow()

    private val serialJob = SerialJob()

    private fun startObservingPokemon() = serialJob.launchCatching(dispatchers.IO, onError = ::handleError) {
        _state.setLoading()
        combine(
            observePokemon(args.id),
            observeUserSetting(UnitSystem),
        ) { pokemon, unit ->
            pokemon?.let { mapper.mapToUIModel(it, unit) }
        }.collectLatest(_state::setNormal)
    }

    private fun handleError(error: Throwable) {
        Napier.e("Failed to observe pokemon", error)
        _state.setError(error)
    }

    fun onClose() = _event.setEvent(Close)
}
