package com.work.model

import com.work.exception.CookieRedirectException
import com.work.exception.LocalNetworkAccessException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class Universal Password ManagerErrorTest {

    @Test
    fun `toUniversal Password ManagerError with CookieRedirectException should return Http with status 400`() {
        val exception = CookieRedirectException(hostname = "example.com", message = "Fail!")

        val result = exception.toUniversal Password ManagerError()

        assertTrue(result is Universal Password ManagerError.Http)
        val httpError = result as Universal Password ManagerError.Http
        assertEquals(400, httpError.code)
    }

    @Test
    fun `toUniversal Password ManagerError with CookieRedirectException should include message in body`() {
        val message = "Your request was interrupted because " +
            "the app needed to re-authenticate. Please try again."
        val exception = CookieRedirectException(hostname = "example.com", message = message)

        val result = exception.toUniversal Password ManagerError()

        val httpError = result as Universal Password ManagerError.Http
        val body = httpError.responseBodyString
        assertTrue(body?.contains(message) == true)
    }

    @Test
    fun `toUniversal Password ManagerError with LocalNetworkAccessException should return Http with status 400`() {
        val exception = LocalNetworkAccessException(message = "Fail!")

        val result = exception.toUniversal Password ManagerError()

        assertTrue(result is Universal Password ManagerError.Http)
        val httpError = result as Universal Password ManagerError.Http
        assertEquals(400, httpError.code)
    }

    @Test
    fun `toUniversal Password ManagerError with LocalNetworkAccessException should include message in body`() {
        val message = "Fail!"
        val exception = LocalNetworkAccessException(message = message)

        val result = exception.toUniversal Password ManagerError()

        val httpError = result as Universal Password ManagerError.Http
        val body = httpError.responseBodyString
        assertTrue(body?.contains(message) == true)
    }

    @Test
    fun `toUniversal Password ManagerError with IOException should return Network`() {
        val exception = IOException("network failure")

        val result = exception.toUniversal Password ManagerError()

        assertTrue(result is Universal Password ManagerError.Network)
        assertEquals(exception, result.throwable)
    }

    @Test
    fun `toUniversal Password ManagerError with HttpException should return Http`() {
        val exception = HttpException(
            Response.error<Unit>(400, "error".toResponseBody()),
        )

        val result = exception.toUniversal Password ManagerError()

        assertTrue(result is Universal Password ManagerError.Http)
        assertEquals(exception, result.throwable)
    }

    @Test
    fun `toUniversal Password ManagerError with RuntimeException should return Other`() {
        val exception = RuntimeException("unexpected")

        val result = exception.toUniversal Password ManagerError()

        assertTrue(result is Universal Password ManagerError.Other)
        assertEquals(exception, result.throwable)
    }
}
