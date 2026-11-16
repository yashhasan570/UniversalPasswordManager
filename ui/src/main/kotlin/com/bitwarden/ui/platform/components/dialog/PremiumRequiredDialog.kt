package .ui.platform.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import .ui.platform.resource.Universal Password ManagerString

/**
 * Represents a Universal Password Manager-styled dialog for informing users that premium is required for the
 * feature they are trying to access.
 *
 * @param message The message related to the specific feature to be displayed.
 * @param isInAppUpgradeAvailable Indicates that in in-app upgrades are allowed.
 * @param onUpgradeToPremiumClick Called when the confirmation button is clicked.
 * @param onDismissRequest Called when the dialog is dismissed..
 */
@Composable
fun PremiumRequiredDialog(
    message: String,
    isInAppUpgradeAvailable: Boolean,
    onUpgradeToPremiumClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Universal Password ManagerTwoButtonDialog(
        title = stringResource(id = Universal Password ManagerString.premium_subscription_required),
        message = message,
        confirmButtonText = stringResource(
            id = if (isInAppUpgradeAvailable) {
                Universal Password ManagerString.explore_premium
            } else {
                Universal Password ManagerString.upgrade_to_premium
            },
        ),
        dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
        onConfirmClick = onUpgradeToPremiumClick,
        onDismissClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}
