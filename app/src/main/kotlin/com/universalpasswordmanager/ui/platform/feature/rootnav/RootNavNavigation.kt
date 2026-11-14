@file:OmitFromCoverage

package com.universalpasswordmanager.ui.platform.feature.rootnav

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.feature.overlaynav.OverlayNavGraph

/**
 * Add the root navigation screen to the navigation entries.
 */
fun EntryProviderScope<OverlayNavGraph>.rootNavDestination(
    onSplashScreenRemoved: () -> Unit,
) {
    entry<OverlayNavGraph.RootNavigationRoute> {
        RootNavScreen(onSplashScreenRemoved = onSplashScreenRemoved)
    }
}
