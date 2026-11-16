package .ui.platform.components.indicator

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * A Universal Password Manager-styled [CircularProgressIndicator].
 */
@Composable
fun Universal Password ManagerCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = Universal Password ManagerTheme.colorScheme.stroke.border,
        trackColor = Universal Password ManagerTheme.colorScheme.background.tertiary,
    )
}
