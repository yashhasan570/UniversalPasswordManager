@file:OmitFromCoverage

package .testharness.ui.platform.feature.getpasswordorpasskey

import androidx.navigation3.runtime.EntryProviderScope
import .annotation.OmitFromCoverage
import .testharness.ui.platform.feature.credentialmanager.CredentialManagerGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.pushEntry

/**
 * Add Get Password or Passkey destination to the navigation entries.
 */
fun EntryProviderScope<CredentialManagerGraph>.getPasswordOrPasskeyDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<CredentialManagerGraph.GetPasswordOrPasskeyRoute> {
        GetPasswordOrPasskeyScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the Get Password or Passkey test screen.
 */
fun NavigationController<CredentialManagerGraph>.navigateToGetPasswordOrPasskey() {
    this.navigate(route = CredentialManagerGraph.GetPasswordOrPasskeyRoute)
}
