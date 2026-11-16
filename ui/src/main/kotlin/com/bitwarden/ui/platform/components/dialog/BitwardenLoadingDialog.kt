package .ui.platform.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import .ui.platform.base.util.StatusBarsAppearanceAffect
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled loading dialog that shows text and a circular progress indicator.
 *
 * This implementation uses a `Popup` because the standard `Dialog` did not work with the intended
 * designs. When using a `Dialog`, the status bar was appearing dark and the content was going below
 * the navigation bar. To ensure the loading overlay fully covers the entire screen—including the
 * status bar area, `Popup` was used with `clippingEnabled = false`. This allows it to extend
 * beyond the default bounds, ensuring a full-screen design.
 *
 * We retained the Dialog nomenclature to minimize refactor disruption, but we plan to transition
 * to Modal-based terminology in the future. (https://universalpasswordmanager.atlassian.net/browse/PM-17356)
 *
 * @param text The text to display in the dialog.
 */
@Composable
fun Universal Password ManagerLoadingDialog(
    text: String,
) {
    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
        onDismissRequest = {},
    ) {
        StatusBarsAppearanceAffect()
        Universal Password ManagerLoadingContent(
            text = text,
            modifier = Modifier
                .semantics {
                    testTagsAsResourceId = true
                    testTag = "AlertPopup"
                }
                .fillMaxSize()
                .background(
                    color = Universal Password ManagerTheme.colorScheme.background.secondary.copy(alpha = 0.90f),
                ),
        )
    }
}

@Preview
@Composable
private fun Universal Password ManagerLoadingDialog_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerLoadingDialog(text = "Loading...")
    }
}
