package .ui.platform.components.dialog.model

import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.util.Text

/**
 * Contains the data for displaying a [Universal Password ManagerTwoButtonDialog].
 *
 * @property title The optional title to show.
 * @property message The message to show.
 * @property confirmButtonText The text to show on confirm button.
 * @property dismissButtonText The text to show on dismiss button.
 */
data class Universal Password ManagerTwoButtonDialogData(
    val title: Text?,
    val message: Text,
    val confirmButtonText: Text,
    val dismissButtonText: Text,
)
