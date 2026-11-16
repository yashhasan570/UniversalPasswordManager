package .ui.platform.components.field.label

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import .ui.platform.components.button.Universal Password ManagerHelpIconButton
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField

/**
 * Represents a Universal Password Manager-styled label for a text field.
 *
 * Intended to be reused for all implementations of a TextField, such as the [Universal Password ManagerTextField]
 * and [Universal Password ManagerPasswordField].
 */
@Composable
internal fun Universal Password ManagerTextFieldLabel(
    label: String,
    textFieldValue: TextFieldValue,
    helpData: Universal Password ManagerHelpButtonData?,
    isFieldFocused: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(text = label)
        helpData?.let { helpButtonData ->
            val size by animateDpAsState(
                targetValue = if (textFieldValue.text.isEmpty() || isFieldFocused) 16.dp else 12.dp,
                label = "${helpButtonData.contentDescription}_animation",
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Universal Password ManagerHelpIconButton(
                helpData = helpButtonData,
                modifier = Modifier.size(size = size),
            )
        }
    }
}
