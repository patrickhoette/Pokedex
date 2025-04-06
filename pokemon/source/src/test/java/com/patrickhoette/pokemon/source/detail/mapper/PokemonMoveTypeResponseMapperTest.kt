package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.pokedex.entity.move.MoveMethod.*
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
class PokemonMoveTypeResponseMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonMoveTypeResponseMapper

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] Given name is {0}, when mapping move method, then return {1}")
    fun `Move method mapping test`(
        name: String,
        method: MoveMethod,
    ) {
        // When
        val result = mapper.mapMoveMethod(name)

        // Then
        result assertEquals method
    }

    companion object {

        @JvmStatic
        fun provideArgs() = listOf(
            Arguments.of("level-up", LevelUp),
            Arguments.of("egg", Egg),
            Arguments.of("tutor", Tutor),
            Arguments.of("machine", Machine),
            Arguments.of("stadium-surfing-pikachu", StadiumSurfingPikachu),
            Arguments.of("light-ball-egg", LightBallEgg),
            Arguments.of("colosseum-purification", ColosseumPurification),
            Arguments.of("xd-shadow", XDShadow),
            Arguments.of("xd-purification", XDPurification),
            Arguments.of("form-change", FormChange),
            Arguments.of("zygarde-cube", ZygardeCube),
            Arguments.of("SDfopfqoewn", Unknown),
        )
    }
}
