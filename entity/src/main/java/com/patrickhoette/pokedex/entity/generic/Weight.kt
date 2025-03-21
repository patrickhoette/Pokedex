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

    companion object {

        fun Grams(value: Number): Weight =
            Weight(value.toDouble())

        fun Milligrams(value: Number): Weight =
            Weight(value.toDouble() / 1_000)

        fun Centigrams(value: Number): Weight =
            Weight(value.toDouble() / 100)

        fun Decigrams(value: Number): Weight =
            Weight(value.toDouble() / 10)

        fun Decagrams(value: Number): Weight =
            Weight(value.toDouble() * 10)

        fun Hectograms(value: Number): Weight =
            Weight(value.toDouble() * 100)

        fun Kilograms(value: Number): Weight =
            Weight(value.toDouble() * 1_000)

        fun Tonnes(value: Number): Weight =
            Weight(value.toDouble() * 1_000_000)

        fun Ounces(value: Number): Weight =
            Weight(value.toDouble() * 28.3495)

        fun Pounds(value: Number): Weight =
            Weight(value.toDouble() * 453.59237)

        fun Stones(value: Number): Weight =
            Weight(value.toDouble() * 14 * 453.59237)
    }
}
