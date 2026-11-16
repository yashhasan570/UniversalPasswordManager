package com.x8bit.universalpasswordmanager.ui.vault.feature.verificationcode

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.action.Universal Password ManagerSearchActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.scaffold.model.rememberUniversal Password ManagerPullToRefreshState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.manager.model.AppResumeScreenData
import com.x8bit.universalpasswordmanager.data.platform.manager.util.AppResumeStateManager
import com.x8bit.universalpasswordmanager.data.platform.manager.util.RegisterScreenDataOnLifecycleEffect
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerMasterPasswordDialog
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAppResumeStateManager
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.vault.feature.verificationcode.handlers.VerificationCodeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemCipherType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Displays the verification codes to the user.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationCodeScreen(
    viewModel: VerificationCodeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToVaultItemScreen: (route: VaultUnlockedGraph.VaultItemRoute) -> Unit,
    appResumeStateManager: AppResumeStateManager = LocalAppResumeStateManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val verificationCodeHandler = remember(viewModel) {
        VerificationCodeHandlers.create(viewModel)
    }

    val pullToRefreshState = rememberUniversal Password ManagerPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(VerificationCodeAction.RefreshPull) },
    )

    RegisterScreenDataOnLifecycleEffect(
        appResumeStateManager = appResumeStateManager,
    ) {
        AppResumeScreenData.VerificationCodeScreen
    }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is VerificationCodeEvent.NavigateBack -> onNavigateBack()
            is VerificationCodeEvent.NavigateToVaultItem -> {
                onNavigateToVaultItemScreen(
                    VaultUnlockedGraph.VaultItemRoute(
                        vaultItemId = event.id,
                        cipherType = VaultItemCipherType.LOGIN,
                    ),
                )
            }

            is VerificationCodeEvent.NavigateToVaultSearchScreen -> {
                onNavigateToSearch()
            }
        }
    }

    VerificationCodeDialogs(
        dialogState = state.dialogState,
        onDismissRequest = verificationCodeHandler.dismissDialog,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.verification_codes),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = verificationCodeHandler.backClick,
                actions = {
                    Universal Password ManagerSearchActionItem(
                        contentDescription = stringResource(id = Universal Password ManagerString.search_vault),
                        onClick = verificationCodeHandler.searchIconClick,
                    )
                    Universal Password ManagerOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.sync),
                                onClick = verificationCodeHandler.syncClick,
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.lock),
                                onClick = verificationCodeHandler.lockClick,
                            ),
                        ),
                    )
                },
            )
        },
        pullToRefreshState = pullToRefreshState,
    ) {
        when (val viewState = state.viewState) {
            is VerificationCodeState.ViewState.Content -> {
                VerificationCodeContent(
                    items = viewState.verificationCodeDisplayItems.toImmutableList(),
                    onCopyClick = verificationCodeHandler.copyClick,
                    onItemClick = verificationCodeHandler.itemClick,
                    onMasterPasswordRepromptSubmit = verificationCodeHandler
                        .masterPasswordRepromptSubmit,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VerificationCodeState.ViewState.Error -> {
                Universal Password ManagerErrorContent(
                    message = viewState.message.invoke(),
                    buttonData = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.try_again.asText(),
                        onClick = verificationCodeHandler.refreshClick,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VerificationCodeState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun VerificationCodeDialogs(
    dialogState: VerificationCodeState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is VerificationCodeState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                throwable = dialogState.throwable,
                onDismissRequest = onDismissRequest,
            )
        }

        is VerificationCodeState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(
                text = dialogState.message(),
            )
        }

        null -> Unit
    }
}

@Composable
private fun VerificationCodeContent(
    items: ImmutableList<VerificationCodeDisplayItem>,
    onItemClick: (id: String) -> Unit,
    onMasterPasswordRepromptSubmit: (id: String, password: String) -> Unit,
    onCopyClick: (text: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var masterPasswordRepromptId by remember { mutableStateOf<String?>(value = null) }
    masterPasswordRepromptId?.let { id ->
        Universal Password ManagerMasterPasswordDialog(
            onConfirmClick = { password ->
                onMasterPasswordRepromptSubmit(id, password)
                masterPasswordRepromptId = null
            },
            onDismissRequest = { masterPasswordRepromptId = null },
        )
    }
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Spacer(modifier = Modifier.height(height = 12.dp))
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.items),
                supportingLabel = items.size.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
        }

        itemsIndexed(items) { index, it ->
            VaultVerificationCodeItem(
                startIcon = it.startIcon,
                label = it.label,
                supportingLabel = it.supportingLabel,
                timeLeftSeconds = it.timeLeftSeconds,
                periodSeconds = it.periodSeconds,
                authCode = it.authCode,
                hideAuthCode = it.hideAuthCode,
                onCopyClick = { onCopyClick(it.authCode) },
                onItemClick = {
                    if (it.hideAuthCode) {
                        masterPasswordRepromptId = it.id
                    } else {
                        onItemClick(it.id)
                    }
                },
                cardStyle = items.toListItemCardStyle(index = index, dividerPadding = 56.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
