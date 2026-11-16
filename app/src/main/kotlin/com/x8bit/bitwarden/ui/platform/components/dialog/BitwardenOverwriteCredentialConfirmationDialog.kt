package com.x8bit.universalpasswordmanager.ui.platform.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.resource.Universal Password ManagerString

/**
 * A reusable dialog for confirming whether the user wants to overwrite an existing credential.
 *
 * @param onConfirmClick A callback for when the overwrite confirmation button is clicked.
 * @param onDismissRequest A callback for when the dialog is requesting dismissal.
 */
@Suppress("MaxLineLength")
@Composable
fun Universal Password ManagerOverwriteCredentialConfirmationDialog(
    title: String,
    message: String,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Universal Password ManagerTwoButtonDialog(
        title = title,
        message = message,
        confirmButtonText = stringResource(id = Universal Password ManagerString.okay),
        dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}
