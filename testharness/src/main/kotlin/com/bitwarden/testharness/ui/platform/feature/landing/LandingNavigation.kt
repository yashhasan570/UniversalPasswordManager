@file:OmitFromCoverage

package .testharness.ui.platform.feature.landing

import androidx.navigation3.runtime.EntryProviderScope
import .annotation.OmitFromCoverage
import .testharness.ui.platform.feature.rootnav.RootNavGraph
import .ui.platform.navigation.util.rootPushEntry

/**
 * Add Landing destination to the navigation entries.
 */
fun EntryProviderScope<RootNavGraph>.landingDestination(
    onNavigateToAutofill: () -> Unit,
    onNavigateToCredentialManager: () -> Unit,
) {
    rootPushEntry<RootNavGraph.LandingRoute> {
        LandingScreen(
            onNavigateToAutofill = onNavigateToAutofill,
            onNavigateToCredentialManager = onNavigateToCredentialManager,
        )
    }
}
