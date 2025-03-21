@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.patrickhoette.pokedex.entity.generic

@JvmInline
value class Length(val centimeter: Double) {

    val millimeters: Double
        get() = centimeter * 10

    val decimeters: Double
        get() = centimeter / 10

    val meters: Double
        get() = centimeter / 100

    val decameters: Double
        get() = centimeter / 1_000

    val hectometers: Double
        get() = centimeter / 10_000

    val kilometers: Double
        get() = centimeter / 100_000

    val inches: Double
        get() = centimeter / 2.54

    val feet: Double
        get() = inches / 12

    val yards: Double
        get() = feet / 3

    val miles: Double
        get() = centimeter / 160934.4

    companion object {

        fun Millimeters(value: Number) = Length(value.toDouble() / 10)

        fun Centimeters(value: Number) = Length(value.toDouble())

        fun Decimeters(value: Number) = Length(value.toDouble() * 10)

        fun Meters(value: Number) = Length(value.toDouble() * 100)

        fun Decameters(value: Number) = Length(value.toDouble() * 1_000)

        fun Hectometers(value: Number) = Length(value.toDouble() * 10_000)

        fun Kilometers(value: Number) = Length(value.toDouble() * 100_000)

        fun Inches(value: Number) = Length(value.toDouble() * 2.54)

        fun Feet(value: Number) = Length(value.toDouble() * 30.48)

        fun Yards(value: Number) = Length(value.toDouble() * 91.44)

        fun Miles(value: Number) = Length(value.toDouble() * 160_934.4)
    }
}
