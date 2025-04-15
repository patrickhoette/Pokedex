package com.patrickhoette.core.store.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.patrickhoette.core.store.util.ObservableSharedPreferences
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.patrickhoette.core.store")
class CoreStoreModule {

    @Single
    @Named(UserSettingScope)
    fun provideSharedPrefs(context: Context): ObservableSharedPreferences {
        val sharedPrefs = context.getSharedPreferences(UserSettingSharedPrefs, MODE_PRIVATE)
        return ObservableSharedPreferences(sharedPrefs)
    }

    companion object {

        const val UserSettingScope = "UserSettingScope"
        const val UserSettingSharedPrefs = "UserSettingsSharedPrefs"
    }
}
