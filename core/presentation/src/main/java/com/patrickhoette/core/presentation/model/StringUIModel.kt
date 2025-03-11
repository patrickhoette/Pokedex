@file:Suppress("unused")

package com.patrickhoette.core.presentation.model

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Representation of a piece of text that supports localisation and pluralisation. Allows for the combining of different
 * "types" of text. See implementations for more details.
 */
@Stable
sealed class StringUIModel {

    /**
     * Representation of an Android string resource. Expects a valid string resource identifier to be passed to [resId],
     * optionally supports formatting arguments via [formatArgs].
     */
    @Immutable
    data class Res(
        @StringRes val resId: Int,
        val formatArgs: ImmutableList<Any>,
    ) : StringUIModel() {

        constructor(
            @StringRes resId: Int,
            formatArgs: Iterable<Any>,
        ) : this(resId = resId, formatArgs = formatArgs.toImmutableList())

        constructor(
            @StringRes resId: Int,
            vararg formatArgs: Any,
        ) : this(resId = resId, formatArgs = persistentListOf(*formatArgs))
    }

    /**
     * Representation of plain text. Doesn't support localisation, pluralisation or arguments.
     */
    @Immutable
    data class Raw(val value: String) : StringUIModel()

    /**
     * Representation of text containing a [quantity]. [quantity] is only used to determine which plural form should be
     * selected, if your text also contains the [quantity] as a formatting arg then you should pass it to [formatArgs]
     * as well. Expects a valid plural resource identifier to be passed to [resId].
     */
    @Immutable
    data class Plural(
        @PluralsRes val resId: Int,
        val quantity: Int,
        val formatArgs: ImmutableList<Any>,
    ) : StringUIModel() {

        constructor(
            @PluralsRes resId: Int,
            quantity: Int,
            formatArgs: Iterable<Any>,
        ) : this(resId = resId, quantity = quantity, formatArgs = formatArgs.toImmutableList())

        constructor(
            @PluralsRes resId: Int,
            quantity: Int,
            vararg formatArgs: Any,
        ) : this(resId = resId, quantity = quantity, formatArgs = persistentListOf(*formatArgs))
    }

    /**
     * Represents a collection of concatenated [StringUIModel]s. The order of the [sections] is the order in which they
     * will be placed in the final text.
     */
    @Immutable
    data class Combined private constructor(
        val sections: ImmutableList<StringUIModel>,
    ) : StringUIModel() {

        constructor(sections: Iterable<StringUIModel>) : this(flatten(sections))

        constructor(vararg sections: StringUIModel) : this(flatten(sections.asIterable()))

        companion object {

            private fun flatten(sections: Iterable<StringUIModel>): ImmutableList<StringUIModel> {
                val newSections = mutableListOf<StringUIModel>()

                for (section in sections) {
                    if (section is Combined) {
                        newSections += flatten(section.sections)
                    } else {
                        newSections += section
                    }
                }

                return newSections.toImmutableList()
            }
        }
    }
}
