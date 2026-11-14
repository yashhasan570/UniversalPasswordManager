package com.universalpasswordmanager.ui.vault.feature.qrcodescan

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the QR code scan screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.vaultQrCodeScanDestination(
    onNavigateBack: () -> Unit,
    onNavigateToManualCodeEntryScreen: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.QrCodeScanRoute> {
        QrCodeScanScreen(
            onNavigateToManualCodeEntryScreen = onNavigateToManualCodeEntryScreen,
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the QR code scan screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToQrCodeScanScreen() {
    this.navigate(route = VaultUnlockedGraph.QrCodeScanRoute)
}
