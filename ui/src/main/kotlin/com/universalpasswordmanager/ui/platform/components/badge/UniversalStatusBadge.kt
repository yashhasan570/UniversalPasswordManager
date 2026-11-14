package com.universalpasswordmanager.ui.platform.components.badge

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
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppTheme
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.theme.color.UniversalColorScheme

/**
 * A reusable status badge composable that displays a colored pill with a label.
 *
 * @param label The text to display in the badge.
 * @param colors The border, background, and text colors for the badge.
 * @param modifier The [Modifier] to apply to this badge.
 */
@Composable
fun UniversalStatusBadge(
    label: String,
    colors: UniversalColorScheme.StatusBadgeVariantColors,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = UniversalTheme.shapes.badge,
        color = colors.background,
        modifier = modifier
            .height(24.dp)
            .border(
                width = 1.dp,
                color = colors.border,
                shape = UniversalTheme.shapes.badge,
            ),
    ) {
        Text(
            text = label,
            style = UniversalTheme.typography.labelMedium,
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
private fun UniversalStatusBadge_Preview() {
    UniversalTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            UniversalStatusBadge(
                label = "Active",
                colors = UniversalTheme.colorScheme.statusBadge.success,
            )
            UniversalStatusBadge(
                label = "Canceled",
                colors = UniversalTheme.colorScheme.statusBadge.error,
            )
            UniversalStatusBadge(
                label = "Update payment",
                colors = UniversalTheme.colorScheme.statusBadge.warning,
            )
        }
    }
}

@OmitFromCoverage
@Preview
@Composable
private fun UniversalStatusBadge_PreviewDark() {
    UniversalTheme(theme = AppTheme.DARK) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            UniversalStatusBadge(
                label = "Active",
                colors = UniversalTheme.colorScheme.statusBadge.success,
            )
            UniversalStatusBadge(
                label = "Canceled",
                colors = UniversalTheme.colorScheme.statusBadge.error,
            )
            UniversalStatusBadge(
                label = "Update payment",
                colors = UniversalTheme.colorScheme.statusBadge.warning,
            )
        }
    }
}
