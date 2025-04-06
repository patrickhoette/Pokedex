package com.patrickhoette.core.utils.extension

private val SplitRegex = Regex("\\s+|_+|-+")

fun String.toSimpleTitleCase() = split(SplitRegex)
    .joinToString(" ") { part ->
        part.lowercase()
            .replaceFirstChar { it.uppercaseChar() }
    }

fun String.toPascalCase() = toSimpleTitleCase().replace(" ", "")
