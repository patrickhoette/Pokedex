package com.patrickhoette.core.domain.config

import com.patrickhoette.pokedex.entity.config.UserSetting
import kotlinx.coroutines.flow.Flow

interface UserSettingRepository {

    fun <T> observeUserSetting(setting: UserSetting<T>): Flow<T>
    suspend fun <T> setUserSetting(setting: UserSetting<T>, value: T?)
}
