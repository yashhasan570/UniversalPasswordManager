@file:OmitFromCoverage

package com.universalpasswordmanager.testharness.ui.platform.feature.rootnav

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.testharness.ui.platform.feature.main.MainGraph

/**
 * Add the root navigation screen to the navigation entries.
 */
fun EntryProviderScope<MainGraph>.rootNavDestination(
    onSplashScreenRemoved: () -> Unit,
) {
    entry<MainGraph.RootNavigationRoute> {
        RootNavScreen(onSplashScreenRemoved = onSplashScreenRemoved)
    }
}
