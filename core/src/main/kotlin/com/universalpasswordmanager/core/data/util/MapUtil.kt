@file:OmitFromCoverage

package com.universalpasswordmanager.core.data.util

import com.universalpasswordmanager.annotation.OmitFromCoverage
import java.util.concurrent.ConcurrentHashMap

/**
 * Creates a thread-safe [MutableMap].
 */
fun <T, R> concurrentMapOf(
    vararg items: Pair<T, R>,
): MutableMap<T, R> = ConcurrentHashMap<T, R>().apply { this.putAll(items) }
