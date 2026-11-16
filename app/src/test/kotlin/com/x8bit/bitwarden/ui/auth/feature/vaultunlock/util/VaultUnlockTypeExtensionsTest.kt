package com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util

import androidx.compose.ui.text.input.KeyboardType
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.auth.repository.model.VaultUnlockType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultUnlockTypeExtensionsTest {
    @Test
    fun `unlockScreenTitle should return the correct title for each type`() {
        mapOf(
            VaultUnlockType.MASTER_PASSWORD to Universal Password ManagerString.verify_master_password.asText(),
            VaultUnlockType.PIN to Universal Password ManagerString.verify_pin.asText(),
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
                Universal Password ManagerString.vault_locked_master_password.asText(),
            VaultUnlockType.PIN to Universal Password ManagerString.vault_locked_pin.asText(),
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
            VaultUnlockType.MASTER_PASSWORD to Universal Password ManagerString.master_password.asText(),
            VaultUnlockType.PIN to Universal Password ManagerString.pin.asText(),
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
            VaultUnlockType.MASTER_PASSWORD to Universal Password ManagerString.invalid_master_password.asText(),
            VaultUnlockType.PIN to Universal Password ManagerString.invalid_pin.asText(),
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
            VaultUnlockType.MASTER_PASSWORD to Universal Password ManagerString.validation_field_required.asText(
                Universal Password ManagerString.master_password.asText(),
            ),
            VaultUnlockType.PIN to
                Universal Password ManagerString.validation_field_required.asText(Universal Password ManagerString.pin.asText()),
        )
            .forEach { (type, expected) ->
                assertEquals(
                    expected,
                    type.emptyInputDialogMessage,
                )
            }
    }
}
