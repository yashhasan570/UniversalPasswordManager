package com.universalpasswordmanager.data.vault.manager

import androidx.credentials.providerevents.exception.ImportCredentialsUnknownErrorException
import com.universalpasswordmanager.core.data.util.asFailure
import com.universalpasswordmanager.core.data.util.asSuccess
import com.universalpasswordmanager.core.data.util.flatMap
import com.universalpasswordmanager.cxf.model.CredentialExchangePayload
import com.universalpasswordmanager.cxf.parser.CredentialExchangePayloadParser
import com.universalpasswordmanager.network.model.ImportCiphersJsonRequest
import com.universalpasswordmanager.network.model.ImportCiphersResponseJson
import com.universalpasswordmanager.network.service.CiphersService
import com.universalpasswordmanager.vault.CipherType
import com.universalpasswordmanager.vault.EncryptionContext
import com.universalpasswordmanager.data.platform.manager.PolicyManager
import com.universalpasswordmanager.data.platform.manager.util.hasRestrictItemTypes
import com.universalpasswordmanager.data.vault.datasource.sdk.VaultSdkSource
import com.universalpasswordmanager.data.vault.manager.model.ImportCxfPayloadResult
import com.universalpasswordmanager.data.vault.manager.model.SyncVaultDataResult
import com.universalpasswordmanager.data.vault.repository.util.toEncryptedNetworkCipher
import timber.log.Timber

/**
 * Default implementation of [CredentialExchangeImportManager].
 */
class CredentialExchangeImportManagerImpl(
    private val vaultSdkSource: VaultSdkSource,
    private val ciphersService: CiphersService,
    private val vaultSyncManager: VaultSyncManager,
    private val policyManager: PolicyManager,
    private val credentialExchangePayloadParser: CredentialExchangePayloadParser,
) : CredentialExchangeImportManager {

    override suspend fun importCxfPayload(
        userId: String,
        payload: String,
    ): ImportCxfPayloadResult =
        when (val exportResponse = credentialExchangePayloadParser.parse(payload)) {
            is CredentialExchangePayload.Importable -> {
                import(
                    userId = userId,
                    accountsJsonList = exportResponse.accountsJsonList,
                )
            }

            CredentialExchangePayload.NoItems -> {
                ImportCxfPayloadResult.NoItems
            }

            is CredentialExchangePayload.Error -> {
                ImportCxfPayloadResult.Error(exportResponse.throwable)
            }
        }

    private suspend fun import(
        userId: String,
        accountsJsonList: List<String>,
    ): ImportCxfPayloadResult {
        val allCiphers = accountsJsonList.flatMap { accountJson ->
            vaultSdkSource
                .importCxf(userId = userId, payload = accountJson)
                .getOrElse { return ImportCxfPayloadResult.Error(error = it) }
        }

        // Filter out card ciphers if RESTRICT_ITEM_TYPES policy is active
        val filteredCipherList = if (policyManager.hasRestrictItemTypes()) {
            allCiphers.filter { context -> context.cipher.type != CipherType.CARD }
        } else {
            allCiphers
        }

        if (filteredCipherList.isEmpty()) {
            return ImportCxfPayloadResult.NoItems
        }

        return uploadCiphers(ciphers = filteredCipherList)
            .map { syncVault(it) }
            .fold(
                onSuccess = { it },
                onFailure = { ImportCxfPayloadResult.Error(error = it) },
            )
    }

    private suspend fun uploadCiphers(
        ciphers: List<EncryptionContext>,
    ): Result<ImportCxfPayloadResult.Success> {
        val request = ImportCiphersJsonRequest(
            ciphers = ciphers.map { it.toEncryptedNetworkCipher() },
            folders = emptyList(),
            folderRelationships = emptyList(),
        )
        return ciphersService
            .importCiphers(request)
            .flatMap { response ->
                when (response) {
                    is ImportCiphersResponseJson.Invalid -> {
                        Timber.w(
                            "Import ciphers validation failed: %s",
                            response.validationErrors,
                        )
                        ImportCredentialsUnknownErrorException().asFailure()
                    }

                    is ImportCiphersResponseJson.Success -> {
                        ImportCxfPayloadResult.Success(itemCount = ciphers.size).asSuccess()
                    }
                }
            }
    }

    private suspend fun syncVault(result: ImportCxfPayloadResult): ImportCxfPayloadResult =
        when (val syncResult = vaultSyncManager.syncForResult(forced = true)) {
            is SyncVaultDataResult.Success -> result
            is SyncVaultDataResult.Error -> {
                ImportCxfPayloadResult.SyncFailed(error = syncResult.throwable)
            }
        }
}
