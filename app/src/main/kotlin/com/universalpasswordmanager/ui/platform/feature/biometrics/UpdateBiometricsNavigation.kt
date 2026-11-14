@file:OmitFromCoverage

package com.universalpasswordmanager.ui.platform.feature.biometrics

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.overlaynav.OverlayNavGraph

/**
 * Add the Update Biometrics screen to the navigation entries.
 */
fun EntryProviderScope<OverlayNavGraph>.updateBiometricsDestination(
    onDismiss: () -> Unit,
    onSplashScreenRemoved: () -> Unit,
) {
    slideUpEntry<OverlayNavGraph.UpdateBiometricsRoute> {
        UpdateBiometricsScreen(onDismiss = onDismiss)
        // If we are displaying the update biometrics screen, then we can just
        // hide the splash screen.
        onSplashScreenRemoved()
    }
}

/**
 * Navigate to the Update Biometrics screen.
 */
fun NavigationController<OverlayNavGraph>.navigateToUpdateBiometrics() {
    this.addSingleTop(route = OverlayNavGraph.UpdateBiometricsRoute)
}
