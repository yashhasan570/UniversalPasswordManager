package com.universalpasswordmanager.network.interceptor

import android.os.Build
import okhttp3.Request
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class HeadersInterceptorTest {

    @Test
    fun `intercept should modify original request to include custom headers`() {
        // We reference the real BuildConfig here, since we don't want the test to break on every
        // version bump. We are also doing the same thing for Build when the SDK gets incremented.
        val versionName = "VersionName"
        val buildType = "BuildType"
        val flavor = "Flavor"
        val release = Build.VERSION.RELEASE
        val sdk = Build.VERSION.SDK_INT
        val originalRequest = Request.Builder().url("http://www.fake.com/").build()
        val chain = FakeInterceptorChain(originalRequest)

        @Suppress("MaxLineLength")
        val headersInterceptors = HeadersInterceptor(
            userAgent = "Universal_Mobile/$versionName ($buildType/$flavor) (Android $release; SDK $sdk; Model robolectric)",
            clientName = "mobile",
            clientVersion = versionName,
        )
        val response = headersInterceptors.intercept(chain)

        val request = response.request
        @Suppress("MaxLineLength")
        assertEquals(
            "Universal_Mobile/$versionName ($buildType/$flavor) (Android $release; SDK $sdk; Model robolectric)",
            request.header("User-Agent"),
        )
        assertEquals("mobile", request.header("Universal-Client-Name"))
        assertEquals(versionName, request.header("Universal-Client-Version"))
        assertEquals("0", request.header("Device-Type"))
    }
}
