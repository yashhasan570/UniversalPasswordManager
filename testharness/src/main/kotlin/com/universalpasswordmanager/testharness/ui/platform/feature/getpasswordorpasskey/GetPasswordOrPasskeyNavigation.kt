@file:OmitFromCoverage

package com.universalpasswordmanager.testharness.ui.platform.feature.getpasswordorpasskey

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.testharness.ui.platform.feature.credentialmanager.CredentialManagerGraph
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry

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
