@file:Suppress("MemberVisibilityCanBePrivate", "unused", "TooManyFunctions", "ObjectPropertyNaming")

package com.patrickhoette.pokedex.entity.generic

@JvmInline
value class Length(val centimeters: Double) {

    val millimeters: Double
        get() = centimeters * 10

    val decimeters: Double
        get() = centimeters / 10

    val meters: Double
        get() = centimeters / 100

    val decameters: Double
        get() = centimeters / 1_000

    val hectometers: Double
        get() = centimeters / 10_000

    val kilometers: Double
        get() = centimeters / 100_000

    val inches: Double
        get() = centimeters / 2.54

    val feet: Double
        get() = inches / 12

    val yards: Double
        get() = feet / 3

    val miles: Double
        get() = centimeters / 160_934.4

    operator fun minus(other: Length) = Length(centimeters - other.centimeters)

    operator fun plus(other: Length) = Length(centimeters + other.centimeters)

    operator fun times(scalar: Number) = Length(centimeters * scalar.toDouble())

    operator fun div(scalar: Number) = Length(centimeters / scalar.toDouble())

    operator fun compareTo(other: Length) = centimeters.compareTo(other.centimeters)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Length(-centimeters)

    companion object {

        val Number.mm
            get() = Millimeters(this)

        fun Millimeters(value: Number) = Length(value.toDouble() / 10)

        val Number.cm
            get() = Centimeters(this)

        fun Centimeters(value: Number) = Length(value.toDouble())

        val Number.dc
            get() = Decimeters(this)

        fun Decimeters(value: Number) = Length(value.toDouble() * 10)

        val Number.m
            get() = Meters(this)

        fun Meters(value: Number) = Length(value.toDouble() * 100)

        val Number.dam
            get() = Decameters(this)

        fun Decameters(value: Number) = Length(value.toDouble() * 1_000)

        val Number.hm
            get() = Hectometers(this)

        fun Hectometers(value: Number) = Length(value.toDouble() * 10_000)

        val Number.km
            get() = Kilometers(this)

        fun Kilometers(value: Number) = Length(value.toDouble() * 100_000)

        // Cause `in` is a keyword
        val Number.inch
            get() = Inches(this)

        fun Inches(value: Number) = Length(value.toDouble() * 2.54)

        val Number.ft
            get() = Feet(this)

        fun Feet(value: Number) = Length(value.toDouble() * 30.48)

        val Number.yd
            get() = Yards(this)

        fun Yards(value: Number) = Length(value.toDouble() * 91.44)

        val Number.mi
            get() = Miles(this)

        fun Miles(value: Number) = Length(value.toDouble() * 160_934.4)

        operator fun Number.times(length: Length) = Length(toDouble() * length.centimeters)
    }
}
