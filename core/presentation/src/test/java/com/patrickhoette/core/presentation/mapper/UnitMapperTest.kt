package com.patrickhoette.core.presentation.mapper

import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.core.presentation.text.UnitTextProvider
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.UnitSystem.*
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.test.assertEquals
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UnitMapperTest {

    @MockK
    private lateinit var textProvider: UnitTextProvider

    @InjectMockKs
    private lateinit var mapper: UnitMapper

    @Test
    fun `Length mapping test`() {
        // Given
        val length = Length.Meters(403)
        val metricText = StringUIModel.Raw("Metric text")
        val imperialText = StringUIModel.Raw("Imperial text")
        every { textProvider.mapToTextMetric(length) } returns metricText
        every { textProvider.mapToTextImperial(length) } returns imperialText

        // When
        val resultMetric = mapper.mapToUIModel(length, Metric)
        val resultImperialUS = mapper.mapToUIModel(length, ImperialUS)
        val resultImperialUK = mapper.mapToUIModel(length, ImperialUK)

        // Then
        resultMetric assertEquals metricText
        resultImperialUS assertEquals imperialText
        resultImperialUK assertEquals imperialText
    }

    @Test
    fun `Weight mapping test`() {
        // Given
        val weight = Weight.Tonnes(1)
        val metricText = StringUIModel.Raw("Metric text")
        val imperialUKText = StringUIModel.Raw("Imperial UK text")
        val imperialUSText = StringUIModel.Raw("Imperial US text")
        every { textProvider.mapToTextMetric(weight) } returns metricText
        every { textProvider.mapToTextImperial(weight, true) } returns imperialUKText
        every { textProvider.mapToTextImperial(weight, false) } returns imperialUSText

        // When
        val resultMetric = mapper.mapToUIModel(weight, Metric)
        val resultImperialUS = mapper.mapToUIModel(weight, ImperialUS)
        val resultImperialUK = mapper.mapToUIModel(weight, ImperialUK)

        // Then
        resultMetric assertEquals metricText
        resultImperialUS assertEquals imperialUSText
        resultImperialUK assertEquals imperialUKText
    }
}
