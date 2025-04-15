package com.patrickhoette.core.store.config

import com.patrickhoette.core.data.config.UserSettingStore
import com.patrickhoette.core.store.di.CoreStoreModule.Companion.UserSettingScope
import com.patrickhoette.core.store.util.ObservableSharedPreferences
import com.patrickhoette.pokedex.entity.config.UserSetting
import com.patrickhoette.pokedex.entity.generic.UnitSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class SharedPrefsUserSettingStore(
    @Named(UserSettingScope) private val sharedPrefs: ObservableSharedPreferences,
) : UserSettingStore {

    @Suppress("UNCHECKED_CAST") // Its okay, I control everything
    override fun <T> observeUserSetting(setting: UserSetting<T>): Flow<T> = when (setting) {
        is UserSetting.UnitSystem -> sharedPrefs.observeString(KeyUnitSystem)
            .mapLatest { it?.let(UnitSystem::valueOf) ?: setting.default }
    } as Flow<T>

    override suspend fun <T> setUserSetting(setting: UserSetting<T>, value: T?) = when (setting) {
        UserSetting.UnitSystem -> sharedPrefs.putString(KeyUnitSystem, (value as? UnitSystem)?.name)
    }

    companion object {

        private const val KeyUnitSystem = "KeyUnitSystem"
    }
}
