package com.x8bit.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.navigation3.runtime.EntryProviderScope

/**
 * Add the setup complete screen to the account setup navigation entries.
 */
fun EntryProviderScope<AccountSetupGraph>.setupCompleteDestination() {
    entry<AccountSetupGraph.SetupCompleteRoute> {
        SetupCompleteScreen()
    }
}
