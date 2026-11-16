package .ui.platform.components.account.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import .ui.platform.components.account.model.AccountSummary
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.resource.Universal Password ManagerString

/**
 * A reusable dialog for confirming whether the user wants to log out.
 *
 * @param onDismissRequest A callback for when the dialog is requesting dismissal.
 * @param onConfirmClick A callback for when the log out confirmation button is clicked.
 * @param accountSummary Optional account information that may be used to provide additional
 * information.
 */
@Composable
fun Universal Password ManagerLogoutConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    accountSummary: AccountSummary? = null,
) {
    val baseConfirmationMessage = stringResource(id = Universal Password ManagerString.logout_confirmation)
    val message = accountSummary
        ?.let { "$baseConfirmationMessage\n\n${it.email}\n${it.environmentLabel}" }
        ?: baseConfirmationMessage
    Universal Password ManagerTwoButtonDialog(
        title = stringResource(id = Universal Password ManagerString.log_out),
        message = message,
        confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
        onConfirmClick = onConfirmClick,
        dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
        onDismissClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}
