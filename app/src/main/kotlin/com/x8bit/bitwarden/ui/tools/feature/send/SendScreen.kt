package com.x8bit.universalpasswordmanager.ui.tools.feature.send

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.components.appbar.Universal Password ManagerMediumTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.action.Universal Password ManagerSearchActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerSelectionDialog
import .ui.platform.components.dialog.PremiumRequiredDialog
import .ui.platform.components.dialog.row.Universal Password ManagerBasicDialogRow
import .ui.platform.components.fab.Universal Password ManagerFloatingActionButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.scaffold.model.rememberUniversal Password ManagerPullToRefreshState
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.manager.model.AppResumeScreenData
import com.x8bit.universalpasswordmanager.data.platform.manager.util.AppResumeStateManager
import com.x8bit.universalpasswordmanager.data.platform.manager.util.RegisterScreenDataOnLifecycleEffect
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAppResumeStateManager
import com.x8bit.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.ModeType
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.handlers.SendHandlers
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.model.SendItemType
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.model.UpgradedToPremiumCardData
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.util.selectionText
import kotlinx.collections.immutable.persistentListOf

/**
 * UI for the send screen.
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendScreen(
    onNavigateToAddEditSend: (VaultUnlockedGraph.AddEditSendRoute) -> Unit,
    onNavigateToViewSend: (VaultUnlockedGraph.ViewSendRoute) -> Unit,
    onNavigateToSendFilesList: () -> Unit,
    onNavigateToSendTextList: () -> Unit,
    onNavigateToSearchSend: (searchType: SearchType.Sends) -> Unit,
    onNavigateToPlan: () -> Unit,
    viewModel: SendViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
    appResumeStateManager: AppResumeStateManager = LocalAppResumeStateManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberUniversal Password ManagerPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(SendAction.RefreshPull) },
    )

    RegisterScreenDataOnLifecycleEffect(
        appResumeStateManager = appResumeStateManager,
    ) {
        AppResumeScreenData.SendScreen
    }

    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is SendEvent.NavigateToSearch -> onNavigateToSearchSend(SearchType.Sends.All)

            is SendEvent.NavigateNewSend -> {
                onNavigateToAddEditSend(
                    VaultUnlockedGraph.AddEditSendRoute(
                        modeType = ModeType.ADD,
                        sendType = event.sendType,
                    ),
                )
            }

            is SendEvent.NavigateToEditSend -> {
                onNavigateToAddEditSend(
                    VaultUnlockedGraph.AddEditSendRoute(
                        modeType = ModeType.EDIT,
                        sendType = event.sendType,
                        sendId = event.sendId,
                    ),
                )
            }

            is SendEvent.NavigateToViewSend -> {
                onNavigateToViewSend(
                    VaultUnlockedGraph.ViewSendRoute(
                        sendId = event.sendId,
                        sendType = event.sendType,
                    ),
                )
            }

            is SendEvent.NavigateToAboutSend -> {
                intentManager.launchUri("https:///products/send".toUri())
            }

            is SendEvent.ShowShareSheet -> intentManager.shareText(event.url)
            is SendEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            SendEvent.NavigateToFileSends -> onNavigateToSendFilesList()
            SendEvent.NavigateToTextSends -> onNavigateToSendTextList()
            is SendEvent.NavigateToUrl -> intentManager.launchUri(event.url.toUri())
            SendEvent.NavigateToPlanModal -> onNavigateToPlan()
        }
    }

    SendDialogs(
        dialogState = state.dialogState,
        onAddSendSelected = { viewModel.trySendAction(SendAction.AddSendSelected(it)) },
        onUpgradeToPremiumClick = {
            viewModel.trySendAction(SendAction.UpgradeToPremiumClick)
        },
        onDismissRequest = { viewModel.trySendAction(SendAction.DismissDialog) },
    )

    val sendHandlers = remember(viewModel) { SendHandlers.create(viewModel) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = rememberTopAppBarState(),
    )
    Universal Password ManagerScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerMediumTopAppBar(
                title = stringResource(id = Universal Password ManagerString.send),
                scrollBehavior = scrollBehavior,
                actions = {
                    Universal Password ManagerSearchActionItem(
                        contentDescription = stringResource(id = Universal Password ManagerString.search_sends),
                        isDisplayed = state.shouldShowSearchIcon,
                        onClick = { viewModel.trySendAction(SendAction.SearchClick) },
                    )
                    Universal Password ManagerOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.sync),
                                onClick = { viewModel.trySendAction(SendAction.SyncClick) },
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.lock),
                                onClick = { viewModel.trySendAction(SendAction.LockClick) },
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.about_send),
                                onClick = { viewModel.trySendAction(SendAction.AboutSendClick) },
                                isExternalLink = true,
                            ),
                        ),
                    )
                },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.viewState.shouldDisplayFab,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Universal Password ManagerFloatingActionButton(
                    onClick = { viewModel.trySendAction(SendAction.AddSendClick) },
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_plus_large),
                    contentDescription = stringResource(id = Universal Password ManagerString.add_item),
                    modifier = Modifier.testTag(tag = "AddItemButton"),
                )
            }
        },
        pullToRefreshState = pullToRefreshState,
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
    ) {
        val contentModifier = Modifier.fillMaxSize()
        val upgradedToPremiumCardData = UpgradedToPremiumCardData(
            onCardClick = {
                viewModel.trySendAction(SendAction.UpgradedToPremiumCardClick)
            },
            onCardDismiss = {
                viewModel.trySendAction(SendAction.UpgradedToPremiumCardDismiss)
            },
        ).takeIf { state.isUpgradedToPremiumCardEligible }
        when (val viewState = state.viewState) {
            is SendState.ViewState.Content -> SendContent(
                policyDisablesSend = state.policyDisablesSend,
                shouldShowTypesSection = state.shouldShowTypesSection,
                state = viewState,
                upgradedToPremiumCardData = upgradedToPremiumCardData,
                sendHandlers = sendHandlers,
                modifier = contentModifier,
            )

            SendState.ViewState.Empty -> SendEmpty(
                policyDisablesSend = state.policyDisablesSend,
                onAddItemClick = { viewModel.trySendAction(SendAction.AddSendClick) },
                upgradedToPremiumCardData = upgradedToPremiumCardData,
                modifier = contentModifier,
            )

            is SendState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = viewState.message(),
                buttonData = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.try_again.asText(),
                    onClick = { viewModel.trySendAction(SendAction.RefreshClick) },
                ),
                modifier = contentModifier,
            )

            SendState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun SendDialogs(
    dialogState: SendState.DialogState?,
    onAddSendSelected: (SendItemType) -> Unit,
    onUpgradeToPremiumClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is SendState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
            throwable = dialogState.throwable,
        )

        is SendState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        SendState.DialogState.SelectSendAddType -> Universal Password ManagerSelectionDialog(
            title = stringResource(id = Universal Password ManagerString.type),
            onDismissRequest = onDismissRequest,
        ) {
            SendItemType.entries.forEach {
                Universal Password ManagerBasicDialogRow(
                    text = it.selectionText(),
                    onClick = { onAddSendSelected(it) },
                )
            }
        }

        is SendState.DialogState.FileTypeRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = Universal Password ManagerString.send_file_premium_required),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = onUpgradeToPremiumClick,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

/**
 * Action card rendered at the top of the Send list when the user has just upgraded to premium.
 * Owned by the screen so it can be hosted inside each view state's scrollable container.
 */
@Composable
internal fun UpgradedToPremiumActionCard(
    onActionClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerActionCard(
        cardTitle = stringResource(id = Universal Password ManagerString.upgraded_to_premium),
        cardSubtitle = stringResource(
            id = Universal Password ManagerString.you_now_have_access_to_all_advanced_security_features,
        ),
        actionButton = Universal Password ManagerButtonData(
            label = Universal Password ManagerString.learn_more.asText(),
            onClick = onActionClick,
            isExternalLink = true,
        ),
        leadingContent = {
            Icon(
                painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_star),
                contentDescription = null,
                tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
            )
        },
        onDismissClick = onDismissClick,
        modifier = modifier,
    )
}
