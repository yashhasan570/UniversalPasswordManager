package com.universalpasswordmanager.ui.platform.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Represents a Universal-styled label text.
 *
 * @param label The text content for the label.
 * @param supportingLabel The optional text for the supporting label.
 * @param modifier The [Modifier] to be applied to the label.
 */
@Composable
fun UniversalListHeaderText(
    label: String,
    modifier: Modifier = Modifier,
    supportingLabel: String? = null,
) {
    val supportLabel = supportingLabel?.let { " ($it)" }.orEmpty()
    Text(
        text = "${label.uppercase()}$supportLabel",
        style = UniversalTheme.typography.eyebrowMedium,
        color = UniversalTheme.colorScheme.text.secondary,
        modifier = modifier.semantics { heading() },
    )
}

@Preview(showBackground = true)
@Composable
private fun UniversalListHeaderText_preview() {
    UniversalTheme {
        Column {
            UniversalListHeaderText(
                label = "Sample Label",
                modifier = Modifier,
            )
            UniversalListHeaderText(
                label = "Sample Label",
                supportingLabel = "4",
                modifier = Modifier,
            )
        }
    }
}
