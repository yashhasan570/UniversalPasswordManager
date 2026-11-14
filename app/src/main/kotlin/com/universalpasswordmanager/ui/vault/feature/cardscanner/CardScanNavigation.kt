@file:OmitFromCoverage

package com.universalpasswordmanager.ui.vault.feature.cardscanner

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the card scan screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.cardScanDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.CardScanRoute> {
        CardScanScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the card scan screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToCardScanScreen() {
    this.navigate(route = VaultUnlockedGraph.CardScanRoute)
}
