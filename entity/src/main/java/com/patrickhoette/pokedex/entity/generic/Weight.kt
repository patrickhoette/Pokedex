@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.patrickhoette.pokedex.entity.generic

@JvmInline
value class Weight(val grams: Double) {

    val milligrams: Double
        get() = grams * 1_000

    val centigrams: Double
        get() = grams * 100

    val decigrams: Double
        get() = grams * 10

    val decagrams: Double
        get() = grams / 10

    val hectograms: Double
        get() = grams / 100

    val kilograms: Double
        get() = grams / 1_000

    val tonnes: Double
        get() = grams / 1_000_000

    val ounces: Double
        get() = grams / 28.3495

    val pounds: Double
        get() = grams / 453.59237

    val stones: Double
        get() = pounds / 14

    operator fun minus(other: Weight) = Weight(grams - other.grams)

    operator fun plus(other: Weight) = Weight(grams + other.grams)

    operator fun times(scalar: Number) = Weight(grams * scalar.toDouble())

    operator fun div(scalar: Number) = Weight(grams / scalar.toDouble())

    operator fun compareTo(other: Weight) = grams.compareTo(other.grams)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Weight(-grams)

    companion object {

        val Number.mg
            get() = Milligrams(this)

        fun Milligrams(value: Number): Weight =
            Weight(value.toDouble() / 1_000)

        val Number.cg
            get() = Centigrams(this)

        fun Centigrams(value: Number): Weight =
            Weight(value.toDouble() / 100)

        val Number.dg
            get() = Decigrams(this)

        fun Decigrams(value: Number): Weight =
            Weight(value.toDouble() / 10)

        val Number.g
            get() = Grams(this)

        fun Grams(value: Number): Weight =
            Weight(value.toDouble())

        val Number.dag
            get() = Decagrams(this)

        fun Decagrams(value: Number): Weight =
            Weight(value.toDouble() * 10)

        val Number.hg
            get() = Hectograms(this)

        fun Hectograms(value: Number): Weight =
            Weight(value.toDouble() * 100)

        val Number.kg
            get() = Kilograms(this)

        fun Kilograms(value: Number): Weight =
            Weight(value.toDouble() * 1_000)

        val Number.t
            get() = Tonnes(this)

        fun Tonnes(value: Number): Weight =
            Weight(value.toDouble() * 1_000_000)

        val Number.oz
            get() = Ounces(this)

        fun Ounces(value: Number): Weight =
            Weight(value.toDouble() * 28.3495)

        val Number.lb
            get() = Pounds(this)

        fun Pounds(value: Number): Weight =
            Weight(value.toDouble() * 453.59237)

        val Number.st
            get() = Stones(this)

        fun Stones(value: Number): Weight =
            Weight(value.toDouble() * 14 * 453.59237)

        operator fun Number.times(weight: Weight) = Weight(toDouble() * weight.grams)
    }
}
