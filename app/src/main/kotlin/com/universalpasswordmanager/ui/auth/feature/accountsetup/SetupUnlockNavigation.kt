package com.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Navigate to the setup unlock screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToSetupUnlockScreen() {
    this.navigate(route = VaultUnlockedGraph.SetupUnlockRoute)
}

/**
 * Add the setup unlock screen to the vault unlocked navigation entries.
 */
fun EntryProviderScope<VaultUnlockedGraph>.setupUnlockDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.SetupUnlockRoute> {
        SetupUnlockScreen(
            viewModel = setupUnlockViewModel(isInitialSetup = false),
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Add the setup unlock screen to the account setup navigation entries.
 */
fun EntryProviderScope<AccountSetupGraph>.setupUnlockDestinationAsRoot() {
    entry<AccountSetupGraph.SetupUnlockRoute> {
        SetupUnlockScreen(
            viewModel = setupUnlockViewModel(isInitialSetup = true),
            onNavigateBack = {
                // No-Op
            },
        )
    }
}

@Composable
private fun setupUnlockViewModel(
    isInitialSetup: Boolean,
): SetupUnlockViewModel = hiltViewModel<SetupUnlockViewModel, SetupUnlockViewModel.Factory>(
    creationCallback = { it.create(isInitialSetup = isInitialSetup) },
)
