package com.universalpasswordmanager.ui.platform.components.card

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
import com.universalpasswordmanager.ui.platform.components.icon.UniversalIcon
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Represents a Universal-styled info callout.
 *
 * @param text The text content for the policy warning.
 * @param modifier The [Modifier] to be applied to the label.
 * @param startIcon The [IconData] to be used for the callout start icon.
 */
@Composable
fun UniversalInfoCalloutCard(
    text: String,
    modifier: Modifier = Modifier,
    startIcon: IconData? = null,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 50.dp)
            .background(
                color = UniversalTheme.colorScheme.background.tertiary,
                shape = UniversalTheme.shapes.infoCallout,
            )
            .padding(all = 16.dp),
    ) {
        startIcon?.let {
            UniversalIcon(
                iconData = it,
                tint = UniversalTheme.colorScheme.text.primary,
                modifier = Modifier.size(size = 16.dp),
            )
            Spacer(modifier = Modifier.width(width = 12.dp))
        }
        Text(
            text = text,
            textAlign = TextAlign.Start,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun UniversalInfoCallout_preview() {
    UniversalInfoCalloutCard(
        text = "text",
    )
}
