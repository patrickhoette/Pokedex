package com.patrickhoette.core.ui.text

import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.core.presentation.model.StringUIModelDSL.buildStringUIModel
import com.patrickhoette.core.presentation.text.UnitTextProvider
import com.patrickhoette.core.ui.R
import com.patrickhoette.pokedex.entity.generic.Length
import com.patrickhoette.pokedex.entity.generic.Length.Companion.ft
import com.patrickhoette.pokedex.entity.generic.Weight
import com.patrickhoette.pokedex.entity.generic.Weight.Companion.lb
import org.koin.core.annotation.Factory
import java.text.NumberFormat
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

@Factory
class ResUnitTextProvider : UnitTextProvider {

    override fun mapToTextMetric(length: Length): StringUIModel {
        val (unit, res) = when (length.centimeters) {
            in 0.1..<1.0 -> length.millimeters to R.string.unit_metric_millimeter
            in 1.0..<10.0 -> length.centimeters to R.string.unit_metric_centimeter
            in 10.0..<100.0 -> length.decimeters to R.string.unit_metric_decimeter
            in 100.0..<1000.0 -> length.meters to R.string.unit_metric_meter
            in 1000.0..<10_000.0 -> length.decameters to R.string.unit_metric_decameter
            in 10_000.0..<100_000.0 -> length.hectometers to R.string.unit_metric_hectometer
            else -> length.kilometers to R.string.unit_metric_kilometer
        }

        return StringUIModel.Res(res, Formatter.format(unit))
    }

    override fun mapToTextMetric(weight: Weight): StringUIModel {
        val (unit, res) = when (weight.grams) {
            in 0.001..<0.01 -> weight.milligrams to R.string.unit_metric_milligram
            in 0.01..<0.1 -> weight.centigrams to R.string.unit_metric_centigram
            in 0.1..<1.0 -> weight.decigrams to R.string.unit_metric_decigram
            in 1.0..<10.0 -> weight.grams to R.string.unit_metric_gram
            in 10.0..<100.0 -> weight.decagrams to R.string.unit_metric_decagram
            in 100.0..<1000.0 -> weight.hectograms to R.string.unit_metric_hectogram
            in 1000.0..<1_000_000.0 -> weight.kilograms to R.string.unit_metric_kilogram
            else -> weight.tonnes to R.string.unit_metric_tonne
        }

        return StringUIModel.Res(res, Formatter.format(unit))
    }

    override fun mapToTextImperial(length: Length): StringUIModel = when {
        length < 100.ft -> formatImperialFeetAndInches(length)
        length < 5280.ft -> StringUIModel.Res(R.string.unit_imperial_foot, Formatter.format(length.feet))
        else -> StringUIModel.Res(R.string.unit_imperial_mile, Formatter.format(length.miles))
    }

    private fun formatImperialFeetAndInches(length: Length): StringUIModel {
        val totalInches = length.inches
        val wholeFeet = floor(totalInches / 12).toInt()
        val inchesRemaining = totalInches - (wholeFeet * 12)
        val wholeInches = floor(inchesRemaining).toInt()
        val fractionalPart = inchesRemaining - wholeInches

        val nearestEighth = round(fractionalPart * 8).toInt()
        val fractionalUnicode = fractionToUnicode(nearestEighth, 8)

        return buildStringUIModel {
            if (wholeFeet > 0) appendRes(R.string.unit_imperial_foot, wholeFeet)
            if (wholeInches > 0 || fractionalUnicode != null) {
                if (wholeFeet > 0) append(" ")
                val inchString = buildString {
                    append(wholeInches)
                    fractionalUnicode?.let(::append)
                }
                appendRes(R.string.unit_imperial_inch, inchString)
            }
        }
    }

    private fun fractionToUnicode(numerator: Int, denominator: Int): String? {
        if (numerator == 0 || numerator == denominator) return null
        return when (denominator) {
            2 -> "½"
            4 -> when (numerator) {
                1 -> "¼"
                2 -> "½"
                else -> "¾"
            }
            8 -> when (numerator) {
                1 -> "⅛"
                2 -> "¼"
                3 -> "⅜"
                4 -> "½"
                5 -> "⅝"
                6 -> "¾"
                else -> "⅞"
            }
            else -> "$numerator⁄$denominator"
        }
    }

    override fun mapToTextImperial(weight: Weight, isUK: Boolean): StringUIModel = when {
        weight < 1.lb -> StringUIModel.Res(R.string.unit_imperial_ounce, weight.ounces.roundToInt())
        !isUK || weight < 14.lb -> formatPoundsAndOunces(weight)
        else -> formatStonesAndPounds(weight)
    }

    private fun formatPoundsAndOunces(weight: Weight): StringUIModel {
        val poundsTotal = weight.pounds
        val pounds = floor(poundsTotal).toInt()
        val ounces = ((poundsTotal - pounds) * 16).roundToInt()

        return buildStringUIModel {
            appendRes(R.string.unit_imperial_pound, pounds)
            if (ounces > 0) {
                append(" ")
                appendRes(R.string.unit_imperial_ounce, ounces)
            }
        }
    }

    private fun formatStonesAndPounds(weight: Weight): StringUIModel {
        val poundsTotal = weight.pounds
        val stones = floor(poundsTotal / 14).toInt()
        val pounds = (poundsTotal - stones * 14).roundToInt()

        return buildStringUIModel {
            appendRes(R.string.unit_imperial_stone, stones)
            if (pounds > 0) {
                append(" ")
                appendRes(R.string.unit_imperial_pound, pounds)
            }
        }
    }

    companion object {

        private val Formatter by lazy {
            NumberFormat.getInstance().apply {
                maximumFractionDigits = 2
                minimumFractionDigits = 0
                minimumIntegerDigits = 1
            }
        }
    }
}
