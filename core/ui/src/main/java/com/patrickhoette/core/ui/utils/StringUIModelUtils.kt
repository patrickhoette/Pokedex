package com.patrickhoette.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.core.presentation.model.StringUIModel.*
import kotlinx.collections.immutable.ImmutableList

/**
 * Convert a [StringUIModel] to a [String]. All types of [StringUIModel] are supported. See [StringUIModel] and its
 * implementations for more details.
 */
@Composable
@ReadOnlyComposable
fun stringResource(model: StringUIModel): String = when (model) {
    is Combined -> buildString {
        for (section in model.sections) append(stringResource(section))
    }
    is Plural -> {
        val args = resolveArguments(model.formatArgs)
        if (args.isEmpty()) {
            pluralStringResource(id = model.resId, count = model.quantity)
        } else {
            pluralStringResource(id = model.resId, count = model.quantity, formatArgs = args.toTypedArray())
        }
    }
    is Raw -> model.value
    is Res -> {
        val args = resolveArguments(model.formatArgs)
        if (args.isEmpty()) {
            stringResource(id = model.resId)
        } else {
            stringResource(id = model.resId, formatArgs = args.toTypedArray())
        }
    }
}

@Composable
@ReadOnlyComposable
private fun resolveArguments(args: ImmutableList<Any>): List<Any> = args.map {
    if (it is StringUIModel) stringResource(it) else it
}
