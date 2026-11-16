package .authenticator.ui.platform.feature.settings.importing

import androidx.navigation3.runtime.EntryProviderScope
import .authenticator.ui.platform.feature.settings.SettingsGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.pushEntry

/**
 * Add the import screen to the navigation entries.
 */
fun EntryProviderScope<SettingsGraph>.importingDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<SettingsGraph.ImportRoute> {
        ImportingScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Navigate to the Import destination.
 */
fun NavigationController<SettingsGraph>.navigateToImporting() {
    this.navigate(route = SettingsGraph.ImportRoute)
}
