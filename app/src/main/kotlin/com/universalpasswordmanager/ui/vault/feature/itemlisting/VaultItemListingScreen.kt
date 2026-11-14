package com.universalpasswordmanager.ui.vault.feature.itemlisting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.components.account.UniversalAccountActionItem
import com.universalpasswordmanager.ui.platform.components.account.UniversalAccountSwitcher
import com.universalpasswordmanager.ui.platform.components.account.util.initials
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalOverflowActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalSearchActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.model.OverflowMenuItemData
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.dialog.PremiumRequiredDialog
import com.universalpasswordmanager.ui.platform.components.fab.UniversalFloatingActionButton
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.scaffold.model.UniversalPullToRefreshState
import com.universalpasswordmanager.ui.platform.components.scaffold.model.rememberUniversalPullToRefreshState
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalBiometricsManager
import com.universalpasswordmanager.ui.platform.composition.LocalExitManager
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.manager.biometrics.BiometricsManager
import com.universalpasswordmanager.ui.platform.manager.exit.ExitManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.credentials.manager.CredentialProviderCompletionManager
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalMasterPasswordDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalOverwriteCredentialConfirmationDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalPinDialog
import com.universalpasswordmanager.ui.platform.composition.LocalCredentialProviderCompletionManager
import com.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.PinInputDialog
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.universalpasswordmanager.ui.tools.feature.send.addedit.ModeType
import com.universalpasswordmanager.ui.vault.components.VaultItemSelectionDialog
import com.universalpasswordmanager.ui.vault.feature.itemlisting.handlers.VaultItemListingHandlers
import com.universalpasswordmanager.ui.vault.feature.itemlisting.handlers.VaultItemListingUserVerificationHandlers
import com.universalpasswordmanager.ui.vault.model.VaultAddEditType
import com.universalpasswordmanager.ui.vault.model.VaultItemListingType
import kotlinx.collections.immutable.persistentListOf

/**
 * Displays the vault item listing screen.
 */
