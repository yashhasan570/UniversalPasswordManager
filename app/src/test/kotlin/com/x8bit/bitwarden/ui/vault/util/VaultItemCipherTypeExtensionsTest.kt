package com.x8bit.universalpasswordmanager.ui.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemCipherType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultItemCipherTypeExtensionsTest {

    @Test
    fun `savedSnackbarMessage should return the correct value for each VaultItemCipherType`() {
        mapOf(
            VaultItemCipherType.LOGIN to Universal Password ManagerString.login_saved.asText(),
            VaultItemCipherType.CARD to Universal Password ManagerString.card_saved.asText(),
            VaultItemCipherType.IDENTITY to Universal Password ManagerString.identity_saved.asText(),
            VaultItemCipherType.SECURE_NOTE to Universal Password ManagerString.secure_note_saved.asText(),
            VaultItemCipherType.SSH_KEY to Universal Password ManagerString.ssh_key_saved.asText(),
            VaultItemCipherType.BANK_ACCOUNT to Universal Password ManagerString.bank_account_saved.asText(),
            VaultItemCipherType.DRIVERS_LICENSE to Universal Password ManagerString.license_saved.asText(),
            VaultItemCipherType.PASSPORT to Universal Password ManagerString.passport_saved.asText(),
        )
            .forEach { (type, message) ->
                assertEquals(
                    message,
                    type.savedSnackbarMessage,
                )
            }
    }
}
