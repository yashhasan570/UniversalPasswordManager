package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.deleteaccountconfirmation

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the delete account confirmation screen to the navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.deleteAccountConfirmationDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.DeleteAccountConfirmationRoute> {
        DeleteAccountConfirmationScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the [DeleteAccountConfirmationScreen].
 */
fun NavigationController<VaultUnlockedGraph>.navigateToDeleteAccountConfirmation() {
    this.navigate(route = VaultUnlockedGraph.DeleteAccountConfirmationRoute)
}
