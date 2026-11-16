package com.x8bit.universalpasswordmanager.ui.vault.feature.importlogins

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Helper function to navigate to the import logins screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToImportLoginsScreen() {
    this.navigate(route = VaultUnlockedGraph.ImportLoginsRoute)
}

/**
 * Adds the import logins screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.importLoginsScreenDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.ImportLoginsRoute> {
        ImportLoginsScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}
