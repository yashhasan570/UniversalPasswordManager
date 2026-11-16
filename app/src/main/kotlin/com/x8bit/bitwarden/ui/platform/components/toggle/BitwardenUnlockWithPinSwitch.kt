package com.x8bit.universalpasswordmanager.ui.platform.components.toggle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.model.CardStyle
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.PinInputDialog

/**
 * Displays a switch for enabling or disabling unlock with pin functionality.
 *
 * @param isUnlockWithPasswordEnabled Indicates whether the password unlocking is enabled.
 * @param isUnlockWithPinEnabled Indicates whether the pin unlocking is enabled.
 * @param onUnlockWithPinToggleAction Callback that is invoked when the current state of the switch
 * changes.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier The [Modifier] to be applied to the switch.
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerUnlockWithPinSwitch(
    isUnlockWithPasswordEnabled: Boolean,
    isUnlockWithPinEnabled: Boolean,
    onUnlockWithPinToggleAction: (UnlockWithPinState) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    var shouldShowPinInputDialog by rememberSaveable { mutableStateOf(value = false) }
    var shouldShowPinConfirmationDialog by rememberSaveable { mutableStateOf(value = false) }
    var pin by remember { mutableStateOf(value = "") }
    Universal Password ManagerSwitch(
        label = stringResource(id = Universal Password ManagerString.unlock_with_pin),
        isChecked = isUnlockWithPinEnabled,
        onCheckedChange = { isChecked ->
            if (isChecked) {
                onUnlockWithPinToggleAction(UnlockWithPinState.PendingEnabled)
                shouldShowPinInputDialog = true
            } else {
                onUnlockWithPinToggleAction(UnlockWithPinState.Disabled)
            }
        },
        cardStyle = cardStyle,
        modifier = modifier,
    )

    when {
        shouldShowPinInputDialog -> {
            PinInputDialog(
                onCancelClick = {
                    shouldShowPinInputDialog = false
                    onUnlockWithPinToggleAction(UnlockWithPinState.Disabled)
                    pin = ""
                },
                onSubmitClick = {
                    pin = it
                    if (pin.isNotEmpty()) {
                        shouldShowPinInputDialog = false
                        if (isUnlockWithPasswordEnabled) {
                            shouldShowPinConfirmationDialog = true
                            onUnlockWithPinToggleAction(UnlockWithPinState.PendingEnabled)
                        } else {
                            onUnlockWithPinToggleAction(
                                UnlockWithPinState.Enabled(
                                    pin = pin,
                                    shouldRequireMasterPasswordOnRestart = false,
                                ),
                            )
                        }
                    } else {
                        shouldShowPinInputDialog = false
                        onUnlockWithPinToggleAction(UnlockWithPinState.Disabled)
                    }
                },
                onDismissRequest = {
                    shouldShowPinInputDialog = false
                    onUnlockWithPinToggleAction(UnlockWithPinState.Disabled)
                    pin = ""
                },
                isPinCreation = true,
            )
        }

        shouldShowPinConfirmationDialog -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.require_master_password_on_app_restart),
                message = stringResource(id = Universal Password ManagerString.pin_require_master_password_restart),
                confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
                dismissButtonText = stringResource(id = Universal Password ManagerString.no),
                onConfirmClick = {
                    shouldShowPinConfirmationDialog = false
                    onUnlockWithPinToggleAction(
                        UnlockWithPinState.Enabled(
                            pin = pin,
                            shouldRequireMasterPasswordOnRestart = true,
                        ),
                    )
                    pin = ""
                },
                onDismissClick = {
                    shouldShowPinConfirmationDialog = false
                    onUnlockWithPinToggleAction(
                        UnlockWithPinState.Enabled(
                            pin = pin,
                            shouldRequireMasterPasswordOnRestart = false,
                        ),
                    )
                    pin = ""
                },
                onDismissRequest = {
                    // Dismissing the dialog is the same as requiring a master password
                    // confirmation.
                    shouldShowPinConfirmationDialog = false
                    onUnlockWithPinToggleAction(
                        UnlockWithPinState.Enabled(
                            pin = pin,
                            shouldRequireMasterPasswordOnRestart = true,
                        ),
                    )
                    pin = ""
                },
            )
        }
    }
}

/**
 * User toggled the unlock with pin switch.
 */
sealed class UnlockWithPinState {
    /**
     * Whether the action represents PIN unlocking being enabled.
     */
    abstract val isUnlockWithPinEnabled: Boolean

    /**
     * The toggle was disabled.
     */
    data object Disabled : UnlockWithPinState() {
        override val isUnlockWithPinEnabled: Boolean get() = false
    }

    /**
     * The toggle was enabled but the behavior is pending confirmation.
     */
    data object PendingEnabled : UnlockWithPinState() {
        override val isUnlockWithPinEnabled: Boolean get() = true
    }

    /**
     * The toggle was enabled and the user's [pin] and [shouldRequireMasterPasswordOnRestart]
     * preference were confirmed.
     */
    data class Enabled(
        val pin: String,
        val shouldRequireMasterPasswordOnRestart: Boolean,
    ) : UnlockWithPinState() {
        override val isUnlockWithPinEnabled: Boolean get() = true
    }
}
