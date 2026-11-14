package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.model.VaultItemCipherType

/**
 * The message to display in a snackbar after a cipher of this type is saved.
 */
val VaultItemCipherType.savedSnackbarMessage: Text
    get() = when (this) {
        VaultItemCipherType.LOGIN -> UniversalString.login_saved.asText()
        VaultItemCipherType.CARD -> UniversalString.card_saved.asText()
        VaultItemCipherType.IDENTITY -> UniversalString.identity_saved.asText()
        VaultItemCipherType.SECURE_NOTE -> UniversalString.secure_note_saved.asText()
        VaultItemCipherType.SSH_KEY -> UniversalString.ssh_key_saved.asText()
        VaultItemCipherType.BANK_ACCOUNT -> UniversalString.bank_account_saved.asText()
        VaultItemCipherType.DRIVERS_LICENSE -> UniversalString.license_saved.asText()
        VaultItemCipherType.PASSPORT -> UniversalString.passport_saved.asText()
    }
