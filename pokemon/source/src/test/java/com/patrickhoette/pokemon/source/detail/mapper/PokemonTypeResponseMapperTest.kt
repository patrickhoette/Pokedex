package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
class PokemonTypeResponseMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonTypeResponseMapper

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}] Given name is {0}, when mapping type, then return {1}")
    fun `Type mapping test`(
        name: String,
        type: Type,
    ) {
        // When
        val result = mapper.mapType(name)

        // Then
        result assertEquals type
    }

    companion object {

        @JvmStatic
        fun provideArgs() = listOf(
            Arguments.of("normal", Normal),
            Arguments.of("fighting", Fighting),
            Arguments.of("flying", Flying),
            Arguments.of("poison", Poison),
            Arguments.of("ground", Ground),
            Arguments.of("rock", Rock),
            Arguments.of("bug", Bug),
            Arguments.of("ghost", Ghost),
            Arguments.of("steel", Steel),
            Arguments.of("fire", Fire),
            Arguments.of("water", Water),
            Arguments.of("grass", Grass),
            Arguments.of("electric", Electric),
            Arguments.of("psychic", Psychic),
            Arguments.of("ice", Ice),
            Arguments.of("dragon", Dragon),
            Arguments.of("dark", Dark),
            Arguments.of("fairy", Fairy),
            Arguments.of("stellar", Stellar),
            Arguments.of("iosafgioew", Unknown),
        )
    }
}
