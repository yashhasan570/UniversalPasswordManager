package com.work.service

import com.work.api.DownloadApi
import com.work.util.toResult
import okhttp3.ResponseBody

/**
 * Default implementation of [DownloadService].
 */
internal class DownloadServiceImpl(
    private val downloadApi: DownloadApi,
) : DownloadService {
    override suspend fun getDataStream(
        url: String,
    ): Result<ResponseBody> =
        downloadApi
            .getDataStream(url = url)
            .toResult()
}
