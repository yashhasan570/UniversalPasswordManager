@file:OmitFromCoverage

package com.x8bit.universalpasswordmanager.ui.vault.feature.migratetomyitems

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import .annotation.OmitFromCoverage
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController
import .ui.platform.navigation.util.rootTransitionEntry
import .ui.platform.theme.RootTransition
import com.x8bit.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph
import com.x8bit.universalpasswordmanager.ui.vault.feature.leaveorganization.leaveOrganizationDestination
import com.x8bit.universalpasswordmanager.ui.vault.feature.leaveorganization.navigateToLeaveOrganization

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
        Universal Password ManagerNavDisplay(
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