@Composable
@Suppress("LongMethod", "CyclomaticComplexMethod")
fun VaultItemListingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVaultItemScreen: (route: VaultUnlockedGraph.VaultItemRoute) -> Unit,
    onNavigateToVaultEditItemScreen: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToVaultItemListing: (vaultItemListingType: VaultItemListingType) -> Unit,
    onNavigateToVaultAddItemScreen: (route: VaultUnlockedGraph.VaultAddEditRoute) -> Unit,
    onNavigateToAddFolder: (selectedFolderId: String?) -> Unit,
    onNavigateToAddEditSendItem: (route: VaultUnlockedGraph.AddEditSendRoute) -> Unit,
    onNavigateToViewSendItem: (route: VaultUnlockedGraph.ViewSendRoute) -> Unit,
    onNavigateToSearch: (searchType: SearchType) -> Unit,
    onNavigateToPlan: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    exitManager: ExitManager = LocalExitManager.current,
    credentialProviderCompletionManager: CredentialProviderCompletionManager =
        LocalCredentialProviderCompletionManager.current,
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    viewModel: VaultItemListingViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val userVerificationHandlers = remember(viewModel) {
        VaultItemListingUserVerificationHandlers.create(viewModel = viewModel)
    }

    val pullToRefreshState = rememberUniversalPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(VaultItemListingsAction.RefreshPull) },
    )
    val snackbarHostState = rememberUniversalSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is VaultItemListingEvent.NavigateBack -> onNavigateBack()

            is VaultItemListingEvent.NavigateToVaultItem -> {
                onNavigateToVaultItemScreen(VaultUnlockedGraph.VaultItemRoute(event.id, event.type))
            }

            is VaultItemListingEvent.ShowShareSheet -> {
                intentManager.shareText(event.content)
            }

            is VaultItemListingEvent.NavigateToAddVaultItem -> {
                onNavigateToVaultAddItemScreen(
                    VaultUnlockedGraph.VaultAddEditRoute(
                        vaultAddEditType = VaultAddEditType.AddItem(),
                        vaultItemCipherType = event.vaultItemCipherType,
                        selectedFolderId = event.selectedFolderId,
                        selectedCollectionId = event.selectedCollectionId,
                    ),
                )
            }

            is VaultItemListingEvent.NavigateToViewSendItem -> {
                onNavigateToViewSendItem(
                    VaultUnlockedGraph.ViewSendRoute(sendId = event.id, sendType = event.sendType),
                )
            }

            is VaultItemListingEvent.NavigateToEditCipher -> {
                onNavigateToVaultEditItemScreen(
                    VaultUnlockedGraph.VaultAddEditRoute(
                        vaultAddEditType = VaultAddEditType.EditItem(vaultItemId = event.cipherId),
                        vaultItemCipherType = event.cipherType,
                    ),
                )
            }

            is VaultItemListingEvent.NavigateToUrl -> {
                intentManager.launchUri(event.url.toUri())
            }

            VaultItemListingEvent.NavigateToPlanModal -> onNavigateToPlan()

            is VaultItemListingEvent.NavigateToAddSendItem -> {
                onNavigateToAddEditSendItem(
                    VaultUnlockedGraph.AddEditSendRoute(
                        sendType = event.sendType,
                        modeType = ModeType.ADD,
                    ),
                )
            }

            is VaultItemListingEvent.NavigateToEditSendItem -> {
                onNavigateToAddEditSendItem(
                    VaultUnlockedGraph.AddEditSendRoute(
                        sendType = event.sendType,
                        modeType = ModeType.EDIT,
                        sendId = event.id,
                    ),
                )
            }

            is VaultItemListingEvent.NavigateToSearchScreen -> {
                onNavigateToSearch(event.searchType)
            }

            is VaultItemListingEvent.NavigateToFolderItem -> {
                onNavigateToVaultItemListing(VaultItemListingType.Folder(event.folderId))
            }

            is VaultItemListingEvent.NavigateToCollectionItem -> {
                onNavigateToVaultItemListing(VaultItemListingType.Collection(event.collectionId))
            }

            is VaultItemListingEvent.CompleteCredentialRegistration -> {
                credentialProviderCompletionManager.completeCredentialRegistration(event.result)
            }

            is VaultItemListingEvent.CredentialManagerUserVerification -> {
                biometricsManager.promptUserVerification(
                    onSuccess = {
                        userVerificationHandlers
                            .onUserVerificationSuccess(event.selectedCipherView)
                    },
                    onCancel = userVerificationHandlers.onUserVerificationCancelled,
                    onLockOut = userVerificationHandlers.onUserVerificationLockOut,
                    onError = userVerificationHandlers.onUserVerificationFail,
                    onNotSupported = {
                        userVerificationHandlers.onUserVerificationNotSupported(
                            event.selectedCipherView.id,
                        )
                    },
                )
            }

            is VaultItemListingEvent.CompleteFido2Assertion -> {
                credentialProviderCompletionManager.completeFido2Assertion(event.result)
            }

            is VaultItemListingEvent.CompleteProviderGetCredentialsRequest -> {
                credentialProviderCompletionManager
                    .completeProviderGetCredentialsRequest(event.result)
            }

            is VaultItemListingEvent.CompleteProviderGetPasswordCredentialRequest -> {
                credentialProviderCompletionManager.completePasswordGet(event.result)
            }

            VaultItemListingEvent.ExitApp -> exitManager.exitApplication()

            is VaultItemListingEvent.NavigateToAddFolder -> {
                onNavigateToAddFolder(event.parentFolderName)
            }

            is VaultItemListingEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    val vaultItemListingHandlers = remember(viewModel) {
        VaultItemListingHandlers.create(viewModel)
    }
    VaultItemListingDialogs(
        dialogState = state.dialogState,
        vaultItemListingHandlers = vaultItemListingHandlers,
    )

    BackHandler(onBack = vaultItemListingHandlers.backClick)
    VaultItemListingScaffold(
        state = state,
        snackbarHostState = snackbarHostState,
        pullToRefreshState = pullToRefreshState,
        vaultItemListingHandlers = vaultItemListingHandlers,
    )
}

