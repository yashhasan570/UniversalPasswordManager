package com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.resource.Universal Password ManagerString

/**
 * A reusable scaffold for the export items screen.
 *
 * @param navIcon The navigation icon to be displayed in the top app bar.
 * @param onNavigationIconClick The action to be performed when the navigation icon is clicked.
 * @param navigationIconContentDescription The content description for the navigation icon.
 * @param scrollBehavior The scroll behavior to be used for the top app bar.
 * @param modifier The modifier to be applied to the scaffold.
 * @param content The content to be displayed inside the scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportItemsScaffold(
    navIcon: Painter,
    onNavigationIconClick: () -> Unit,
    navigationIconContentDescription: String,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Universal Password ManagerScaffold(
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(Universal Password ManagerString.import_from_universalpasswordmanager),
                onNavigationIconClick = onNavigationIconClick,
                navigationIconContentDescription = navigationIconContentDescription,
                navigationIcon = navIcon,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier,
        content = content,
    )
}
