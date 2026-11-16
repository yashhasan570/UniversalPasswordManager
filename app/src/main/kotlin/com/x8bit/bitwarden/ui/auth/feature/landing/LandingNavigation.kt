package com.x8bit.universalpasswordmanager.ui.auth.feature.landing

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.stayEntry
import com.x8bit.universalpasswordmanager.ui.auth.feature.auth.AuthGraph

/**
 * Navigate to the landing screen.
 */
fun NavigationController<AuthGraph>.navigateToLanding() {
    this.navigate(route = AuthGraph.LandingRoute)
}

/**
 * Add the Landing screen to the navigation entries.
 */
fun EntryProviderScope<AuthGraph>.landingDestination(
    onNavigateToLogin: (emailAddress: String) -> Unit,
    onNavigateToEnvironment: () -> Unit,
    onNavigateToStartRegistration: () -> Unit,
    onNavigateToPreAuthSettings: () -> Unit,
) {
    stayEntry<AuthGraph.LandingRoute> {
        LandingScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToEnvironment = onNavigateToEnvironment,
            onNavigateToStartRegistration = onNavigateToStartRegistration,
            onNavigateToPreAuthSettings = onNavigateToPreAuthSettings,
        )
    }
}
