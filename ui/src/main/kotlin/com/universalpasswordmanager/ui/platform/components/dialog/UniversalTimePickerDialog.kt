package com.universalpasswordmanager.ui.platform.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * A custom composable representing a dialog that displays the time picker dialog.
 *
 * @param initialHour The initial hour to display.
 * @param initialMinute The initial minute to display.
 * @param onTimeSelect The callback to be invoked when a new time is selected.
 * @param onDismissRequest The callback to be invoked when a time has been selected.
 * @param is24Hour Indicates if the time selector should use a 24 hour format or a 12 hour format
 * with AM/PM.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelect: (hour: Int, minute: Int) -> Unit,
    onDismissRequest: () -> Unit,
    is24Hour: Boolean,
) {
    var showTimeInput by rememberSaveable { mutableStateOf(value = false) }
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = is24Hour,
    )
    TimePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            UniversalTextButton(
                modifier = Modifier.testTag(tag = "AcceptAlertButton"),
                label = stringResource(id = UniversalString.okay),
                onClick = { onTimeSelect(timePickerState.hour, timePickerState.minute) },
            )
        },
        dismissButton = {
            UniversalTextButton(
                modifier = Modifier.testTag(tag = "DismissAlertButton"),
                label = stringResource(id = UniversalString.cancel),
                onClick = onDismissRequest,
            )
        },
        inputToggleButton = {
            UniversalStandardIconButton(
                vectorIconRes = UniversalDrawable.ic_keyboard,
                contentDescription = stringResource(id = UniversalString.switch_input_mode),
                onClick = { showTimeInput = !showTimeInput },
            )
        },
    ) {
        val modifier = Modifier.weight(1f)
        if (showTimeInput) {
            TimeInput(
                state = timePickerState,
                colors = bitwardenTimePickerColors(),
                modifier = modifier,
            )
        } else {
            TimePicker(
                state = timePickerState,
                colors = bitwardenTimePickerColors(),
                modifier = modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun bitwardenTimePickerColors(): TimePickerColors = TimePickerColors(
    clockDialColor = UniversalTheme.colorScheme.filledButton.backgroundReversed,
    selectorColor = UniversalTheme.colorScheme.filledButton.background,
    containerColor = UniversalTheme.colorScheme.filledButton.foreground,
    clockDialSelectedContentColor = UniversalTheme.colorScheme.background.secondary,
    clockDialUnselectedContentColor = UniversalTheme.colorScheme.text.primary,
    periodSelectorBorderColor = UniversalTheme.colorScheme.stroke.divider,
    periodSelectorSelectedContainerColor = UniversalTheme
        .colorScheme
        .filledButton
        .backgroundReversed,
    periodSelectorUnselectedContainerColor = UniversalTheme.colorScheme.background.primary,
    periodSelectorSelectedContentColor = UniversalTheme.colorScheme.filledButton.foregroundReversed,
    periodSelectorUnselectedContentColor = UniversalTheme.colorScheme.text.secondary,
    timeSelectorSelectedContainerColor = UniversalTheme.colorScheme.background.tertiary,
    timeSelectorUnselectedContainerColor = UniversalTheme.colorScheme.background.secondary,
    timeSelectorSelectedContentColor = UniversalTheme.colorScheme.text.primary,
    timeSelectorUnselectedContentColor = UniversalTheme.colorScheme.text.primary,
)

@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    inputToggleButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = UniversalTheme.shapes.dialog,
            color = UniversalTheme.colorScheme.background.primary,
            contentColor = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .semantics {
                    testTagsAsResourceId = true
                    testTag = "AlertPopup"
                }
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .testTag("AlertTitleText")
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = stringResource(id = UniversalString.select_time),
                    color = UniversalTheme.colorScheme.text.secondary,
                    style = UniversalTheme.typography.labelMedium,
                )

                content()

                Row(modifier = Modifier.fillMaxWidth()) {
                    inputToggleButton()
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton()
                    Spacer(modifier = Modifier.width(8.dp))
                    confirmButton()
                }
            }
        }
    }
}
