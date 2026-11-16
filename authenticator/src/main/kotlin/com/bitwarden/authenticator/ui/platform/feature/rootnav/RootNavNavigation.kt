@file:OmitFromCoverage

package .authenticator.ui.platform.feature.rootnav

import androidx.navigation3.runtime.EntryProviderScope
import .annotation.OmitFromCoverage
import .authenticator.ui.main.MainGraph

/**
 * Add the root navigation screen to the navigation entries.
 */
fun EntryProviderScope<MainGraph>.rootNavDestination(
    onSplashScreenRemoved: () -> Unit,
) {
    entry<MainGraph.RootNavigationRoute> {
        RootNavScreen(onSplashScreenRemoved = onSplashScreenRemoved)
    }
}
