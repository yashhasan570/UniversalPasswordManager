package com.universalpasswordmanager.ui.platform.components.dropdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universalpasswordmanager.ui.platform.base.util.nullableTestTag
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextSelectionButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalHelpButtonData
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalSelectionDialog
import com.universalpasswordmanager.ui.platform.components.dialog.row.UniversalSelectionRow
import com.universalpasswordmanager.ui.platform.components.dropdown.model.MultiSelectOption
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * A custom composable representing a multi-select button.
 *
 * This composable displays an [OutlinedTextField] with a dropdown icon as a trailing icon.
 * When the field is clicked, a dropdown menu appears with a list of options to select from.
 *
 * @param label The descriptive text label for the [OutlinedTextField].
 * @param options A list of strings representing the available options in the dialog.
 * @param selectedOption The currently selected option that is displayed in the [OutlinedTextField]
 * (or `null` if no option is selected).
 * @param onOptionSelected A lambda that is invoked when an option is selected from the dropdown
 * menu.
 * @param isEnabled Whether the button is enabled.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier A [Modifier] that you can use to apply custom modifications to the composable.
 * @param dialogSubtitle The subtitle to apply to the dialog.
 * @param supportingText An optional supporting text that will appear below the text field.
 * @param helpData An optional [UniversalHelpButtonData], representing the help button.
 * @param insets Inner padding to be applied withing the card.
 * @param textFieldTestTag The optional test tag associated with the inner text field.
 * @param actionsPadding Padding to be applied to the [actions] block.
 * @param actions A lambda containing the set of actions (usually icons or similar) to display
 * in the app bar's trailing side. This lambda extends [RowScope], allowing flexibility in
 * defining the layout of the actions.
 */
