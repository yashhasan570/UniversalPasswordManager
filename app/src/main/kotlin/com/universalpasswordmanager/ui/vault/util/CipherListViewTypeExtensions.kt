package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.vault.CipherListViewType
import com.universalpasswordmanager.vault.CipherType

/**
 * Converts the [CipherListViewType] to its corresponding [CipherType].
 */
fun CipherListViewType.toSdkCipherType(): CipherType =
    when (this) {
        is CipherListViewType.Card -> CipherType.CARD
        CipherListViewType.Identity -> CipherType.IDENTITY
        is CipherListViewType.Login -> CipherType.LOGIN
        CipherListViewType.SecureNote -> CipherType.SECURE_NOTE
        CipherListViewType.SshKey -> CipherType.SSH_KEY
        is CipherListViewType.BankAccount -> CipherType.BANK_ACCOUNT
        CipherListViewType.DriversLicense -> CipherType.DRIVERS_LICENSE
        CipherListViewType.Passport -> CipherType.PASSPORT
    }
