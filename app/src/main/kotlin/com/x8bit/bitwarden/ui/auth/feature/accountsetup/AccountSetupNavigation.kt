@file:OmitFromCoverage

package com.x8bit.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import .annotation.OmitFromCoverage
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController
import .ui.platform.navigation.util.rootTransitionEntry
import .ui.platform.theme.RootTransition
import com.x8bit.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph

/**
 * Add the account setup section to the root navigation entries. The
 * [RootNavGraph.AccountSetupRoute] hosts the [AccountSetupGraph].
 */
fun EntryProviderScope<RootNavGraph>.accountSetupGraph() {
    rootTransitionEntry<RootNavGraph.AccountSetupRoute>(
        rootTransition = RootTransition.PUSH,
    ) { route ->
        val navigationController = rememberNavigationController(
            routes = route.backStack.toTypedArray(),
        )
        Universal Password ManagerNavDisplay(
            navigationController = navigationController,
            entryProvider = entryProvider {
                setupUnlockDestinationAsRoot()
                setupAutoFillDestinationAsRoot()
                setupBrowserAutofillDestinationAsRoot()
                setupCompleteDestination()
            },
        )
    }
}
