package .ui.platform.components.header

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled label text.
 *
 * @param label The text content for the label.
 * @param supportingLabel The optional text for the supporting label.
 * @param modifier The [Modifier] to be applied to the label.
 */
@Composable
fun Universal Password ManagerListHeaderText(
    label: String,
    modifier: Modifier = Modifier,
    supportingLabel: String? = null,
) {
    val supportLabel = supportingLabel?.let { " ($it)" }.orEmpty()
    Text(
        text = "${label.uppercase()}$supportLabel",
        style = Universal Password ManagerTheme.typography.eyebrowMedium,
        color = Universal Password ManagerTheme.colorScheme.text.secondary,
        modifier = modifier.semantics { heading() },
    )
}

@Preview(showBackground = true)
@Composable
private fun Universal Password ManagerListHeaderText_preview() {
    Universal Password ManagerTheme {
        Column {
            Universal Password ManagerListHeaderText(
                label = "Sample Label",
                modifier = Modifier,
            )
            Universal Password ManagerListHeaderText(
                label = "Sample Label",
                supportingLabel = "4",
                modifier = Modifier,
            )
        }
    }
}
