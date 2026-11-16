package .ui.platform.components.dropdown

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import .ui.platform.components.button.Universal Password ManagerTextSelectionButton
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.dialog.Universal Password ManagerTimePickerDialog
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerPlurals
import .ui.platform.resource.Universal Password ManagerString

private const val MINUTES_PER_HOUR: Int = 60

/**
 * A button that displays a selected time duration and opens a time picker dialog when clicked.
 *
 * @param label The descriptive text label for the [OutlinedTextField].
 * @param totalMinutes The currently selected time value in minutes.
 * @param onTimeSelect A lambda that is invoked when a time is selected from the menu.
 * @param is24Hour Whether the time should be displayed in 24-hour format.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier A [Modifier] that you can use to apply custom modifications to the composable.
 * @param isEnabled Whether the button is enabled.
 * @param supportingContent An optional supporting content that will appear below the button.
 * @param helpData An optional [Universal Password ManagerHelpButtonData], representing the help button.
 * @param insets Inner padding to be applied within the card.
 * @param textFieldTestTag The optional test tag associated with the inner text field.
 * @param actionsPadding Padding to be applied to the [actions] block.
 * @param actions A lambda containing the set of actions (usually icons or similar) to display
 * in the app bar's trailing side. This lambda extends [RowScope], allowing flexibility in
 * defining the layout of the actions.
 */
@Composable
fun Universal Password ManagerTimePickerButton(
    label: String,
    totalMinutes: Int,
    onTimeSelect: (minutes: Int) -> Unit,
    is24Hour: Boolean,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    supportingContent: @Composable (ColumnScope.() -> Unit)?,
    helpData: Universal Password ManagerHelpButtonData? = null,
    insets: PaddingValues = PaddingValues(),
    textFieldTestTag: String? = null,
    actionsPadding: PaddingValues = PaddingValues(end = 4.dp),
    actions: @Composable RowScope.() -> Unit = {},
) {
    Universal Password ManagerTimePickerButton(
        label = label,
        hours = totalMinutes / MINUTES_PER_HOUR,
        minutes = totalMinutes.mod(MINUTES_PER_HOUR),
        onTimeSelect = { hour, minute -> onTimeSelect((hour * MINUTES_PER_HOUR) + minute) },
        cardStyle = cardStyle,
        is24Hour = is24Hour,
        modifier = modifier,
        isEnabled = isEnabled,
        supportingContent = supportingContent,
        helpData = helpData,
        insets = insets,
        textFieldTestTag = textFieldTestTag,
        actionsPadding = actionsPadding,
        actions = actions,
    )
}

/**
 * A button that displays a selected time duration and opens a time picker dialog when clicked.
 *
 * @param label The descriptive text label for the [OutlinedTextField].
 * @param hours The currently selected time value in hours.
 * @param minutes The currently selected time value in minutes.
 * @param onTimeSelect A lambda that is invoked when a time is selected from the menu.
 * @param is24Hour Whether the time should be displayed in 24-hour format.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier A [Modifier] that you can use to apply custom modifications to the composable.
 * @param isEnabled Whether the button is enabled.
 * @param supportingContent An optional supporting content that will appear below the button.
 * @param helpData An optional [Universal Password ManagerHelpButtonData], representing the help button.
 * @param insets Inner padding to be applied within the card.
 * @param textFieldTestTag The optional test tag associated with the inner text field.
 * @param actionsPadding Padding to be applied to the [actions] block.
 * @param actions A lambda containing the set of actions (usually icons or similar) to display
 * in the app bar's trailing side. This lambda extends [RowScope], allowing flexibility in
 * defining the layout of the actions.
 */
@Composable
fun Universal Password ManagerTimePickerButton(
    label: String,
    hours: Int,
    minutes: Int,
    onTimeSelect: (hour: Int, minute: Int) -> Unit,
    is24Hour: Boolean,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    supportingContent: @Composable (ColumnScope.() -> Unit)?,
    helpData: Universal Password ManagerHelpButtonData? = null,
    insets: PaddingValues = PaddingValues(),
    textFieldTestTag: String? = null,
    actionsPadding: PaddingValues = PaddingValues(end = 4.dp),
    actions: @Composable RowScope.() -> Unit = {},
) {
    var shouldShowDialog by rememberSaveable { mutableStateOf(value = false) }
    Universal Password ManagerTextSelectionButton(
        label = label,
        selectedOption = if (hours != 0 && minutes != 0) {
            // Since both hours and minutes are non-zero, we display both of them.
            stringResource(
                id = Universal Password ManagerString.hours_minutes_format,
                formatArgs = arrayOf(
                    pluralStringResource(
                        id = Universal Password ManagerPlurals.hours_format,
                        count = hours,
                        formatArgs = arrayOf(hours),
                    ),
                    pluralStringResource(
                        id = Universal Password ManagerPlurals.minutes_format,
                        count = minutes,
                        formatArgs = arrayOf(minutes),
                    ),
                ),
            )
        } else if (hours != 0) {
            // Since only hours are non-zero, we only display hours.
            pluralStringResource(
                id = Universal Password ManagerPlurals.hours_format,
                count = hours,
                formatArgs = arrayOf(hours),
            )
        } else {
            // We display this if there are only minutes or if both hours and minutes are 0.
            pluralStringResource(
                id = Universal Password ManagerPlurals.minutes_format,
                count = minutes,
                formatArgs = arrayOf(minutes),
            )
        },
        onClick = { shouldShowDialog = true },
        cardStyle = cardStyle,
        enabled = isEnabled,
        showChevron = false,
        supportingContent = supportingContent,
        helpData = helpData,
        insets = insets,
        textFieldTestTag = textFieldTestTag,
        actionsPadding = actionsPadding,
        actions = actions,
        modifier = modifier,
    )
    if (shouldShowDialog) {
        Universal Password ManagerTimePickerDialog(
            initialHour = hours,
            initialMinute = minutes,
            onTimeSelect = { hour, minute ->
                onTimeSelect(hour, minute)
                shouldShowDialog = false
            },
            onDismissRequest = { shouldShowDialog = false },
            is24Hour = is24Hour,
        )
    }
}