@Composable
fun UniversalMultiSelectButton(
    label: String,
    options: ImmutableList<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    dialogSubtitle: String? = null,
    isEnabled: Boolean = true,
    supportingText: String? = null,
    helpData: UniversalHelpButtonData? = null,
    insets: PaddingValues = PaddingValues(),
    textFieldTestTag: String? = null,
    actionsPadding: PaddingValues = PaddingValues(end = 4.dp),
    actions: @Composable RowScope.() -> Unit = {},
) {
    UniversalMultiSelectButton(
        label = label,
        dialogSubtitle = dialogSubtitle,
        options = options.map { MultiSelectOption.Row(it) }.toImmutableList(),
        selectedOption = selectedOption?.let { MultiSelectOption.Row(it) },
        onOptionSelected = { onOptionSelected(it.title) },
        cardStyle = cardStyle,
        modifier = modifier,
        isEnabled = isEnabled,
        supportingContent = supportingText?.let {
            {
                Text(
                    text = it,
                    style = UniversalTheme.typography.bodySmall,
                    color = UniversalTheme.colorScheme.text.secondary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        helpData = helpData,
        insets = insets,
        textFieldTestTag = textFieldTestTag,
        actionsPadding = actionsPadding,
        actions = actions,
    )
}

/**
 * A custom composable representing a multi-select button.
 *
 * This composable displays an [OutlinedTextField] with a dropdown icon as a trailing icon.
 * When the field is clicked, a dropdown menu appears with a list of options to select from.
 *
 * @param label The descriptive text label for the [OutlinedTextField].
 * @param options A list of [MultiSelectOption] representing the available options in the dialog.
 * @param selectedOption The currently selected option that is displayed in the [OutlinedTextField]
 * (or `null` if no option is selected).
 * @param onOptionSelected A lambda that is invoked when an option is selected from the dropdown
 * menu.
 * @param isEnabled Whether the button is enabled.
 * @param supportingContent An optional supporting content that will appear below the button.
 * @param cardStyle Indicates the type of card style to be applied.
 * @param modifier A [Modifier] that you can use to apply custom modifications to the composable.
 * @param dialogSubtitle The subtitle to apply to the dialog.
 * @param helpData An optional [UniversalHelpButtonData], representing the help button.
 * @param insets Inner padding to be applied withing the card.
 * @param textFieldTestTag The optional test tag associated with the inner text field.
 * @param actionsPadding Padding to be applied to the [actions] block.
 * @param actions A lambda containing the set of actions (usually icons or similar) to display
 * in the app bar's trailing side. This lambda extends [RowScope], allowing flexibility in
 * defining the layout of the actions.
 */
@Composable
fun UniversalMultiSelectButton(
    label: String,
    options: ImmutableList<MultiSelectOption>,
    selectedOption: MultiSelectOption.Row?,
    onOptionSelected: (MultiSelectOption.Row) -> Unit,
    cardStyle: CardStyle?,
    modifier: Modifier = Modifier,
    dialogSubtitle: String? = null,
    isEnabled: Boolean = true,
    supportingContent: @Composable (ColumnScope.() -> Unit)?,
    helpData: UniversalHelpButtonData? = null,
    insets: PaddingValues = PaddingValues(),
    textFieldTestTag: String? = null,
    actionsPadding: PaddingValues = PaddingValues(end = 4.dp),
    actions: @Composable RowScope.() -> Unit = {},
) {
    var shouldShowDialog by rememberSaveable { mutableStateOf(false) }

    UniversalTextSelectionButton(
        label = label,
        selectedOption = selectedOption?.title,
        onClick = {
            shouldShowDialog = true
        },
        cardStyle = cardStyle,
        enabled = isEnabled,
        supportingContent = supportingContent,
        helpData = helpData,
        insets = insets,
        textFieldTestTag = textFieldTestTag,
        actionsPadding = actionsPadding,
        actions = actions,
        semanticRole = Role.DropdownList,
        modifier = modifier,
    )

    if (shouldShowDialog) {
        UniversalSelectionDialog(
            title = label,
            subTitle = dialogSubtitle,
            onDismissRequest = { shouldShowDialog = false },
        ) {
            UniversalMultiSelectDialogContent(
                options = options,
                selectedOption = selectedOption,
                onOptionSelected = { selectedItem ->
                    shouldShowDialog = false
                    onOptionSelected(selectedItem)
                },
            )
        }
    }
}

/**
 * Renders the list of items within a multi-select dialog.
 *
 * This composable is typically used as the content for [UniversalSelectionDialog].
 *
 * @param options A list of strings representing the available options in the dialog.
 * @param selectedOption The currently selected option that is displayed in the [OutlinedTextField]
 * (or `null` if no option is selected).
 * @param onOptionSelected A lambda that is invoked when an option
 * is selected from the dropdown menu.
 */
@Composable
fun ColumnScope.UniversalMultiSelectDialogContent(
    options: ImmutableList<MultiSelectOption>,
    selectedOption: MultiSelectOption.Row?,
    onOptionSelected: (MultiSelectOption.Row) -> Unit,
) {
    options.forEach {
        when (it) {
            is MultiSelectOption.Header -> {
                Column(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) { heading() }
                        .nullableTestTag(tag = it.testTag)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.height(height = 4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.title,
                        color = UniversalTheme.colorScheme.text.secondary,
                        style = UniversalTheme.typography.titleSmall,
                    )
                }
            }

            is MultiSelectOption.Row -> {
                UniversalSelectionRow(
                    text = it.title.asText(),
                    isSelected = it == selectedOption,
                    onClick = { onOptionSelected(it) },
                    modifier = Modifier.nullableTestTag(tag = it.testTag),
                )
            }
        }
    }
}

@Preview
@Composable
private fun UniversalMultiSelectButton_preview() {
    UniversalTheme {
        UniversalMultiSelectButton(
            label = "Label",
            options = persistentListOf("a", "b"),
            selectedOption = "",
            onOptionSelected = {},
            cardStyle = CardStyle.Full,
        )
    }
}
