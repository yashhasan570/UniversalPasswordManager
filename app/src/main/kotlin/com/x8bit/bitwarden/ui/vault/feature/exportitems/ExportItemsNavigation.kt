@file:OmitFromCoverage

package com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import .annotation.OmitFromCoverage
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController
import com.x8bit.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.reviewexport.navigateToReviewExport
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.reviewexport.reviewExportDestination
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.selectaccount.selectAccountDestination
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword.navigateToVerifyPassword
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword.verifyPasswordDestination

/**
 * Add the export items section to the root navigation entries. The [RootNavGraph.ExportItemsRoute]
 * hosts the [ExportItemsGraph].
 */
fun EntryProviderScope<RootNavGraph>.exportItemsGraph() {
    entry<RootNavGraph.ExportItemsRoute> { route ->
        val navigationController = rememberNavigationController(
            routes = route.backStack.toTypedArray(),
        )
        Universal Password ManagerNavDisplay(
            navigationController = navigationController,
            entryProvider = entryProvider {
                selectAccountDestination(
                    onAccountSelected = { userId, hasOtherAccounts ->
                        navigationController.navigateToVerifyPassword(
                            userId = userId,
                            hasOtherAccounts = hasOtherAccounts,
                        )
                    },
                )
                verifyPasswordDestination(
                    onNavigateBack = { navigationController.navigateBack() },
                    onPasswordVerified = {
                        navigationController.navigateToReviewExport()
                    },
                )
                reviewExportDestination(navigationController = navigationController)
            },
        )
    }
}
