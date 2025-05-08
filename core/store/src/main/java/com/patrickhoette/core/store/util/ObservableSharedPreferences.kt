@file:Suppress("RedundantSuspendModifier")

package com.patrickhoette.core.store.util

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.core.content.edit
import com.patrickhoette.core.utils.coroutine.MutableMapFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
class ObservableSharedPreferences(
    private val sharedPrefs: SharedPreferences,
) {

    private val cache = MutableMapFlow<String, Any?>(sharedPrefs.all)

    init {
        sharedPrefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == null) {
                cache.clear()
            } else {
                cache.update { sharedPreferences.all }
            }
        }
    }

    fun observeString(key: String): Flow<String?> = observeKeySafely(key)

    fun observeStringSet(key: String): Flow<Set<String>?> = observeKeySafely(key)

    fun observeBoolean(key: String): Flow<Boolean?> = observeKeySafely(key)

    fun observeLong(key: String): Flow<Long?> = observeKeySafely(key)

    fun observeInt(key: String): Flow<Int?> = observeKeySafely(key)

    fun observeFloat(key: String): Flow<Float?> = observeKeySafely(key)

    private inline fun <reified T> observeKeySafely(key: String) = cache[key].mapLatest {
        if (it != null && it !is T) {
            error("The value for this key is not a ${T::class.simpleName} instead a ${it::class.simpleName} was found!")
        }
        it as T?
    }

    fun observeAll() = cache.entries()

    suspend fun remove(key: String) = sharedPrefs.commit { remove(key) }

    suspend fun clear() {
        sharedPrefs.commit { clear() }
        cache.clear()
    }

    suspend fun putString(key: String, value: String?) = putOrRemoveNull(key, value, Editor::putString)

    suspend fun putStringSet(key: String, value: Set<String>?) = putOrRemoveNull(key, value, Editor::putStringSet)

    suspend fun putBoolean(key: String, value: Boolean?) = putOrRemoveNull(key, value, Editor::putBoolean)

    suspend fun putLong(key: String, value: Long?) = putOrRemoveNull(key, value, Editor::putLong)

    suspend fun putInt(key: String, value: Int?) = putOrRemoveNull(key, value, Editor::putInt)

    suspend fun putFloat(key: String, value: Float?) = putOrRemoveNull(key, value, Editor::putFloat)

    private suspend inline fun <reified T> putOrRemoveNull(
        key: String,
        value: T?,
        crossinline edit: Editor.(String, T) -> Unit,
    ) {
        val currentValue = sharedPrefs.all[key]
        if (currentValue != null && currentValue !is T) {
            throw IllegalArgumentException(
                "Trying to set a ${T::class.simpleName} for $key however existing value is a " +
                    "${currentValue::class.simpleName}"
            )
        }
        sharedPrefs.commit { if (value != null) edit(key, value) else remove(key) }
    }

    private fun SharedPreferences.commit(edit: Editor.() -> Unit) = edit(commit = true, action = edit)
}
