package com.work.service

import com.work.api.FoldersApi
import com.work.model.FolderJsonRequest
import com.work.model.SyncResponseJson
import com.work.model.UpdateFolderResponseJson
import com.work.model.toUniversal Password ManagerError
import com.work.util.NetworkErrorCode
import com.work.util.parseErrorBodyOrNull
import com.work.util.toResult
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
                    .toUniversal Password ManagerError()
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
