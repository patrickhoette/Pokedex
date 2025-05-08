package com.patrickhoette.core.store.config

import app.cash.turbine.test
import com.patrickhoette.core.store.util.ObservableSharedPreferences
import com.patrickhoette.pokedex.entity.config.UserSetting
import com.patrickhoette.pokedex.entity.config.UserSetting.UnitSystem
import com.patrickhoette.pokedex.entity.generic.UnitSystem.ImperialUK
import com.patrickhoette.pokedex.entity.generic.UnitSystem.Metric
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertSucceeds
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.model.TestException
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
class SharedPrefsUserSettingStoreTest {

    @MockK
    private lateinit var sharedPrefs: ObservableSharedPreferences

    @InjectMockKs
    private lateinit var store: SharedPrefsUserSettingStore

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] {0}")
    fun `Given observing fails, when observing user setting, then throw error`(
        setting: UserSetting<*>,
    ) = runTest {
        // Given
        every { sharedPrefs.observeStringSet(any()) } throws TestException
        every { sharedPrefs.observeString(any()) } throws TestException
        every { sharedPrefs.observeInt(any()) } throws TestException
        every { sharedPrefs.observeFloat(any()) } throws TestException
        every { sharedPrefs.observeBoolean(any()) } throws TestException
        every { sharedPrefs.observeLong(any()) } throws TestException

        // When -> Then
        assertTestException { store.observeUserSetting(setting) }
    }

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] {0}")
    fun `Given observing emits error, when observing user setting, then emit error`(
        setting: UserSetting<*>,
    ) = runTest {
        // Given
        every { sharedPrefs.observeStringSet(any()) } returns flow { throw TestException }
        every { sharedPrefs.observeString(any()) } returns flow { throw TestException }
        every { sharedPrefs.observeInt(any()) } returns flow { throw TestException }
        every { sharedPrefs.observeFloat(any()) } returns flow { throw TestException }
        every { sharedPrefs.observeBoolean(any()) } returns flow { throw TestException }
        every { sharedPrefs.observeLong(any()) } returns flow { throw TestException }

        // When -> Then
        store.observeUserSetting(setting).test {
            awaitError() assertEquals TestException
        }
    }

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] {0}")
    fun `Given observing emits, when observing user setting, then emit`(
        setting: UserSetting<*>,
    ) = runTest {
        // Given
        val expected = when (setting) {
            UnitSystem -> {
                val value = Metric
                every { sharedPrefs.observeString("KeyUnitSystem") } returns flowOf(value.name)
                value
            }
        }

        // When
        store.observeUserSetting(setting).test {
            // Then
            awaitItem() assertEquals expected
            awaitComplete()
        }
    }

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] {0}")
    fun `Given observing emits null, when observing user setting, then emit default`(
        setting: UserSetting<*>,
    ) = runTest {
        // Given
        every { sharedPrefs.observeStringSet(any()) } returns flowOf(null)
        every { sharedPrefs.observeString(any()) } returns flowOf(null)
        every { sharedPrefs.observeInt(any()) } returns flowOf(null)
        every { sharedPrefs.observeFloat(any()) } returns flowOf(null)
        every { sharedPrefs.observeBoolean(any()) } returns flowOf(null)
        every { sharedPrefs.observeLong(any()) } returns flowOf(null)

        // When
        store.observeUserSetting(setting).test {
            // Then
            awaitItem() assertEquals setting.default
            awaitComplete()
        }
    }

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] {0}")
    fun `Given setting fails, when setting user setting, then throw error`(
        setting: UserSetting<*>,
    ) = runTest {
        // Given
        coEvery { sharedPrefs.putString(any(), any()) } throws TestException
        coEvery { sharedPrefs.putStringSet(any(), any()) } throws TestException
        coEvery { sharedPrefs.putInt(any(), any()) } throws TestException
        coEvery { sharedPrefs.putFloat(any(), any()) } throws TestException
        coEvery { sharedPrefs.putBoolean(any(), any()) } throws TestException
        coEvery { sharedPrefs.putLong(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.setUserSetting(setting, setting.default) }
    }

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] {0}")
    fun `Given setting succeeds, when setting user setting, then put it in the shared prefs`(
        setting: UserSetting<*>,
    ) = runTest {
        // Given
        val value = when (setting) {
            UnitSystem -> ImperialUK
        }
        coEvery { sharedPrefs.putString(any(), any()) } just runs
        coEvery { sharedPrefs.putStringSet(any(), any()) } just runs
        coEvery { sharedPrefs.putInt(any(), any()) } just runs
        coEvery { sharedPrefs.putFloat(any(), any()) } just runs
        coEvery { sharedPrefs.putBoolean(any(), any()) } just runs
        coEvery { sharedPrefs.putLong(any(), any()) } just runs

        // When -> Then
        assertSucceeds { store.setUserSetting(setting, value) }
        coVerify {
            when (setting) {
                UnitSystem -> sharedPrefs.putString("KeyUnitSystem", value.name)
            }
        }
    }

    companion object {

        @JvmStatic
        fun provideArgs() = UserSetting::class.sealedSubclasses
            .map { Arguments.of(it.objectInstance) }
    }
}
