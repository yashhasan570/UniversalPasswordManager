package com.x8bit.universalpasswordmanager.ui.platform.feature.splash

import androidx.navigation3.runtime.EntryProviderScope
import .ui.platform.navigation.util.rootTransitionEntry
import .ui.platform.theme.RootTransition
import com.x8bit.universalpasswordmanager.ui.platform.feature.rootnav.RootNavGraph

/**
 * Add the splash screen to the navigation entries.
 */
fun EntryProviderScope<RootNavGraph>.splashDestination() {
    rootTransitionEntry<RootNavGraph.SplashRoute>(rootTransition = RootTransition.NONE) {
        SplashScreen()
    }
}
