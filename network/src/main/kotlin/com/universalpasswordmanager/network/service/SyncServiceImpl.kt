package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.SyncApi
import com.universalpasswordmanager.network.model.SyncResponseJson
import com.universalpasswordmanager.network.util.toResult

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
