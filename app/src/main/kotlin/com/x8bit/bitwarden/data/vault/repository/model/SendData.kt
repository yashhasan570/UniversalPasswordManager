package com.x8bit.universalpasswordmanager.data.vault.repository.model

import .send.SendView

/**
 * Represents decrypted send data.
 *
 * @param sendViewList List of decrypted sends.
 */
data class SendData(
    val sendViewList: List<SendView>,
)
