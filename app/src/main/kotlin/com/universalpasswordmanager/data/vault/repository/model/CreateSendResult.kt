package com.universalpasswordmanager.data.vault.repository.model

import com.universalpasswordmanager.send.SendView

/**
 * Models result of creating a send.
 */
sealed class CreateSendResult {

    /**
     * Send created successfully and contains the decrypted [SendView].
     */
    data class Success(val sendView: SendView) : CreateSendResult()

    /**
     * Generic error while creating a send.
     */
    data class Error(val message: String?, val error: Throwable?) : CreateSendResult()
}