@Suppress("LongMethod")
@Composable
private fun VaultItemListingDialogs(
    dialogState: VaultItemListingState.DialogState?,
    vaultItemListingHandlers: VaultItemListingHandlers,
) {
    when (dialogState) {
        is VaultItemListingState.DialogState.Error -> UniversalBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            throwable = dialogState.throwable,
        )

        is VaultItemListingState.DialogState.Loading -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        is VaultItemListingState.DialogState.CipherDecryptionError -> {
            UniversalTwoButtonDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                confirmButtonText = stringResource(UniversalString.copy_error_report),
                dismissButtonText = stringResource(UniversalString.close),
                onConfirmClick = {
                    vaultItemListingHandlers.shareCipherDecryptionErrorClick(
                        dialogState.selectedCipherId,
                    )
                },
                onDismissClick = vaultItemListingHandlers.dismissDialogRequest,
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            )
        }

        is VaultItemListingState.DialogState.CredentialManagerOperationFail -> UniversalBasicDialog(
            title = dialogState.title(),
            message = dialogState.message(),
            onDismissRequest = {
                vaultItemListingHandlers.dismissCredentialManagerErrorDialog(dialogState.message)
            },
        )

        is VaultItemListingState.DialogState.OverwritePasskeyConfirmationPrompt -> {
            @Suppress("MaxLineLength")
            UniversalOverwriteCredentialConfirmationDialog(
                title = stringResource(id = UniversalString.overwrite_passkey),
                message = stringResource(
                    id = UniversalString
                        .this_item_already_contains_a_passkey_are_you_sure_you_want_to_overwrite_the_current_passkey,
                ),
                onConfirmClick = {
                    vaultItemListingHandlers.confirmOverwriteExistingPasskey(
                        dialogState.cipherViewId,
                    )
                },
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            )
        }

        is VaultItemListingState.DialogState.UserVerificationMasterPasswordPrompt -> {
            UniversalMasterPasswordDialog(
                onConfirmClick = { password ->
                    vaultItemListingHandlers.submitMasterPasswordCredentialVerification(
                        password,
                        dialogState.selectedCipherId,
                    )
                },
                onDismissRequest = vaultItemListingHandlers.dismissUserVerification,
            )
        }

        is VaultItemListingState.DialogState.UserVerificationMasterPasswordError -> {
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = {
                    vaultItemListingHandlers.retryGetCredentialPasswordVerification(
                        dialogState.selectedCipherId,
                    )
                },
            )
        }

        is VaultItemListingState.DialogState.UserVerificationPinPrompt -> {
            UniversalPinDialog(
                onConfirmClick = { pin ->
                    vaultItemListingHandlers.submitPinCredentialVerification(
                        pin,
                        dialogState.selectedCipherId,
                    )
                },
                onDismissRequest = vaultItemListingHandlers.dismissUserVerification,
            )
        }

        is VaultItemListingState.DialogState.UserVerificationPinError -> {
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = {
                    vaultItemListingHandlers.retryPinCredentialVerification(
                        dialogState.selectedCipherId,
                    )
                },
            )
        }

        is VaultItemListingState.DialogState.UserVerificationPinSetUpPrompt -> {
            PinInputDialog(
                onCancelClick = vaultItemListingHandlers.dismissUserVerification,
                onSubmitClick = { pin ->
                    vaultItemListingHandlers.submitPinSetUpCredentialVerification(
                        pin,
                        dialogState.selectedCipherId,
                    )
                },
                onDismissRequest = vaultItemListingHandlers.dismissUserVerification,
            )
        }

        is VaultItemListingState.DialogState.UserVerificationPinSetUpError -> {
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = {
                    vaultItemListingHandlers.retryPinSetUpCredentialVerification(
                        dialogState.selectedCipherId,
                    )
                },
            )
        }

        is VaultItemListingState.DialogState.VaultItemTypeSelection -> {
            VaultItemSelectionDialog(
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
                onOptionSelected = vaultItemListingHandlers.vaultItemTypeSelected,
                excludedOptions = dialogState.excludedOptions,
            )
        }

        is VaultItemListingState.DialogState.TrustPrivilegedAddPrompt -> {
            UniversalTwoButtonDialog(
                title = stringResource(UniversalString.unrecognized_browser),
                message = dialogState.message.invoke(),
                confirmButtonText = stringResource(UniversalString.trust),
                dismissButtonText = stringResource(UniversalString.cancel),
                onConfirmClick = {
                    vaultItemListingHandlers.trustPrivilegedAppClick(dialogState.selectedCipherId)
                },
                onDismissClick = {
                    vaultItemListingHandlers.dismissCredentialManagerErrorDialog(
                        UniversalString
                            .passkey_operation_failed_because_the_browser_is_not_trusted
                            .asText(),
                    )
                },
                onDismissRequest = {
                    vaultItemListingHandlers.dismissCredentialManagerErrorDialog(
                        UniversalString
                            .passkey_operation_failed_because_the_browser_is_not_trusted
                            .asText(),
                    )
                },
            )
        }

        is VaultItemListingState.DialogState.ArchiveRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = UniversalString.archiving_items_is_a_premium_feature),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = vaultItemListingHandlers.upgradeToPremiumClick,
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            )
        }

        is VaultItemListingState.DialogState.FileTypeRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = UniversalString.send_file_premium_required),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = vaultItemListingHandlers.upgradeToPremiumClick,
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            )
        }

        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
