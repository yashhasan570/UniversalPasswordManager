package .ui.platform.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.components.button.color.universalpasswordmanagerFilledErrorButtonColors
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable

/**
 * Represents a Universal Password Manager-styled filled [Button] for error scenarios.
 *
 * @param label The label for the button.
 * @param onClick The callback when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param icon The icon for the button.
 * @param isEnabled Whether the button is enabled.
 */
@Composable
fun Universal Password ManagerFilledErrorButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    isEnabled: Boolean = true,
) {
    Universal Password ManagerFilledButton(
        label = label,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        isEnabled = isEnabled,
        colors = universalpasswordmanagerFilledErrorButtonColors(),
    )
}

@Preview
@Composable
private fun Universal Password ManagerErrorButton_preview() {
    Column {
        Universal Password ManagerFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = true,
        )
        Universal Password ManagerFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
            isEnabled = true,
        )
        Universal Password ManagerFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = false,
        )
        Universal Password ManagerFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
            isEnabled = false,
        )
    }
}
