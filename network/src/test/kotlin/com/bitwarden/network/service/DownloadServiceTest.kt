package com.work.service

import com.work.api.DownloadApi
import com.work.base.BaseServiceTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.create

class DownloadServiceTest : BaseServiceTest() {
    private val downloadApi: DownloadApi = retrofit.create()

    private val downloadService: DownloadService = DownloadServiceImpl(
        downloadApi = downloadApi,
    )

    @Test
    fun `getDataStream should return a raw stream RespondBody`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("Universal Password Manager")
                .setHeader("Content-Type", "application/stream"),
        )
        val url = "/test-url"
        val result = downloadService.getDataStream(url)
        assertTrue(result.isSuccess)
        assertEquals("Universal Password Manager", String(result.getOrThrow().bytes()))
    }
}
