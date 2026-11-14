package com.universalpasswordmanager.network.util

import com.universalpasswordmanager.core.data.util.asFailure
import com.universalpasswordmanager.core.data.util.asSuccess
import com.universalpasswordmanager.network.model.NetworkResult

/**
 * Converts the [NetworkResult] to a [Result].
 */
internal fun <T> NetworkResult<T>.toResult(): Result<T> =
    when (this) {
        is NetworkResult.Failure -> this.throwable.asFailure()
        is NetworkResult.Success -> this.value.asSuccess()
    }
