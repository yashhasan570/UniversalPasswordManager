package com.universalpasswordmanager.network.model

import com.universalpasswordmanager.network.exception.CookieRedirectException
import com.universalpasswordmanager.network.exception.LocalNetworkAccessException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UniversalErrorTest {

    @Test
    fun `toUniversalError with CookieRedirectException should return Http with status 400`() {
        val exception = CookieRedirectException(hostname = "example.com", message = "Fail!")

        val result = exception.toUniversalError()

        assertTrue(result is UniversalError.Http)
        val httpError = result as UniversalError.Http
        assertEquals(400, httpError.code)
    }

    @Test
    fun `toUniversalError with CookieRedirectException should include message in body`() {
        val message = "Your request was interrupted because " +
            "the app needed to re-authenticate. Please try again."
        val exception = CookieRedirectException(hostname = "example.com", message = message)

        val result = exception.toUniversalError()

        val httpError = result as UniversalError.Http
        val body = httpError.responseBodyString
        assertTrue(body?.contains(message) == true)
    }

    @Test
    fun `toUniversalError with LocalNetworkAccessException should return Http with status 400`() {
        val exception = LocalNetworkAccessException(message = "Fail!")

        val result = exception.toUniversalError()

        assertTrue(result is UniversalError.Http)
        val httpError = result as UniversalError.Http
        assertEquals(400, httpError.code)
    }

    @Test
    fun `toUniversalError with LocalNetworkAccessException should include message in body`() {
        val message = "Fail!"
        val exception = LocalNetworkAccessException(message = message)

        val result = exception.toUniversalError()

        val httpError = result as UniversalError.Http
        val body = httpError.responseBodyString
        assertTrue(body?.contains(message) == true)
    }

    @Test
    fun `toUniversalError with IOException should return Network`() {
        val exception = IOException("network failure")

        val result = exception.toUniversalError()

        assertTrue(result is UniversalError.Network)
        assertEquals(exception, result.throwable)
    }

    @Test
    fun `toUniversalError with HttpException should return Http`() {
        val exception = HttpException(
            Response.error<Unit>(400, "error".toResponseBody()),
        )

        val result = exception.toUniversalError()

        assertTrue(result is UniversalError.Http)
        assertEquals(exception, result.throwable)
    }

    @Test
    fun `toUniversalError with RuntimeException should return Other`() {
        val exception = RuntimeException("unexpected")

        val result = exception.toUniversalError()

        assertTrue(result is UniversalError.Other)
        assertEquals(exception, result.throwable)
    }
}
