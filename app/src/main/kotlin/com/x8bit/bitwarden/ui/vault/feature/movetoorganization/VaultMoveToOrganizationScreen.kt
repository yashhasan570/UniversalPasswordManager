package com.x8bit.universalpasswordmanager.ui.vault.feature.movetoorganization

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultCollection

/**
 * Displays the vault move to organization screen.
 */
@Composable
fun VaultMoveToOrganizationScreen(
    viewModel: VaultMoveToOrganizationViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is VaultMoveToOrganizationEvent.NavigateBack -> onNavigateBack()
        }
    }
    VaultMoveToOrganizationScaffold(
        state = state,
        closeClick = { viewModel.trySendAction(VaultMoveToOrganizationAction.BackClick) },
        moveClick = { viewModel.trySendAction(VaultMoveToOrganizationAction.MoveClick) },
        dismissClick = { viewModel.trySendAction(VaultMoveToOrganizationAction.DismissClick) },
        organizationSelect = {
            viewModel.trySendAction(VaultMoveToOrganizationAction.OrganizationSelect(it))
        },
        collectionSelect = {
            viewModel.trySendAction(VaultMoveToOrganizationAction.CollectionSelect(it))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
private fun VaultMoveToOrganizationScaffold(
    state: VaultMoveToOrganizationState,
    closeClick: () -> Unit,
    moveClick: () -> Unit,
    dismissClick: () -> Unit,
    organizationSelect: (VaultMoveToOrganizationState.ViewState.Content.Organization) -> Unit,
    collectionSelect: (VaultCollection) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    when (val dialog = state.dialogState) {
        is VaultMoveToOrganizationState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.an_error_has_occurred),
                message = dialog.message(),
                onDismissRequest = dismissClick,
                throwable = dialog.throwable,
            )
        }

        is VaultMoveToOrganizationState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.appBarText(),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = closeClick,
                actions = {
                    Universal Password ManagerTextButton(
                        label = state.appBarButtonText(),
                        onClick = moveClick,
                        isEnabled = state.viewState is
                            VaultMoveToOrganizationState.ViewState.Content,
                        modifier = Modifier.testTag("MoveButton"),
                    )
                },
            )
        },
    ) {
        val modifier = Modifier
            .fillMaxSize()
        when (state.viewState) {
            is VaultMoveToOrganizationState.ViewState.Content -> {
                VaultMoveToOrganizationContent(
                    state = state.viewState,
                    showOnlyCollections = state.onlyShowCollections,
                    organizationSelect = organizationSelect,
                    collectionSelect = collectionSelect,
                    modifier = modifier,
                )
            }

            is VaultMoveToOrganizationState.ViewState.Error -> {
                Universal Password ManagerErrorContent(
                    message = state.viewState.message(),
                    modifier = modifier,
                )
            }

            is VaultMoveToOrganizationState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(modifier = modifier)
            }

            is VaultMoveToOrganizationState.ViewState.Empty -> {
                VaultMoveToOrganizationEmpty(modifier = modifier)
            }
        }
    }
}
