package com.universalpasswordmanager.ui.auth.feature.preventaccountlockout

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.auth.feature.auth.AuthGraph
import com.universalpasswordmanager.ui.auth.feature.resetpassword.ResetPasswordGraph

/**
 * Navigate to the Prevent Account Lockout screen.
 */
fun NavigationController<AuthGraph>.navigateToPreventAccountLockout() {
    this.navigate(route = AuthGraph.PreventAccountLockoutRoute)
}

/**
 * Navigate to the Prevent Account Lockout screen while resetting the password.
 */
fun NavigationController<ResetPasswordGraph>.navigateToResetPasswordPreventAccountLockout() {
    this.navigate(route = ResetPasswordGraph.PreventAccountLockoutRoute)
}

/**
 * Add the Prevent Account Lockout screen to the auth navigation entries.
 */
fun EntryProviderScope<AuthGraph>.preventAccountLockoutDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<AuthGraph.PreventAccountLockoutRoute> {
        PreventAccountLockoutScreen(onNavigateBack = onNavigateBack)
    }
}

/**
 * Add the Prevent Account Lockout screen to the reset password navigation entries.
 */
fun EntryProviderScope<ResetPasswordGraph>.resetPasswordPreventAccountLockoutDestination(
    onNavigateBack: () -> Unit,
) {
    slideUpEntry<ResetPasswordGraph.PreventAccountLockoutRoute> {
        PreventAccountLockoutScreen(onNavigateBack = onNavigateBack)
    }
}
