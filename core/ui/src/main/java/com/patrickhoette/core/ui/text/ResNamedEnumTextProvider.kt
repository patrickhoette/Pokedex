package com.patrickhoette.core.ui.text

import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.core.presentation.text.NamedEnumTextProvider
import com.patrickhoette.core.ui.R
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.pokedex.entity.generic.Type.Unknown
import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.pokedex.entity.move.MoveMethod.*
import org.koin.core.annotation.Factory

@Factory
class ResNamedEnumTextProvider : NamedEnumTextProvider {

    override fun <T : Enum<out T>> getName(enum: T): StringUIModel = when (enum) {
        is Type -> getName(enum)
        is MoveMethod -> getName(enum)
        else -> throw IllegalArgumentException("Unsupported enum ${enum::class.simpleName}, cannot map name!")
    }

    private fun getName(type: Type): StringUIModel = StringUIModel.Res(
        when (type) {
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
            Stellar -> R.string.type_stellar
            Unknown -> R.string.unknown
        }
    )

    private fun getName(moveMethod: MoveMethod): StringUIModel = StringUIModel.Res(
        when (moveMethod) {
            LevelUp -> R.string.move_method_level_up
            Egg -> R.string.move_method_egg
            Tutor -> R.string.move_method_tutor
            Machine -> R.string.move_method_machine
            StadiumSurfingPikachu -> R.string.move_method_stadium_surfing_pickachu
            LightBallEgg -> R.string.move_method_light_ball_egg
            ColosseumPurification -> R.string.move_method_colosseum_purification
            XDShadow -> R.string.move_method_xd_shadow
            XDPurification -> R.string.move_method_xd_purification
            FormChange -> R.string.move_method_form_change
            ZygardeCube -> R.string.move_method_zygard_cube
            MoveMethod.Unknown -> R.string.unknown
        }
    )
}
