package .ui.platform.components.account.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import .ui.platform.components.account.model.AccountSummary
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.resource.Universal Password ManagerString

/**
 * A reusable dialog for confirming whether the user wants to remove their account.
 *
 * @param onDismissRequest A callback for when the dialog is requesting dismissal.
 * @param onConfirmClick A callback for when the log-out confirmation button is clicked.
 * @param accountSummary Optional account information that may be used to provide additional
 * information.
 */
@Composable
fun Universal Password ManagerRemovalConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    accountSummary: AccountSummary? = null,
) {
    Universal Password ManagerTwoButtonDialog(
        title = stringResource(id = Universal Password ManagerString.remove_account),
        message = removalConfirmationMessage(accountSummary = accountSummary),
        confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
        onConfirmClick = onConfirmClick,
        dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
        onDismissClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun removalConfirmationMessage(accountSummary: AccountSummary?): String {
    val baseConfirmationMessage = stringResource(id = Universal Password ManagerString.remove_account_confirmation)
    return accountSummary
        ?.let { "$baseConfirmationMessage\n\n${it.email}\n${it.environmentLabel}" }
        ?: baseConfirmationMessage
}
