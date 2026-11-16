package com.work.util

import .core.data.util.asFailure
import .core.data.util.asSuccess
import com.work.model.NetworkResult

/**
 * Converts the [NetworkResult] to a [Result].
 */
internal fun <T> NetworkResult<T>.toResult(): Result<T> =
    when (this) {
        is NetworkResult.Failure -> this.throwable.asFailure()
        is NetworkResult.Success -> this.value.asSuccess()
    }
