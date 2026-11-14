package com.universalpasswordmanager.ui.platform.components.dialog.model

import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.util.Text

/**
 * Contains the data for displaying a [UniversalTwoButtonDialog].
 *
 * @property title The optional title to show.
 * @property message The message to show.
 * @property confirmButtonText The text to show on confirm button.
 * @property dismissButtonText The text to show on dismiss button.
 */
data class UniversalTwoButtonDialogData(
    val title: Text?,
    val message: Text,
    val confirmButtonText: Text,
    val dismissButtonText: Text,
)
