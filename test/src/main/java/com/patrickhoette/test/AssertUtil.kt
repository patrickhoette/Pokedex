@file:Suppress("unused")

package com.patrickhoette.test

import com.patrickhoette.test.model.TestException
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.asserter
import kotlin.test.fail

private fun messagePrefix(message: String?) = if (message == null) "" else "$message. "

/**
 * Opposite of the [kotlin.test.assertFails] function.
 */
inline fun assertJustRuns(block: () -> Unit) = checkResultIsSuccess(null, runCatching(block))

/**
 * Opposite of the [kotlin.test.assertFails] function.
 */
inline fun assertJustRuns(message: String?, block: () -> Unit) = checkResultIsSuccess(message, runCatching(block))

/**
 * Opposite of the [kotlin.test.assertFails] function.
 */
inline fun assertSucceeds(block: () -> Unit) = checkResultIsSuccess(null, runCatching(block))

/**
 * Opposite of the [kotlin.test.assertFails] function.
 */
inline fun assertSucceeds(message: String?, block: () -> Unit) = checkResultIsSuccess(message, runCatching(block))

@PublishedApi
internal fun checkResultIsSuccess(message: String?, blockResult: Result<Unit>) {
    blockResult.fold(
        onSuccess = { return },
        onFailure = { asserter.fail(messagePrefix(message) + "Expected call to succeed, but failed instead", it) }
    )
}

/**
 * DSL version of [kotlin.test.assertEquals].
 */
infix fun <T> T.assertEquals(other: T) = kotlin.test.assertEquals(expected = other, actual = this)

/**
 * DSL version of [kotlin.test.assertNotEquals].
 */
infix fun <T> T.assertNotEquals(other: T) = kotlin.test.assertNotEquals(illegal = other, actual = this)

/**
 * DSL version of [kotlin.test.assertSame].
 */
infix fun <T> T.assertSame(other: T) = kotlin.test.assertSame(expected = other, actual = this)

/**
 * DSL version of [kotlin.test.assertNotSame].
 */
infix fun <T> T.assertNotSame(other: T) = kotlin.test.assertNotSame(illegal = other, actual = this)

/**
 * DSL version of [kotlin.test.assertIs].
 */
inline infix fun <reified T : Any> Any.assertIs(type: KClass<T>) = kotlin.test.assertIs<T>(this)

/**
 * DSL version of [kotlin.test.assertIsNot].
 */
inline infix fun <reified T : Any> Any.assertIsNot(type: KClass<T>) = kotlin.test.assertIsNot<T>(this)

/**
 * See [assertThrows].
 */
inline fun assertFailsWith(error: Throwable, block: () -> Unit) = assertThrows(error = error, block = block)

/**
 * Checks all assertions before failing the test and outputs a report of all failed [assertions].
 */
fun assertAll(assertions: Iterable<() -> Unit>, heading: String? = null) {
    val failures = assertions.mapNotNull {
        try {
            it()
            null
        } catch (error: Throwable) {
            if (error is OutOfMemoryError) throw error else error
        }
    }
    if (failures.isNotEmpty()) {
        val message = buildString {
            append(heading ?: "Some assertions failed.")
            append(" (${failures.size}/${assertions.toList().size} failed)")
            append("\n")
            for ((index, failure) in failures.withIndex()) {
                appendLine("[$index]")
                appendLine(failure.message)
            }
        }

        fail(message)
    }
}

/**
 * Assert that [Throwable] throw in [block] is [equal to][Any.equals] the given [error].
 */
inline fun assertThrows(error: Throwable, block: () -> Unit) = try {
    block()
    fail("Expected error to be thrown but block didn't throw anything")
} catch (thrownError: Throwable) {
    thrownError assertSame error
}

/**
 * Assert that the [Throwable] throw in [block] is a [TestException].
 */
inline fun assertTestException(block: () -> Unit) = assertFailsWith(
    exceptionClass = TestException::class,
    block = block,
)

