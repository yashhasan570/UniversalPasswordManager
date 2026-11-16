package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.deleteaccount

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the delete account screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.deleteAccountDestination(
    onNavigateBack: () -> Unit,
    onNavigateToDeleteAccountConfirmation: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.DeleteAccountRoute> {
        DeleteAccountScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToDeleteAccountConfirmation = onNavigateToDeleteAccountConfirmation,
        )
    }
}

/**
 * Navigate to the delete account screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToDeleteAccount() {
    this.navigate(route = VaultUnlockedGraph.DeleteAccountRoute)
}
