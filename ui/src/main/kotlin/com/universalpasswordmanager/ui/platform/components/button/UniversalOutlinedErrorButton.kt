package com.universalpasswordmanager.ui.platform.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.universalpasswordmanager.ui.platform.components.button.color.universalpasswordmanagerOutlinedButtonColors
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Represents a Universal-styled filled [OutlinedButton] for error states.
 *
 * @param label The label for the button.
 * @param onClick The callback when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param icon The icon for the button.
 * @param isEnabled Whether the button is enabled.
 */
@Composable
fun UniversalOutlinedErrorButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    isEnabled: Boolean = true,
) {
    UniversalOutlinedButton(
        label = label,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        isEnabled = isEnabled,
        colors = universalpasswordmanagerOutlinedButtonColors(
            contentColor = UniversalTheme.colorScheme.status.error,
            outlineColor = UniversalTheme.colorScheme.status.error,
            outlineColorDisabled = UniversalTheme.colorScheme.status.error.copy(alpha = 0.12f),
        ),
    )
}

@Preview
@Composable
private fun BUniversalOutlinedErrorButton_preview() {
    Column {
        UniversalOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = true,
        )
        UniversalOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = UniversalDrawable.ic_question_circle),
            isEnabled = true,
        )
        UniversalOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = false,
        )
        UniversalOutlinedErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = UniversalDrawable.ic_question_circle),
            isEnabled = false,
        )
    }
}
