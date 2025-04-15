package com.patrickhoette.core.utils.collection

interface Differ<S, T> {

    fun diff(old: S, new: S): Set<Diff<T>>
}
