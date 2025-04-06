package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.pokedex.entity.move.MoveMethod.*
import org.koin.core.annotation.Factory

@Factory
class PokemonMoveTypeResponseMapper {

    fun mapMoveMethod(name: String) = when (name) {
        "level-up" -> LevelUp
        "egg" -> Egg
        "tutor" -> Tutor
        "machine" -> Machine
        "stadium-surfing-pikachu" -> StadiumSurfingPikachu
        "light-ball-egg" -> LightBallEgg
        "colosseum-purification" -> ColosseumPurification
        "xd-shadow" -> XDShadow
        "xd-purification" -> XDPurification
        "form-change" -> FormChange
        "zygarde-cube" -> ZygardeCube
        else -> Unknown
    }
}
