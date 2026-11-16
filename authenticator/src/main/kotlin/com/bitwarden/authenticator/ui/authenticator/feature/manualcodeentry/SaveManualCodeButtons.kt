package .authenticator.ui.authenticator.feature.manualcodeentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.resource.Universal Password ManagerString

/**
 * Displays save buttons for saving a manually entered code.
 *
 * @param state State of the buttons to show.
 * @param onSaveLocallyClick Callback invoked when the user clicks save locally.
 * @param onSaveToUniversal Password ManagerClick Callback invoked when the user clicks save to Universal Password Manager.
 * @param modifier The modifier to be applied to the composable.
 */
@Composable
fun SaveManualCodeButtons(
    state: ManualCodeEntryState.ButtonState,
    onSaveLocallyClick: () -> Unit,
    onSaveToUniversal Password ManagerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ManualCodeEntryState.ButtonState.LocalOnly -> {
            Universal Password ManagerFilledButton(
                label = stringResource(id = Universal Password ManagerString.add_code),
                onClick = onSaveLocallyClick,
                modifier = modifier.testTag(tag = "AddCodeButton"),
            )
        }

        ManualCodeEntryState.ButtonState.SaveLocallyPrimary -> {
            Column(modifier = modifier) {
                Universal Password ManagerFilledButton(
                    label = stringResource(id = Universal Password ManagerString.save_here),
                    onClick = onSaveLocallyClick,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                Universal Password ManagerOutlinedButton(
                    label = stringResource(Universal Password ManagerString.save_to_universalpasswordmanager),
                    onClick = onSaveToUniversal Password ManagerClick,
                    isExternalLink = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        ManualCodeEntryState.ButtonState.SaveToUniversal Password ManagerPrimary -> {
            Column(modifier = modifier) {
                Universal Password ManagerFilledButton(
                    label = stringResource(id = Universal Password ManagerString.save_to_universalpasswordmanager),
                    isExternalLink = true,
                    onClick = onSaveToUniversal Password ManagerClick,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                Universal Password ManagerOutlinedButton(
                    label = stringResource(Universal Password ManagerString.save_here),
                    onClick = onSaveLocallyClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
