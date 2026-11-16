package .ui.platform.components.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import .ui.platform.base.util.topDivider
import .ui.platform.components.navigation.model.NavigationItem
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.ImmutableList

/**
 * A custom Universal Password Manager-themed bottom app bar.
 */
@Composable
fun Universal Password ManagerBottomAppBar(
    navigationItems: ImmutableList<NavigationItem>,
    selectedItem: NavigationItem?,
    onClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
) {
    BottomAppBar(
        containerColor = Universal Password ManagerTheme.colorScheme.background.secondary,
        contentColor = Color.Unspecified,
        windowInsets = windowInsets,
        modifier = modifier.topDivider(),
    ) {
        navigationItems.forEach { navigationItem ->
            Universal Password ManagerNavigationBarItem(
                labelRes = navigationItem.labelRes,
                iconRes = navigationItem.iconRes,
                notificationCount = navigationItem.notificationCount,
                isSelected = selectedItem == navigationItem,
                onClick = { onClick(navigationItem) },
                modifier = Modifier.testTag(tag = navigationItem.testTag),
            )
        }
    }
}
