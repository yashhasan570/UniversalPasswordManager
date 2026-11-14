package com.universalpasswordmanager.ui.platform.components.indicator

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * A Universal-styled [CircularProgressIndicator].
 */
@Composable
fun UniversalCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = UniversalTheme.colorScheme.stroke.border,
        trackColor = UniversalTheme.colorScheme.background.tertiary,
    )
}
