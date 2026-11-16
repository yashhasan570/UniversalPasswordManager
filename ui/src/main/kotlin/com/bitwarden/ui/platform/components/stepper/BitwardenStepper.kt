package .ui.platform.components.stepper

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import .ui.platform.base.util.orNullIfBlank
import .ui.platform.components.button.Universal Password ManagerFilledIconButton
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.field.color.universalpasswordmanagerTextFieldColors
import .ui.platform.components.model.CardStyle
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Displays a stepper that allows the user to increment and decrement an int value.
 *
 * @param label Label for the stepper.
 * @param value Value to display. Null will display nothing. Will be clamped to [range] before
 * display.
 * @param onValueChange callback invoked when the user increments or decrements the count. Note
 * that this will not be called if the attempts to move value outside of [range].
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier Modifier.
 * @param supportingText An optional supporting text that will appear below the stepper.
 * @param range Range of valid values.
 * @param isIncrementEnabled whether the increment button should be enabled.
 * @param isDecrementEnabled whether the decrement button should be enabled.
 * @param textFieldReadOnly whether the text field should be read only. The stepper
 * increment and decrement buttons function regardless of this value.
 */
@Composable
fun Universal Password ManagerStepper(
    label: String?,
    value: Int?,
    onValueChange: (Int) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    range: ClosedRange<Int> = 1..Int.MAX_VALUE,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
    textFieldReadOnly: Boolean = true,
) {
    Universal Password ManagerStepper(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        supportingContent = supportingText?.let {
            {
                Text(
                    text = it,
                    style = Universal Password ManagerTheme.typography.bodySmall,
                    color = universalpasswordmanagerTextFieldColors().focusedSupportingTextColor,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        range = range,
        isIncrementEnabled = isIncrementEnabled,
        isDecrementEnabled = isDecrementEnabled,
        textFieldReadOnly = textFieldReadOnly,
        cardStyle = cardStyle,
    )
}

/**
 * Displays a stepper that allows the user to increment and decrement an int value.
 *
 * @param label Label for the stepper.
 * @param value Value to display. Null will display nothing. Will be clamped to [range] before
 * display.
 * @param onValueChange callback invoked when the user increments or decrements the count. Note
 * that this will not be called if the attempts to move value outside of [range].
 * @param supportingContent An optional supporting text composable that will appear below the
 * stepper.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier Modifier.
 * @param range Range of valid values.
 * @param isIncrementEnabled whether the increment button should be enabled.
 * @param isDecrementEnabled whether the decrement button should be enabled.
 * @param textFieldReadOnly whether the text field should be read only. The stepper
 * increment and decrement buttons function regardless of this value.
 */
@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
fun Universal Password ManagerStepper(
    label: String?,
    value: Int?,
    onValueChange: (Int) -> Unit,
    supportingContent: @Composable (ColumnScope.() -> Unit)?,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    supportingContentPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
    range: ClosedRange<Int> = 1..Int.MAX_VALUE,
    isIncrementEnabled: Boolean = true,
    isDecrementEnabled: Boolean = true,
    textFieldReadOnly: Boolean = true,
) {
    val clampedValue = value?.coerceIn(range)
    if (clampedValue != value && clampedValue != null) {
        onValueChange(clampedValue)
    }
    val isAtRangeMinimum = clampedValue?.let { (it - 1) !in range } ?: true
    val isAtRangeMaximum = clampedValue?.let { (it + 1) !in range } ?: false
    Universal Password ManagerTextField(
        label = label,
        value = clampedValue?.toString().orEmpty(),
        textFieldTestTag = "StepperValueLabel",
        actions = {
            Universal Password ManagerFilledIconButton(
                vectorIconRes = Universal Password ManagerDrawable.ic_minus,
                contentDescription = stringResource(
                    id = Universal Password ManagerString.stepper_decrement_format,
                    formatArgs = arrayOf(label ?: stringResource(id = Universal Password ManagerString.value)),
                ),
                onClick = {
                    val decrementedValue = ((clampedValue ?: 0) - 1).coerceIn(range)
                    if (decrementedValue != clampedValue) {
                        onValueChange(decrementedValue)
                    }
                },
                isEnabled = isDecrementEnabled && !isAtRangeMinimum,
                modifier = Modifier.testTag("DecrementValue"),
            )
            Universal Password ManagerFilledIconButton(
                vectorIconRes = Universal Password ManagerDrawable.ic_plus,
                contentDescription = stringResource(
                    id = Universal Password ManagerString.stepper_increment_format,
                    formatArgs = arrayOf(label ?: stringResource(id = Universal Password ManagerString.value)),
                ),
                onClick = {
                    val incrementedValue = ((clampedValue ?: 0) + 1).coerceIn(range)
                    if (incrementedValue != clampedValue) {
                        onValueChange(incrementedValue)
                    }
                },
                isEnabled = isIncrementEnabled && !isAtRangeMaximum,
                modifier = Modifier.testTag("IncrementValue"),
            )
        },
        readOnly = textFieldReadOnly,
        keyboardType = KeyboardType.Number,
        onValueChange = { newValue ->
            onValueChange(
                newValue
                    .orNullIfBlank()
                    ?.let { it.toIntOrNull()?.coerceIn(range) ?: clampedValue }
                    ?: range.start,
            )
        },
        actionsPadding = PaddingValues(end = 12.dp),
        supportingContent = supportingContent,
        supportingContentPadding = supportingContentPadding,
        cardStyle = cardStyle,
        modifier = modifier,
    )
}
