package com.patrickhoette.pokedex.entity.config

import com.patrickhoette.pokedex.entity.generic.UnitSystem.Metric
import com.patrickhoette.pokedex.entity.generic.UnitSystem as UnitSystemEnum

sealed interface UserSetting<out T> {

    val default: T

    data object UnitSystem : UserSetting<UnitSystemEnum> {

        @Suppress("ObjectPropertyNaming")
        override val default: UnitSystemEnum = Metric
    }
}
