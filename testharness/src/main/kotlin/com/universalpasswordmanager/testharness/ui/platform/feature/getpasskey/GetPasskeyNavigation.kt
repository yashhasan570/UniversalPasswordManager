@file:OmitFromCoverage

package com.universalpasswordmanager.testharness.ui.platform.feature.getpasskey

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.testharness.ui.platform.feature.credentialmanager.CredentialManagerGraph
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry

/**
 * Add Get Passkey destination to the navigation entries.
 */
fun EntryProviderScope<CredentialManagerGraph>.getPasskeyDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<CredentialManagerGraph.GetPasskeyRoute> {
        GetPasskeyScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the Get Passkey test screen.
 */
fun NavigationController<CredentialManagerGraph>.navigateToGetPasskey() {
    this.navigate(route = CredentialManagerGraph.GetPasskeyRoute)
}
