package .ui.platform.components.badge

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import .annotation.OmitFromCoverage
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.color.Universal Password ManagerColorScheme

/**
 * A reusable status badge composable that displays a colored pill with a label.
 *
 * @param label The text to display in the badge.
 * @param colors The border, background, and text colors for the badge.
 * @param modifier The [Modifier] to apply to this badge.
 */
@Composable
fun Universal Password ManagerStatusBadge(
    label: String,
    colors: Universal Password ManagerColorScheme.StatusBadgeVariantColors,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = Universal Password ManagerTheme.shapes.badge,
        color = colors.background,
        modifier = modifier
            .height(24.dp)
            .border(
                width = 1.dp,
                color = colors.border,
                shape = Universal Password ManagerTheme.shapes.badge,
            ),
    ) {
        Text(
            text = label,
            style = Universal Password ManagerTheme.typography.labelMedium,
            color = colors.text,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        )
    }
}

@OmitFromCoverage
@Preview
@Composable
private fun Universal Password ManagerStatusBadge_Preview() {
    Universal Password ManagerTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Universal Password ManagerStatusBadge(
                label = "Active",
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.success,
            )
            Universal Password ManagerStatusBadge(
                label = "Canceled",
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.error,
            )
            Universal Password ManagerStatusBadge(
                label = "Update payment",
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.warning,
            )
        }
    }
}

@OmitFromCoverage
@Preview
@Composable
private fun Universal Password ManagerStatusBadge_PreviewDark() {
    Universal Password ManagerTheme(theme = AppTheme.DARK) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Universal Password ManagerStatusBadge(
                label = "Active",
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.success,
            )
            Universal Password ManagerStatusBadge(
                label = "Canceled",
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.error,
            )
            Universal Password ManagerStatusBadge(
                label = "Update payment",
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.warning,
            )
        }
    }
}
