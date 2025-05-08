package com.patrickhoette.core.domain.config

import app.cash.turbine.test
import com.patrickhoette.pokedex.entity.config.UserSetting.UnitSystem
import com.patrickhoette.pokedex.entity.generic.UnitSystem.Metric
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.model.TestException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ObserveUserSettingTest {

    @MockK
    private lateinit var repository: UserSettingRepository

    @InjectMockKs
    private lateinit var observeUserSetting: ObserveUserSetting

    @Test
    fun `Given observing fails, when invoked, then throw error`() = runTest {
        // Given
        val setting = UnitSystem
        every { repository.observeUserSetting(setting) } throws TestException

        // When -> Then
        assertTestException { observeUserSetting(setting) }
    }

    @Test
    fun `Given observing emits error, when invoked, then emit error`() = runTest {
        // Given
        val setting = UnitSystem
        every { repository.observeUserSetting(setting) } returns flow { throw TestException }

        // When
        observeUserSetting(setting).test {
            // Then
            awaitError() assertEquals TestException
        }
    }

    @Test
    fun `Given observing emits, when invoked, then emit item`() = runTest {
        // Given
        val setting = UnitSystem
        every { repository.observeUserSetting(setting) } returns flowOf(Metric)

        // When
        observeUserSetting(setting).test {
            // Then
            awaitItem() assertEquals Metric
            awaitComplete()
        }
    }
}
