package com.patrickhoette.core.domain.config

import com.patrickhoette.pokedex.entity.config.UserSetting.UnitSystem
import com.patrickhoette.pokedex.entity.generic.UnitSystem.Metric
import com.patrickhoette.test.assertSucceeds
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.model.TestException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SetUserSettingTest {

    @MockK
    private lateinit var repository: UserSettingRepository

    @InjectMockKs
    private lateinit var setUserSetting: SetUserSetting

    @Test
    fun `Given setting fails, when invoked, then throw error`() = runTest {
        // Given
        val setting = UnitSystem
        val value = Metric
        coEvery { repository.setUserSetting(setting, value) } throws TestException

        // When -> Then
        assertTestException { setUserSetting(setting, value) }
    }

    @Test
    fun `Given setting succeeds, when invoked, then set with repository`() = runTest {
        // Given
        val setting = UnitSystem
        val value = Metric
        coEvery { repository.setUserSetting(setting, value) } just runs

        // When -> Then
        assertSucceeds { setUserSetting(setting, value) }
        coVerify { repository.setUserSetting(setting, value) }
    }
}
