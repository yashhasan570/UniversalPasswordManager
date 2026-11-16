package com.x8bit.universalpasswordmanager.ui.auth.feature.welcome

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.util.stayEntry
import com.x8bit.universalpasswordmanager.ui.auth.feature.auth.AuthGraph

/**
 * Add the Welcome screen to the navigation entries.
 */
fun EntryProviderScope<AuthGraph>.welcomeDestination(
    onNavigateToLogin: () -> Unit,
    onNavigateToStartRegistration: () -> Unit,
) {
    stayEntry<AuthGraph.WelcomeRoute> {
        WelcomeScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToStartRegistration = onNavigateToStartRegistration,
        )
    }
}
