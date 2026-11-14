@file:OmitFromCoverage

package com.universalpasswordmanager.testharness.ui.platform.feature.getpassword

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.testharness.ui.platform.feature.credentialmanager.CredentialManagerGraph
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry

/**
 * Add Get Password destination to the navigation entries.
 */
fun EntryProviderScope<CredentialManagerGraph>.getPasswordDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<CredentialManagerGraph.GetPasswordRoute> {
        GetPasswordScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the Get Password test screen.
 */
fun NavigationController<CredentialManagerGraph>.navigateToGetPassword() {
    this.navigate(route = CredentialManagerGraph.GetPasswordRoute)
}
