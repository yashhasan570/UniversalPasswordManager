@file:OmitFromCoverage

package com.universalpasswordmanager.ui.vault.feature.exportitems.selectaccount

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.popUpTo
import com.universalpasswordmanager.ui.platform.navigation.util.rootPushEntry
import com.universalpasswordmanager.ui.vault.feature.exportitems.ExportItemsGraph

/**
 * Add the [SelectAccountScreen] to the navigation entries.
 */
fun EntryProviderScope<ExportItemsGraph>.selectAccountDestination(
    onAccountSelected: (id: String, hasOtherAccounts: Boolean) -> Unit,
) {
    rootPushEntry<ExportItemsGraph.SelectAccountRoute> {
        SelectAccountScreen(
            onAccountSelected = onAccountSelected,
        )
    }
}

/**
 * Navigate to the [SelectAccountScreen].
 */
fun NavigationController<ExportItemsGraph>.navigateToSelectAccountScreen() {
    this.navigate(route = ExportItemsGraph.SelectAccountRoute)
}

/**
 * Pop up to the [SelectAccountScreen].
 */
fun NavigationController<ExportItemsGraph>.popUpToSelectAccountScreen() {
    this.popUpTo<ExportItemsGraph.SelectAccountRoute>(inclusive = false)
}
