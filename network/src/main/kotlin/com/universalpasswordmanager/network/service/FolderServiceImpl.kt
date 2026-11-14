package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.FoldersApi
import com.universalpasswordmanager.network.model.FolderJsonRequest
import com.universalpasswordmanager.network.model.SyncResponseJson
import com.universalpasswordmanager.network.model.UpdateFolderResponseJson
import com.universalpasswordmanager.network.model.toUniversalError
import com.universalpasswordmanager.network.util.NetworkErrorCode
import com.universalpasswordmanager.network.util.parseErrorBodyOrNull
import com.universalpasswordmanager.network.util.toResult
import kotlinx.serialization.json.Json

internal class FolderServiceImpl(
    private val foldersApi: FoldersApi,
    private val json: Json,
) : FolderService {
    override suspend fun createFolder(body: FolderJsonRequest): Result<SyncResponseJson.Folder> =
        foldersApi
            .createFolder(body = body)
            .toResult()

    override suspend fun updateFolder(
        folderId: String,
        body: FolderJsonRequest,
    ): Result<UpdateFolderResponseJson> =
        foldersApi
            .updateFolder(
                folderId = folderId,
                body = body,
            )
            .toResult()
            .map { UpdateFolderResponseJson.Success(folder = it) }
            .recoverCatching { throwable ->
                throwable
                    .toUniversalError()
                    .parseErrorBodyOrNull<UpdateFolderResponseJson.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun deleteFolder(folderId: String): Result<Unit> =
        foldersApi
            .deleteFolder(folderId = folderId)
            .toResult()

    override suspend fun getFolder(
        folderId: String,
    ): Result<SyncResponseJson.Folder> = foldersApi
        .getFolder(folderId = folderId)
        .toResult()
}
