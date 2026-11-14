package com.universalpasswordmanager.ui.tools.feature.send

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalMediumTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalOverflowActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalSearchActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.model.OverflowMenuItemData
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCard
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalSelectionDialog
import com.universalpasswordmanager.ui.platform.components.dialog.PremiumRequiredDialog
import com.universalpasswordmanager.ui.platform.components.dialog.row.UniversalBasicDialogRow
import com.universalpasswordmanager.ui.platform.components.fab.UniversalFloatingActionButton
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.scaffold.model.rememberUniversalPullToRefreshState
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.manager.model.AppResumeScreenData
import com.universalpasswordmanager.data.platform.manager.util.AppResumeStateManager
import com.universalpasswordmanager.data.platform.manager.util.RegisterScreenDataOnLifecycleEffect
import com.universalpasswordmanager.ui.platform.composition.LocalAppResumeStateManager
import com.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.universalpasswordmanager.ui.tools.feature.send.addedit.ModeType
import com.universalpasswordmanager.ui.tools.feature.send.handlers.SendHandlers
import com.universalpasswordmanager.ui.tools.feature.send.model.SendItemType
import com.universalpasswordmanager.ui.tools.feature.send.model.UpgradedToPremiumCardData
import com.universalpasswordmanager.ui.tools.feature.send.util.selectionText
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
    val pullToRefreshState = rememberUniversalPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(SendAction.RefreshPull) },
    )

    RegisterScreenDataOnLifecycleEffect(
        appResumeStateManager = appResumeStateManager,
    ) {
        AppResumeScreenData.SendScreen
    }

    val snackbarHostState = rememberUniversalSnackbarHostState()
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
    UniversalScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalMediumTopAppBar(
                title = stringResource(id = UniversalString.send),
                scrollBehavior = scrollBehavior,
                actions = {
                    UniversalSearchActionItem(
                        contentDescription = stringResource(id = UniversalString.search_sends),
                        isDisplayed = state.shouldShowSearchIcon,
                        onClick = { viewModel.trySendAction(SendAction.SearchClick) },
                    )
                    UniversalOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.sync),
                                onClick = { viewModel.trySendAction(SendAction.SyncClick) },
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.lock),
                                onClick = { viewModel.trySendAction(SendAction.LockClick) },
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.about_send),
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
                UniversalFloatingActionButton(
                    onClick = { viewModel.trySendAction(SendAction.AddSendClick) },
                    painter = rememberVectorPainter(id = UniversalDrawable.ic_plus_large),
                    contentDescription = stringResource(id = UniversalString.add_item),
                    modifier = Modifier.testTag(tag = "AddItemButton"),
                )
            }
        },
        pullToRefreshState = pullToRefreshState,
        snackbarHost = { UniversalSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
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

            is SendState.ViewState.Error -> UniversalErrorContent(
                message = viewState.message(),
                buttonData = UniversalButtonData(
                    label = UniversalString.try_again.asText(),
                    onClick = { viewModel.trySendAction(SendAction.RefreshClick) },
                ),
                modifier = contentModifier,
            )

            SendState.ViewState.Loading -> UniversalLoadingContent(
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
        is SendState.DialogState.Error -> UniversalBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
            throwable = dialogState.throwable,
        )

        is SendState.DialogState.Loading -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        SendState.DialogState.SelectSendAddType -> UniversalSelectionDialog(
            title = stringResource(id = UniversalString.type),
            onDismissRequest = onDismissRequest,
        ) {
            SendItemType.entries.forEach {
                UniversalBasicDialogRow(
                    text = it.selectionText(),
                    onClick = { onAddSendSelected(it) },
                )
            }
        }

        is SendState.DialogState.FileTypeRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = UniversalString.send_file_premium_required),
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
    UniversalActionCard(
        cardTitle = stringResource(id = UniversalString.upgraded_to_premium),
        cardSubtitle = stringResource(
            id = UniversalString.you_now_have_access_to_all_advanced_security_features,
        ),
        actionButton = UniversalButtonData(
            label = UniversalString.learn_more.asText(),
            onClick = onActionClick,
            isExternalLink = true,
        ),
        leadingContent = {
            Icon(
                painter = rememberVectorPainter(id = UniversalDrawable.ic_star),
                contentDescription = null,
                tint = UniversalTheme.colorScheme.icon.secondary,
            )
        },
        onDismissClick = onDismissClick,
        modifier = modifier,
    )
}
