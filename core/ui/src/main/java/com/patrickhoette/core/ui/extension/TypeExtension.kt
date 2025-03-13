package com.patrickhoette.core.ui.extension

import androidx.annotation.StringRes
import com.patrickhoette.core.ui.R
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Type.*

val Type.nameRes: Int
    @StringRes
    get() = when (this) {
        Normal -> R.string.type_normal
        Fire -> R.string.type_fire
        Water -> R.string.type_water
        Grass -> R.string.type_grass
        Electric -> R.string.type_electric
        Ice -> R.string.type_ice
        Fighting -> R.string.type_fighting
        Poison -> R.string.type_poison
        Ground -> R.string.type_ground
        Flying -> R.string.type_flying
        Psychic -> R.string.type_psychic
        Bug -> R.string.type_bug
        Rock -> R.string.type_rock
        Ghost -> R.string.type_ghost
        Dragon -> R.string.type_dragon
        Dark -> R.string.type_dark
        Steel -> R.string.type_steel
        Fairy -> R.string.type_fairy
        Unknown -> R.string.type_unknown
    }
