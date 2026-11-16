package .authenticator.ui.authenticator.feature.qrcodescan

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.util.maxDialogHeight
import .ui.platform.components.dialog.util.maxDialogWidth
import .ui.platform.components.model.CardStyle
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Displays a dialog asking the user where they would like to save a new QR code.
 *
 * @param onSaveHereClick Invoked when the user clicks "Save here". The boolean parameter is true if
 * the user check "Save option as default".
 * @param onTakeMeToUniversal Password ManagerClick Invoked when the user clicks "Take me to Universal Password Manager". The boolean
 * parameter is true if the user checked "Save option as default".
 */
@Composable
@Suppress("LongMethod")
fun ChooseSaveLocationDialog(
    onSaveHereClick: (Boolean) -> Unit,
    onTakeMeToUniversal Password ManagerClick: (Boolean) -> Unit,
) {
    Dialog(
        onDismissRequest = { }, // Not dismissible
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        var isSaveAsDefaultChecked by remember { mutableStateOf(false) }
        val configuration = LocalConfiguration.current
        Column(
            modifier = Modifier
                .requiredHeightIn(
                    max = configuration.maxDialogHeight,
                )
                .requiredWidthIn(
                    max = configuration.maxDialogWidth,
                )
                .background(
                    color = Universal Password ManagerTheme.colorScheme.background.primary,
                    shape = Universal Password ManagerTheme.shapes.dialog,
                ),
            horizontalAlignment = Alignment.End,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                text = stringResource(Universal Password ManagerString.verification_code_created),
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                text = stringResource(Universal Password ManagerString.choose_save_location_message),
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(16.dp))
            Universal Password ManagerSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
                label = stringResource(Universal Password ManagerString.save_option_as_default),
                isChecked = isSaveAsDefaultChecked,
                onCheckedChange = { isSaveAsDefaultChecked = !isSaveAsDefaultChecked },
                cardStyle = CardStyle.Full,
            )
            Spacer(Modifier.height(16.dp))
            FlowRow(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Universal Password ManagerTextButton(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    label = stringResource(Universal Password ManagerString.save_here),
                    onClick = { onSaveHereClick.invoke(isSaveAsDefaultChecked) },
                )
                Universal Password ManagerTextButton(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    label = stringResource(Universal Password ManagerString.save_to_universalpasswordmanager),
                    isExternalLink = true,
                    onClick = { onTakeMeToUniversal Password ManagerClick.invoke(isSaveAsDefaultChecked) },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
