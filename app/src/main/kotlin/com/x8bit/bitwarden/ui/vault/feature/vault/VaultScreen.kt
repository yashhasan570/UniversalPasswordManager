package com.x8bit.universalpasswordmanager.ui.vault.feature.vault

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.LifecycleEventEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.account.Universal Password ManagerAccountActionItem
import .ui.platform.components.account.Universal Password ManagerAccountSwitcher
import .ui.platform.components.animation.AnimateNullableContentVisibility
import .ui.platform.components.appbar.Universal Password ManagerMediumTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.action.Universal Password ManagerSearchActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.appbar.model.TopAppBarDividerStyle
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.actionCardExitAnimation
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dialog.PremiumRequiredDialog
import .ui.platform.components.fab.Universal Password ManagerFloatingActionButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.scaffold.model.Universal Password ManagerPullToRefreshState
import .ui.platform.components.scaffold.model.rememberUniversal Password ManagerPullToRefreshState
import .ui.platform.components.snackbar.Universal Password ManagerSnackbar
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarHostState
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerPlurals
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerMasterPasswordDialog
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAppReviewManager
import com.x8bit.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.platform.manager.review.AppReviewManager
import com.x8bit.universalpasswordmanager.ui.vault.components.VaultItemSelectionDialog
import com.x8bit.universalpasswordmanager.ui.vault.components.model.CreateVaultItemType
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.handlers.VaultHandlers
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultAddEditType
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemListingType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val APP_REVIEW_DELAY = 3000L

