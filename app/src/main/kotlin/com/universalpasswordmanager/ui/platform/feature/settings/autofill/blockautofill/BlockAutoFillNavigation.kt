package com.universalpasswordmanager.ui.platform.feature.settings.autofill.blockautofill

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.pushEntry
import com.universalpasswordmanager.ui.platform.feature.settings.SettingsGraph

/**
 * Add the block autofill screen to the navigation entries.
 */
fun EntryProviderScope<SettingsGraph>.blockAutoFillDestination(
    onNavigateBack: () -> Unit,
) {
    pushEntry<SettingsGraph.BlockAutofillRoute> {
        BlockAutoFillScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Navigate to the block autofill screen.
 */
fun NavigationController<SettingsGraph>.navigateToBlockAutoFillScreen() {
    this.navigate(route = SettingsGraph.BlockAutofillRoute)
}
