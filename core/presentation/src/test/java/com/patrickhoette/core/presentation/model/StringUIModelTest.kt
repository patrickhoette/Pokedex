package com.patrickhoette.core.presentation.model

import com.patrickhoette.core.presentation.model.StringUIModel.*
import com.patrickhoette.core.presentation.model.StringUIModelDSL.buildStringUIModel
import com.patrickhoette.core.presentation.model.StringUIModelDSL.plus
import com.patrickhoette.test.assertEquals
import com.patrickhoette.test.assertIs
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class StringUIModelTest {

    @Test
    fun `Res iterable constructor`() {
        // Given
        val id = 0
        val argOne = "Some argument who knows"
        val argTwo = 23
        val argThree = Raw("Some raw text")

        // When
        val model = Res(id, listOf(argOne, argTwo, argThree))

        // Then
        model.resId assertEquals id
        model.formatArgs assertIs ImmutableList::class
        model.formatArgs assertEquals persistentListOf(argOne, argTwo, argThree)
    }

    @Test
    fun `Res vararg constructor`() {
        // Given
        val id = 12
        val argOne = "Some argument who knows"
        val argTwo = 1243
        val argThree = Plural(123, 12412, "Some format arg")
        val argFour = 124F
        val argFive = 0.234

        // When
        val model = Res(id, argOne, argTwo, argThree, argFour, argFive)

        // Then
        model.resId assertEquals id
        model.formatArgs assertIs ImmutableList::class
        model.formatArgs assertEquals persistentListOf(argOne, argTwo, argThree, argFour, argFive)
    }

    @Test
    fun `Plural iterable constructor`() {
        // Given
        val id = 14
        val quantity = 124
        val argOne = "Some argument who knows"
        val argTwo = 12452
        val argThree = Raw("Some raw text")

        // When
        val model = Plural(id, quantity, listOf(argOne, argTwo, argThree))

        // Then
        model.resId assertEquals id
        model.quantity assertEquals quantity
        model.formatArgs assertIs ImmutableList::class
        model.formatArgs assertEquals persistentListOf(argOne, argTwo, argThree)
    }

    @Test
    fun `Plural vararg constructor`() {
        // Given
        val id = 423
        val quantity = 324
        val argOne = "Some argument who knows"
        val argTwo = 23523
        val argThree = Res(123, "Some format arg")
        val argFour = 143F
        val argFive = 124.234

        // When
        val model = Plural(id, quantity, argOne, argTwo, argThree, argFour, argFive)

        // Then
        model.resId assertEquals id
        model.quantity assertEquals quantity
        model.formatArgs assertIs ImmutableList::class
        model.formatArgs assertEquals persistentListOf(argOne, argTwo, argThree, argFour, argFive)
    }

    @Test
    fun `Combined iterable constructor`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Plural(4235, 3254, "something")
        val argThree = Raw("Some other raw text")

        // When
        val model = Combined(listOf(argOne, argTwo, argThree))

        // Then
        model.sections assertIs ImmutableList::class
        model.sections assertEquals persistentListOf(argOne, argTwo, argThree)
    }

    @Test
    fun `Combined vararg constructor`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Plural(4235, 3254, "something")
        val argThree = Plural(94823, 92300, "something else")
        val argFour = Res(123, "Some format arg")
        val argFive = Raw("Some other raw text")

        // When
        val model = Combined(argOne, argTwo, argThree, argFour, argFive)

        // Then
        model.sections assertIs ImmutableList::class
        model.sections assertEquals persistentListOf(argOne, argTwo, argThree, argFour, argFive)
    }

    @Test
    fun `Combined + Combined`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Raw("Some other raw text")
        val argThree = Res(123, "Some format arg")
        val argFour = Plural(4235, 3254, "something")
        val argFive = Plural(94823, 92300, "something else")

        // When
        val model = Combined(argOne, argTwo, argThree) + Combined(argFour, argFive)

        // Then
        model.sections assertEquals persistentListOf(argOne, argTwo, argThree, argFour, argFive)
    }

    @Test
    fun `Combined + StringUIModel`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Res(123, "Some format arg")
        val argThree = Plural(4235, 3254, "something")

        // When
        val model = Combined(argOne, argTwo) + argThree

        // Then
        model.sections assertEquals persistentListOf(argOne, argTwo, argThree)
    }

    @Test
    fun `StringUIModel + Combined`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Res(123, "Some format arg")
        val argThree = Plural(4235, 3254, "something")
        val argFour = Raw("Some other raw text")

        // When
        val model = argOne + Combined(argTwo, argThree, argFour)

        // Then
        model.sections assertEquals persistentListOf(argOne, argTwo, argThree, argFour)
    }

    @Test
    fun `StringUIModel + StringUIModel`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Res(123, "Some format arg")

        // When
        val model = argOne + argTwo

        // Then
        model.sections assertEquals persistentListOf(argOne, argTwo)
    }

    @Test
    fun `Combined + String`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Res(123, "Some format arg")
        val argThree = Plural(4235, 3254, "something")
        val string = "Some string"

        // When
        val model = Combined(argOne, argTwo, argThree) + string

        // Then
        model.sections assertEquals persistentListOf(argOne, argTwo, argThree, Raw(string))
    }

    @Test
    fun `StringUIModel + String`() {
        // Given
        val argOne = Res(123, "Some format arg")
        val string = "Some string"

        // When
        val model = argOne + string

        // Then
        model.sections assertEquals persistentListOf(argOne, Raw(string))
    }

    @Test
    fun `Builder DSL Test`() {
        // Given
        val string = "Some string"
        val res = Res(123, "Some format arg")
        val plural = Plural(4235, 3254, "something")
        val raw = Raw("Some raw text")
        val stringResId = 102342
        val stringResArgOne = "Some argument who knows"
        val stringResArgTwo = 21493
        val stringResVarArgId = 214349
        val stringResVarArgOne = "Some argument who knows"
        val stringResVarArgTwo = 94543
        val pluralResId = 2349235
        val pluralQuantity = 32454
        val pluralResArgOne = "Some argument who knows"
        val pluralResArgTwo = 2304203
        val pluralResArgThree = 23423F
        val pluralResVarArgId = 32942354
        val pluralVarArgQuantity = 943534
        val pluralResVarArgOne = "Some argument who knows"
        val pluralResVarArgTwo = 234235
        val pluralResVarArgThree = 2132F
        val combinedArgOne = Raw("Some raw text")
        val combinedArgTwo = Res(123, "Some format arg")
        val combinedArgThree = Plural(4235, 3254, "something")

        // When
        val model = buildStringUIModel {
            append(string)
            append(res)
            append(plural)
            append(raw)

            appendRes(resId = stringResId, formatArgs = listOf(stringResArgOne, stringResArgTwo))
            appendRes(resId = stringResVarArgId, stringResVarArgOne, stringResVarArgTwo)

            appendPlural(
                resId = pluralResId,
                quantity = pluralQuantity,
                formatArgs = listOf(pluralResArgOne, pluralResArgTwo, pluralResArgThree),
            )
            appendPlural(
                resId = pluralResVarArgId,
                quantity = pluralVarArgQuantity,
                pluralResVarArgOne,
                pluralResVarArgTwo,
                pluralResVarArgThree,
            )

            append(Combined(combinedArgOne, combinedArgTwo, combinedArgThree))
        }

        // Then
        model assertIs Combined::class
        (model as Combined).sections assertEquals persistentListOf(
            Raw(string),
            res,
            plural,
            raw,
            Res(stringResId, stringResArgOne, stringResArgTwo),
            Res(stringResVarArgId, stringResVarArgOne, stringResVarArgTwo),
            Plural(
                resId = pluralResId,
                quantity = pluralQuantity,
                pluralResArgOne,
                pluralResArgTwo,
                pluralResArgThree,
            ),
            Plural(
                resId = pluralResVarArgId,
                quantity = pluralVarArgQuantity,
                pluralResVarArgOne,
                pluralResVarArgTwo,
                pluralResVarArgThree,
            ),
            combinedArgOne,
            combinedArgTwo,
            combinedArgThree,
        )
    }

    @Test
    fun `Combined constructor flattening`() {
        // Given
        val argOne = Raw("Some raw text")
        val argTwo = Res(123, "Some format arg")
        val argThree = Plural(4235, 3254, "something")
        val argFour = Raw("Some other raw text")
        val argFive = Raw("Some more raw text")
        val argSix = Plural(94823, 92300, "something else")
        val argSeven = Raw("Some final raw text")
        val argEight = Plural(94823, 92300, "something else")

        // When
        val model = Combined(
            Combined(argOne, argTwo),
            argThree,
            argFour,
            Combined(argFive, argSix, argSeven),
            argEight,
        )

        // Then
        model.sections assertEquals persistentListOf(
            argOne,
            argTwo,
            argThree,
            argFour,
            argFive,
            argSix,
            argSeven,
            argEight,
        )
    }
}
