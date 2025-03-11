package com.patrickhoette.core.presentation.model

import com.patrickhoette.core.presentation.model.StringUIModel.Combined
import com.patrickhoette.core.presentation.model.StringUIModel.Raw
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@DslMarker
annotation class StringUIModelBuilderMarker

object StringUIModelDSL {

    /**
     * Creates a [StringUIModel].
     *
     * Example usage:
     * ```
     * val text = buildStringUIModel {
     *     append("Hello, ")
     *     appendRes(R.string.app_name)
     *     append("!")
     *
     *     append(" ")
     *
     *     appendPlural(R.plural.something, 4)
     * }
     * ```
     *
     * @param block Lambda with [StringUIModelBuilder] receiver to define the structure of the text.
     * @return A [StringUIModel] representing the constructed text.
     */
    fun buildStringUIModel(block: StringUIModelBuilder.() -> Unit): StringUIModel {
        val builder = StringUIModelBuilder()
        block(builder)
        return builder.build()
    }

    /**
     * Concatenate [other] to the end of `this`.
     */
    operator fun StringUIModel.plus(other: StringUIModel): Combined = when {
        this is Combined -> Combined(
            (if (other is Combined) sections + other.sections else sections + other).toImmutableList()
        )
        other is Combined -> Combined((listOf(this) + other.sections).toImmutableList())
        else -> Combined(persistentListOf(this, other))
    }

    /**
     * Concatenate [other] to the end of `this` as a [Raw].
     */
    operator fun StringUIModel.plus(other: String): Combined = this + Raw(other)

    /**
     * Concatenate [other] to the end of `this` as a [Combined].
     */
    operator fun StringUIModel.plus(other: Iterable<StringUIModel>): Combined = this + Combined(other.toImmutableList())
}
