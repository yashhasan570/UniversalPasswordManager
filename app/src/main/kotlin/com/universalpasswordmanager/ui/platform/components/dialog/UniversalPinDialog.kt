package com.universalpasswordmanager.ui.platform.components.dialog

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
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Represents a Universal-styled dialog for the user to enter their PIN.
 *
 * @param onConfirmClick called when the confirm button is clicked and emits the entered PIN.
 * @param onDismissRequest called when the user attempts to dismiss the dialog (for example by
 * tapping outside of it).
 */
@Composable
fun UniversalPinDialog(
    onConfirmClick: (pin: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            UniversalTextButton(
                label = stringResource(id = UniversalString.cancel),
                onClick = onDismissRequest,
                modifier = Modifier.testTag("DismissAlertButton"),
            )
        },
        confirmButton = {
            UniversalTextButton(
                label = stringResource(id = UniversalString.submit),
                isEnabled = pin.isNotEmpty(),
                onClick = { onConfirmClick(pin) },
                modifier = Modifier.testTag("AcceptAlertButton"),
            )
        },
        title = {
            Text(
                text = stringResource(id = UniversalString.verify_pin),
                style = UniversalTheme.typography.headlineSmall,
                modifier = Modifier.testTag("AlertTitleText"),
            )
        },
        text = {
            UniversalPasswordField(
                label = stringResource(id = UniversalString.pin),
                value = pin,
                onValueChange = { pin = it },
                autoFocus = true,
                passwordFieldTestTag = "AlertInputField",
                cardStyle = CardStyle.Full,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        shape = UniversalTheme.shapes.dialog,
        containerColor = UniversalTheme.colorScheme.background.primary,
        iconContentColor = UniversalTheme.colorScheme.icon.secondary,
        titleContentColor = UniversalTheme.colorScheme.text.primary,
        textContentColor = UniversalTheme.colorScheme.text.primary,
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
            testTag = "AlertPopup"
        },
    )
}
