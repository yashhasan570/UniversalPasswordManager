@file:OmitFromCoverage

package com.x8bit.universalpasswordmanager.ui.platform.feature.cookieacquisition

import androidx.navigation3.runtime.EntryProviderScope
import .annotation.OmitFromCoverage
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry
import com.x8bit.universalpasswordmanager.ui.platform.feature.overlaynav.OverlayNavGraph

/**
 * Add the cookie acquisition screen to the navigation entries.
 */
fun EntryProviderScope<OverlayNavGraph>.cookieAcquisitionDestination(
    onDismiss: () -> Unit,
    onSplashScreenRemoved: () -> Unit,
) {
    slideUpEntry<OverlayNavGraph.CookieAcquisitionRoute> {
        CookieAcquisitionScreen(onDismiss = onDismiss)
        // If we are displaying the cookie acquisition screen, then we can just hide the
        // splash screen.
        onSplashScreenRemoved()
    }
}

/**
 * Navigate to the cookie acquisition screen.
 */
fun NavigationController<OverlayNavGraph>.navigateToCookieAcquisition() {
    this.addSingleTop(route = OverlayNavGraph.CookieAcquisitionRoute)
}
