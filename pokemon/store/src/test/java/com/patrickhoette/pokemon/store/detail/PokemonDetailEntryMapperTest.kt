package com.patrickhoette.pokemon.store.detail

import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Type.Fire
import com.patrickhoette.pokedex.entity.generic.Type.Flying
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.move.MoveMethod.LevelUp
import com.patrickhoette.pokedex.entity.move.MoveMethod.Machine
import com.patrickhoette.pokedex.entity.pokemon.*
import com.patrickhoette.pokedex.entity.species.Species
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import com.patrickhoette.pokedex.database.Pokemon_full_view as PokemonFullEntry
import com.patrickhoette.pokedex.database.Sprite as SpriteEntry
import com.patrickhoette.pokedex.database.pokemon.SelectAbilitiesById as AbilityEntry
import com.patrickhoette.pokedex.database.pokemon.SelectMovesById as MoveEntry

@ExtendWith(MockKExtension::class)
class PokemonDetailEntryMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonDetailEntryMapper

    @Test
    fun `Pokemon detail entry mapping test`() = runTest {
        // Given
        val entry = createFullEntry()
        val abilities = listOf(
            AbilityEntry(
                id = 5L,
                name = "ability 5",
                isHidden = 0L,
            ),
            AbilityEntry(
                id = 9L,
                name = "ability 9",
                isHidden = 1L,
            )
        )
        val moves = listOf(
            MoveEntry(
                id = 1L,
                name = "move 1",
                method = LevelUp.name,
                level = 6L
            ),
            MoveEntry(
                id = 2L,
                name = "move 2",
                method = Machine.name,
                level = 0L,
            )
        )
        val sprites = listOf(
            SpriteEntry(
                pokemonId = 6,
                name = "first sprite",
                url = "https://www.sprite.com/first",
                groupName = "group one",
            ),
            SpriteEntry(
                pokemonId = 6,
                name = "second sprite",
                url = "https://www.sprite.com/second",
                groupName = "group one",
            ),
            SpriteEntry(
                pokemonId = 6,
                name = "third sprite",
                url = "https://www.sprite.com/third",
                groupName = "group two",
            ),
            SpriteEntry(
                pokemonId = 6,
                name = "fourth sprite",
                url = "https://www.sprite.com/fourth",
                groupName = "group three",
            ),
            SpriteEntry(
                pokemonId = 6,
                name = "fifth sprite",
                url = "https://www.sprite.com/fifth",
                groupName = "group one",
            ),
            SpriteEntry(
                pokemonId = 6,
                name = "sixth sprite",
                url = "https://www.sprite.com/sixth",
                groupName = "group four",
            ),
            SpriteEntry(
                pokemonId = 6,
                name = "seventh sprite",
                url = "https://www.sprite.com/seventh",
                groupName = "group two",
            ),
        )

        // When
        val result = mapper.mapToModel(entry, abilities, moves, sprites)

        // Then
        result assertEquals Pokemon(
            id = 6,
            name = "charizard",
            types = listOf(Fire, Flying),
            detail = PokemonDetail(
                baseExperience = 100,
                height = Length.Centimeters(10),
                weight = Weight.Kilograms(10),
                abilities = listOf(
                    Ability(
                        id = 5,
                        name = "ability 5",
                        isHidden = false,
                    ),
                    Ability(
                        id = 9,
                        name = "ability 9",
                        isHidden = true,
                    ),
                ),
                moves = listOf(
                    Move(
                        id = 1,
                        name = "move 1",
                        method = LevelUp,
                        level = 6,
                    ),
                    Move(
                        id = 2,
                        name = "move 2",
                        method = Machine,
                        level = 0,
                    ),
                ),
                sprites = listOf(
                    SpriteGroup(
                        name = "group one",
                        sprites = listOf(
                            Sprite(
                                name = "first sprite",
                                url = "https://www.sprite.com/first",
                            ),
                            Sprite(
                                name = "second sprite",
                                url = "https://www.sprite.com/second",
                            ),
                            Sprite(
                                name = "fifth sprite",
                                url = "https://www.sprite.com/fifth",
                            ),
                        ),
                    ),
                    SpriteGroup(
                        name = "group two",
                        sprites = listOf(
                            Sprite(
                                name = "third sprite",
                                url = "https://www.sprite.com/third",
                            ),
                            Sprite(
                                name = "seventh sprite",
                                url = "https://www.sprite.com/seventh",
                            ),
                        ),
                    ),
                    SpriteGroup(
                        name = "group three",
                        sprites = listOf(
                            Sprite(
                                name = "fourth sprite",
                                url = "https://www.sprite.com/fourth",
                            ),
                        ),
                    ),
                    SpriteGroup(
                        name = "group four",
                        sprites = listOf(
                            Sprite(
                                name = "sixth sprite",
                                url = "https://www.sprite.com/sixth",
                            ),
                        ),
                    ),
                ),
                cry = "something or other",
                species = Species(
                    id = 9,
                    name = "charizard",
                ),
                stats = PokemonStats(
                    hp = 324,
                    attack = 231,
                    defense = 123,
                    specialAttack = 45,
                    specialDefense = 87,
                    speed = 100,
                ),
            ),
        )
    }

    @MethodSource("provideArgs")
    @ParameterizedTest(name = "[{index}]")
    fun `Pokemon detail entry null mapping test`(
        entry: PokemonFullEntry,
    ) = runTest {
        // When
        val result = mapper.mapToModel(
            full = entry,
            abilities = emptyList(),
            moves = emptyList(),
            sprites = emptyList(),
        )

        // Then
        result assertEquals Pokemon(
            id = 6,
            name = "charizard",
            types = listOf(Fire, Flying),
            detail = null
        )
    }

    companion object {

        @JvmStatic
        fun provideArgs() = listOf(
            Arguments.of(createFullEntry(baseExperience = null)),
            Arguments.of(createFullEntry(heightCm = null)),
            Arguments.of(createFullEntry(weightG = null)),
            Arguments.of(createFullEntry(hp = null)),
            Arguments.of(createFullEntry(attack = null)),
            Arguments.of(createFullEntry(defense = null)),
            Arguments.of(createFullEntry(specialAttack = null)),
            Arguments.of(createFullEntry(specialDefense = null)),
            Arguments.of(createFullEntry(speed = null)),
            Arguments.of(createFullEntry(speciesId = null)),
            Arguments.of(createFullEntry(speciesName = null)),
        )

        private fun createFullEntry(
            baseExperience: Long? = 100L,
            heightCm: Double? = 10.0,
            weightG: Double? = 10_000.0,
            cry: String? = "something or other",
            hp: Long? = 324L,
            attack: Long? = 231L,
            defense: Long? = 123L,
            specialAttack: Long? = 45L,
            specialDefense: Long? = 87L,
            speed: Long? = 100L,
            speciesId: Long? = 9L,
            speciesName: String? = "charizard",
        ) = PokemonFullEntry(
            id = 6,
            name = "charizard",
            lastUpdate = 1000L,
            baseExperience = baseExperience,
            heightCm = heightCm,
            weightG = weightG,
            cry = cry,
            hp = hp,
            attack = attack,
            defense = defense,
            specialAttack = specialAttack,
            specialDefense = specialDefense,
            speed = speed,
            primaryType = Fire.name,
            secondaryType = Flying.name,
            speciesId = speciesId,
            speciesName = speciesName,
        )
    }
}
