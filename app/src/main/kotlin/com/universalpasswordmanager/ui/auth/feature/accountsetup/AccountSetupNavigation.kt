@file:OmitFromCoverage

package com.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.components.UniversalNavDisplay
import com.universalpasswordmanager.ui.platform.navigation.util.rememberNavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.rootTransitionEntry
import com.universalpasswordmanager.ui.platform.theme.RootTransition
import com.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph

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
        UniversalNavDisplay(
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
