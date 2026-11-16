package .authenticator.ui.platform.feature.splash

import androidx.navigation3.runtime.EntryProviderScope
import .authenticator.ui.platform.feature.rootnav.RootNavGraph
import .ui.platform.navigation.util.rootTransitionEntry
import .ui.platform.theme.RootTransition

/**
 * Add splash destinations to the navigation entries.
 */
fun EntryProviderScope<RootNavGraph>.splashDestination() {
    rootTransitionEntry<RootNavGraph.SplashRoute>(rootTransition = RootTransition.NONE) {
        SplashScreen()
    }
}
