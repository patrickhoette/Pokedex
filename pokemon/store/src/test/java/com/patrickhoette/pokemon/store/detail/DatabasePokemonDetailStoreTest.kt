package com.patrickhoette.pokemon.store.detail

import app.cash.sqldelight.SuspendingTransactionWithoutReturn
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.turbine.test
import com.patrickhoette.core.store.extension.toLong
import com.patrickhoette.pokedex.database.ability.AbilityQueries
import com.patrickhoette.pokedex.database.move.MoveQueries
import com.patrickhoette.pokedex.database.pokemon.PokemonQueries
import com.patrickhoette.pokedex.database.species.SpeciesQueries
import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.pokemon.data.generic.model.CacheStatus.*
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertTestException
import com.patrickhoette.test.coVerifyNever
import com.patrickhoette.test.coroutine.TestDispatcherProvider
import com.patrickhoette.test.date.StaticClock
import com.patrickhoette.test.factory.pokemon.PokemonFactory
import com.patrickhoette.test.model.TestException
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration.Companion.hours
import com.patrickhoette.pokedex.database.Pokemon_full_view as PokemonFullEntry
import com.patrickhoette.pokedex.database.Sprite as SpriteEntry
import com.patrickhoette.pokedex.database.pokemon.SelectAbilitiesById as AbilityEntry
import com.patrickhoette.pokedex.database.pokemon.SelectMovesById as MoveEntry

@ExtendWith(MockKExtension::class)
class DatabasePokemonDetailStoreTest {

    @MockK
    private lateinit var pokemonQueries: PokemonQueries

    @MockK
    private lateinit var abilityQueries: AbilityQueries

    @MockK
    private lateinit var moveQueries: MoveQueries

    @MockK
    private lateinit var speciesQueries: SpeciesQueries

    @MockK
    private lateinit var mapper: PokemonDetailEntryMapper

    private val dispatchers = TestDispatcherProvider()

    private val clock = StaticClock(2025, 4, 6, 15, 10)

    @InjectMockKs
    private lateinit var store: DatabasePokemonDetailStore

