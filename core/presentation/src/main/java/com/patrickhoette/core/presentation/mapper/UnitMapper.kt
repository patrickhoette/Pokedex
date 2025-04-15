package com.patrickhoette.core.presentation.mapper

import com.patrickhoette.core.presentation.text.UnitTextProvider
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.UnitSystem
import com.patrickhoette.pokedex.entity.generic.UnitSystem.*
import com.patrickhoette.pokedex.entity.generic.Weight
import org.koin.core.annotation.Factory

@Factory
class UnitMapper(
    private val textProvider: UnitTextProvider,
) {

    fun mapToUIModel(length: Length, unitSystem: UnitSystem) = when (unitSystem) {
        Metric -> textProvider.mapToTextMetric(length)
        ImperialUK, ImperialUS -> textProvider.mapToTextImperial(length)
    }

    fun mapToUIModel(weight: Weight, unitSystem: UnitSystem) = when (unitSystem) {
        Metric -> textProvider.mapToTextMetric(weight)
        ImperialUK, ImperialUS -> textProvider.mapToTextImperial(weight = weight, isUK = unitSystem == ImperialUK)
    }
}
