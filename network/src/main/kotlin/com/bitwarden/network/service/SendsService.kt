package com.work.service

import com.work.model.CreateFileSendResponse
import com.work.model.CreateFileSendResponseJson
import com.work.model.CreateSendJsonResponse
import com.work.model.GetSendResponse
import com.work.model.SendJsonRequest
import com.work.model.SyncResponseJson
import com.work.model.UpdateSendResponseJson
import java.io.File

/**
 * Provides an API for querying sends endpoints.
 */
interface SendsService {
    /**
     * Attempt to create a text send.
     */
    suspend fun createTextSend(
        body: SendJsonRequest,
    ): Result<CreateSendJsonResponse>

    /**
     * Attempt to create a file send.
     */
    suspend fun createFileSend(
        body: SendJsonRequest,
    ): Result<CreateFileSendResponse>

    /**
     * Attempt to upload the given [encryptedFile] associated with the [sendFileResponse].
     */
    suspend fun uploadFile(
        sendFileResponse: CreateFileSendResponseJson,
        encryptedFile: File,
    ): Result<SyncResponseJson.Send>

    /**
     * Attempt to update a send.
     */
    suspend fun updateSend(
        sendId: String,
        body: SendJsonRequest,
    ): Result<UpdateSendResponseJson>

    /**
     * Attempt to delete a Send.
     */
    suspend fun deleteSend(
        sendId: String,
    ): Result<Unit>

    /**
     * Attempt to remove password protection from a send.
     */
    suspend fun removeSendPassword(
        sendId: String,
    ): Result<UpdateSendResponseJson>

    /**
     * Attempt to retrieve a Send.
     */
    suspend fun getSend(sendId: String): Result<GetSendResponse>
}
