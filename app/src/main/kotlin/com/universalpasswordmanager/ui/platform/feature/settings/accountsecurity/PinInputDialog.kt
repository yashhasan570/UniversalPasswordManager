package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.dialog.util.maxDialogHeight
import com.universalpasswordmanager.ui.platform.components.divider.UniversalHorizontalDivider
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

private const val MINIMUM_PIN_LENGTH: Int = 4

/**
 * A dialog for setting a user's PIN.
 *
 * @param onCancelClick A callback for when the "Cancel" button is clicked.
 * @param onSubmitClick A callback for when the "Submit" button is clicked.
 * @param onDismissRequest A callback for when the dialog is requesting to be dismissed.
 * @param isPinCreation A flag for determining if the dialog is being used for PIN creation. We
 * want to restrict PINs to numeric values but also support any existing PINs with non-numeric
 * characters.
 */
@Suppress("LongMethod")
@Composable
fun PinInputDialog(
    onCancelClick: () -> Unit,
    onSubmitClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
    isPinCreation: Boolean = false,
) {
    var pin by remember { mutableStateOf(value = "") }
    val isDoneEnabled: () -> Boolean = { !isPinCreation || pin.length >= MINIMUM_PIN_LENGTH }
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        val configuration = LocalConfiguration.current
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .semantics {
                    testTagsAsResourceId = true
                    testTag = "AlertPopup"
                }
                .requiredHeightIn(
                    max = configuration.maxDialogHeight,
                )
                // This background is necessary for the dialog to not be transparent.
                .background(
                    color = UniversalTheme.colorScheme.background.primary,
                    shape = UniversalTheme.shapes.dialog,
                ),
            horizontalAlignment = Alignment.End,
        ) {
            Spacer(modifier = Modifier.height(height = 24.dp))
            Text(
                modifier = Modifier
                    .testTag(tag = "AlertTitleText")
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                text = stringResource(id = UniversalString.enter_pin),
                color = UniversalTheme.colorScheme.text.primary,
                style = UniversalTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            if (scrollState.canScrollBackward) {
                UniversalHorizontalDivider()
            }
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(state = scrollState),
            ) {
                Text(
                    modifier = Modifier
                        .testTag(tag = "AlertContentText")
                        .fillMaxWidth(),
                    text = stringResource(id = UniversalString.set_pin_description),
                    color = UniversalTheme.colorScheme.text.primary,
                    style = UniversalTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
                UniversalTextField(
                    label = stringResource(id = UniversalString.pin),
                    value = pin,
                    autoFocus = true,
                    onValueChange = { newValue ->
                        pin = newValue.filter { it.isDigit() || !isPinCreation }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            pin
                                .takeIf { isDoneEnabled() }
                                ?.let(onSubmitClick)
                                ?: defaultKeyboardAction(ImeAction.Done)
                        },
                    ),
                    keyboardType = KeyboardType.Number,
                    textFieldTestTag = "AlertInputField",
                    cardStyle = CardStyle.Full,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
            if (scrollState.canScrollForward) {
                UniversalHorizontalDivider()
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UniversalTextButton(
                    label = stringResource(id = UniversalString.cancel),
                    onClick = onCancelClick,
                    modifier = Modifier.testTag(tag = "DismissAlertButton"),
                )

                UniversalFilledButton(
                    label = stringResource(id = UniversalString.submit),
                    isEnabled = isDoneEnabled(),
                    onClick = { onSubmitClick(pin) },
                    modifier = Modifier.testTag(tag = "AcceptAlertButton"),
                )
            }
            Spacer(modifier = Modifier.height(height = 24.dp))
        }
    }
}
