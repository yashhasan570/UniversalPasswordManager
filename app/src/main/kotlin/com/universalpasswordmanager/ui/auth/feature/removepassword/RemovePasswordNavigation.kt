package com.universalpasswordmanager.ui.auth.feature.removepassword

import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph

/**
 * Add the Remove Password screen to the navigation entries.
 */
fun EntryProviderScope<RootNavGraph>.removePasswordDestination() {
    entry<RootNavGraph.RemovePasswordRoute> {
        RemovePasswordScreen()
    }
}
