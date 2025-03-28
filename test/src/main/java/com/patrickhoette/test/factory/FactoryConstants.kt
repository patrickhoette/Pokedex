package com.patrickhoette.test.factory

object FactoryConstants {

    val DefaultCollectionSize = 10

    internal fun <T> List<T>.moduloGet(index: Int): T? = if (isNotEmpty()) get(index % size) else null
}
