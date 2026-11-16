package .authenticator.ui.authenticator.feature.edititem

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import .authenticator.ui.authenticator.feature.authenticator.AuthenticatorGraph
import .ui.platform.navigation.NavigationController
import .ui.platform.navigation.util.slideUpEntry

/**
 * Add the edit item screen to the navigation entries.
 */
fun EntryProviderScope<AuthenticatorGraph>.editItemDestination(
    onNavigateBack: () -> Unit = { },
) {
    slideUpEntry<AuthenticatorGraph.EditItemRoute> { route ->
        EditItemScreen(
            viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory>(
                creationCallback = { factory -> factory.create(route = route) },
            ),
            onNavigateBack = onNavigateBack,
        )
    }
}

/**
 * Navigate to the edit item screen.
 */
fun NavigationController<AuthenticatorGraph>.navigateToEditItem(
    itemId: String,
) {
    this.navigate(route = AuthenticatorGraph.EditItemRoute(itemId = itemId))
}
