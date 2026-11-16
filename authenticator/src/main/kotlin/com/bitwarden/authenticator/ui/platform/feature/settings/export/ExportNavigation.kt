package .authenticator.ui.platform.feature.settings.export

import androidx.navigation3.runtime.EntryProviderScope
import .authenticator.ui.platform.feature.settings.SettingsGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.pushEntry

/**
 * Add the export data destination to the navigation entries.
 */
fun EntryProviderScope<SettingsGraph>.exportDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<SettingsGraph.ExportRoute> {
        ExportScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Navigate to the export data screen.
 */
fun NavigationController<SettingsGraph>.navigateToExport() {
    this.navigate(route = SettingsGraph.ExportRoute)
}
