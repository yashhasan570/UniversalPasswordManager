@file:OmitFromCoverage

package com.universalpasswordmanager.testharness.ui.platform.feature.landing

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.testharness.ui.platform.feature.rootnav.RootNavGraph
import com.universalpasswordmanager.ui.platform.navigation.util.rootPushEntry

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
