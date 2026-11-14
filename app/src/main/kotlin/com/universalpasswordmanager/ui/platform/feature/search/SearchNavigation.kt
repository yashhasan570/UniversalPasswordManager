@file:OmitFromCoverage

package com.universalpasswordmanager.ui.platform.feature.search

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.ui.platform.navigation.NavigationController
import com.universalpasswordmanager.ui.platform.navigation.util.slideUpEntry
import com.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph

/**
 * Add the search screen to the navigation entries.
 */
@Suppress("LongParameterList")
fun EntryProviderScope<VaultUnlockedGraph>.searchDestination(
    onNavigateBack: () -> Unit,
    onNavigateToAddEditSend: (route: VaultUnlockedGraph.AddEditSendRoute) -> Unit,
    onNavigateToViewSend: (route: VaultUnlockedGraph.ViewSendRoute) -> Unit,
    onNavigateToEditCipher: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToViewCipher: (route: VaultUnlockedGraph.VaultItemRoute) -> Unit,
    onNavigateToPlan: () -> Unit,
) {
    slideUpEntry<VaultUnlockedGraph.SearchRoute> { route ->
        SearchScreen(
            viewModel = hiltViewModel<SearchViewModel, SearchViewModel.Factory>(
                creationCallback = { factory -> factory.create(route = route) },
            ),
            onNavigateBack = onNavigateBack,
            onNavigateToAddEditSend = onNavigateToAddEditSend,
            onNavigateToViewSend = onNavigateToViewSend,
            onNavigateToEditCipher = onNavigateToEditCipher,
            onNavigateToViewCipher = onNavigateToViewCipher,
            onNavigateToPlan = onNavigateToPlan,
        )
    }
}

/**
 * Navigate to the search screen.
 */
fun NavigationController<VaultUnlockedGraph>.navigateToSearch(searchType: SearchType) {
    this.navigate(route = VaultUnlockedGraph.SearchRoute(searchType = searchType))
}
