package com.work.interceptor

import com.work.exception.CookieRedirectException
import com.work.model.NetworkCookie
import com.work.provider.CookieProvider
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CookieInterceptorTest {

    private val mockCookieProvider: CookieProvider = mockk {
        every { acquireCookies(any()) } just runs
    }

    private val interceptor = CookieInterceptor(
        cookieProvider = mockCookieProvider,
    )

    @Test
    fun `intercept should skip cookie handling for config path`() {
        val originalRequest = Request.Builder()
            .url("https://api./api/config")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        val response = interceptor.intercept(chain)

        assertEquals(originalRequest, response.request)
        verify(exactly = 0) {
            mockCookieProvider.needsBootstrap(any())
            mockCookieProvider.getCookies(any())
        }
    }

    @Test
    fun `intercept should skip cookie handling for sso-cookie-vendor path`() {
        val originalRequest = Request.Builder()
            .url("https://api./api/sso-cookie-vendor")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        val response = interceptor.intercept(chain)

        assertEquals(originalRequest, response.request)
        verify(exactly = 0) {
            mockCookieProvider.needsBootstrap(any())
            mockCookieProvider.getCookies(any())
        }
    }

    @Test
    fun `intercept should throw CookieRedirectException when bootstrap is needed`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        every { mockCookieProvider.needsBootstrap("vault.") } returns true
        every { mockCookieProvider.errorMessageString } returns "Error"

        val exception = assertThrows<CookieRedirectException> {
            interceptor.intercept(chain)
        }

        assertEquals("vault.", exception.hostname)
        verify { mockCookieProvider.acquireCookies("vault.") }
        verify(exactly = 0) { mockCookieProvider.getCookies(any()) }
    }

    @Test
    fun `intercept should proceed without cookies when no cookies available`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        every { mockCookieProvider.needsBootstrap("vault.") } returns false
        every { mockCookieProvider.getCookies("vault.") } returns emptyList()

        val response = interceptor.intercept(chain)

        assertEquals(originalRequest, response.request)
        assertNull(response.request.header("Cookie"))
        verify(exactly = 1) { mockCookieProvider.getCookies("vault.") }
    }

    @Test
    fun `intercept should attach single cookie when cookies available`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        val cookies = listOf(
            NetworkCookie(name = "awselb", value = "session123"),
        )

        every { mockCookieProvider.needsBootstrap("vault.") } returns false
        every { mockCookieProvider.getCookies("vault.") } returns cookies

        val response = interceptor.intercept(chain)

        assertEquals("awselb=session123", response.request.header("Cookie"))
        verify(exactly = 1) { mockCookieProvider.getCookies("vault.") }
    }

    @Test
    fun `intercept should attach multiple cookies with correct format`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        val cookies = listOf(
            NetworkCookie(name = "awselb", value = "session123"),
            NetworkCookie(name = "awselbcors", value = "cors456"),
            NetworkCookie(name = "custom", value = "value789"),
        )

        every { mockCookieProvider.needsBootstrap("vault.") } returns false
        every { mockCookieProvider.getCookies("vault.") } returns cookies

        val response = interceptor.intercept(chain)

        assertEquals(
            "awselb=session123; awselbcors=cors456; custom=value789",
            response.request.header("Cookie"),
        )
        verify(exactly = 1) { mockCookieProvider.getCookies("vault.") }
    }

    @Test
    fun `intercept should throw CookieRedirectException on 302 response`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()

        val redirectResponse = Response.Builder()
            .code(302)
            .message("Found")
            .protocol(Protocol.HTTP_1_1)
            .request(originalRequest)
            .header("Location", "https://idp.example.com/auth")
            .build()

        val chain = FakeInterceptorChain(
            request = originalRequest,
            responseProvider = { redirectResponse },
        )

        every { mockCookieProvider.needsBootstrap("vault.") } returns false
        every { mockCookieProvider.getCookies("vault.") } returns emptyList()
        every { mockCookieProvider.errorMessageString } returns "Error"

        val exception = assertThrows<CookieRedirectException> {
            interceptor.intercept(chain)
        }

        assertEquals("vault.", exception.hostname)
        verify { mockCookieProvider.acquireCookies("vault.") }
    }

    @Test
    fun `intercept should not throw exception on successful 200 response`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        every { mockCookieProvider.needsBootstrap("vault.") } returns false
        every { mockCookieProvider.getCookies("vault.") } returns emptyList()

        val response = interceptor.intercept(chain)

        assertEquals(200, response.code)
    }

    @Test
    fun `intercept should skip cookie handling for cloud config path`() {
        val originalRequest = Request.Builder()
            .url("https://api./config")
            .build()
        val chain = FakeInterceptorChain(originalRequest)

        val response = interceptor.intercept(chain)

        assertEquals(originalRequest, response.request)
        verify(exactly = 0) {
            mockCookieProvider.needsBootstrap(any())
            mockCookieProvider.getCookies(any())
        }
    }

    @Test
    fun `intercept should throw CookieRedirectException on 302 when cookies were attached`() {
        val originalRequest = Request.Builder()
            .url("https://vault./api/accounts/profile")
            .build()

        val cookies = listOf(
            NetworkCookie(name = "awselb", value = "session123"),
        )

        val redirectResponse = Response.Builder()
            .code(302)
            .message("Found")
            .protocol(Protocol.HTTP_1_1)
            .request(originalRequest)
            .header("Location", "https://idp.example.com/auth")
            .build()

        val chain = FakeInterceptorChain(
            request = originalRequest,
            responseProvider = { redirectResponse },
        )

        every { mockCookieProvider.needsBootstrap("vault.") } returns false
        every { mockCookieProvider.getCookies("vault.") } returns cookies
        every { mockCookieProvider.errorMessageString } returns "Error"

        val exception = assertThrows<CookieRedirectException> {
            interceptor.intercept(chain)
        }

        assertEquals("vault.", exception.hostname)
        verify { mockCookieProvider.acquireCookies("vault.") }
    }
}
