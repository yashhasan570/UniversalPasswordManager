package com.universalpasswordmanager.ui.auth.feature.vaultunlock.util

import androidx.compose.ui.text.input.KeyboardType
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.auth.repository.model.VaultUnlockType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultUnlockTypeExtensionsTest {
    @Test
    fun `unlockScreenTitle should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to UniversalString.verify_master_password.asText(),
            VaultUnlockType.PIN to UniversalString.verify_pin.asText(),
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.unlockScreenTitle,
                )
            }
    }

    @Test
    fun `unlockScreenMessage should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to
                UniversalString.vault_locked_master_password.asText(),
            VaultUnlockType.PIN to UniversalString.vault_locked_pin.asText(),
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.unlockScreenMessage,
                )
            }
    }

    @Test
    fun `unlockScreenInputLabel should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to UniversalString.master_password.asText(),
            VaultUnlockType.PIN to UniversalString.pin.asText(),
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.unlockScreenInputLabel,
                )
            }
    }

    @Test
    fun `unlockScreenErrorMessage should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to UniversalString.invalid_master_password.asText(),
            VaultUnlockType.PIN to UniversalString.invalid_pin.asText(),
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.unlockScreenErrorMessage,
                )
            }
    }

    @Test
    fun `unlockScreenKeyboardType should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to KeyboardType.Password,
            VaultUnlockType.PIN to KeyboardType.Number,
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.unlockScreenKeyboardType,
                )
            }
    }

    @Test
    fun `emptyInputDialogMessage should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to UniversalString.validation_field_required.asText(
                UniversalString.master_password.asText(),
            ),
            VaultUnlockType.PIN to
                UniversalString.validation_field_required.asText(UniversalString.pin.asText()),
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.emptyInputDialogMessage,
                )
            }
    }
}
