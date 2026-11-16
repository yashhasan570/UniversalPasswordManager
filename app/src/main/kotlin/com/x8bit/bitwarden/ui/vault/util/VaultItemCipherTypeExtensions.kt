package com.x8bit.universalpasswordmanager.ui.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemCipherType

/**
 * The message to display in a snackbar after a cipher of this type is saved.
 */
val VaultItemCipherType.savedSnackbarMessage: Text
    get() = when (this) {
        VaultItemCipherType.LOGIN -> Universal Password ManagerString.login_saved.asText()
        VaultItemCipherType.CARD -> Universal Password ManagerString.card_saved.asText()
        VaultItemCipherType.IDENTITY -> Universal Password ManagerString.identity_saved.asText()
        VaultItemCipherType.SECURE_NOTE -> Universal Password ManagerString.secure_note_saved.asText()
        VaultItemCipherType.SSH_KEY -> Universal Password ManagerString.ssh_key_saved.asText()
        VaultItemCipherType.BANK_ACCOUNT -> Universal Password ManagerString.bank_account_saved.asText()
        VaultItemCipherType.DRIVERS_LICENSE -> Universal Password ManagerString.license_saved.asText()
        VaultItemCipherType.PASSPORT -> Universal Password ManagerString.passport_saved.asText()
    }
