package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.core.source.response.RemoteReferenceResponse
import com.patrickhoette.pokedex.entity.pokemon.Sprite
import com.patrickhoette.pokedex.entity.pokemon.SpriteGroup
import com.patrickhoette.pokemon.source.detail.response.PokemonCryResponse
import com.patrickhoette.pokemon.source.detail.response.PokemonDetailResponse
import com.patrickhoette.test.assertEquals
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PokemonSpriteResponseMapperTest {

    @InjectMockKs
    private lateinit var mapper: PokemonSpriteResponseMapper

    @Test
    fun `Sprite group mapping test`() = runTest {
        // Given
        val backDefaultUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/6.png"
        val backShinyUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/6.png"
        val frontDefaultUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png"
        val frontShinyUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/6.png"

        val dreamWorldFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/6.svg"

        val homeFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/6.png"
        val homeFrontShinyUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/6.png"

        val officialArtworkFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"
        val officialArtworkFrontShinyUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/6.png"

        val redBlueBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/back/6.png"
        val redBlueFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/6.png"

        val yellowBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/yellow/back/6.png"
        val yellowFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/yellow/6.png"

        val crystalBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/crystal/back/6.png"
        val crystalFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/crystal/6.png"

        val goldBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/back/6.png"
        val goldFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/6.png"

        val silverBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/silver/back/6.png"
        val silverFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/silver/6.png"

        val emeraldBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/emerald/6.png"

        val fireredLeafgreenBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/firered-leafgreen/back/6.png"
        val fireredLeafgreenFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/firered-leafgreen/6.png"

        val rubySapphireBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/ruby-sapphire/back/6.png"
        val rubySapphireFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/ruby-sapphire/6.png"

        val diamondPearlBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/diamond-pearl/back/6.png"
        val diamondPearlFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/diamond-pearl/6.png"

        val heartgoldSoulsilverBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/heartgold-soulsilver/back/6.png"
        val heartgoldSoulsilverFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/heartgold-soulsilver/6.png"

        val platinumBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/platinum/back/6.png"
        val platinumFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/platinum/6.png"

        val blackWhiteBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/back/6.png"
        val blackWhiteFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/6.png"
        val blackWhiteBackDefaultAnimatedUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/back/6.gif"
        val blackWhiteFrontDefaultAnimatedUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/6.gif"

        val omegarubyAlphasapphireBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/omegaruby-alphasapphire/6.png"
        val omegarubyAlphasapphireFrontDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/omegaruby-alphasapphire/shiny/6.png"

        val xYBackDefaultUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/x-y/6.png"

        val ultrasunUltramoonFrontDefault =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/ultra-sun-ultra-moon/6.png"

        val response = PokemonDetailResponse(
            abilities = emptyList(),
            baseExperience = 0,
            cries = PokemonCryResponse(null, null),
            forms = emptyList(),
            gameIndices = emptyList(),
            height = 0,
            heldItems = emptyList(),
            id = 0,
            isDefault = true,
            locationAreaEncounters = "",
            moves = emptyList(),
            name = "",
            order = 0,
            pastAbilities = emptyList(),
            pastTypes = emptyList(),
            species = RemoteReferenceResponse("", ""),
            sprites = buildJsonObject {
                put("back_default", backDefaultUrl)
                put("back_female", null as String?)
                put("back_shiny", backShinyUrl)
                put("back_shiny_female", null as String?)
                put("front_default", frontDefaultUrl)
                put("front_female", null as String?)
                put("front_shiny", frontShinyUrl)
                put("front_shiny_female", null as String?)

                putJsonObject("other") {
                    putJsonObject("dream_world") {
                        put("front_default", dreamWorldFrontDefaultUrl)
                        put("front_female", null as String?)
                    }

                    putJsonObject("home") {
                        put("front_default", homeFrontDefaultUrl)
                        put("front_female", null as String?)
                        put("front_shiny", homeFrontShinyUrl)
                        put("front_shiny_female", null as String?)
                    }

                    putJsonObject("official-artwork") {
                        put("front_default", officialArtworkFrontDefaultUrl)
                        put("front_shiny", officialArtworkFrontShinyUrl)
                    }
                }

                putJsonObject("versions") {
                    putJsonObject("generation-i") {
                        putJsonObject("red-blue") {
                            put("back_default", redBlueBackDefaultUrl)
                            put("front_default", redBlueFrontDefaultUrl)
                        }

                        putJsonObject("yellow") {
                            put("back_default", yellowBackDefaultUrl)
                            put("front_default", yellowFrontDefaultUrl)
                        }
                    }

                    putJsonObject("generation-ii") {
                        putJsonObject("crystal") {
                            put("back_default", crystalBackDefaultUrl)
                            put("front_default", crystalFrontDefaultUrl)
                        }

                        putJsonObject("gold") {
                            put("back_default", goldBackDefaultUrl)
                            put("front_default", goldFrontDefaultUrl)
                        }

                        putJsonObject("silver") {
                            put("back_default", silverBackDefaultUrl)
                            put("front_default", silverFrontDefaultUrl)
                        }
                    }

                    putJsonObject("generation-iii") {
                        putJsonObject("emerald") {
                            put("back_default", emeraldBackDefaultUrl)
                        }

                        putJsonObject("firered-leafgreen") {
                            put("back_default", fireredLeafgreenBackDefaultUrl)
                            put("front_default", fireredLeafgreenFrontDefaultUrl)
                        }

                        putJsonObject("ruby-sapphire") {
                            put("back_default", rubySapphireBackDefaultUrl)
                            put("front_default", rubySapphireFrontDefaultUrl)
                        }
                    }

                    putJsonObject("generation-iv") {
                        putJsonObject("diamond-pearl") {
                            put("back_default", diamondPearlBackDefaultUrl)
                            put("front_default", diamondPearlFrontDefaultUrl)
                        }

                        putJsonObject("heartgold-soulsilver") {
                            put("back_default", heartgoldSoulsilverBackDefaultUrl)
                            put("front_default", heartgoldSoulsilverFrontDefaultUrl)
                        }

                        putJsonObject("platinum") {
                            put("back_default", platinumBackDefaultUrl)
                            put("front_default", platinumFrontDefaultUrl)
                        }
                    }

                    putJsonObject("generation-v") {
                        putJsonObject("black-white") {
                            putJsonObject("animated") {
                                put("back_default", blackWhiteBackDefaultAnimatedUrl)
                                put("front_default", blackWhiteFrontDefaultAnimatedUrl)
                            }

                            put("back_default", blackWhiteBackDefaultUrl)
                            put("front_default", blackWhiteFrontDefaultUrl)
                        }
                    }

                    putJsonObject("generation-vi") {
                        putJsonObject("omegaruby-alphasapphire") {
                            put("back_default", omegarubyAlphasapphireBackDefaultUrl)
                            put("front_default", omegarubyAlphasapphireFrontDefaultUrl)
                        }

                        putJsonObject("x-y") {
                            put("back_default", xYBackDefaultUrl)
                        }
                    }

                    putJsonObject("generation-vii") {
                        putJsonObject("icons") {
                            put("front_default", "something")
                            put("front_female", null as String?)
                        }

                        putJsonObject("ultra-sun-ultra-moon") {
                            put("back_default", ultrasunUltramoonFrontDefault)
                        }
                    }

                    putJsonObject("generation-viii") {
                        putJsonObject("icons") {
                            put("front_default", "something")
                            put("front_female", null as String?)
                        }
                    }
                }
            },
            stats = emptyList(),
            types = emptyList(),
            weight = 0,
        )

        // When
        val result = mapper.mapSpriteGroups(response)

        // Then
        result assertEquals listOf(
            SpriteGroup(
                name = "Default",
                sprites = listOf(
                    Sprite("Back Default", backDefaultUrl),
                    Sprite("Back Shiny", backShinyUrl),
                    Sprite("Front Default", frontDefaultUrl),
                    Sprite("Front Shiny", frontShinyUrl),
                ),
            ),
            SpriteGroup(
                name = "Dream World",
                sprites = listOf(
                    Sprite("Front Default", dreamWorldFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Home",
                sprites = listOf(
                    Sprite("Front Default", homeFrontDefaultUrl),
                    Sprite("Front Shiny", homeFrontShinyUrl),
                ),
            ),
            SpriteGroup(
                name = "Official Artwork",
                sprites = listOf(
                    Sprite("Front Default", officialArtworkFrontDefaultUrl),
                    Sprite("Front Shiny", officialArtworkFrontShinyUrl),
                ),
            ),
            SpriteGroup(
                name = "Red & Blue",
                sprites = listOf(
                    Sprite("Back Default", redBlueBackDefaultUrl),
                    Sprite("Front Default", redBlueFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Yellow",
                sprites = listOf(
                    Sprite("Back Default", yellowBackDefaultUrl),
                    Sprite("Front Default", yellowFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Crystal",
                sprites = listOf(
                    Sprite("Back Default", crystalBackDefaultUrl),
                    Sprite("Front Default", crystalFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Gold",
                sprites = listOf(
                    Sprite("Back Default", goldBackDefaultUrl),
                    Sprite("Front Default", goldFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Silver",
                sprites = listOf(
                    Sprite("Back Default", silverBackDefaultUrl),
                    Sprite("Front Default", silverFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Emerald",
                sprites = listOf(
                    Sprite("Back Default", emeraldBackDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Fire Red & Leaf Green",
                sprites = listOf(
                    Sprite("Back Default", fireredLeafgreenBackDefaultUrl),
                    Sprite("Front Default", fireredLeafgreenFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Ruby & Sapphire",
                sprites = listOf(
                    Sprite("Back Default", rubySapphireBackDefaultUrl),
                    Sprite("Front Default", rubySapphireFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Diamond & Pearl",
                sprites = listOf(
                    Sprite("Back Default", diamondPearlBackDefaultUrl),
                    Sprite("Front Default", diamondPearlFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "HeartGold & SoulSilver",
                sprites = listOf(
                    Sprite("Back Default", heartgoldSoulsilverBackDefaultUrl),
                    Sprite("Front Default", heartgoldSoulsilverFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Platinum",
                sprites = listOf(
                    Sprite("Back Default", platinumBackDefaultUrl),
                    Sprite("Front Default", platinumFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Black & White",
                sprites = listOf(
                    Sprite("Back Default", blackWhiteBackDefaultUrl),
                    Sprite("Front Default", blackWhiteFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Black & White - Animated",
                sprites = listOf(
                    Sprite("Back Default", blackWhiteBackDefaultAnimatedUrl),
                    Sprite("Front Default", blackWhiteFrontDefaultAnimatedUrl),
                ),
            ),
            SpriteGroup(
                name = "Omega Ruby & Alpha Sapphire",
                sprites = listOf(
                    Sprite("Back Default", omegarubyAlphasapphireBackDefaultUrl),
                    Sprite("Front Default", omegarubyAlphasapphireFrontDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "X & Y",
                sprites = listOf(
                    Sprite("Back Default", xYBackDefaultUrl),
                ),
            ),
            SpriteGroup(
                name = "Ultra Sun & Ultra Moon",
                sprites = listOf(
                    Sprite("Back Default", ultrasunUltramoonFrontDefault),
                ),
            ),
        )
    }
}
