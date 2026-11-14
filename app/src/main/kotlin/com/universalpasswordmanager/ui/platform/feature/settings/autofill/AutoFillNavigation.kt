@file:OmitFromCoverage

package com.universalpasswordmanager.ui.platform.feature.settings.autofill

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry
import com.universalpasswordmanager.ui.platform.feature.settings.SettingsGraph

/**
 * Add the autofill screen to the navigation entries.
 */
@Suppress("LongParameterList")
fun EntryProviderScope<SettingsGraph>.autoFillDestination(
    onNavigateBack: () -> Unit,
    onNavigateToBlockAutoFillScreen: () -> Unit,
    onNavigateToSetupAutofill: () -> Unit,
    onNavigateToSetupBrowserAutofill: () -> Unit,
    onNavigateToAboutPrivilegedAppsScreen: () -> Unit,
    onNavigateToPrivilegedAppsList: () -> Unit,
) {
    pushEntry<SettingsGraph.AutofillRoute> {
        AutoFillScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToBlockAutoFillScreen = onNavigateToBlockAutoFillScreen,
            onNavigateToSetupAutofill = onNavigateToSetupAutofill,
            onNavigateToSetupBrowserAutofill = onNavigateToSetupBrowserAutofill,
            onNavigateToAboutPrivilegedAppsScreen = onNavigateToAboutPrivilegedAppsScreen,
            onNavigateToPrivilegedAppsList = onNavigateToPrivilegedAppsList,
        )
    }
}

/**
 * Navigate to the autofill screen.
 */
fun NavigationController<SettingsGraph>.navigateToAutoFill() {
    this.navigate(route = SettingsGraph.AutofillRoute)
}