private fun VaultItemListingScaffold(
    state: VaultItemListingState,
    pullToRefreshState: UniversalPullToRefreshState,
    snackbarHostState: UniversalSnackbarHostState,
    vaultItemListingHandlers: VaultItemListingHandlers,
) {
    var isAccountMenuVisible by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = state.appBarTitle(),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = UniversalString.back),
                    onNavigationIconClick = vaultItemListingHandlers.backClick,
                )
                    .takeIf { state.shouldShowNavigationIcon },
                actions = {
                    if (state.shouldShowAccountSwitcher) {
                        UniversalAccountActionItem(
                            initials = state.activeAccountSummary.initials,
                            color = state.activeAccountSummary.avatarColor,
                            onClick = { isAccountMenuVisible = !isAccountMenuVisible },
                        )
                    }
                    UniversalSearchActionItem(
                        contentDescription = stringResource(id = UniversalString.search_vault),
                        isDisplayed = state.shouldShowSearchIcon,
                        onClick = vaultItemListingHandlers.searchIconClick,
                    )
                    UniversalOverflowActionItem(
                        isVisible = state.shouldShowOverflowMenu,
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.sync),
                                onClick = vaultItemListingHandlers.syncClick,
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.lock),
                                onClick = vaultItemListingHandlers.lockClick,
                            ),
                        ),
                    )
                },
            )
        },
        floatingActionButton = {
            if (state.hasAddItemFabButton) {
                UniversalFloatingActionButton(
                    onClick = vaultItemListingHandlers.addVaultItemClick,
                    painter = rememberVectorPainter(id = UniversalDrawable.ic_plus_large),
                    contentDescription = stringResource(id = UniversalString.add_item),
                    modifier = Modifier.testTag(tag = "AddItemButton"),
                )
            }
        },
        overlay = {
            UniversalAccountSwitcher(
                isVisible = isAccountMenuVisible,
                accountSummaries = state.accountSummaries,
                onSwitchAccountClick = vaultItemListingHandlers.switchAccountClick,
                onLockAccountClick = vaultItemListingHandlers.lockAccountClick,
                onLogoutAccountClick = vaultItemListingHandlers.logoutAccountClick,
                onAddAccountClick = {
                    // Not available
                },
                onDismissRequest = { isAccountMenuVisible = false },
                isAddAccountAvailable = false,
                topAppBarScrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxSize(),
            )
        },
        pullToRefreshState = pullToRefreshState,
        snackbarHost = { UniversalSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
    ) {
        when (state.viewState) {
            is VaultItemListingState.ViewState.Content -> {
                VaultItemListingContent(
                    state = state.viewState,
                    actionCard = state.actionCard,
                    showAddTotpBanner = state.isTotp,
                    policyDisablesSend = state.policyDisablesSend &&
                        state.itemListingType is VaultItemListingState.ItemListingType.Send,
                    vaultItemListingHandlers = vaultItemListingHandlers,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VaultItemListingState.ViewState.NoItems -> {
                VaultItemListingEmpty(
                    state = state.viewState,
                    policyDisablesSend = state.policyDisablesSend &&
                        state.itemListingType is VaultItemListingState.ItemListingType.Send,
                    addItemClickAction = vaultItemListingHandlers.addVaultItemClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VaultItemListingState.ViewState.Error -> {
                UniversalErrorContent(
                    message = state.viewState.message(),
                    buttonData = UniversalButtonData(
                        label = UniversalString.try_again.asText(),
                        onClick = vaultItemListingHandlers.refreshClick,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VaultItemListingState.ViewState.Loading -> {
                UniversalLoadingContent(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