/**
 * DSL version of [kotlin.test.assertTrue].
 */
fun Boolean.assertTrue() = assertTrue(this)

/**
 * DSL version of [kotlin.test.assertTrue].
 */
fun (() -> Boolean).assertTrue() = assertTrue(block = this)

private const val MessageNotAllTrue = "Not all values are true"

/**
 * DSL version of [kotlin.test.assertTrue].
 */
fun BooleanArray.assertAllTrue() = assertTrue(
    actual = all { it },
    message = toList().suffixIndices(base = MessageNotAllTrue, illegal = false),
)

/**
 * DSL version of [kotlin.test.assertTrue].
 */
fun Iterable<Boolean>.assertAllTrue() = assertTrue(
    actual = all { it },
    message = suffixIndices(base = MessageNotAllTrue, illegal = false),
)

/**
 * DSL version of [kotlin.test.assertFalse].
 */
fun Boolean.assertFalse() = kotlin.test.assertFalse(this)

/**
 * DSL version of [kotlin.test.assertFalse].
 */
fun (() -> Boolean).assertFalse() = kotlin.test.assertFalse(block = this)

private const val MessageNotAllFalse = "Not all values are false"

/**
 * DSL version of [kotlin.test.assertFalse].
 */
fun BooleanArray.assertAllFalse() = assertTrue(
    actual = none { it },
    message = toList().suffixIndices(base = MessageNotAllFalse, illegal = true),
)

/**
 * DSL version of [kotlin.test.assertFalse].
 */
fun Iterable<Boolean>.assertAllFalse() = assertTrue(
    actual = none { it },
    message = suffixIndices(base = MessageNotAllFalse, illegal = true),
)

/**
 * DSL version of [kotlin.test.assertNull].
 */
fun Any?.assertNull() = kotlin.test.assertNull(this)

private const val MessageNotAllNull = "Not all values are null"

/**
 * DSL version of [kotlin.test.assertNull].
 */
fun Array<Any?>.assertAllNull() = assertTrue(
    actual = all { it == null },
    message = toList().suffixIndices(MessageNotAllNull) { it != null },
)

/**
 * DSL version of [kotlin.test.assertNull].
 */
fun Iterable<Any?>.assertAllNull() = assertTrue(
    actual = all { it == null },
    message = suffixIndices(MessageNotAllNull) { it != null },
)

/**
 * DSL version of [kotlin.test.assertNotNull].
 */
fun Any?.assertNotNull() {
    kotlin.test.assertNotNull(this)
}

private const val MessageNotNoneNull = "Some values are null"

/**
 * DSL version of [kotlin.test.assertNotNull].
 */
fun Array<Any?>.assertAllNotNull() = assertTrue(
    actual = none { it == null },
    message = toList().suffixIndices(MessageNotNoneNull) { it == null },
)

/**
 * DSL version of [kotlin.test.assertNotNull].
 */
fun Array<Any?>.assertNoneNull() = assertTrue(
    actual = none { it == null },
    message = toList().suffixIndices(MessageNotNoneNull) { it == null },
)

/**
 * DSL version of [kotlin.test.assertNotNull].
 */
fun Iterable<Any?>.assertAllNotNull() = assertTrue(
    actual = none { it == null },
    message = suffixIndices(MessageNotNoneNull) { it == null },
)

/**
 * DSL version of [kotlin.test.assertNotNull].
 */
