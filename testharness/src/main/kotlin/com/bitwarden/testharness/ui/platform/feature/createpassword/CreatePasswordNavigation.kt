@file:OmitFromCoverage

package .testharness.ui.platform.feature.createpassword

import androidx.navigation3.runtime.EntryProviderScope
import .annotation.OmitFromCoverage
import .testharness.ui.platform.feature.credentialmanager.CredentialManagerGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.pushEntry

/**
 * Add Create Password destination to the navigation entries.
 */
fun EntryProviderScope<CredentialManagerGraph>.createPasswordDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<CredentialManagerGraph.CreatePasswordRoute> {
        CreatePasswordScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the Create Password test screen.
 */
fun NavigationController<CredentialManagerGraph>.navigateToCreatePassword() {
    this.navigate(route = CredentialManagerGraph.CreatePasswordRoute)
}
