package com.patrickhoette.core.store.util

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import app.cash.turbine.test
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertIs
import com.patrickhoette.test.assertNull
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
class ObservableSharedPreferencesTest {

    private val listenerSlot = slot<OnSharedPreferenceChangeListener>()

    private val editor: Editor = mockk {
        every { commit() } returns true
    }

    private val initialKey = "Initial Key"
    private val initialValue = "Initial Value"
    private val initialMap = mapOf(initialKey to initialValue)

    private val sharedPrefs: SharedPreferences = mockk {
        every { all } returns initialMap
        every { registerOnSharedPreferenceChangeListener(capture(listenerSlot)) } just runs
        every { edit() } answers { editor }
    }

    @InjectMockKs
    private lateinit var observableSharedPrefs: ObservableSharedPreferences

    @Test
    fun `When initialized, then observe all should emit initial map`() = runTest {
        // When
        observableSharedPrefs.observeAll().test {
            // Then
            awaitItem() assertEquals initialMap
        }
    }

    @Test
    fun `Given key is null, when listener gets called, then observe all should emit empty map`() = runTest {
        // When
        observableSharedPrefs.observeAll().test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, null)

            // Then
            awaitItem() assertEquals emptyMap()
        }
    }

    @Test
    fun `Given key is not null, when listener gets called, then observe all should emit all shared preferences entries`() =
        runTest {
            // Given
            val newMap = mapOf("new key" to "new value")
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeAll().test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, "new key")

                // Then
                awaitItem() assertEquals newMap
            }
        }

    @Test
    fun `Given key does not exists, when observing string, then return null`() = runTest {
        // Given
        val key = "new key"

        // When
        observableSharedPrefs.observeString(key).test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given value is not a string, when observing string, then emit illegal state exception`() = runTest {
        // Given
        val key = "new key"
        val newMap = mapOf(key to 190)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeString(key).test {
            // Then
            awaitError() assertIs IllegalStateException::class
        }
    }

    @Test
    fun `Given key exists, when observing string, then emit value`() = runTest {
        // Given
        val key = "new key"
        val value = "new value"
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeString(key).test {
            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in, when observing string, then emit new value`() = runTest {
        // Given
        val key = "new key"
        val value = "new value"
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap

        // When
        observableSharedPrefs.observeString(key).test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in but of a different type, when observing string, then emit Illegal state exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = 10023
            val newMap = mapOf(key to value)
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeString(key).test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

                // Then
                awaitError() assertIs IllegalStateException::class
            }
        }

    @Test
    fun `Given key does not exists, when observing string set, then return null`() = runTest {
        // Given
        val key = "new key"

        // When
        observableSharedPrefs.observeStringSet(key).test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given value is not a string set, when observing string set, then emit illegal state exception`() = runTest {
        // Given
        val key = "new key"
        val newMap = mapOf(key to 190)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeStringSet(key).test {
            // Then
            awaitError() assertIs IllegalStateException::class
        }
    }

    @Test
    fun `Given key exists, when observing string set, then emit value`() = runTest {
        // Given
        val key = "new key"
        val value = setOf("new value")
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeStringSet(key).test {
            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in, when observing string set, then emit new value`() = runTest {
        // Given
        val key = "new key"
        val value = setOf("new value")
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap

        // When
        observableSharedPrefs.observeStringSet(key).test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in but of a different type, when observing string set, then emit Illegal state exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            val newMap = mapOf(key to value)
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeStringSet(key).test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

                // Then
                awaitError() assertIs IllegalStateException::class
            }
        }

    @Test
    fun `Given key does not exists, when observing boolean, then return null`() = runTest {
        // Given
        val key = "new key"

        // When
        observableSharedPrefs.observeBoolean(key).test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given value is not a boolean, when observing boolean, then emit illegal state exception`() = runTest {
        // Given
        val key = "new key"
        val newMap = mapOf(key to 190)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeBoolean(key).test {
            // Then
            awaitError() assertIs IllegalStateException::class
        }
    }

    @Test
    fun `Given key exists, when observing boolean, then emit value`() = runTest {
        // Given
        val key = "new key"
        val value = true
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeBoolean(key).test {
            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in, when observing boolean, then emit new value`() = runTest {
        // Given
        val key = "new key"
        val value = true
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap

        // When
        observableSharedPrefs.observeBoolean(key).test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in but of a different type, when observing boolean, then emit Illegal state exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            val newMap = mapOf(key to value)
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeBoolean(key).test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

                // Then
                awaitError() assertIs IllegalStateException::class
            }
        }

    @Test
    fun `Given key does not exists, when observing long, then return null`() = runTest {
        // Given
        val key = "new key"

        // When
        observableSharedPrefs.observeLong(key).test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given value is not a long, when observing long, then emit illegal state exception`() = runTest {
        // Given
        val key = "new key"
        val newMap = mapOf(key to "new value")
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeLong(key).test {
            // Then
            awaitError() assertIs IllegalStateException::class
        }
    }

    @Test
    fun `Given key exists, when observing long, then emit value`() = runTest {
        // Given
        val key = "new key"
        val value = 100L
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeLong(key).test {
            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in, when observing long, then emit new value`() = runTest {
        // Given
        val key = "new key"
        val value = 100L
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap

        // When
        observableSharedPrefs.observeLong(key).test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in but of a different type, when observing long, then emit Illegal state exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            val newMap = mapOf(key to value)
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeLong(key).test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

                // Then
                awaitError() assertIs IllegalStateException::class
            }
        }

    @Test
    fun `Given key does not exists, when observing int, then return null`() = runTest {
        // Given
        val key = "new key"

        // When
        observableSharedPrefs.observeInt(key).test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given value is not a int, when observing int, then emit illegal state exception`() = runTest {
        // Given
        val key = "new key"
        val newMap = mapOf(key to "new value")
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeInt(key).test {
            // Then
            awaitError() assertIs IllegalStateException::class
        }
    }

    @Test
    fun `Given key exists, when observing int, then emit value`() = runTest {
        // Given
        val key = "new key"
        val value = 102
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeInt(key).test {
            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in, when observing int, then emit new value`() = runTest {
        // Given
        val key = "new key"
        val value = 102
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap

        // When
        observableSharedPrefs.observeInt(key).test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in but of a different type, when observing int, then emit Illegal state exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            val newMap = mapOf(key to value)
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeInt(key).test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

                // Then
                awaitError() assertIs IllegalStateException::class
            }
        }

    @Test
    fun `Given key does not exists, when observing float, then return null`() = runTest {
        // Given
        val key = "new key"

        // When
        observableSharedPrefs.observeFloat(key).test {
            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `Given value is not a float, when observing float, then emit illegal state exception`() = runTest {
        // Given
        val key = "new key"
        val newMap = mapOf(key to "new value")
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeFloat(key).test {
            // Then
            awaitError() assertIs IllegalStateException::class
        }
    }

    @Test
    fun `Given key exists, when observing float, then emit value`() = runTest {
        // Given
        val key = "new key"
        val value = 2332F
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeFloat(key).test {
            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in, when observing float, then emit new value`() = runTest {
        // Given
        val key = "new key"
        val value = 2332F
        val newMap = mapOf(key to value)
        every { sharedPrefs.all } returns newMap

        // When
        observableSharedPrefs.observeFloat(key).test {
            skipItems(1)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // Then
            awaitItem() assertEquals value
        }
    }

    @Test
    fun `Given value for key is put in but of a different type, when observing float, then emit Illegal state exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            val newMap = mapOf(key to value)
            every { sharedPrefs.all } returns newMap

            // When
            observableSharedPrefs.observeFloat(key).test {
                skipItems(1)
                listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

                // Then
                awaitError() assertIs IllegalStateException::class
            }
        }

    @Test
    fun `When cleared, then clear shared prefs`() = runTest {
        // Given
        every { editor.clear() } returns editor

        // When
        observableSharedPrefs.clear()

        // Then
        verify { editor.clear() }
    }

    @Test
    fun `When cleared and observing all, then emit empty map`() = runTest {
        // Given
        every { editor.clear() } returns editor

        // When
        observableSharedPrefs.observeAll().test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem() assertEquals emptyMap()
        }
    }

    @Test
    fun `When cleared and observing string, then emit null`() = runTest {
        // Given
        val key = "new key"
        val value = "new value"
        every { sharedPrefs.all } returns mapOf(key to value)
        every { editor.clear() } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeString(key).test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `When cleared and observing string set, then emit null`() = runTest {
        // Given
        val key = "new key"
        val value = setOf("new value")
        every { sharedPrefs.all } returns mapOf(key to value)
        every { editor.clear() } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeStringSet(key).test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `When cleared and observing boolean, then emit null`() = runTest {
        // Given
        val key = "new key"
        val value = true
        every { sharedPrefs.all } returns mapOf(key to value)
        every { editor.clear() } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeBoolean(key).test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `When cleared and observing long, then emit null`() = runTest {
        // Given
        val key = "new key"
        val value = 100L
        every { sharedPrefs.all } returns mapOf(key to value)
        every { editor.clear() } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeLong(key).test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `When cleared and observing int, then emit null`() = runTest {
        // Given
        val key = "new key"
        val value = 2343
        every { sharedPrefs.all } returns mapOf(key to value)
        every { editor.clear() } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeInt(key).test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `When cleared and observing float, then emit null`() = runTest {
        // Given
        val key = "new key"
        val value = 123234F
        every { sharedPrefs.all } returns mapOf(key to value)
        every { editor.clear() } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.observeFloat(key).test {
            skipItems(1)
            observableSharedPrefs.clear()

            // Then
            awaitItem().assertNull()
        }
    }

    @Test
    fun `When key is removed, then remove key from shared prefs`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.remove(key)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given value is null, when putting string, then remove key`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.putString(key, null)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given key doesn't exist yet and value is not null, when putting in string, then commit string for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            every { editor.putString(key, value) } returns editor

            // When
            observableSharedPrefs.putString(key, value)

            // Then
            verify { editor.putString(key, value) }
        }

    @Test
    fun `Given key exists and current value is null, when putting string, then commit string for key`() = runTest {
        // Given
        val key = "new key"
        val value = "new value"
        every { sharedPrefs.all } returns mapOf(key to null)
        every { editor.putString(key, value) } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.putString(key, value)

        // Then
        verify { editor.putString(key, value) }
    }

    @Test
    fun `Given key exists but current value is a different type, when putting string, then throw illegal argument exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = "new value"
            every { sharedPrefs.all } returns mapOf(key to 12324)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When -> Then
            assertFailsWith<IllegalArgumentException> { observableSharedPrefs.putString(key, value) }
        }

    @Test
    fun `Given value is null, when putting string set, then remove key`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.putStringSet(key, null)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given key doesn't exist yet and value is not null, when putting in string set, then commit string set for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = setOf("new value")
            every { editor.putStringSet(key, value) } returns editor

            // When
            observableSharedPrefs.putStringSet(key, value)

            // Then
            verify { editor.putStringSet(key, value) }
        }

    @Test
    fun `Given key exists and current value is null, when putting string set, then commit string set for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = setOf("new value")
            every { sharedPrefs.all } returns mapOf(key to null)
            every { editor.putStringSet(key, value) } returns editor
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When
            observableSharedPrefs.putStringSet(key, value)

            // Then
            verify { editor.putStringSet(key, value) }
        }

    @Test
    fun `Given key exists but current value is a different type, when putting string set, then throw illegal argument exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = setOf("new value")
            every { sharedPrefs.all } returns mapOf(key to 12324)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When -> Then
            assertFailsWith<IllegalArgumentException> { observableSharedPrefs.putStringSet(key, value) }
        }

    @Test
    fun `Given value is null, when putting boolean, then remove key`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.putBoolean(key, null)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given key doesn't exist yet and value is not null, when putting in boolean, then commit boolean for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = true
            every { editor.putBoolean(key, value) } returns editor

            // When
            observableSharedPrefs.putBoolean(key, value)

            // Then
            verify { editor.putBoolean(key, value) }
        }

    @Test
    fun `Given key exists and current value is null, when putting boolean, then commit boolean for key`() = runTest {
        // Given
        val key = "new key"
        val value = true
        every { sharedPrefs.all } returns mapOf(key to null)
        every { editor.putBoolean(key, value) } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.putBoolean(key, value)

        // Then
        verify { editor.putBoolean(key, value) }
    }

    @Test
    fun `Given key exists but current value is a different type, when putting boolean, then throw illegal argument exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = true
            every { sharedPrefs.all } returns mapOf(key to 12324)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When -> Then
            assertFailsWith<IllegalArgumentException> { observableSharedPrefs.putBoolean(key, value) }
        }

    @Test
    fun `Given value is null, when putting long, then remove key`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.putLong(key, null)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given key doesn't exist yet and value is not null, when putting in long, then commit long for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = 1342L
            every { editor.putLong(key, value) } returns editor

            // When
            observableSharedPrefs.putLong(key, value)

            // Then
            verify { editor.putLong(key, value) }
        }

    @Test
    fun `Given key exists and current value is null, when putting long, then commit long for key`() = runTest {
        // Given
        val key = "new key"
        val value = 1423L
        every { sharedPrefs.all } returns mapOf(key to null)
        every { editor.putLong(key, value) } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.putLong(key, value)

        // Then
        verify { editor.putLong(key, value) }
    }

    @Test
    fun `Given key exists but current value is a different type, when putting long, then throw illegal argument exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = 1234L
            every { sharedPrefs.all } returns mapOf(key to 12324)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When -> Then
            assertFailsWith<IllegalArgumentException> { observableSharedPrefs.putLong(key, value) }
        }

    @Test
    fun `Given value is null, when putting int, then remove key`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.putInt(key, null)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given key doesn't exist yet and value is not null, when putting in int, then commit int for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = 123
            every { editor.putInt(key, value) } returns editor

            // When
            observableSharedPrefs.putInt(key, value)

            // Then
            verify { editor.putInt(key, value) }
        }

    @Test
    fun `Given key exists and current value is null, when putting int, then commit int for key`() = runTest {
        // Given
        val key = "new key"
        val value = 342
        every { sharedPrefs.all } returns mapOf(key to null)
        every { editor.putInt(key, value) } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.putInt(key, value)

        // Then
        verify { editor.putInt(key, value) }
    }

    @Test
    fun `Given key exists but current value is a different type, when putting int, then throw illegal argument exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = 463
            every { sharedPrefs.all } returns mapOf(key to "value")
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When -> Then
            assertFailsWith<IllegalArgumentException> { observableSharedPrefs.putInt(key, value) }
        }

    @Test
    fun `Given value is null, when putting float, then remove key`() = runTest {
        // Given
        val key = "key to remove"
        every { editor.remove(key) } returns editor

        // When
        observableSharedPrefs.putFloat(key, null)

        // Then
        verify { editor.remove(key) }
    }

    @Test
    fun `Given key doesn't exist yet and value is not null, when putting in float, then commit float for key`() =
        runTest {
            // Given
            val key = "new key"
            val value = 23425F
            every { editor.putFloat(key, value) } returns editor

            // When
            observableSharedPrefs.putFloat(key, value)

            // Then
            verify { editor.putFloat(key, value) }
        }

    @Test
    fun `Given key exists and current value is null, when putting float, then commit float for key`() = runTest {
        // Given
        val key = "new key"
        val value = 124F
        every { sharedPrefs.all } returns mapOf(key to null)
        every { editor.putFloat(key, value) } returns editor
        listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

        // When
        observableSharedPrefs.putFloat(key, value)

        // Then
        verify { editor.putFloat(key, value) }
    }

    @Test
    fun `Given key exists but current value is a different type, when putting float, then throw illegal argument exception`() =
        runTest {
            // Given
            val key = "new key"
            val value = 2534F
            every { sharedPrefs.all } returns mapOf(key to 12324)
            listenerSlot.captured.onSharedPreferenceChanged(sharedPrefs, key)

            // When -> Then
            assertFailsWith<IllegalArgumentException> { observableSharedPrefs.putFloat(key, value) }
        }
}