fun Iterable<Any?>.assertNoneNull() = assertTrue(
    actual = none { it == null },
    message = suffixIndices(MessageNotNoneNull) { it == null },
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun <T> Iterable<T>.assertContains(element: T) = assertTrue(
    actual = contains(element),
    message = createAssertContainsMessage(element),
)

private const val HeadingAssertContains = "Not all elements could be found."

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun <T> Iterable<T>.assertContains(elements: Iterable<T>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun <T> Sequence<T>.assertContains(element: T) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun <T> Sequence<T>.assertContains(elements: Iterable<T>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun <T> Array<T>.assertContains(element: T) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun <T> Array<T>.assertContains(elements: Iterable<T>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun ByteArray.assertContains(element: Byte) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun ByteArray.assertContains(elements: Iterable<Byte>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun ByteArray.assertContains(elements: ByteArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun ShortArray.assertContains(element: Short) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun ShortArray.assertContains(elements: Iterable<Short>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun ShortArray.assertContains(elements: ShortArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun IntArray.assertContains(element: Int) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun IntArray.assertContains(elements: Iterable<Int>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun IntArray.assertContains(elements: IntArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun LongArray.assertContains(element: Long) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun LongArray.assertContains(elements: Iterable<Long>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun LongArray.assertContains(elements: LongArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun BooleanArray.assertContains(element: Boolean) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun BooleanArray.assertContains(elements: Iterable<Boolean>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun BooleanArray.assertContains(elements: BooleanArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun CharArray.assertContains(element: Char) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun CharArray.assertContains(elements: Iterable<Char>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun CharArray.assertContains(elements: CharArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
@ExperimentalUnsignedTypes
infix fun UByteArray.assertContains(element: UByte) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UByteArray.assertContains(elements: Iterable<UByte>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UByteArray.assertContains(elements: UByteArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
@ExperimentalUnsignedTypes
infix fun UShortArray.assertContains(element: UShort) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UShortArray.assertContains(elements: Iterable<UShort>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UShortArray.assertContains(elements: UShortArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
@ExperimentalUnsignedTypes
infix fun UIntArray.assertContains(element: UInt) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UIntArray.assertContains(elements: Iterable<UInt>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UIntArray.assertContains(elements: UIntArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
@ExperimentalUnsignedTypes
infix fun ULongArray.assertContains(element: ULong) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun ULongArray.assertContains(elements: Iterable<ULong>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun ULongArray.assertContains(elements: ULongArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun IntRange.assertContains(element: Int) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun IntRange.assertContains(elements: Iterable<Int>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun IntRange.assertContains(elements: IntArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun LongRange.assertContains(element: Long) = assertTrue(
    actual = contains(element),
    message = toList().createAssertContainsMessage(element),
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun LongRange.assertContains(elements: Iterable<Long>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun LongRange.assertContains(elements: LongArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun <T : Comparable<T>> ClosedRange<T>.assertContains(element: T) =
    kotlin.test.assertContains(range = this, value = element)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun <T : Comparable<T>> ClosedRange<T>.assertContains(elements: Iterable<T>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun CharRange.assertContains(element: Char) = kotlin.test.assertContains(range = this, value = element)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun CharRange.assertContains(elements: Iterable<Char>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun CharRange.assertContains(elements: CharArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun UIntRange.assertContains(element: UInt) = kotlin.test.assertContains(range = this, value = element)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun UIntRange.assertContains(elements: Iterable<UInt>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun UIntRange.assertContains(elements: UIntArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun ULongRange.assertContains(element: ULong) = kotlin.test.assertContains(range = this, value = element)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun ULongRange.assertContains(elements: Iterable<ULong>) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
@ExperimentalUnsignedTypes
infix fun ULongRange.assertContains(elements: ULongArray) = assertAll(
    assertions = elements.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * Assert that [Map.entries] contains [pair].
 */
infix fun <K, V> Map<K, V>.assertContains(pair: Pair<K, V>) = assertTrue(
    actual = pair in entries.toPairs(),
    message = createAssertContainsMessage(pair),
)

/**
 * Assert that [Map.entries] contains [pairs].
 */
@JvmName("assertContainsPairs")
infix fun <K, V> Map<K, V>.assertContains(pairs: Iterable<Pair<K, V>>) = assertAll(
    assertions = pairs.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * Assert that [this.entries][Map.entries] contains [entry].
 */
@JvmName("assertContainsEntries")
infix fun <K, V> Map<K, V>.assertContains(entry: Map.Entry<K, V>) = assertTrue(
    actual = entry in entries,
    message = createAssertContainsMessage(entry.toPair()),
)

/**
 * Assert that [this.entries][Map.entries] contains [other.entries][Map.entries].
 */
infix fun <K, V> Map<K, V>.assertContains(other: Map<K, V>) = assertAll(
    assertions = other.entries.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * Assert that [this.entries][Map.entries] contains [entries].
 */
infix fun <K, V> Map<K, V>.assertContains(entries: Iterable<Map.Entry<K, V>>) = assertAll(
    assertions = entries.map { { assertContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun <K> Map<K, *>.assertKeysContains(key: K) = keys assertContains key

/**
 * DSL version of [kotlin.test.assertContains]. Also see [assertAll].
 */
infix fun <K> Map<K, *>.assertKeysContains(keys: Iterable<K>) = assertAll(
    assertions = keys.map { { assertKeysContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * Assert that [Map.values] contains [value]. Also see [assertContains].
 */
infix fun <V> Map<*, V>.assertValuesContains(value: V) = values assertContains value

/**
 * Assert that [Map.values] contains [values]. Also see [assertContains] and [assertAll]
 */
infix fun <V> Map<*, V>.assertValuesContains(values: Iterable<V>) = assertAll(
    assertions = values.map { { assertValuesContains(it) } },
    heading = HeadingAssertContains,
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun CharSequence.assertContains(char: Char) = assertTrue(
    actual = contains(char),
    message = "Could not find '$char' in \"$this\"\n",
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun CharSequence.assertContains(other: CharSequence) = assertTrue(
    actual = contains(other),
    message = buildString {
        appendLine("Could not find: $other")
        appendLine("In: ${this@assertContains}")
    },
)

/**
 * DSL version of [kotlin.test.assertContains].
 */
infix fun CharSequence.assertContains(regex: Regex) = assertTrue(
    actual = contains(regex),
    message = buildString {
        appendLine("Could not find: ${regex.pattern}")
        appendLine("In: ${this@assertContains}")
    }
)

/**
 * DSL version of [kotlin.test.assertContains]. Converts the [pattern] to [Regex].
 */
infix fun CharSequence.assertContains(pattern: Pattern) = assertContains(Regex(pattern.pattern()))

/**
 * Assert that `this` [Collection.isEmpty].
 */
fun <T> Collection<T>.assertEmpty() = assertTrue("Expected collection to be empty but it was:\n$this") { isEmpty() }

/**
 * Assert that `this` [Collection.isNotEmpty].
 */
fun <T> Collection<T>.assertNotEmpty() =
    assertTrue("Expected collection to contain items but it was empty") { isNotEmpty() }

private fun <T> Iterable<T>.suffixIndices(base: String, illegal: T): String = "$base, indices: ${findIndices(illegal)}"

private fun <T> Iterable<T>.suffixIndices(base: String, predicate: (T) -> Boolean): String =
    "$base, indices: ${findIndices(predicate)}"

private fun <T> Iterable<T>.findIndices(illegal: T): List<Int> = findIndices { it == illegal }

private fun <T> Iterable<T>.findIndices(predicate: (T) -> Boolean) = mapIndexedNotNull { index, value ->
    if (predicate(value)) index else null
}

private fun <T> Iterable<T>.createAssertContainsMessage(missing: T) = buildString {
    appendLine("Could not find: $missing")
    append("In collection: ${this@createAssertContainsMessage}")
}

private fun <K, V> Map<K, V>.createAssertContainsMessage(missing: Pair<K, V>) = buildString {
    appendLine("Could not find: ${missing.print()}")
    appendLine("In map:")
    for (entry in entries) appendLine(entry.print())
}

private fun <A, B> Pair<A, B>.print() = "$first => $second"

private fun <K, V> Map.Entry<K, V>.print() = toPair().print()

private fun <K, V> Map.Entry<K, V>.toPair() = key to value

private fun <K, V> Iterable<Map.Entry<K, V>>.toPairs() = map { it.toPair() }
