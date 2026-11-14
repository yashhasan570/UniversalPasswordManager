package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.model.VaultItemCipherType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultItemCipherTypeExtensionsTest {

    @Test
    fun `savedSnackbarMessage should return the correct value for each VaultItemCipherType`() {
        mapOf(
            VaultItemCipherType.LOGIN to UniversalString.login_saved.asText(),
            VaultItemCipherType.CARD to UniversalString.card_saved.asText(),
            VaultItemCipherType.IDENTITY to UniversalString.identity_saved.asText(),
            VaultItemCipherType.SECURE_NOTE to UniversalString.secure_note_saved.asText(),
            VaultItemCipherType.SSH_KEY to UniversalString.ssh_key_saved.asText(),
            VaultItemCipherType.BANK_ACCOUNT to UniversalString.bank_account_saved.asText(),
            VaultItemCipherType.DRIVERS_LICENSE to UniversalString.license_saved.asText(),
            VaultItemCipherType.PASSPORT to UniversalString.passport_saved.asText(),
        )
            .forEach { (type, message) ->
                assertEquals(
                    message,
                    type.savedSnackbarMessage,
                )
            }
    }
}
