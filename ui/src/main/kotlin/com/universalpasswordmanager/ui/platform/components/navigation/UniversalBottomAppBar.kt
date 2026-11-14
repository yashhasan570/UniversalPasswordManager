package com.universalpasswordmanager.ui.platform.components.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.universalpasswordmanager.ui.platform.base.util.topDivider
import com.universalpasswordmanager.ui.platform.components.navigation.model.NavigationItem
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import kotlinx.collections.immutable.ImmutableList

/**
 * A custom Universal-themed bottom app bar.
 */
@Composable
fun UniversalBottomAppBar(
    navigationItems: ImmutableList<NavigationItem>,
    selectedItem: NavigationItem?,
    onClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
) {
    BottomAppBar(
        containerColor = UniversalTheme.colorScheme.background.secondary,
        contentColor = Color.Unspecified,
        windowInsets = windowInsets,
        modifier = modifier.topDivider(),
    ) {
        navigationItems.forEach { navigationItem ->
            UniversalNavigationBarItem(
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
