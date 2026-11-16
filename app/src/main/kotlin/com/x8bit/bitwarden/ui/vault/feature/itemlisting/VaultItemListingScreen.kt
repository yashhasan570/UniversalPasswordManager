package com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting

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
import .ui.platform.base.util.EventsEffect
import .ui.platform.components.account.Universal Password ManagerAccountActionItem
import .ui.platform.components.account.Universal Password ManagerAccountSwitcher
import .ui.platform.components.account.util.initials
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.action.Universal Password ManagerSearchActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.model.Universal Password ManagerButtonData
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
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarHostState
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.composition.LocalExitManager
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.manager.exit.ExitManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.credentials.manager.CredentialProviderCompletionManager
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerMasterPasswordDialog
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerOverwriteCredentialConfirmationDialog
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerPinDialog
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalCredentialProviderCompletionManager
import com.x8bit.universalpasswordmanager.ui.platform.feature.search.model.SearchType
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.PinInputDialog
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.ModeType
import com.x8bit.universalpasswordmanager.ui.vault.components.VaultItemSelectionDialog
import com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.handlers.VaultItemListingHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.handlers.VaultItemListingUserVerificationHandlers
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultAddEditType
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemListingType
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

    val pullToRefreshState = rememberUniversal Password ManagerPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(VaultItemListingsAction.RefreshPull) },
    )
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
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
        is VaultItemListingState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            throwable = dialogState.throwable,
        )

        is VaultItemListingState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        is VaultItemListingState.DialogState.CipherDecryptionError -> {
            Universal Password ManagerTwoButtonDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                confirmButtonText = stringResource(Universal Password ManagerString.copy_error_report),
                dismissButtonText = stringResource(Universal Password ManagerString.close),
                onConfirmClick = {
                    vaultItemListingHandlers.shareCipherDecryptionErrorClick(
                        dialogState.selectedCipherId,
                    )
                },
                onDismissClick = vaultItemListingHandlers.dismissDialogRequest,
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            )
        }

        is VaultItemListingState.DialogState.CredentialManagerOperationFail -> Universal Password ManagerBasicDialog(
            title = dialogState.title(),
            message = dialogState.message(),
            onDismissRequest = {
                vaultItemListingHandlers.dismissCredentialManagerErrorDialog(dialogState.message)
            },
        )

        is VaultItemListingState.DialogState.OverwritePasskeyConfirmationPrompt -> {
            @Suppress("MaxLineLength")
            Universal Password ManagerOverwriteCredentialConfirmationDialog(
                title = stringResource(id = Universal Password ManagerString.overwrite_passkey),
                message = stringResource(
                    id = Universal Password ManagerString
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
            Universal Password ManagerMasterPasswordDialog(
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
            Universal Password ManagerBasicDialog(
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
            Universal Password ManagerPinDialog(
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
            Universal Password ManagerBasicDialog(
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
            Universal Password ManagerBasicDialog(
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
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.unrecognized_browser),
                message = dialogState.message.invoke(),
                confirmButtonText = stringResource(Universal Password ManagerString.trust),
                dismissButtonText = stringResource(Universal Password ManagerString.cancel),
                onConfirmClick = {
                    vaultItemListingHandlers.trustPrivilegedAppClick(dialogState.selectedCipherId)
                },
                onDismissClick = {
                    vaultItemListingHandlers.dismissCredentialManagerErrorDialog(
                        Universal Password ManagerString
                            .passkey_operation_failed_because_the_browser_is_not_trusted
                            .asText(),
                    )
                },
                onDismissRequest = {
                    vaultItemListingHandlers.dismissCredentialManagerErrorDialog(
                        Universal Password ManagerString
                            .passkey_operation_failed_because_the_browser_is_not_trusted
                            .asText(),
                    )
                },
            )
        }

        is VaultItemListingState.DialogState.ArchiveRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = Universal Password ManagerString.archiving_items_is_a_premium_feature),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = vaultItemListingHandlers.upgradeToPremiumClick,
                onDismissRequest = vaultItemListingHandlers.dismissDialogRequest,
            )
        }

        is VaultItemListingState.DialogState.FileTypeRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = Universal Password ManagerString.send_file_premium_required),
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
    pullToRefreshState: Universal Password ManagerPullToRefreshState,
    snackbarHostState: Universal Password ManagerSnackbarHostState,
    vaultItemListingHandlers: VaultItemListingHandlers,
) {
    var isAccountMenuVisible by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.appBarTitle(),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                    onNavigationIconClick = vaultItemListingHandlers.backClick,
                )
                    .takeIf { state.shouldShowNavigationIcon },
                actions = {
                    if (state.shouldShowAccountSwitcher) {
                        Universal Password ManagerAccountActionItem(
                            initials = state.activeAccountSummary.initials,
                            color = state.activeAccountSummary.avatarColor,
                            onClick = { isAccountMenuVisible = !isAccountMenuVisible },
                        )
                    }
                    Universal Password ManagerSearchActionItem(
                        contentDescription = stringResource(id = Universal Password ManagerString.search_vault),
                        isDisplayed = state.shouldShowSearchIcon,
                        onClick = vaultItemListingHandlers.searchIconClick,
                    )
                    Universal Password ManagerOverflowActionItem(
                        isVisible = state.shouldShowOverflowMenu,
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.sync),
                                onClick = vaultItemListingHandlers.syncClick,
                            ),
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.lock),
                                onClick = vaultItemListingHandlers.lockClick,
                            ),
                        ),
                    )
                },
            )
        },
        floatingActionButton = {
            if (state.hasAddItemFabButton) {
                Universal Password ManagerFloatingActionButton(
                    onClick = vaultItemListingHandlers.addVaultItemClick,
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_plus_large),
                    contentDescription = stringResource(id = Universal Password ManagerString.add_item),
                    modifier = Modifier.testTag(tag = "AddItemButton"),
                )
            }
        },
        overlay = {
            Universal Password ManagerAccountSwitcher(
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
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
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
                Universal Password ManagerErrorContent(
                    message = state.viewState.message(),
                    buttonData = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.try_again.asText(),
                        onClick = vaultItemListingHandlers.refreshClick,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VaultItemListingState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
