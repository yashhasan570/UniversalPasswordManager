package com.universalpasswordmanager.data.vault.repository.model

import com.universalpasswordmanager.send.SendView

/**
 * Represents decrypted send data.
 *
 * @param sendViewList List of decrypted sends.
 */
data class SendData(
    val sendViewList: List<SendView>,
)
