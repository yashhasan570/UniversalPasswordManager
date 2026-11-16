package .ui.platform.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.util.maxDialogHeight
import .ui.platform.components.dialog.util.maxDialogWidth
import .ui.platform.components.divider.Universal Password ManagerHorizontalDivider
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled dialog.
 *
 * @param title The optional title to be displayed by the dialog.
 * @param message The message to be displayed under the [title] by the dialog.
 * @param confirmButtonLabel The label for the confirm button.
 * @param throwable An optional [Throwable] that can be shared from this dialog.
 * @param onDismissRequest A lambda that is invoked when the user has requested to dismiss the
 * dialog, whether by tapping "OK", tapping outside the dialog, or pressing the back button.
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerBasicDialog(
    title: String?,
    message: String,
    confirmButtonLabel: String = stringResource(id = Universal Password ManagerString.okay),
    onDismissRequest: () -> Unit,
    throwable: Throwable? = null,
    intentManager: IntentManager = LocalIntentManager.current,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        val configuration = LocalConfiguration.current
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .semantics {
                    testTagsAsResourceId = true
                    testTag = "AlertPopup"
                }
                .requiredHeightIn(max = configuration.maxDialogHeight)
                .requiredWidthIn(max = configuration.maxDialogWidth)
                .background(
                    color = Universal Password ManagerTheme.colorScheme.background.primary,
                    shape = Universal Password ManagerTheme.shapes.dialog,
                ),
            horizontalAlignment = Alignment.End,
        ) {
            Spacer(modifier = Modifier.height(height = 24.dp))
            title?.let {
                Text(
                    text = it,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    style = Universal Password ManagerTheme.typography.headlineSmall,
                    modifier = Modifier
                        .testTag(tag = "AlertTitleText")
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
            if (scrollState.canScrollBackward) {
                Universal Password ManagerHorizontalDivider()
            }
            Text(
                text = message,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                modifier = Modifier
                    .testTag(tag = "AlertContentText")
                    .weight(weight = 1f, fill = false)
                    .verticalScroll(state = scrollState)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
            )
            if (scrollState.canScrollForward) {
                Universal Password ManagerHorizontalDivider()
            }
            Spacer(modifier = Modifier.height(height = 24.dp))

            FlowRow(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                throwable?.let { error ->
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.share_error_details),
                        onClick = {
                            intentManager.shareErrorReport(throwable = error)
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .testTag(tag = "ShareErrorDetailsAlertButton")
                            .padding(horizontal = 4.dp),
                    )
                }
                Universal Password ManagerTextButton(
                    label = confirmButtonLabel,
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .testTag(tag = "AcceptAlertButton")
                        .padding(horizontal = 4.dp),
                )
            }
            Spacer(modifier = Modifier.height(height = 24.dp))
        }
    }
}

@Preview
@Composable
private fun Universal Password ManagerBasicDialog_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerBasicDialog(
            title = "An error has occurred",
            message = "Username or password is incorrect. Try again.",
            onDismissRequest = {},
        )
    }
}
