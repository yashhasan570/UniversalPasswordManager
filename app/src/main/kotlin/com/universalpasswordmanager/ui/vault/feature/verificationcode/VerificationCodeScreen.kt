package com.universalpasswordmanager.ui.vault.feature.verificationcode

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toListItemCardStyle
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalOverflowActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalSearchActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.model.OverflowMenuItemData
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.scaffold.model.rememberUniversalPullToRefreshState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.manager.model.AppResumeScreenData
import com.universalpasswordmanager.data.platform.manager.util.AppResumeStateManager
import com.universalpasswordmanager.data.platform.manager.util.RegisterScreenDataOnLifecycleEffect
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalMasterPasswordDialog
import com.universalpasswordmanager.ui.platform.composition.LocalAppResumeStateManager
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.universalpasswordmanager.ui.vault.feature.verificationcode.handlers.VerificationCodeHandlers
import com.universalpasswordmanager.ui.vault.model.VaultItemCipherType
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

    val pullToRefreshState = rememberUniversalPullToRefreshState(
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.verification_codes),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
                onNavigationIconClick = verificationCodeHandler.backClick,
                actions = {
                    UniversalSearchActionItem(
                        contentDescription = stringResource(id = UniversalString.search_vault),
                        onClick = verificationCodeHandler.searchIconClick,
                    )
                    UniversalOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.sync),
                                onClick = verificationCodeHandler.syncClick,
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.lock),
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
                UniversalErrorContent(
                    message = viewState.message.invoke(),
                    buttonData = UniversalButtonData(
                        label = UniversalString.try_again.asText(),
                        onClick = verificationCodeHandler.refreshClick,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VerificationCodeState.ViewState.Loading -> {
                UniversalLoadingContent(modifier = Modifier.fillMaxSize())
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
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                throwable = dialogState.throwable,
                onDismissRequest = onDismissRequest,
            )
        }

        is VerificationCodeState.DialogState.Loading -> {
            UniversalLoadingDialog(
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
        UniversalMasterPasswordDialog(
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
            UniversalListHeaderText(
                label = stringResource(id = UniversalString.items),
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
