package com.universalpasswordmanager.ui.platform.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.universalpasswordmanager.ui.platform.components.button.color.universalpasswordmanagerFilledErrorButtonColors
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable

/**
 * Represents a Universal-styled filled [Button] for error scenarios.
 *
 * @param label The label for the button.
 * @param onClick The callback when the button is clicked.
 * @param modifier The [Modifier] to be applied to the button.
 * @param icon The icon for the button.
 * @param isEnabled Whether the button is enabled.
 */
@Composable
fun UniversalFilledErrorButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    isEnabled: Boolean = true,
) {
    UniversalFilledButton(
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
private fun UniversalErrorButton_preview() {
    Column {
        UniversalFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = true,
        )
        UniversalFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = UniversalDrawable.ic_question_circle),
            isEnabled = true,
        )
        UniversalFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = null,
            isEnabled = false,
        )
        UniversalFilledErrorButton(
            label = "Label",
            onClick = {},
            icon = rememberVectorPainter(id = UniversalDrawable.ic_question_circle),
            isEnabled = false,
        )
    }
}
