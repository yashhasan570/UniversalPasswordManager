package com.universalpasswordmanager.ui.auth.feature.welcome

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.util.stayEntry
import com.universalpasswordmanager.ui.auth.feature.auth.AuthGraph

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
