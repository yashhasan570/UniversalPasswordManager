package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.vault.CipherType
import com.universalpasswordmanager.ui.vault.model.VaultItemCipherType

/**
 * Converts the [CipherType] to its corresponding [VaultItemCipherType].
 */
fun CipherType.toVaultItemCipherType(): VaultItemCipherType =
    when (this) {
        CipherType.LOGIN -> VaultItemCipherType.LOGIN
        CipherType.SECURE_NOTE -> VaultItemCipherType.SECURE_NOTE
        CipherType.CARD -> VaultItemCipherType.CARD
        CipherType.IDENTITY -> VaultItemCipherType.IDENTITY
        CipherType.SSH_KEY -> VaultItemCipherType.SSH_KEY
        CipherType.BANK_ACCOUNT -> VaultItemCipherType.BANK_ACCOUNT
        CipherType.DRIVERS_LICENSE -> VaultItemCipherType.DRIVERS_LICENSE
        CipherType.PASSPORT -> VaultItemCipherType.PASSPORT
    }
