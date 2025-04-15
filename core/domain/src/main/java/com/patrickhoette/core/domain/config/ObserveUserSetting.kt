package com.patrickhoette.core.domain.config

import com.patrickhoette.pokedex.entity.config.UserSetting
import org.koin.core.annotation.Factory

@Factory
class ObserveUserSetting(
    private val repository: UserSettingRepository,
) {

    operator fun <T> invoke(setting: UserSetting<T>) = repository.observeUserSetting(setting)
}