/**
 * The vault screen for the application.
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun VaultScreen(
    viewModel: VaultViewModel = hiltViewModel(),
    onNavigateToVaultAddItemScreen: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToVaultItemScreen: (route: VaultUnlockedGraph.VaultItemRoute) -> Unit,
    onNavigateToVaultEditItemScreen: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToVerificationCodeScreen: () -> Unit,
    onNavigateToVaultItemListingScreen: (vaultItemType: VaultItemListingType) -> Unit,
    onNavigateToSearchVault: (searchType: SearchType.Vault) -> Unit,
    onDimBottomNavBarRequest: (shouldDim: Boolean) -> Unit,
    onNavigateToImportLogins: () -> Unit,
    onNavigateToAddFolderScreen: (selectedFolderId: String?) -> Unit,
    onNavigateToAboutScreen: () -> Unit,
    onNavigateToAutofillScreen: () -> Unit,
    onNavigateToPlan: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    appReviewManager: AppReviewManager = LocalAppReviewManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberUniversal Password ManagerPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(VaultAction.RefreshPull) },
    )
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    LifecycleEventEffect { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.trySendAction(VaultAction.LifecycleResumed)
            }

            else -> Unit
        }
    }
    val scope = rememberCoroutineScope()
    val launchPrompt = remember {
        {
            scope.launch {
                delay(APP_REVIEW_DELAY.milliseconds)
                appReviewManager.promptForReview()
            }
        }
    }
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is VaultEvent.NavigateToAddItemScreen -> {
                onNavigateToVaultAddItemScreen(
                    VaultUnlockedGraph.VaultAddEditRoute(
                        vaultAddEditType = VaultAddEditType.AddItem(),
                        vaultItemCipherType = event.type,
                    ),
                )
            }

            VaultEvent.NavigateToVaultSearchScreen -> onNavigateToSearchVault(SearchType.Vault.All)

            is VaultEvent.NavigateToVerificationCodeScreen -> {
                onNavigateToVerificationCodeScreen()
            }

            is VaultEvent.NavigateToVaultItem -> {
                onNavigateToVaultItemScreen(
                    VaultUnlockedGraph.VaultItemRoute(
                        vaultItemId = event.itemId,
                        cipherType = event.type,
                    ),
                )
            }

            is VaultEvent.NavigateToEditVaultItem -> {
                onNavigateToVaultEditItemScreen(
                    VaultUnlockedGraph.VaultAddEditRoute(
                        vaultAddEditType = VaultAddEditType.EditItem(vaultItemId = event.itemId),
                        vaultItemCipherType = event.type,
                    ),
                )
            }

            is VaultEvent.NavigateToItemListing -> {
                onNavigateToVaultItemListingScreen(event.itemListingType)
            }

            is VaultEvent.NavigateToUrl -> intentManager.launchUri(event.url.toUri())
            VaultEvent.NavigateToImportLogins -> onNavigateToImportLogins()
            is VaultEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            VaultEvent.PromptForAppReview -> {
                launchPrompt.invoke()
            }

            VaultEvent.NavigateToAddFolder -> {
                onNavigateToAddFolderScreen(null)
            }

            VaultEvent.NavigateToAbout -> onNavigateToAboutScreen()

            is VaultEvent.ShowShareSheet -> {
                intentManager.shareText(event.content)
            }

            VaultEvent.NavigateToAutofillSettings -> onNavigateToAutofillScreen()

            VaultEvent.NavigateToUpgradePremium -> onNavigateToPlan()
        }
    }
    val vaultHandlers = remember(viewModel) { VaultHandlers.create(viewModel) }
    VaultScreenScaffold(
        state = state,
        pullToRefreshState = pullToRefreshState,
        vaultHandlers = vaultHandlers,
        onDimBottomNavBarRequest = onDimBottomNavBarRequest,
        snackbarHostState = snackbarHostState,
    )
}

/**
 * Scaffold for the [VaultScreen]
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VaultScreenScaffold(
    state: VaultState,
    pullToRefreshState: Universal Password ManagerPullToRefreshState,
    vaultHandlers: VaultHandlers,
    onDimBottomNavBarRequest: (shouldDim: Boolean) -> Unit,
    snackbarHostState: Universal Password ManagerSnackbarHostState,
) {
    var accountMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val updateAccountMenuVisibility = { shouldShowMenu: Boolean ->
        accountMenuVisible = shouldShowMenu
        onDimBottomNavBarRequest(shouldShowMenu)
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { !accountMenuVisible },
    )

    // Dynamic dialogs
    VaultDialogs(dialogState = state.dialog, vaultHandlers = vaultHandlers)
    Universal Password ManagerScaffold(
        topBar = {
            Universal Password ManagerMediumTopAppBar(
                title = state.appBarTitle(),
                scrollBehavior = scrollBehavior,
                dividerStyle = state
                    .vaultFilterDataWithFilter
                    ?.let { TopAppBarDividerStyle.STATIC }
                    ?: TopAppBarDividerStyle.ON_SCROLL,
                actions = {
                    Universal Password ManagerAccountActionItem(
                        initials = state.initials,
                        color = state.avatarColor,
                        onClick = {
                            updateAccountMenuVisibility(!accountMenuVisible)
                        },
                    )
                    Universal Password ManagerSearchActionItem(
                        contentDescription = stringResource(id = Universal Password ManagerString.search_vault),
                        isDisplayed = state.shouldShowSearchIcon,
                        onClick = vaultHandlers.searchIconClickAction,
                    )
                    Universal Password ManagerOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.sync),
                                onClick = vaultHandlers.syncAction,
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.lock),
                                onClick = vaultHandlers.lockAction,
                            ),
                        ),
                    )
                },
            )
        },
        utilityBar = {
            state.vaultFilterDataWithFilter?.let {
                VaultFilter(
                    selectedVaultFilterType = it.selectedVaultFilterType,
                    vaultFilterTypes = it.vaultFilterTypes.toImmutableList(),
                    onVaultFilterTypeSelect = vaultHandlers.vaultFilterTypeSelect,
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
        snackbarHost = {
            AnimateNullableContentVisibility(
                targetState = state.flightRecorderSnackBar,
                label = "AnimateFlightRecorderSnackbar",
            ) { data ->
                Universal Password ManagerSnackbar(
                    universalpasswordmanagerSnackbarData = data,
                    onDismiss = vaultHandlers.dismissFlightRecorderSnackbar,
                    onActionClick = vaultHandlers.flightRecorderGoToSettingsClick,
                )
            }
            if (state.flightRecorderSnackBar == null) {
                // We don't want additional animations from the Animated Visibility and we only
                // want this displayed if the flight recorder snackbar is not set.
                Universal Password ManagerSnackbarHost(
                    universalpasswordmanagerHostState = snackbarHostState,
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.viewState.hasFab && !accountMenuVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Universal Password ManagerFloatingActionButton(
                    onClick = vaultHandlers.selectAddItemTypeClickAction,
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_plus_large),
                    contentDescription = stringResource(id = Universal Password ManagerString.add_item),
                    modifier = Modifier.testTag(tag = "AddItemButton"),
                )
            }
        },
        overlay = {
            Universal Password ManagerAccountSwitcher(
                isVisible = accountMenuVisible,
                accountSummaries = state.accountSummaries,
                onSwitchAccountClick = vaultHandlers.accountSwitchClickAction,
                onLockAccountClick = vaultHandlers.accountLockClickAction,
                onLogoutAccountClick = vaultHandlers.accountLogoutClickAction,
                onAddAccountClick = vaultHandlers.addAccountClickAction,
                onDismissRequest = { updateAccountMenuVisibility(false) },
                topAppBarScrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxSize(),
            )
        },
        pullToRefreshState = pullToRefreshState,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (val viewState = state.viewState) {
                is VaultState.ViewState.Content -> VaultContent(
                    state = viewState,
                    actionCardState = state.actionCard,
                    collapsedSections = state.collapsedSections,
                    vaultHandlers = vaultHandlers,
                    modifier = Modifier.fillMaxSize(),
                )

                is VaultState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                    modifier = Modifier.fillMaxSize(),
                )

                is VaultState.ViewState.NoItems -> {
                    AnimateNullableContentVisibility(
                        targetState = state.actionCard,
                        exit = actionCardExitAnimation(),
                        label = "VaultNoItemsActionCard",
                    ) { actionCard ->
                        VaultActionCard(
                            actionCardState = actionCard,
                            vaultHandlers = vaultHandlers,
                            modifier = Modifier
                                .fillMaxWidth()
                                .standardHorizontalMargin()
                                .padding(top = 12.dp),
                        )
                    }
                    VaultNoItems(
                        vectorRes = Universal Password ManagerDrawable.ill_items,
                        headerText = stringResource(
                            id = Universal Password ManagerString.save_and_protect_your_data,
                        ),
                        message = stringResource(
                            Universal Password ManagerString.the_vault_protects_more_than_just_passwords,
                        ),
                        buttonText = stringResource(id = Universal Password ManagerString.add_login),
                        policyDisablesSend = false,
                        addItemClickAction = {
                            vaultHandlers.addItemClickAction(CreateVaultItemType.LOGIN)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is VaultState.ViewState.Error -> Universal Password ManagerErrorContent(
                    message = viewState.message(),
                    buttonData = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.try_again.asText(),
                        onClick = vaultHandlers.tryAgainClick,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun VaultDialogs(
    dialogState: VaultState.DialogState?,
    vaultHandlers: VaultHandlers,
) {
    when (dialogState) {
        is VaultState.DialogState.ArchiveRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = Universal Password ManagerString.archiving_items_is_a_premium_feature),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = vaultHandlers.upgradeToPremiumClick,
                onDismissRequest = vaultHandlers.dialogDismiss,
            )
        }

        is VaultState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.message())
        }

        is VaultState.DialogState.Syncing -> Universal Password ManagerLoadingDialog(
            text = stringResource(id = Universal Password ManagerString.syncing),
        )

        is VaultState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title(),
            message = dialogState.message(),
            throwable = dialogState.error,
            onDismissRequest = vaultHandlers.dialogDismiss,
        )

        is VaultState.DialogState.SelectVaultAddItemType -> VaultItemSelectionDialog(
            onOptionSelected = {
                vaultHandlers.addItemClickAction(it)
            },
            onDismissRequest = vaultHandlers.dialogDismiss,
            excludedOptions = dialogState.excludedOptions,
        )

        is VaultState.DialogState.CipherDecryptionError -> {
            Universal Password ManagerTwoButtonDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                confirmButtonText = stringResource(Universal Password ManagerString.copy_error_report),
                dismissButtonText = stringResource(Universal Password ManagerString.close),
                onConfirmClick = {
                    vaultHandlers.onShareCipherDecryptionErrorClick(dialogState.selectedCipherId)
                },
                onDismissClick = vaultHandlers.dialogDismiss,
                onDismissRequest = vaultHandlers.dialogDismiss,
            )
        }

        is VaultState.DialogState.ThirdPartyBrowserAutofill -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(
                    id = Universal Password ManagerString.enable_browser_autofill_to_keep_filling_passwords,
                ),
                message = stringResource(
                    id = Universal Password ManagerString.your_browser_recently_updated_how_autofill_works_singular,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.go_to_settings),
                dismissButtonText = stringResource(id = Universal Password ManagerString.not_now),
                onConfirmClick = vaultHandlers.onEnabledThirdPartyAutofillClick,
                onDismissClick = vaultHandlers.onDismissThirdPartyAutofillDialogClick,
                onDismissRequest = vaultHandlers.onDismissThirdPartyAutofillDialogClick,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            )
        }

        is VaultState.DialogState.VaultLoadCipherDecryptionError -> {
            Universal Password ManagerTwoButtonDialog(
                title = dialogState.title(),
                message = pluralStringResource(
                    id = Universal Password ManagerPlurals
                        .universalpasswordmanager_could_not_decrypt_x_vault_item_copy_and_share_description_long,
                    count = dialogState.cipherCount,
                    dialogState.cipherCount,
                ),
                confirmButtonText = stringResource(Universal Password ManagerString.copy_error_report),
                dismissButtonText = stringResource(Universal Password ManagerString.close),
                onConfirmClick = {
                    vaultHandlers.onShareAllCipherDecryptionErrorsClick()
                },
                onDismissClick = vaultHandlers.dialogDismiss,
                onDismissRequest = vaultHandlers.dialogDismiss,
            )
        }

        is VaultState.DialogState.VaultLoadKdfUpdateRequired -> {
            Universal Password ManagerMasterPasswordDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                dismissButtonText = stringResource(Universal Password ManagerString.later),
                onConfirmClick = {
                    vaultHandlers.onKdfUpdatePasswordRepromptSubmit(it)
                },
                onDismissRequest = vaultHandlers.dialogDismiss,
            )
        }

        is VaultState.DialogState.SyncError -> {
            Universal Password ManagerTwoButtonDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                confirmButtonText = stringResource(id = Universal Password ManagerString.try_again),
                dismissButtonText = stringResource(id = Universal Password ManagerString.not_now),
                onConfirmClick = vaultHandlers.tryAgainClick,
                onDismissClick = vaultHandlers.dialogDismiss,
                onDismissRequest = vaultHandlers.dialogDismiss,
            )
        }

        null -> Unit
    }
}
