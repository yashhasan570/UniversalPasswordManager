package com.universalpasswordmanager.ui.platform.components.snackbar

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
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.button.color.bitwardenOutlinedButtonColors
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText

/**
 * Custom snackbar for Universal.
 *
 * @param bitwardenSnackbarData The data required to display the Snackbar.
 * @param modifier The [Modifier] to be applied to the button.
 * @param windowInsets The insets to be applied to this composable. By default this will account for
 * the insets that are on the sides and bottom of the screen (Display Cutout and Navigation bars).
 * @param onDismiss The callback invoked when the Snackbar is dismissed.
 * @param onActionClick The callback invoked when the Snackbar action occurs.
 */
@Suppress("LongMethod")
@Composable
fun UniversalSnackbar(
    bitwardenSnackbarData: UniversalSnackbarData,
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
                    color = UniversalTheme.colorScheme.background.alert,
                    shape = UniversalTheme.shapes.snackbar,
                )
                // I there is no explicit dismiss action, the Snackbar can be dismissed by clicking
                // anywhere on the Snackbar.
                .clickable(
                    enabled = !bitwardenSnackbarData.withDismissAction,
                    onClick = onDismiss,
                ),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                    .weight(weight = 1f),
            ) {
                bitwardenSnackbarData.messageHeader?.let {
                    Text(
                        text = it(),
                        color = UniversalTheme.colorScheme.text.reversed,
                        style = UniversalTheme.typography.titleSmall,
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = bitwardenSnackbarData.message(),
                    color = UniversalTheme.colorScheme.text.reversed,
                    style = if (bitwardenSnackbarData.messageHeader != null) {
                        UniversalTheme.typography.bodyMedium
                    } else {
                        // Upgrade the font when it is stand alone.
                        UniversalTheme.typography.titleSmall
                    },
                )
                bitwardenSnackbarData.actionLabel?.let {
                    Spacer(Modifier.height(12.dp))
                    UniversalOutlinedButton(
                        label = it(),
                        onClick = onActionClick,
                        colors = bitwardenOutlinedButtonColors(
                            contentColor = UniversalTheme.colorScheme.text.reversed,
                            outlineColor = UniversalTheme
                                .colorScheme
                                .outlineButton
                                .borderReversed,
                        ),
                    )
                }
            }
            if (bitwardenSnackbarData.withDismissAction) {
                UniversalStandardIconButton(
                    onClick = onDismiss,
                    vectorIconRes = UniversalDrawable.ic_close,
                    contentDescription = stringResource(UniversalString.close),
                    contentColor = UniversalTheme.colorScheme.icon.reversed,
                    focusColor = UniversalTheme.colorScheme.icon.reversed,
                )
            }
        }
    }
}

@Preview
@Composable
private fun UniversalCustomSnackbar_preview() {
    UniversalTheme {
        Surface {
            UniversalSnackbar(
                UniversalSnackbarData(
                    messageHeader = "Header".asText(),
                    message = "Message".asText(),
                    actionLabel = "Action".asText(),
                    withDismissAction = true,
                ),
            )
        }
    }
}
