package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.deleteaccountconfirmation

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

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
