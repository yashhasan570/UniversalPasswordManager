package com.x8bit.universalpasswordmanager.ui.vault.feature.manualcodeentry

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the manual code entry screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.vaultManualCodeEntryDestination(
    onNavigateBack: () -> Unit,
    onNavigateToQrCodeScreen: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.ManualCodeEntryRoute> {
        ManualCodeEntryScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToQrCodeScreen = onNavigateToQrCodeScreen,
        )
    }
}

/**
 * Navigate to the manual code entry screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToManualCodeEntryScreen() {
    this.navigate(route = VaultUnlockedGraph.ManualCodeEntryRoute)
}
