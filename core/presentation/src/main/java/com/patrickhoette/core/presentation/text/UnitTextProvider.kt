package com.patrickhoette.core.presentation.text

import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Weight

interface UnitTextProvider {

    fun mapToTextMetric(length: Length): StringUIModel
    fun mapToTextImperial(length: Length): StringUIModel

    fun mapToTextMetric(weight: Weight): StringUIModel
    fun mapToTextImperial(weight: Weight, isUK: Boolean): StringUIModel
}
