package com.work.service

import androidx.core.net.toUri
import com.work.api.AzureApi
import com.work.api.SendsApi
import com.work.model.CreateFileSendResponse
import com.work.model.CreateFileSendResponseJson
import com.work.model.CreateSendJsonResponse
import com.work.model.FileUploadType
import com.work.model.GetSendResponse
import com.work.model.SendJsonRequest
import com.work.model.SyncResponseJson
import com.work.model.UpdateSendResponseJson
import com.work.model.toUniversal Password ManagerError
import com.work.util.NetworkErrorCode
import com.work.util.isErrorCode
import com.work.util.parseErrorBodyOrNull
import com.work.util.toResult
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.Clock
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Default implementation of the [SendsService].
 */
internal class SendsServiceImpl(
    private val azureApi: AzureApi,
    private val sendsApi: SendsApi,
    private val clock: Clock,
    private val json: Json,
) : SendsService {
    override suspend fun createTextSend(
        body: SendJsonRequest,
    ): Result<CreateSendJsonResponse> =
        sendsApi
            .createTextSend(body = body)
            .toResult()
            .map { CreateSendJsonResponse.Success(send = it) }
            .recoverCatching { throwable ->
                throwable.toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<CreateSendJsonResponse.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun createFileSend(
        body: SendJsonRequest,
    ): Result<CreateFileSendResponse> =
        sendsApi
            .createFileSend(body = body)
            .toResult()
            .map { CreateFileSendResponse.Success(it) }
            .recoverCatching { throwable ->
                throwable.toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<CreateFileSendResponse.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun updateSend(
        sendId: String,
        body: SendJsonRequest,
    ): Result<UpdateSendResponseJson> =
        sendsApi
            .updateSend(
                sendId = sendId,
                body = body,
            )
            .toResult()
            .map { UpdateSendResponseJson.Success(send = it) }
            .recoverCatching { throwable ->
                throwable
                    .toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<UpdateSendResponseJson.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun uploadFile(
        sendFileResponse: CreateFileSendResponseJson,
        encryptedFile: File,
    ): Result<SyncResponseJson.Send> {
        val send = sendFileResponse.sendResponse
        return when (sendFileResponse.fileUploadType) {
            FileUploadType.DIRECT -> {
                sendsApi.uploadFile(
                    sendId = requireNotNull(send.id),
                    fileId = requireNotNull(send.file?.id),
                    body = MultipartBody
                        .Builder(
                            boundary = "--BWMobileFormBoundary${clock.instant().toEpochMilli()}",
                        )
                        .setType(type = MultipartBody.FORM)
                        .addPart(
                            part = MultipartBody.Part.createFormData(
                                body = encryptedFile.asRequestBody(
                                    contentType = "application/octet-stream".toMediaType(),
                                ),
                                name = "data",
                                filename = send.file.fileName,
                            ),
                        )
                        .build(),
                )
            }

            FileUploadType.AZURE -> {
                azureApi.uploadAzureBlob(
                    url = sendFileResponse.url,
                    date = DateTimeFormatter
                        .RFC_1123_DATE_TIME
                        .withZone(ZoneOffset.UTC)
                        .format(clock.instant()),
                    version = sendFileResponse.url.toUri().getQueryParameter("sv"),
                    body = encryptedFile.asRequestBody(),
                )
            }
        }
            .toResult()
            .onFailure { sendsApi.deleteSend(send.id) }
            .map { send }
    }

    override suspend fun deleteSend(sendId: String): Result<Unit> =
        sendsApi
            .deleteSend(sendId = sendId)
            .toResult()

    override suspend fun removeSendPassword(sendId: String): Result<UpdateSendResponseJson> =
        sendsApi
            .removeSendPassword(sendId = sendId)
            .toResult()
            .map { UpdateSendResponseJson.Success(send = it) }
            .recoverCatching { throwable ->
                throwable
                    .toUniversal Password ManagerError()
                    .parseErrorBodyOrNull<UpdateSendResponseJson.Invalid>(
                        code = NetworkErrorCode.BAD_REQUEST,
                        json = json,
                    )
                    ?: throw throwable
            }

    override suspend fun getSend(
        sendId: String,
    ): Result<GetSendResponse> =
        sendsApi
            .getSend(sendId = sendId)
            .toResult()
            .map { GetSendResponse.Success(send = it) }
            .recoverCatching { throwable ->
                val universalpasswordmanagerError = throwable.toUniversal Password ManagerError()
                if (universalpasswordmanagerError.isErrorCode(code = NetworkErrorCode.NOT_FOUND)) {
                    GetSendResponse.NotFound(throwable = throwable)
                } else {
                    throw throwable
                }
            }
}