    @Test
    fun `Given details are null, when storing pokemon, then do not insert abilities`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever { abilityQueries.transaction(any(), any()) }
        coVerifyNever { abilityQueries.upsert(any(), any()) }
    }

    @Test
    fun `Given details are null, when storing pokemon, then do not insert moves`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever { moveQueries.transaction(any(), any()) }
        coVerifyNever { moveQueries.upsert(any(), any()) }
    }

    @Test
    fun `Given details are null, when storing pokemon, then do not insert species`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever { speciesQueries.transaction(any(), any()) }
        coVerifyNever { speciesQueries.upsert(any(), any()) }
    }

    @Test
    fun `Given details are null, when storing pokemon, then do not insert details`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever {
            pokemonQueries.upsertPokemonDetails(
                baseExperience = any(),
                heightCm = any(),
                weightG = any(),
                cry = any(),
                speciesId = any(),
                hp = any(),
                attack = any(),
                defense = any(),
                specialAttack = any(),
                specialDefense = any(),
                speed = any(),
                primaryType = any(),
                secondaryType = any(),
                pokemonId = any(),
                lastUpdate = any(),
            )
        }
    }

    @Test
    fun `Given details are null, when storing pokemon, then do not insert pokemon abilities`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever {
            pokemonQueries.upsertPokemonAbility(
                abilityId = any(),
                isHidden = any(),
                pokemonId = any(),
            )
        }
    }

    @Test
    fun `Given details are null, when storing pokemon, then do not insert pokemon moves`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever {
            pokemonQueries.upsertPokemonMove(
                moveId = any(),
                method = any(),
                pokemonId = any(),
                level = any(),
            )
        }
    }

    @Test
    fun `Given details are null, when storing pokemon, then do not insert pokemon sprites`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerifyNever {
            pokemonQueries.upsertSprites(
                name = any(),
                url = any(),
                groupName = any(),
                pokemonId = any(),
            )
        }
    }

    @Test
    fun `Given details are null, when storing pokemon, then store pokemon entry`() = runTest {
        // Given
        val pokemon = PokemonFactory.create().copy(detail = null)
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerify { pokemonQueries.upsertPokemon(pokemon.name, clock.now().epochSeconds, pokemon.id.toLong()) }
    }

    @Test
    fun `Given details are null and storing pokemon entry fails, when storing pokemon, then throw error`() =
        runTest {
            // Given
            val pokemon = PokemonFactory.create().copy(detail = null)
            coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
                secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
            }
            coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } throws TestException

            // When -> Then
            assertTestException { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given details are present, when storing pokemon, then store abilities`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { abilityQueries.upsert(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        for (ability in pokemon.detail!!.abilities) {
            coVerify { abilityQueries.upsert(ability.name, ability.id.toLong()) }
        }
    }

    @Test
    fun `Given details are present and storing abilities fails, when storing pokemon, then throw error`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { abilityQueries.upsert(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given details are present, when storing pokemon, then store moves`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { moveQueries.upsert(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        for (move in pokemon.detail!!.moves) {
            coVerify { moveQueries.upsert(move.name, move.id.toLong()) }
        }
    }

    @Test
    fun `Given details are present and storing moves fails, when storing pokemon, then throw error`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { moveQueries.upsert(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given details are present, when storing pokemon, then store species`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { speciesQueries.upsert(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerify { speciesQueries.upsert(pokemon.detail!!.species.name, pokemon.detail!!.species.id.toLong()) }
    }

    @Test
    fun `Given details are present and storing species fails, when storing pokemon, then throw error`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { speciesQueries.upsert(any(), any()) } throws TestException

        // When -> Then
        assertTestException { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given details are present, when storing pokemon, then store details`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
        coEvery {
            pokemonQueries.upsertPokemonDetails(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
            )
        } just runs
        coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerify {
            pokemonQueries.upsertPokemonDetails(
                baseExperience = pokemon.detail!!.baseExperience.toLong(),
                heightCm = pokemon.detail!!.height.centimeter,
                weightG = pokemon.detail!!.weight.grams,
                cry = pokemon.detail!!.cry,
                speciesId = pokemon.detail!!.species.id.toLong(),
                hp = pokemon.detail!!.stats.hp.toLong(),
                attack = pokemon.detail!!.stats.attack.toLong(),
                defense = pokemon.detail!!.stats.defense.toLong(),
                specialAttack = pokemon.detail!!.stats.specialAttack.toLong(),
                specialDefense = pokemon.detail!!.stats.specialDefense.toLong(),
                speed = pokemon.detail!!.stats.speed.toLong(),
                primaryType = pokemon.types[0].name,
                secondaryType = pokemon.types[1].name,
                pokemonId = pokemon.id.toLong(),
                lastUpdate = clock.now().epochSeconds,
            )
        }
    }

    @Test
    fun `Given details are present and storing details fails, when storing pokemon, then throw error`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
        coEvery {
            pokemonQueries.upsertPokemonDetails(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
            )
        } throws TestException

        // When -> Then
        assertTestException { store.storePokemon(pokemon) }
    }

    @Test
    fun `Given details are present, when storing pokemon, then store pokemon abilities`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
        coEvery {
            pokemonQueries.upsertPokemonDetails(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
            )
        } just runs
        coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        for (ability in pokemon.detail!!.abilities) {
            coVerify {
                pokemonQueries.upsertPokemonAbility(ability.id.toLong(), ability.isHidden.toLong(), pokemon.id.toLong())
            }
        }
    }

    @Test
    fun `Given details are present and storing pokemon abilities fails, when storing pokemon, then throw error`() =
        runTest {
            // Given
            val pokemon = PokemonFactory.create()
            coEvery { abilityQueries.transaction(any(), any()) } just runs
            coEvery { moveQueries.transaction(any(), any()) } just runs
            coEvery { speciesQueries.transaction(any(), any()) } just runs
            coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
                secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
            }
            coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
            coEvery {
                pokemonQueries.upsertPokemonDetails(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } just runs
            coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } throws TestException
            coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
            coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

            // When -> Then
            assertTestException { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given details are present, when storing pokemon, then store pokemon moves`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
        coEvery {
            pokemonQueries.upsertPokemonDetails(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
            )
        } just runs
        coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        for (move in pokemon.detail!!.moves) {
            coVerify {
                pokemonQueries.upsertPokemonMove(
                    move.id.toLong(),
                    move.method.name,
                    move.level.toLong(),
                    pokemon.id.toLong(),
                )
            }
        }
    }

    @Test
    fun `Given details are present and storing pokemon moves fails, when storing pokemon, then throw error`() =
        runTest {
            // Given
            val pokemon = PokemonFactory.create()
            coEvery { abilityQueries.transaction(any(), any()) } just runs
            coEvery { moveQueries.transaction(any(), any()) } just runs
            coEvery { speciesQueries.transaction(any(), any()) } just runs
            coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
                secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
            }
            coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
            coEvery {
                pokemonQueries.upsertPokemonDetails(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } just runs
            coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
            coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } throws TestException
            coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

            // When -> Then
            assertTestException { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given details are present, when storing pokemon, then store pokemon sprites`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
        coEvery {
            pokemonQueries.upsertPokemonDetails(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
            )
        } just runs
        coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        for (group in pokemon.detail!!.sprites) {
            for (sprite in group.sprites) {
                coVerify { pokemonQueries.upsertSprites(sprite.name, sprite.url, group.name, pokemon.id.toLong()) }
            }
        }
    }

    @Test
    fun `Given details are present and storing pokemon sprites fails, when storing pokemon, then throw error`() =
        runTest {
            // Given
            val pokemon = PokemonFactory.create()
            coEvery { abilityQueries.transaction(any(), any()) } just runs
            coEvery { moveQueries.transaction(any(), any()) } just runs
            coEvery { speciesQueries.transaction(any(), any()) } just runs
            coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
                secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
            }
            coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
            coEvery {
                pokemonQueries.upsertPokemonDetails(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } just runs
            coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
            coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
            coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } throws TestException

            // When -> Then
            assertTestException { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given details are present, when storing pokemon, then store pokemon entry`() = runTest {
        // Given
        val pokemon = PokemonFactory.create()
        coEvery { abilityQueries.transaction(any(), any()) } just runs
        coEvery { moveQueries.transaction(any(), any()) } just runs
        coEvery { speciesQueries.transaction(any(), any()) } just runs
        coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
            secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
        }
        coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } just runs
        coEvery {
            pokemonQueries.upsertPokemonDetails(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
            )
        } just runs
        coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
        coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

        // When
        store.storePokemon(pokemon)

        // Then
        coVerify { pokemonQueries.upsertPokemon(pokemon.name, clock.now().epochSeconds, pokemon.id.toLong()) }
    }

    @Test
    fun `Given details are present but storing pokemon entry fails, when storing pokemon, then throw error`() =
        runTest {
            // Given
            val pokemon = PokemonFactory.create()
            coEvery { abilityQueries.transaction(any(), any()) } just runs
            coEvery { moveQueries.transaction(any(), any()) } just runs
            coEvery { speciesQueries.transaction(any(), any()) } just runs
            coEvery { pokemonQueries.transaction(any(), any()) } coAnswers {
                secondArg<suspend SuspendingTransactionWithoutReturn.() -> Unit>().invoke(mockk())
            }
            coEvery { pokemonQueries.upsertPokemon(any(), any(), any()) } throws TestException
            coEvery {
                pokemonQueries.upsertPokemonDetails(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } just runs
            coEvery { pokemonQueries.upsertPokemonAbility(any(), any(), any()) } just runs
            coEvery { pokemonQueries.upsertPokemonMove(any(), any(), any(), any()) } just runs
            coEvery { pokemonQueries.upsertSprites(any(), any(), any(), any()) } just runs

            // When -> Then
            assertTestException { store.storePokemon(pokemon) }
        }

    @Test
    fun `Given checking if detail is in database fails, when getting pokemon status, then throw error`() = runTest {
        // Given
        val id = 8
        coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } throws TestException

        // When -> Then
        assertTestException { store.getPokemonStatus(id) }
    }

    @Test
    fun `Given checking if detail is in database returns null, when getting pokemon status, then return missing`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val id = 8
            coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns null
            }

            // When
            val result = store.getPokemonStatus(id)

            // Then
            result assertEquals Missing
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given checking if detail is in database returns false, when getting pokemon status, then return missing`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val id = 8
            coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns false
            }

            // When
            val result = store.getPokemonStatus(id)

            // Then
            result assertEquals Missing
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given detail exists but getting last detail fails, when getting pokemon status, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        val id = 8
        coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } returns mockk {
            coEvery { awaitAsOneOrNull() } returns true
        }
        coEvery { pokemonQueries.getLastDetailUpdated(id.toLong()) } throws TestException

        // When -> Then
        assertTestException { store.getPokemonStatus(id) }

        unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
    }

    @Test
    fun `Given detail exists but getting last detail returns null, when getting pokemon status, then return stale`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val id = 8
            coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns true
            }
            coEvery { pokemonQueries.getLastDetailUpdated(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns null
            }

            // When
            val result = store.getPokemonStatus(id)

            // Then
            result assertEquals Stale
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given detail exists and last updated was less than 6 hours ago, when getting pokemon status, then return available`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val id = 8
            coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns true
            }
            coEvery { pokemonQueries.getLastDetailUpdated(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns clock.now().minus(3.hours).epochSeconds
            }

            // When
            val result = store.getPokemonStatus(id)

            // Then
            result assertEquals Available
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given detail exists and last updated was more than 6 hours ago, when getting pokemon status, then return stale`() =
        runTest {
            // Given
            mockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
            val id = 8
            coEvery { pokemonQueries.isDetailInDatabase(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns true
            }
            coEvery { pokemonQueries.getLastDetailUpdated(id.toLong()) } returns mockk {
                coEvery { awaitAsOneOrNull() } returns clock.now().minus(7.hours).epochSeconds
            }

            // When
            val result = store.getPokemonStatus(id)

            // Then
            result assertEquals Stale
            unmockkStatic("app.cash.sqldelight.async.coroutines.QueryExtensionsKt")
        }

    @Test
    fun `Given observing full fails, when observing pokemon, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } throws TestException
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }

        // When -> Then
        assertTestException { store.observePokemon(id) }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing pokemon abilities fails, when observing pokemon, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } throws TestException
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }

        // When -> Then
        assertTestException { store.observePokemon(id) }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing pokemon moves fails, when observing pokemon, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } throws TestException
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }

        // When -> Then
        assertTestException { store.observePokemon(id) }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing pokemon sprites fails, when observing pokemon, then throw error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } throws TestException

        // When -> Then
        assertTestException { store.observePokemon(id) }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing full emits error, when observing pokemon, then emit error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flow { throw TestException }
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }

        // When
        store.observePokemon(id).test {
            awaitError() assertEquals TestException
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing pokemon abilities emits error, when observing pokemon, then emit error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flow { throw TestException }
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }

        // When
        store.observePokemon(id).test {
            awaitError() assertEquals TestException
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing pokemon moves emits error, when observing pokemon, then emit error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flow { throw TestException }
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }

        // When
        store.observePokemon(id).test {
            awaitError() assertEquals TestException
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing pokemon sprites emits error, when observing pokemon, then emit error`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flowOf()
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns flow { throw TestException }
        }

        // When
        store.observePokemon(id).test {
            awaitError() assertEquals TestException
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `Given observing full emits null, when observing pokemon, then emit null`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToOneOrNull(any()) } returns flowOf(null)
            }
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(emptyList())
            }
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(emptyList())
            }
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(emptyList())
            }
        }

        // When
        store.observePokemon(id).test {
            awaitItem() assertEquals null
            awaitComplete()
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }

    @Test
    fun `When observing pokemon, then emit mapped model`() = runTest {
        // Given
        mockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
        val id = 13
        val full = PokemonFullEntry(
            id = id.toLong(),
            name = "charizard",
            lastUpdate = 9000L,
            baseExperience = null,
            heightCm = null,
            weightG = null,
            cry = null,
            hp = null,
            attack = null,
            defense = null,
            specialAttack = null,
            specialDefense = null,
            speed = null,
            primaryType = null,
            secondaryType = null,
            speciesId = null,
            speciesName = null,
        )
        val abilities = listOf(
            AbilityEntry(
                id = 9L,
                name = "ability",
                isHidden = 0L,
            )
        )
        val moves = listOf(
            MoveEntry(
                id = 91L,
                name = "move",
                method = MoveMethod.Machine.name,
                level = 90L,
            )
        )
        val sprites = listOf(
            SpriteEntry(
                pokemonId = id.toLong(),
                name = "sprite",
                url = "sprite url",
                groupName = "sprite group name",
            )
        )
        val pokemon = PokemonFactory.create()
        every { pokemonQueries.selectFullById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToOneOrNull(any()) } returns flowOf(full)
            }
        }
        every { pokemonQueries.selectAbilitiesById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(abilities)
            }
        }
        every { pokemonQueries.selectMovesById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(moves)
            }
        }
        every { pokemonQueries.selectSpritesById(id.toLong()) } returns mockk {
            every { asFlow() } returns mockk {
                every { mapToList(any()) } returns flowOf(sprites)
            }
        }
        every { mapper.mapToModel(full, abilities, moves, sprites) } returns pokemon

        // When
        store.observePokemon(id).test {
            awaitItem() assertEquals pokemon
            awaitComplete()
        }
        unmockkStatic("app.cash.sqldelight.coroutines.FlowQuery")
    }
}
