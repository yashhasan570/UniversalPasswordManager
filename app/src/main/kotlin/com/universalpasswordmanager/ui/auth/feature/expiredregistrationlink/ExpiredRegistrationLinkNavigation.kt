package com.universalpasswordmanager.ui.auth.feature.expiredregistrationlink

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry
import com.universalpasswordmanager.ui.auth.feature.auth.AuthGraph

/**
 * Add the expired registration link screen to the navigation entries.
 */
fun EntryProviderScope<AuthGraph>.expiredRegistrationLinkDestination(
    onNavigateBack: () -> Unit,
    onNavigateToStartRegistration: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    pushEntry<AuthGraph.ExpiredRegistrationLinkRoute> {
        ExpiredRegistrationLinkScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToStartRegistration = onNavigateToStartRegistration,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}
