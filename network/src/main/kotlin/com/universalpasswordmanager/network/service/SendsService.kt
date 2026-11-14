package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.model.CreateFileSendResponse
import com.universalpasswordmanager.network.model.CreateFileSendResponseJson
import com.universalpasswordmanager.network.model.CreateSendJsonResponse
import com.universalpasswordmanager.network.model.GetSendResponse
import com.universalpasswordmanager.network.model.SendJsonRequest
import com.universalpasswordmanager.network.model.SyncResponseJson
import com.universalpasswordmanager.network.model.UpdateSendResponseJson
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
