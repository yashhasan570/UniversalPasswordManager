package com.x8bit.universalpasswordmanager.ui.platform.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled dialog for entering client certificate password and alias.
 *
 * @param onConfirmClick called when the confirm button is clicked and emits the input values.
 * @param onDismissRequest called when the user attempts to dismiss the dialog (for example by
 * tapping outside of it).
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerClientCertificateDialog(
    onConfirmClick: (alias: String, password: String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var alias by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                isEnabled = password.isNotEmpty(),
                onClick = { onConfirmClick(alias, password) },
                modifier = Modifier.testTag("AcceptAlertButton"),
            )
        },
        title = {
            Text(
                text = stringResource(Universal Password ManagerString.import_client_certificate),
                style = Universal Password ManagerTheme.typography.headlineSmall,
                modifier = Modifier.testTag("AlertTitleText"),
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(
                        Universal Password ManagerString.enter_the_client_certificate_password_and_alias,
                    ),
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("AlertContentText"),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Universal Password ManagerTextField(
                    label = stringResource(Universal Password ManagerString.alias),
                    value = alias,
                    onValueChange = { alias = it },
                    autoFocus = true,
                    cardStyle = CardStyle.Top(dividerPadding = 0.dp),
                    textFieldTestTag = "AlertClientCertificateAliasInputField",
                    modifier = Modifier.fillMaxWidth(),
                )

                Universal Password ManagerPasswordField(
                    label = stringResource(Universal Password ManagerString.password),
                    value = password,
                    onValueChange = { password = it },
                    cardStyle = CardStyle.Bottom,
                    passwordFieldTestTag = "AlertClientCertificatePasswordInputField",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        shape = Universal Password ManagerTheme.shapes.dialog,
        containerColor = Universal Password ManagerTheme.colorScheme.background.primary,
        iconContentColor = Universal Password ManagerTheme.colorScheme.icon.secondary,
        titleContentColor = Universal Password ManagerTheme.colorScheme.text.primary,
        textContentColor = Universal Password ManagerTheme.colorScheme.text.primary,
        modifier = modifier.semantics {
            testTagsAsResourceId = true
            testTag = "AlertPopup"
        },
    )
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun Universal Password ManagerClientCertificateDialogPreview() {
    Universal Password ManagerTheme {
        Universal Password ManagerClientCertificateDialog(
            onConfirmClick = { alias, password -> },
            onDismissRequest = {},
        )
    }
}
