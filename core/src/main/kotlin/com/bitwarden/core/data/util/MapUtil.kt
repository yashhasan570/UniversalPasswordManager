@file:OmitFromCoverage

package .core.data.util

import .annotation.OmitFromCoverage
import java.util.concurrent.ConcurrentHashMap

/**
 * Creates a thread-safe [MutableMap].
 */
fun <T, R> concurrentMapOf(
    vararg items: Pair<T, R>,
): MutableMap<T, R> = ConcurrentHashMap<T, R>().apply { this.putAll(items) }
