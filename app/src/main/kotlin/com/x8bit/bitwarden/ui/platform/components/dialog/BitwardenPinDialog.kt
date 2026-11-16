package com.x8bit.universalpasswordmanager.ui.platform.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled dialog for the user to enter their PIN.
 *
 * @param onConfirmClick called when the confirm button is clicked and emits the entered PIN.
 * @param onDismissRequest called when the user attempts to dismiss the dialog (for example by
 * tapping outside of it).
 */
@Composable
fun Universal Password ManagerPinDialog(
    onConfirmClick: (pin: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            Universal Password ManagerTextButton(
                label = stringResource(id = Universal Password ManagerString.cancel),
                onClick = onDismissRequest,
                modifier = Modifier.testTag("DismissAlertButton"),
            )
        },
        confirmButton = {
            Universal Password ManagerTextButton(
                label = stringResource(id = Universal Password ManagerString.submit),
                isEnabled = pin.isNotEmpty(),
                onClick = { onConfirmClick(pin) },
                modifier = Modifier.testTag("AcceptAlertButton"),
            )
        },
        title = {
            Text(
                text = stringResource(id = Universal Password ManagerString.verify_pin),
                style = Universal Password ManagerTheme.typography.headlineSmall,
                modifier = Modifier.testTag("AlertTitleText"),
            )
        },
        text = {
            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.pin),
                value = pin,
                onValueChange = { pin = it },
                autoFocus = true,
                passwordFieldTestTag = "AlertInputField",
                cardStyle = CardStyle.Full,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        shape = Universal Password ManagerTheme.shapes.dialog,
        containerColor = Universal Password ManagerTheme.colorScheme.background.primary,
        iconContentColor = Universal Password ManagerTheme.colorScheme.icon.secondary,
        titleContentColor = Universal Password ManagerTheme.colorScheme.text.primary,
        textContentColor = Universal Password ManagerTheme.colorScheme.text.primary,
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
            testTag = "AlertPopup"
        },
    )
}
