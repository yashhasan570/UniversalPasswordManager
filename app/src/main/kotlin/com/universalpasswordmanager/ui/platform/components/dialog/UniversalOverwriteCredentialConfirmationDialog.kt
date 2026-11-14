package com.universalpasswordmanager.ui.platform.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.resource.UniversalString

/**
 * A reusable dialog for confirming whether the user wants to overwrite an existing credential.
 *
 * @param onConfirmClick A callback for when the overwrite confirmation button is clicked.
 * @param onDismissRequest A callback for when the dialog is requesting dismissal.
 */
@Suppress("MaxLineLength")
@Composable
fun UniversalOverwriteCredentialConfirmationDialog(
    title: String,
    message: String,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    UniversalTwoButtonDialog(
        title = title,
        message = message,
        confirmButtonText = stringResource(id = UniversalString.okay),
        dismissButtonText = stringResource(id = UniversalString.cancel),
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}
