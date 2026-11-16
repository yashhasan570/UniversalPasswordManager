package .authenticator.ui.authenticator.feature.search

import androidx.navigation3.runtime.EntryProviderScope
import .authenticator.ui.authenticator.feature.authenticator.AuthenticatorGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry

/**
 * Add item search destination to the navigation entries.
 */
fun EntryProviderScope<AuthenticatorGraph>.itemSearchDestination(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
) {
    slideUpEntry<AuthenticatorGraph.ItemSearchRoute> {
        ItemSearchScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToEdit = onNavigateToEdit,
        )
    }
}

/**
 * Navigate to the item search screen.
 */
fun NavigationController<AuthenticatorGraph>.navigateToSearch() {
    this.navigate(route = AuthenticatorGraph.ItemSearchRoute)
}
