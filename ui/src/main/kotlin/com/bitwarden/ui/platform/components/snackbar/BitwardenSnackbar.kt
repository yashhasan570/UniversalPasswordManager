package .ui.platform.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.button.color.universalpasswordmanagerOutlinedButtonColors
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText

/**
 * Custom snackbar for Universal Password Manager.
 *
 * @param universalpasswordmanagerSnackbarData The data required to display the Snackbar.
 * @param modifier The [Modifier] to be applied to the button.
 * @param windowInsets The insets to be applied to this composable. By default this will account for
 * the insets that are on the sides and bottom of the screen (Display Cutout and Navigation bars).
 * @param onDismiss The callback invoked when the Snackbar is dismissed.
 * @param onActionClick The callback invoked when the Snackbar action occurs.
 */
@Suppress("LongMethod")
@Composable
fun Universal Password ManagerSnackbar(
    universalpasswordmanagerSnackbarData: Universal Password ManagerSnackbarData,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .only(sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        .union(insets = WindowInsets.navigationBars),
    onDismiss: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(insets = windowInsets)
            .consumeWindowInsets(insets = windowInsets)
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Universal Password ManagerTheme.colorScheme.background.alert,
                    shape = Universal Password ManagerTheme.shapes.snackbar,
                )
                // I there is no explicit dismiss action, the Snackbar can be dismissed by clicking
                // anywhere on the Snackbar.
                .clickable(
                    enabled = !universalpasswordmanagerSnackbarData.withDismissAction,
                    onClick = onDismiss,
                ),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                    .weight(weight = 1f),
            ) {
                universalpasswordmanagerSnackbarData.messageHeader?.let {
                    Text(
                        text = it(),
                        color = Universal Password ManagerTheme.colorScheme.text.reversed,
                        style = Universal Password ManagerTheme.typography.titleSmall,
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = universalpasswordmanagerSnackbarData.message(),
                    color = Universal Password ManagerTheme.colorScheme.text.reversed,
                    style = if (universalpasswordmanagerSnackbarData.messageHeader != null) {
                        Universal Password ManagerTheme.typography.bodyMedium
                    } else {
                        // Upgrade the font when it is stand alone.
                        Universal Password ManagerTheme.typography.titleSmall
                    },
                )
                universalpasswordmanagerSnackbarData.actionLabel?.let {
                    Spacer(Modifier.height(12.dp))
                    Universal Password ManagerOutlinedButton(
                        label = it(),
                        onClick = onActionClick,
                        colors = universalpasswordmanagerOutlinedButtonColors(
                            contentColor = Universal Password ManagerTheme.colorScheme.text.reversed,
                            outlineColor = Universal Password ManagerTheme
                                .colorScheme
                                .outlineButton
                                .borderReversed,
                        ),
                    )
                }
            }
            if (universalpasswordmanagerSnackbarData.withDismissAction) {
                Universal Password ManagerStandardIconButton(
                    onClick = onDismiss,
                    vectorIconRes = Universal Password ManagerDrawable.ic_close,
                    contentDescription = stringResource(Universal Password ManagerString.close),
                    contentColor = Universal Password ManagerTheme.colorScheme.icon.reversed,
                    focusColor = Universal Password ManagerTheme.colorScheme.icon.reversed,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Universal Password ManagerCustomSnackbar_preview() {
    Universal Password ManagerTheme {
        Surface {
            Universal Password ManagerSnackbar(
                Universal Password ManagerSnackbarData(
                    messageHeader = "Header".asText(),
                    message = "Message".asText(),
                    actionLabel = "Action".asText(),
                    withDismissAction = true,
                ),
            )
        }
    }
}
