@file:OmitFromCoverage

package .authenticator.ui.authenticator.feature.authenticator

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import .annotation.OmitFromCoverage
import .authenticator.ui.authenticator.feature.edititem.editItemDestination
import .authenticator.ui.authenticator.feature.edititem.navigateToEditItem
import .authenticator.ui.authenticator.feature.manualcodeentry.manualCodeEntryDestination
import .authenticator.ui.authenticator.feature.navbar.authenticatorNavBarDestination
import .authenticator.ui.authenticator.feature.qrcodescan.qrCodeScanDestination
import .authenticator.ui.authenticator.feature.search.itemSearchDestination
import .authenticator.ui.platform.feature.rootnav.RootNavGraph
import .authenticator.ui.platform.feature.tutorial.tutorialSettingsDestination
import .ui.platform.navigation.components.Universal Password ManagerNavDisplay
import .ui.platform.navigation.util.rememberNavigationController

/**
 * Add the authenticator section to the root navigation entries. The
 * [RootNavGraph.AuthenticatorRoute] hosts the [AuthenticatorGraph].
 *
 * @param onBackStackUpdate Invoked whenever the back stack of the [AuthenticatorGraph] changes.
 */
fun EntryProviderScope<RootNavGraph>.authenticatorGraph(
    onBackStackUpdate: () -> Unit,
) {
    entry<RootNavGraph.AuthenticatorRoute> {
        val navigationController = rememberNavigationController<AuthenticatorGraph>(
            routes = arrayOf(AuthenticatorGraph.NavBarRoute),
        )
        LaunchedEffect(key1 = navigationController) {
            navigationController.backStackSnapshot.collect { onBackStackUpdate() }
        }
        Universal Password ManagerNavDisplay(
            navigationController = navigationController,
            entryProvider = entryProvider {
                authenticatorNavBarDestination(navigationController = navigationController)
                editItemDestination(onNavigateBack = { navigationController.navigateBack() })
                itemSearchDestination(
                    onNavigateBack = { navigationController.navigateBack() },
                    onNavigateToEdit = { navigationController.navigateToEditItem(itemId = it) },
                )
                qrCodeScanDestination(
                    onNavigateBack = { navigationController.navigateBack() },
                    onNavigateToManualCodeEntryScreen = {
                        navigationController.replaceLast(
                            route = AuthenticatorGraph.ManualCodeEntryRoute,
                        )
                    },
                )
                manualCodeEntryDestination(
                    onNavigateBack = { navigationController.navigateBack() },
                    onNavigateToQrCodeScreen = {
                        navigationController.replaceLast(route = AuthenticatorGraph.QrCodeScanRoute)
                    },
                )
                tutorialSettingsDestination(
                    onTutorialFinished = { navigationController.navigateBack() },
                )
            },
        )
    }
}
