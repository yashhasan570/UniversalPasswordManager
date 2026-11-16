package com.work.util

import .core.data.util.asFailure
import .core.data.util.asSuccess
import com.work.model.NetworkResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkResultExtensionsTest {
    @Test
    fun `NetworkResult toResult with success should return successful result`() {
        val value = "test"
        val original = NetworkResult.Success(value)

        val result = original.toResult()

        assertEquals(value.asSuccess(), result)
    }

    @Test
    fun `NetworkResult toResult with failure should return failure result`() {
        val exception = Exception("Failed")
        val original = NetworkResult.Failure(exception)

        val result = original.toResult()

        assertEquals(exception.asFailure(), result)
    }
}
