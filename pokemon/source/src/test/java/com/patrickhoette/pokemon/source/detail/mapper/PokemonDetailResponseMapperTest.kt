package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import com.patrickhoette.pokedex.entity.ability.Ability
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.move.Move
import com.patrickhoette.pokedex.entity.move.MoveMethod.LevelUp
import com.patrickhoette.pokedex.entity.move.MoveMethod.Machine
import com.patrickhoette.pokedex.entity.pokemon.*
import com.patrickhoette.pokedex.entity.species.Species
import com.patrickhoette.pokemon.source.detail.response.*
import com.patrickhoette.test.assertEquals
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PokemonDetailResponseMapperTest {

    @MockK
    private lateinit var typeMapper: PokemonTypeResponseMapper

    @MockK
    private lateinit var moveTypeMapper: PokemonMoveTypeResponseMapper

    @MockK
    private lateinit var spriteMapper: PokemonSpriteResponseMapper

    @InjectMockKs
    private lateinit var mapper: PokemonDetailResponseMapper

    @Test
    fun `Pokemon details response mapper`() = runTest {
        // Given
        val abilityOneName = "blaze"
        val abilityOneUrl = "https://pokeapi.co/api/v2/ability/66/"
        val abilityTwoName = "solar-power"
        val abilityTwoUrl = "https://pokeapi.co/api/v2/ability/94/"

        val formName = "charizard"
        val formUrl = "https://pokeapi.co/api/v2/pokemon-form/6/"

        val pastAbilityName = "something"
        val pastAbilityUrl = "https://pokeapi.co/api/v2/ability/999/"

        val sprites = buildJsonObject {
            put("something", "or other")
            put("something else", "who cares")
        }

        val spriteGroups = listOf(
            SpriteGroup(
                name = "Default",
                sprites = listOf(
                    Sprite(
                        name = "Default Back",
                        url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/6.png",
                    )
                ),
            )
        )

        val response = PokemonDetailResponse(
            abilities = listOf(
                PokemonAbilityResponse(
                    ability = RemoteReferenceResponse(
                        name = abilityOneName,
                        url = abilityOneUrl,
                    ),
                    isHidden = false,
                    slot = 1,
                ),
                PokemonAbilityResponse(
                    ability = RemoteReferenceResponse(
                        name = abilityTwoName,
                        url = abilityTwoUrl,
                    ),
                    isHidden = true,
                    slot = 3,
                ),
            ),
            baseExperience = 267,
            cries = PokemonCryResponse(
                latest = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/6.ogg",
                legacy = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/legacy/6.ogg",
            ),
            forms = listOf(
                RemoteReferenceResponse(
                    name = formName,
                    url = formUrl,
                ),
            ),
            gameIndices = emptyList(),
            height = 17,
            heldItems = emptyList(),
            id = 6,
            isDefault = true,
            locationAreaEncounters = "https://pokeapi.co/api/v2/pokemon/6/encounters",
            moves = listOf(
                PokemonMoveResponse(
                    move = RemoteReferenceResponse(
                        name = "mega-punch",
                        url = "https://pokeapi.co/api/v2/move/5/",
                    ),
                    versionGroupDetails = listOf(
                        PokemonMoveVersionGroupDetailsResponse(
                            levelLearnedAt = 0,
                            moveLearnMethod = RemoteReferenceResponse(
                                name = "machine",
                                url = "https://pokeapi.co/api/v2/move-learn-method/4/"
                            ),
                            versionGroup = RemoteReferenceResponse(
                                name = "red-blue",
                                url = "https://pokeapi.co/api/v2/version-group/1/"
                            ),
                        ),
                    ),
                ),
                PokemonMoveResponse(
                    move = RemoteReferenceResponse(
                        name = "ember",
                        url = "https://pokeapi.co/api/v2/move/52/",
                    ),
                    versionGroupDetails = listOf(
                        PokemonMoveVersionGroupDetailsResponse(
                            levelLearnedAt = 9,
                            moveLearnMethod = RemoteReferenceResponse(
                                name = "level-up",
                                url = "https://pokeapi.co/api/v2/move-learn-method/1/",
                            ),
                            versionGroup = RemoteReferenceResponse(
                                name = "red-blue",
                                url = "https://pokeapi.co/api/v2/version-group/1/",
                            ),
                        ),
                    ),
                ),
            ),
            name = "charizard",
            order = 7,
            pastAbilities = listOf(
                PokemonAbilityResponse(
                    ability = RemoteReferenceResponse(
                        name = pastAbilityName,
                        url = pastAbilityUrl,
                    ),
                    isHidden = true,
                    slot = 5,
                ),
            ),
            pastTypes = listOf(
                PokemonTypeResponse(
                    slot = 1,
                    type = RemoteReferenceResponse(
                        name = "ghost",
                        url = "https://pokeapi.co/api/v2/type/8/",
                    )
                )
            ),
            species = RemoteReferenceResponse(
                name = "charizard",
                url = "https://pokeapi.co/api/v2/pokemon-species/6/",
            ),
            sprites = sprites,
            stats = listOf(
                PokemonStatResponse(
                    baseStat = 78,
                    effort = 0,
                    stat = RemoteReferenceResponse(
                        name = "hp",
                        url = "https://pokeapi.co/api/v2/stat/1/",
                    ),
                ),
                PokemonStatResponse(
                    baseStat = 84,
                    effort = 0,
                    stat = RemoteReferenceResponse(
                        name = "attack",
                        url = "https://pokeapi.co/api/v2/stat/2/",
                    ),
                ),
                PokemonStatResponse(
                    baseStat = 78,
                    effort = 0,
                    stat = RemoteReferenceResponse(
                        name = "defense",
                        url = "https://pokeapi.co/api/v2/stat/3/",
                    ),
                ),
                PokemonStatResponse(
                    baseStat = 109,
                    effort = 0,
                    stat = RemoteReferenceResponse(
                        name = "special-attack",
                        url = "https://pokeapi.co/api/v2/stat/4/",
                    ),
                ),
                PokemonStatResponse(
                    baseStat = 85,
                    effort = 0,
                    stat = RemoteReferenceResponse(
                        name = "special-defence",
                        url = "https://pokeapi.co/api/v2/stat/5/",
                    ),
                ),
                PokemonStatResponse(
                    baseStat = 100,
                    effort = 0,
                    stat = RemoteReferenceResponse(
                        name = "speed",
                        url = "https://pokeapi.co/api/v2/stat/6/",
                    ),
                ),
            ),
            types = listOf(
                PokemonTypeResponse(
                    slot = 2,
                    type = RemoteReferenceResponse(
                        name = "flying",
                        url = "https://pokeapi.co/api/v2/type/3/",
                    ),
                ),
                PokemonTypeResponse(
                    slot = 1,
                    type = RemoteReferenceResponse(
                        name = "fire",
                        url = "https://pokeapi.co/api/v2/type/10/",
                    ),
                ),
            ),
            weight = 905,
        )

        every { typeMapper.mapType("fire") } returns Fire
        every { typeMapper.mapType("flying") } returns Flying
        every { typeMapper.mapType("ghost") } returns Ghost

        every { moveTypeMapper.mapMoveMethod("machine") } returns Machine
        every { moveTypeMapper.mapMoveMethod("level-up") } returns LevelUp

        every { spriteMapper.mapSpriteGroups(response) } returns spriteGroups

        // When
        val result = mapper.mapToPokemon(response)

        // Then
        result assertEquals Pokemon(
            id = 6,
            name = "charizard",
            types = listOf(Fire, Flying),
            detail = PokemonDetail(
                baseExperience = 267,
                height = Length.Centimeters(170),
                weight = Weight.Grams(90500),
                abilities = listOf(
                    Ability(
                        id = 66,
                        name = abilityOneName,
                        isHidden = false,
                    ),
                    Ability(
                        id = 94,
                        name = abilityTwoName,
                        isHidden = true,
                    ),
                ),
                moves = listOf(
                    Move(
                        id = 5,
                        name = "mega-punch",
                        method = Machine,
                        level = 0
                    ),
                    Move(
                        id = 52,
                        name = "ember",
                        method = LevelUp,
                        level = 9,
                    ),
                ),
                sprites = spriteGroups,
                cry = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/6.ogg",
                species = Species(
                    id = 6,
                    name = "charizard",
                ),
                stats = PokemonStats(
                    hp = 78,
                    attack = 84,
                    defense = 78,
                    specialAttack = 109,
                    specialDefense = 85,
                    speed = 100,
                ),
            ),
        )
    }
}
