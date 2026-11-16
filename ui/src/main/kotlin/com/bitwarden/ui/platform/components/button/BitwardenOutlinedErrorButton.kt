package .ui.platform.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.components.button.color.universalpasswordmanagerOutlinedButtonColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled filled [OutlinedButton] for error states.
 *
 * @param label The label for the button.
 * @param onClick The callback when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param icon The icon for the button.
 * @param isEnabled Whether the button is enabled.
 */
@Composable
fun Universal Password ManagerOutlinedErrorButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    isEnabled: Boolean = true,
) {
    Universal Password ManagerOutlinedButton(
        label = label,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        isEnabled = isEnabled,
        colors = universalpasswordmanagerOutlinedButtonColors(
            contentColor = Universal Password ManagerTheme.colorScheme.status.error,
            outlineColor = Universal Password ManagerTheme.colorScheme.status.error,
            outlineColorDisabled = Universal Password ManagerTheme.colorScheme.status.error.copy(alpha = 0.12f),
        ),
    )
}

@Preview
@Composable
private fun BUniversal Password ManagerOutlinedErrorButton_preview() {
    Column {
        Universal Password ManagerOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = true,
        )
        Universal Password ManagerOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
            isEnabled = true,
        )
        Universal Password ManagerOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = false,
        )
        Universal Password ManagerOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
            isEnabled = false,
        )
    }
}
