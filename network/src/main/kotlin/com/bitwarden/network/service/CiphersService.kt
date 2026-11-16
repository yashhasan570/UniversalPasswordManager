package com.work.service

import com.work.model.ArchiveCipherResponseJson
import com.work.model.AttachmentInfo
import com.work.model.AttachmentJsonRequest
import com.work.model.AttachmentJsonResponse
import com.work.model.BulkShareCiphersJsonRequest
import com.work.model.CipherJsonRequest
import com.work.model.CipherMiniResponseJson
import com.work.model.CreateCipherInOrganizationJsonRequest
import com.work.model.CreateCipherResponseJson
import com.work.model.GetCipherResponse
import com.work.model.ImportCiphersJsonRequest
import com.work.model.ImportCiphersResponseJson
import com.work.model.ShareCipherJsonRequest
import com.work.model.SyncResponseJson
import com.work.model.UnarchiveCipherResponseJson
import com.work.model.UpdateCipherCollectionsJsonRequest
import com.work.model.UpdateCipherCollectionsResponseJson
import com.work.model.UpdateCipherResponseJson
import java.io.File

/**
 * Provides an API for querying ciphers endpoints.
 */
@Suppress("TooManyFunctions")
interface CiphersService {
    /**
     * Attempt to archive a cipher.
     */
    suspend fun archiveCipher(cipherId: String): Result<ArchiveCipherResponseJson>

    /**
     * Attempt to unarchive a cipher.
     */
    suspend fun unarchiveCipher(cipherId: String): Result<UnarchiveCipherResponseJson>

    /**
     * Attempt to create a cipher.
     */
    suspend fun createCipher(body: CipherJsonRequest): Result<CreateCipherResponseJson>

    /**
     * Attempt to create a cipher that belongs to an organization.
     */
    suspend fun createCipherInOrganization(
        body: CreateCipherInOrganizationJsonRequest,
    ): Result<CreateCipherResponseJson>

    /**
     * Attempt to upload an attachment file.
     */
    suspend fun uploadAttachment(
        attachment: AttachmentJsonResponse.Success,
        encryptedFile: File,
    ): Result<SyncResponseJson.Cipher>

    /**
     * Attempt to create an attachment.
     */
    suspend fun createAttachment(
        cipherId: String,
        body: AttachmentJsonRequest,
    ): Result<AttachmentJsonResponse>

    /**
     * Attempt to update a cipher.
     */
    suspend fun updateCipher(
        cipherId: String,
        body: CipherJsonRequest,
    ): Result<UpdateCipherResponseJson>

    /**
     * Attempt to share a cipher.
     */
    suspend fun shareCipher(
        cipherId: String,
        body: ShareCipherJsonRequest,
    ): Result<SyncResponseJson.Cipher>

    /**
     * Attempt to share multiple ciphers in bulk.
     */
    suspend fun bulkShareCiphers(
        body: BulkShareCiphersJsonRequest,
    ): Result<CipherMiniResponseJson>

    /**
     * Attempt to share an attachment.
     */
    suspend fun shareAttachment(
        cipherId: String,
        attachment: AttachmentInfo,
        organizationId: String,
        encryptedFile: File,
    ): Result<Unit>

    /**
     * Attempt to update a cipher's collections.
     */
    suspend fun updateCipherCollections(
        cipherId: String,
        body: UpdateCipherCollectionsJsonRequest,
    ): Result<UpdateCipherCollectionsResponseJson>

    /**
     * Attempt to hard delete a cipher.
     */
    suspend fun hardDeleteCipher(cipherId: String): Result<Unit>

    /**
     * Attempt to soft delete a cipher.
     */
    suspend fun softDeleteCipher(cipherId: String): Result<Unit>

    /**
     * Attempt to delete an attachment from a cipher.
     */
    suspend fun deleteCipherAttachment(
        cipherId: String,
        attachmentId: String,
    ): Result<Unit>

    /**
     * Attempt to restore a cipher.
     */
    suspend fun restoreCipher(cipherId: String): Result<SyncResponseJson.Cipher>

    /**
     * Attempt to retrieve a cipher.
     */
    suspend fun getCipher(cipherId: String): Result<GetCipherResponse>

    /**
     * Attempt to retrieve a cipher's attachment data.
     */
    suspend fun getCipherAttachment(
        cipherId: String,
        attachmentId: String,
    ): Result<SyncResponseJson.Cipher.Attachment>

    /**
     * Returns a boolean indicating if the active user has unassigned ciphers.
     */
    suspend fun hasUnassignedCiphers(): Result<Boolean>

    /**
     * Attempt to import ciphers.
     */
    suspend fun importCiphers(request: ImportCiphersJsonRequest): Result<ImportCiphersResponseJson>
}
