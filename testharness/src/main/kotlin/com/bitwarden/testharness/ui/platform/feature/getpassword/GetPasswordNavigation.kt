@file:OmitFromCoverage

package .testharness.ui.platform.feature.getpassword

import androidx.navigation3.runtime.EntryProviderScope
import .annotation.OmitFromCoverage
import .testharness.ui.platform.feature.credentialmanager.CredentialManagerGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.pushEntry

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
