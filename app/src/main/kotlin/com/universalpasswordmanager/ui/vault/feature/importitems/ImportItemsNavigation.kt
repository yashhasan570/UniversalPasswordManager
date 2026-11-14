@file:OmitFromCoverage

package com.universalpasswordmanager.ui.vault.feature.importitems

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry
import com.universalpasswordmanager.ui.platform.feature.settings.SettingsGraph

/**
 * Helper function to navigate to the import items screen.
 */
fun NavigationController<SettingsGraph>.navigateToImportItemsScreen() {
    this.navigate(route = SettingsGraph.ImportItemsRoute)
}

/**
 * Add the import items screen to the navigation entries.
 */
fun EntryProviderScope<SettingsGraph>.importItemsDestination(
    onNavigateBack: () -> Unit,
    onNavigateToImportLogins: () -> Unit,
) {
    pushEntry<SettingsGraph.ImportItemsRoute> {
        ImportItemsScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToImportFromComputer = onNavigateToImportLogins,
        )
    }
}
