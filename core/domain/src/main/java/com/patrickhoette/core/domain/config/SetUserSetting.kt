package com.patrickhoette.core.domain.config

import com.patrickhoette.pokedex.entity.config.UserSetting
import org.koin.core.annotation.Factory

@Factory
class SetUserSetting(
    private val repository: UserSettingRepository,
) {

    suspend operator fun <T> invoke(setting: UserSetting<T>, value: T?) = repository.setUserSetting(
        setting = setting,
        value = value,
    )
}
