@file:OmitFromCoverage

package com.universalpasswordmanager.ui.vault.feature.migratetomyitems

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.components.UniversalNavDisplay
import com.universalpasswordmanager.ui.platform.navigation.util.rememberNavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.rootTransitionEntry
import com.universalpasswordmanager.ui.platform.theme.RootTransition
import com.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph
import com.universalpasswordmanager.ui.vault.feature.leaveorganization.leaveOrganizationDestination
import com.universalpasswordmanager.ui.vault.feature.leaveorganization.navigateToLeaveOrganization

/**
 * Add the migrate to my items section to the root navigation entries. The
 * [RootNavGraph.MigrateToMyItemsRoute] hosts the [MigrateToMyItemsGraph].
 */
fun EntryProviderScope<RootNavGraph>.migrateToMyItemsGraph() {
    rootTransitionEntry<RootNavGraph.MigrateToMyItemsRoute>(
        rootTransition = RootTransition.SLIDE_UP,
    ) {
        val navigationController = rememberNavigationController<MigrateToMyItemsGraph>(
            routes = arrayOf(MigrateToMyItemsGraph.MigrateToMyItemsRoute),
        )
        UniversalNavDisplay(
            navigationController = navigationController,
            entryProvider = entryProvider {
                migrateToMyItemsDestination(
                    onNavigateToLeaveOrganization = { organizationId, organizationName ->
                        navigationController.navigateToLeaveOrganization(
                            organizationId = organizationId,
                            organizationName = organizationName,
                        )
                    },
                )
                leaveOrganizationDestination(
                    onNavigateBack = { navigationController.navigateBack() },
                )
            },
        )
    }
}
