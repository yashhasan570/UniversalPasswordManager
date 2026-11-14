package com.universalpasswordmanager.ui.auth.feature.environment

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.auth.feature.auth.AuthGraph

/**
 * Add the environment destination to the navigation entries.
 */
fun EntryProviderScope<AuthGraph>.environmentDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<AuthGraph.EnvironmentRoute> {
        EnvironmentScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Navigate to the environment screen.
 */
fun NavigationController<AuthGraph>.navigateToEnvironment() {
    this.navigate(route = AuthGraph.EnvironmentRoute)
}
