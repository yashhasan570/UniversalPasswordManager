package com.universalpasswordmanager.ui.auth.feature.resetpassword

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import com.universalpasswordmanager.ui.platform.navigation.components.UniversalNavDisplay
import com.universalpasswordmanager.ui.platform.navigation.util.rememberNavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.rootTransitionEntry
import com.universalpasswordmanager.ui.platform.theme.RootTransition
import com.universalpasswordmanager.ui.auth.feature.preventaccountlockout.navigateToResetPasswordPreventAccountLockout
import com.universalpasswordmanager.ui.auth.feature.preventaccountlockout.resetPasswordPreventAccountLockoutDestination
import com.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph

/**
 * Add the reset password section to the root navigation entries. The
 * [RootNavGraph.ResetPasswordRoute] hosts the [ResetPasswordGraph].
 */
fun EntryProviderScope<RootNavGraph>.resetPasswordGraph() {
    rootTransitionEntry<RootNavGraph.ResetPasswordRoute>(rootTransition = RootTransition.SLIDE_UP) {
        val navigationController = rememberNavigationController<ResetPasswordGraph>(
            routes = arrayOf(ResetPasswordGraph.ResetPasswordRoute),
        )
        UniversalNavDisplay(
            navigationController = navigationController,
            entryProvider = entryProvider {
                resetPasswordDestination(
                    onNavigateToPreventAccountLockOut = {
                        navigationController.navigateToResetPasswordPreventAccountLockout()
                    },
                )
                resetPasswordPreventAccountLockoutDestination(
                    onNavigateBack = { navigationController.navigateBack() },
                )
            },
        )
    }
}

private fun EntryProviderScope<ResetPasswordGraph>.resetPasswordDestination(
    onNavigateToPreventAccountLockOut: () -> Unit,
) {
    entry<ResetPasswordGraph.ResetPasswordRoute> {
        ResetPasswordScreen(onNavigateToPreventAccountLockOut = onNavigateToPreventAccountLockOut)
    }
}
