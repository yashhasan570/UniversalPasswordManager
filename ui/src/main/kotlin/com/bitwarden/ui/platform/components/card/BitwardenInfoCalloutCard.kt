package .ui.platform.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Represents a Universal Password Manager-styled info callout.
 *
 * @param text The text content for the policy warning.
 * @param modifier The [Modifier] to be applied to the label.
 * @param startIcon The [IconData] to be used for the callout start icon.
 */
@Composable
fun Universal Password ManagerInfoCalloutCard(
    text: String,
    modifier: Modifier = Modifier,
    startIcon: IconData? = null,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 50.dp)
            .background(
                color = Universal Password ManagerTheme.colorScheme.background.tertiary,
                shape = Universal Password ManagerTheme.shapes.infoCallout,
            )
            .padding(all = 16.dp),
    ) {
        startIcon?.let {
            Universal Password ManagerIcon(
                iconData = it,
                tint = Universal Password ManagerTheme.colorScheme.text.primary,
                modifier = Modifier.size(size = 16.dp),
            )
            Spacer(modifier = Modifier.width(width = 12.dp))
        }
        Text(
            text = text,
            textAlign = TextAlign.Start,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun Universal Password ManagerInfoCallout_preview() {
    Universal Password ManagerInfoCalloutCard(
        text = "text",
    )
}
