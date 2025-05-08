@file:Suppress("unused")

package com.patrickhoette.core.presentation.model

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.patrickhoette.core.presentation.model.StringUIModel.*

/**
 * Builder class for constructing a [StringUIModel]. Appended segments are combined in the order in which they are
 * added.
 */
@StringUIModelBuilderMarker
class StringUIModelBuilder {

    private val sections = mutableListOf<StringUIModel>()

    /**
     * Appends an existing [StringUIModel] to the builder. If the [StringUIModel] is [Combined],
     * it is flattened into the sections of the builder.
     *
     * @param stringUIModel The [StringUIModel] instance to append.
     */
    fun append(stringUIModel: StringUIModel) = if (stringUIModel is Combined) {
        sections += stringUIModel.sections
    } else {
        sections += stringUIModel
    }

    /**
     * Appends a raw text segment as a [Raw] to the builder.
     *
     * @param string The plain text to append.
     */
    fun append(string: String) {
        sections += Raw(string)
    }

    /**
     * Appends an Android string resource with optional formatting arguments to the builder.
     *
     * @param resId The string resource identifier.
     * @param formatArgs Optional formatting arguments for the resource string.
     */
    fun appendRes(@StringRes resId: Int, vararg formatArgs: Any) {
        sections += Res(resId = resId, formatArgs = formatArgs)
    }

    /**
     * Appends an Android string resource with an iterable of formatting arguments to the builder.
     *
     * @param resId The string resource identifier.
     * @param formatArgs Iterable formatting arguments for the resource string.
     */
    fun appendRes(@StringRes resId: Int, formatArgs: Iterable<Any>) {
        sections += Res(resId = resId, formatArgs = formatArgs)
    }

    /**
     * Appends a pluralized string resource with quantity and optional formatting arguments.
     *
     * @param resId The plural resource identifier.
     * @param quantity The quantity to determine the plural form.
     * @param formatArgs Optional formatting arguments for the plural resource.
     */
    fun appendPlural(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any) {
        sections += Plural(resId = resId, quantity = quantity, formatArgs = formatArgs)
    }

    /**
     * Appends a pluralized string resource with quantity and an iterable of formatting arguments.
     *
     * @param resId The plural resource identifier.
     * @param quantity The quantity to determine the plural form.
     * @param formatArgs Iterable formatting arguments for the plural resource.
     */
    fun appendPlural(@PluralsRes resId: Int, quantity: Int, formatArgs: Iterable<Any>) {
        sections += Plural(resId = resId, quantity = quantity, formatArgs = formatArgs)
    }

    /**
     * Builds and returns the final [StringUIModel] containing all appended segments in the order they were added.
     *
     * @return A [StringUIModel] representing the entire constructed text.
     */
    fun build(): StringUIModel = if (sections.size == 1) sections.single() else Combined(sections)
}
