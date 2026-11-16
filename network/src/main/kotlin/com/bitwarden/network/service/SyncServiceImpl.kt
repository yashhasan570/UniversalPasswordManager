package com.work.service

import com.work.api.SyncApi
import com.work.model.SyncResponseJson
import com.work.util.toResult

internal class SyncServiceImpl(
    private val syncApi: SyncApi,
) : SyncService {
    override suspend fun sync(): Result<SyncResponseJson> = syncApi
        .sync()
        .toResult()

    override suspend fun getAccountRevisionDateMillis(): Result<Long> =
        syncApi
            .getAccountRevisionDateMillis()
            .toResult()
}
