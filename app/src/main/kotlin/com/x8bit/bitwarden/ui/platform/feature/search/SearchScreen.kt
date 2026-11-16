package com.x8bit.universalpasswordmanager.ui.platform.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.components.appbar.Universal Password ManagerSearchTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.PremiumRequiredDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.data.platform.manager.model.AppResumeScreenData
import com.x8bit.universalpasswordmanager.data.platform.manager.util.AppResumeStateManager
import com.x8bit.universalpasswordmanager.data.platform.manager.util.RegisterScreenDataOnLifecycleEffect
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAppResumeStateManager
import com.x8bit.universalpasswordmanager.ui.platform.feature.search.handlers.SearchHandlers
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.ModeType
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.VaultFilter
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultAddEditType
import kotlinx.collections.immutable.toImmutableList

/**
 * The search UI for vault items or send items.
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddEditSend: (route: VaultUnlockedGraph.AddEditSendRoute) -> Unit,
    onNavigateToViewSend: (route: VaultUnlockedGraph.ViewSendRoute) -> Unit,
    onNavigateToEditCipher: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToViewCipher: (route: VaultUnlockedGraph.VaultItemRoute) -> Unit,
    onNavigateToPlan: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: SearchViewModel,
    appResumeStateManager: AppResumeStateManager = LocalAppResumeStateManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val searchHandlers = remember(viewModel) { SearchHandlers.create(viewModel) }
    RegisterScreenDataOnLifecycleEffect(
        appResumeStateManager = appResumeStateManager,
    ) {
        AppResumeScreenData.SearchScreen(
            searchTerm = state.searchTerm,
        )
    }

    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            SearchEvent.NavigateBack -> onNavigateBack()
            is SearchEvent.NavigateToEditSend -> {
                onNavigateToAddEditSend(
                    VaultUnlockedGraph.AddEditSendRoute(
                        sendType = event.sendType,
                        modeType = ModeType.EDIT,
                        sendId = event.sendId,
                    ),
                )
            }

            is SearchEvent.NavigateToViewSend -> {
                onNavigateToViewSend(
                    VaultUnlockedGraph.ViewSendRoute(
                        sendId = event.sendId,
                        sendType = event.sendType,
                    ),
                )
            }

            is SearchEvent.NavigateToEditCipher -> {
                onNavigateToEditCipher(
                    VaultUnlockedGraph.VaultAddEditRoute(
                        vaultAddEditType = VaultAddEditType.EditItem(vaultItemId = event.cipherId),
                        vaultItemCipherType = event.cipherType,
                    ),
                )
            }

            is SearchEvent.NavigateToViewCipher -> {
                onNavigateToViewCipher(
                    VaultUnlockedGraph.VaultItemRoute(
                        vaultItemId = event.cipherId,
                        cipherType = event.cipherType,
                    ),
                )
            }

            is SearchEvent.NavigateToUrl -> intentManager.launchUri(event.url.toUri())
            SearchEvent.NavigateToPlanModal -> onNavigateToPlan()
            is SearchEvent.ShowShareSheet -> intentManager.shareText(event.content)
            is SearchEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    SearchDialogs(
        dialogState = state.dialogState,
        onDismissRequest = searchHandlers.onDismissRequest,
        onUpgradeToPremiumClick = searchHandlers.onUpgradeToPremiumClick,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        topBar = {
            Universal Password ManagerSearchTopAppBar(
                searchTerm = state.searchTerm,
                placeholder = state.searchType.title(),
                onSearchTermChange = searchHandlers.onSearchTermChange,
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                    onNavigationIconClick = searchHandlers.onBackClick,
                ),
                clearIconContentDescription = stringResource(id = Universal Password ManagerString.clear),
            )
        },
        utilityBar = {
            val vaultFilterData = state.vaultFilterData
            if (state.viewState.hasVaultFilter && vaultFilterData != null) {
                VaultFilter(
                    selectedVaultFilterType = vaultFilterData.selectedVaultFilterType,
                    vaultFilterTypes = vaultFilterData.vaultFilterTypes.toImmutableList(),
                    onVaultFilterTypeSelect = searchHandlers.onVaultFilterSelect,
                    topAppBarScrollBehavior = scrollBehavior,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            // There is some built-in padding to the menu button that makes up
                            // the visual difference here.
                            end = 12.dp,
                        )
                        .fillMaxWidth(),
                )
            }
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        when (val viewState = state.viewState) {
            is SearchState.ViewState.Content -> SearchContent(
                viewState = viewState,
                searchHandlers = searchHandlers,
                searchType = state.searchType,
                modifier = Modifier
                    .fillMaxSize(),
            )

            is SearchState.ViewState.Empty -> SearchEmptyContent(
                viewState = viewState,
                modifier = Modifier
                    .fillMaxSize(),
            )

            is SearchState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = viewState.message(),
                modifier = Modifier
                    .fillMaxSize(),
            )

            SearchState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SearchDialogs(
    dialogState: SearchState.DialogState?,
    onDismissRequest: () -> Unit,
    onUpgradeToPremiumClick: () -> Unit,
) {
    when (dialogState) {
        is SearchState.DialogState.ArchiveRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = Universal Password ManagerString.archiving_items_is_a_premium_feature),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = onUpgradeToPremiumClick,
                onDismissRequest = onDismissRequest,
            )
        }

        is SearchState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
            throwable = dialogState.throwable,
        )

        is SearchState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}
