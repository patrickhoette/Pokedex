package com.patrickhoette.pokemon.source.detail.mapper

import com.patrickhoette.core.utils.extension.toSimpleTitleCase
import com.patrickhoette.pokedex.entity.pokemon.Sprite
import com.patrickhoette.pokedex.entity.pokemon.SpriteGroup
import com.patrickhoette.pokemon.source.detail.response.PokemonDetailResponse
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.annotation.Factory

@Factory
class PokemonSpriteResponseMapper {

    @Suppress("LongMethod", "SwallowedException")
    fun mapSpriteGroups(response: PokemonDetailResponse) = buildList {
        val root = response.sprites
        val others = root[KeyOther]
        val versions = root[KeyVersions]

        add(
            SpriteGroup(
                name = DefaultSpriteGroup,
                sprites = parseSprites(
                    jsonObject = root,
                    groupName = DefaultSpriteGroup,
                    pokemonName = response.name,
                ),
            )
        )

        val otherGroups = try {
            others?.jsonObject?.let { jsonObject ->
                parseSpriteGroups(jsonObject = jsonObject, pokemonName = response.name) { it.toSimpleTitleCase() }
            }
        } catch (error: Throwable) {
            Napier.e("Failed to parse other sprite group for ${response.name}")
            null
        }

        otherGroups?.let(::addAll)

        val generations = try {
            versions?.jsonObject?.let { jsonObject ->
                jsonObject.mapNotNull { (key, value) ->
                    try {
                        value.jsonObject
                    } catch (error: Throwable) {
                        Napier.e("Failed to parse generation $key for ${response.name}")
                        null
                    }
                }
            }
        } catch (error: Throwable) {
            Napier.e("Failed to parse versions group for ${response.name}")
            null
        }

        generations
            ?.mapNotNull { jsonObject ->
                parseSpriteGroups(
                    jsonObject = jsonObject,
                    pokemonName = response.name,
                    nameTransform = ::transformVersionGroupName,
                )
            }
            ?.flatten()
            ?.let(::addAll)
    }

    @Suppress("LongMethod", "SwallowedException")
    private fun parseSpriteGroups(
        jsonObject: JsonObject,
        pokemonName: String,
        nameTransform: (String) -> String,
    ) = jsonObject
        .filterKeys { it != KeyIcons }
        .mapNotNull { (key, value) ->
            val groupName = nameTransform(key)

            val valueObject = try {
                value.jsonObject
            } catch (error: Throwable) {
                Napier.e("Failed to parse sprite group $groupName for $pokemonName")
                null
            }

            if (valueObject == null) return@mapNotNull null

            val sprites = parseSprites(
                jsonObject = valueObject,
                groupName = groupName,
                pokemonName = pokemonName,
            ).takeUnless { it.isEmpty() }

            val animatedObject = try {
                valueObject[KeyAnimated]?.jsonObject
            } catch (error: Throwable) {
                Napier.e("Failed to parse animated sprites for $groupName of $pokemonName")
                null
            }
            val animatedGroupName = "$groupName - Animated"
            val animatedSprites = animatedObject
                ?.let {
                    parseSprites(jsonObject = animatedObject, groupName = animatedGroupName, pokemonName = pokemonName)
                }
                ?.takeUnless { it.isEmpty() }

            listOfNotNull(
                sprites?.let { SpriteGroup(name = groupName, sprites = it) },
                animatedSprites?.let { SpriteGroup(name = animatedGroupName, sprites = it) },
            )
        }
        .flatten()
        .takeUnless { it.isEmpty() }

    @Suppress("SwallowedException")
    private fun parseSprites(
        jsonObject: JsonObject,
        groupName: String,
        pokemonName: String,
    ) = jsonObject
        .filterKeys { it != KeyOther && it != KeyVersions && it != KeyAnimated }
        .mapNotNull { (key, value) ->
            val spriteName = key.toSimpleTitleCase()
            val parsedValue = try {
                value.jsonPrimitive.contentOrNull
            } catch (error: Throwable) {
                Napier.d("Failed to parse sprite $groupName/$spriteName for $pokemonName")
                null
            }
            parsedValue?.let { Sprite(name = spriteName, url = it) }
        }

    private fun transformVersionGroupName(original: String): String = when (original) {
        "red-blue" -> "Red & Blue"
        "firered-leafgreen" -> "Fire Red & Leaf Green"
        "ruby-sapphire" -> "Ruby & Sapphire"
        "diamond-pearl" -> "Diamond & Pearl"
        "heartgold-soulsilver" -> "HeartGold & SoulSilver"
        "black-white" -> "Black & White"
        "omegaruby-alphasapphire" -> "Omega Ruby & Alpha Sapphire"
        "x-y" -> "X & Y"
        "ultra-sun-ultra-moon" -> "Ultra Sun & Ultra Moon"
        else -> original.toSimpleTitleCase()
    }

    companion object {

        private const val DefaultSpriteGroup = "Default"

        private const val KeyOther = "other"
        private const val KeyVersions = "versions"
        private const val KeyAnimated = "animated"
        private const val KeyIcons = "icons"
    }
}
