package com.x8bit.universalpasswordmanager.ui.vault.feature.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .core.util.persistentListOfNotNull
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.bottomsheet.Universal Password ManagerModalBottomSheet
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.coachmark.CoachMarkContainer
import .ui.platform.components.coachmark.model.rememberLazyListCoachMarkState
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dialog.PremiumRequiredDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.radio.Universal Password ManagerRadioButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.composition.LocalExitManager
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.manager.exit.ExitManager
import .ui.platform.manager.util.startAppSettingsActivity
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.Text
import com.x8bit.universalpasswordmanager.ui.credentials.manager.CredentialProviderCompletionManager
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerMasterPasswordDialog
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerOverwriteCredentialConfirmationDialog
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerPinDialog
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalCredentialProviderCompletionManager
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.PinInputDialog
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager
import com.x8bit.universalpasswordmanager.ui.tools.feature.generator.model.GeneratorMode
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditBankAccountTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditCardTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditCommonHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditIdentityTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditLicenseTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditLoginTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditSshKeyTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.VaultAddEditUserVerificationHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.addedit.handlers.rememberVaultAddEditPassportTypeHandlers
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultItemCipherType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

/**
 * Top level composable for the vault add item screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun VaultAddEditScreen(
    onNavigateBack: () -> Unit,
    onNavigateToQrCodeScanScreen: () -> Unit,
    onNavigateToCardScanScreen: () -> Unit,
    viewModel: VaultAddEditViewModel,
    permissionsManager: PermissionsManager = LocalPermissionsManager.current,
    intentManager: IntentManager = LocalIntentManager.current,
    exitManager: ExitManager = LocalExitManager.current,
    credentialProviderCompletionManager: CredentialProviderCompletionManager =
        LocalCredentialProviderCompletionManager.current,
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    onNavigateToManualCodeEntryScreen: () -> Unit,
    onNavigateToGeneratorModal: (GeneratorMode.Modal) -> Unit,
    onNavigateToAttachments: (cipherId: String) -> Unit,
    onNavigateToMoveToOrganization: (cipherId: String, showOnlyCollections: Boolean) -> Unit,
    onNavigateToPlan: () -> Unit,
    onCloseAndNavigateToVaultItem: (cipherId: String, cipherType: VaultItemCipherType) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val userVerificationHandlers = remember(viewModel) {
        VaultAddEditUserVerificationHandlers.create(viewModel = viewModel)
    }

    val lazyListState = rememberLazyListState()
    val coachMarkState = rememberLazyListCoachMarkState(
        lazyListState = lazyListState,
        orderedList = AddEditItemCoachMark.entries,
    )
    val cardHolderNameFocusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is VaultAddEditEvent.NavigateToQrCodeScan -> {
                onNavigateToQrCodeScanScreen()
            }

            is VaultAddEditEvent.NavigateToCardScan -> {
                onNavigateToCardScanScreen()
            }

            is VaultAddEditEvent.NavigateToManualCodeEntry -> {
                onNavigateToManualCodeEntryScreen()
            }

            is VaultAddEditEvent.NavigateToGeneratorModal -> {
                onNavigateToGeneratorModal(event.generatorMode)
            }

            is VaultAddEditEvent.NavigateToAttachments -> onNavigateToAttachments(event.cipherId)
            is VaultAddEditEvent.CloseAndNavigateToVaultItem -> {
                onCloseAndNavigateToVaultItem(event.cipherId, event.cipherType)
            }

            is VaultAddEditEvent.NavigateToMoveToOrganization -> {
                onNavigateToMoveToOrganization(event.cipherId, false)
            }

            is VaultAddEditEvent.NavigateToCollections -> {
                onNavigateToMoveToOrganization(event.cipherId, true)
            }

            VaultAddEditEvent.ExitApp -> exitManager.exitApplication()
            VaultAddEditEvent.NavigateBack -> onNavigateBack.invoke()

            is VaultAddEditEvent.NavigateToTooltipUri -> {
                intentManager.launchUri(
                    "https:///help/managing-items/#protect-individual-items".toUri(),
                )
            }

            is VaultAddEditEvent.NavigateToAuthenticatorKeyTooltipUri -> {
                intentManager.launchUri(
                    "https:///help/integrated-authenticator".toUri(),
                )
            }

            is VaultAddEditEvent.CompleteCredentialRegistration -> {
                credentialProviderCompletionManager.completeCredentialRegistration(
                    result = event.result,
                )
            }

            is VaultAddEditEvent.Fido2UserVerification -> {
                biometricsManager.promptUserVerification(
                    onSuccess = userVerificationHandlers.onUserVerificationSuccess,
                    onCancel = userVerificationHandlers.onUserVerificationCancelled,
                    onError = userVerificationHandlers.onUserVerificationFail,
                    onLockOut = userVerificationHandlers.onUserVerificationLockOut,
                    onNotSupported = userVerificationHandlers.onUserVerificationNotSupported,
                )
            }

            VaultAddEditEvent.StartAddLoginItemCoachMarkTour -> {
                scope.launch {
                    coachMarkState.showCoachMark(
                        coachMarkToShow = AddEditItemCoachMark.GENERATE_PASSWORD,
                    )
                }
            }

            is VaultAddEditEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)

            VaultAddEditEvent.NavigateToLearnMore -> {
                intentManager.launchUri("https:///help/uri-match-detection/".toUri())
            }

            is VaultAddEditEvent.NavigateToPremium -> {
                intentManager.launchUri(uri = event.uri.toUri())
            }

            VaultAddEditEvent.FocusCardHolderName -> {
                cardHolderNameFocusRequester.requestFocus()
            }

            VaultAddEditEvent.NavigateToAppSettings -> {
                intentManager.startAppSettingsActivity()
            }

            VaultAddEditEvent.NavigateToPlanModal -> onNavigateToPlan()
        }
    }

    val loginItemTypeHandlers = remember(viewModel) {
        VaultAddEditLoginTypeHandlers.create(viewModel = viewModel)
    }

    val commonTypeHandlers = remember(viewModel) {
        VaultAddEditCommonHandlers.create(viewModel = viewModel)
    }

    val identityItemTypeHandlers = remember(viewModel) {
        VaultAddEditIdentityTypeHandlers.create(viewModel = viewModel)
    }

    val cardItemTypeHandlers = remember(viewModel) {
        VaultAddEditCardTypeHandlers.create(viewModel = viewModel)
    }

    val sshKeyItemTypeHandlers = remember(viewModel) {
        VaultAddEditSshKeyTypeHandlers.create(viewModel = viewModel)
    }

    val bankAccountItemTypeHandlers = remember(viewModel) {
        VaultAddEditBankAccountTypeHandlers.create(viewModel = viewModel)
    }

    val licenseItemTypeHandlers = remember(viewModel) {
        VaultAddEditLicenseTypeHandlers.create(viewModel = viewModel)
    }

    val passportItemTypeHandlers = rememberVaultAddEditPassportTypeHandlers(viewModel = viewModel)

    val archiveClickAction = { viewModel.trySendAction(VaultAddEditAction.Common.ArchiveClick) }

    val unarchiveClickAction = { viewModel.trySendAction(VaultAddEditAction.Common.UnarchiveClick) }

    val confirmDeleteClickAction = {
        viewModel.trySendAction(VaultAddEditAction.Common.ConfirmDeleteClick)
    }

    var pendingDeleteCipher by rememberSaveable { mutableStateOf(false) }

    VaultAddEditItemDialogs(
        dialogState = state.dialog,
        onDismissRequest = { viewModel.trySendAction(VaultAddEditAction.Common.DismissDialog) },
        onAutofillDismissRequest = {
            viewModel.trySendAction(VaultAddEditAction.Common.InitialAutofillDialogDismissed)
        },
        onCredentialErrorDismiss = { errorMessage ->
            viewModel.trySendAction(
                VaultAddEditAction.Common.CredentialErrorDialogDismissed(message = errorMessage),
            )
        },
        onConfirmOverwriteExistingPasskey = {
            viewModel.trySendAction(VaultAddEditAction.Common.ConfirmOverwriteExistingPasskeyClick)
        },
        onSubmitMasterPasswordFido2Verification = {
            viewModel.trySendAction(
                action = VaultAddEditAction.Common.MasterPasswordFido2VerificationSubmit(it),
            )
        },
        onRetryFido2PasswordVerification = {
            viewModel.trySendAction(VaultAddEditAction.Common.RetryFido2PasswordVerificationClick)
        },
        onSubmitPinFido2Verification = {
            viewModel.trySendAction(VaultAddEditAction.Common.PinFido2VerificationSubmit(it))
        },
        onRetryFido2PinVerification = {
            viewModel.trySendAction(VaultAddEditAction.Common.RetryFido2PinVerificationClick)
        },
        onSubmitPinSetUpFido2Verification = {
            viewModel.trySendAction(VaultAddEditAction.Common.PinFido2SetUpSubmit(it))
        },
        onRetryPinSetUpFido2Verification = {
            viewModel.trySendAction(VaultAddEditAction.Common.PinFido2SetUpRetryClick)
        },
        onDismissFido2Verification = {
            viewModel.trySendAction(VaultAddEditAction.Common.DismissFido2VerificationDialogClick)
        },
        onUpgradeToPremiumClick = {
            viewModel.trySendAction(VaultAddEditAction.Common.UpgradeToPremiumClick)
        },
        onCameraPermissionSettingsClick = {
            viewModel.trySendAction(
                VaultAddEditAction.Common.CameraPermissionSettingsClick,
            )
        },
    )

    if (pendingDeleteCipher) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.delete),
            message = stringResource(id = Universal Password ManagerString.do_you_really_want_to_soft_delete_cipher),
            confirmButtonText = stringResource(id = Universal Password ManagerString.okay),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                pendingDeleteCipher = false
                confirmDeleteClickAction()
            },
            onDismissClick = { pendingDeleteCipher = false },
            onDismissRequest = { pendingDeleteCipher = false },
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val scrollBackToTop: () -> Unit = remember {
        {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
            }
        }
    }
    CoachMarkContainer(
        state = coachMarkState,
    ) {
        Universal Password ManagerScaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Universal Password ManagerTopAppBar(
                    title = state.screenDisplayName(),
                    navigationIcon = NavigationIcon(
                        navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                        navigationIconContentDescription = stringResource(
                            id = Universal Password ManagerString.close,
                        ),
                        onNavigationIconClick = {
                            viewModel.trySendAction(VaultAddEditAction.Common.CloseClick)
                        },
                    )
                        .takeIf { state.shouldShowCloseButton },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        Universal Password ManagerTextButton(
                            label = stringResource(id = Universal Password ManagerString.save),
                            onClick = {
                                viewModel.trySendAction(VaultAddEditAction.Common.SaveClick)
                            },
                            modifier = Modifier.testTag("SaveButton"),
                        )
                        Universal Password ManagerOverflowActionItem(
                            menuItemDataList = persistentListOfNotNull(
                                OverflowMenuItemData(
                                    text = stringResource(id = Universal Password ManagerString.attachments),
                                    onClick = {
                                        viewModel.trySendAction(
                                            VaultAddEditAction.Common.AttachmentsClick,
                                        )
                                    },
                                )
                                    .takeUnless { state.isAddItemMode },
                                OverflowMenuItemData(
                                    text = stringResource(id = Universal Password ManagerString.move),
                                    onClick = {
                                        viewModel.trySendAction(
                                            VaultAddEditAction.Common.MoveToOrganizationClick,
                                        )
                                    },
                                )
                                    .takeUnless { !state.shouldShowMoveToOrganization },
                                OverflowMenuItemData(
                                    text = stringResource(id = Universal Password ManagerString.shared_folders),
                                    onClick = {
                                        viewModel.trySendAction(
                                            VaultAddEditAction.Common.CollectionsClick,
                                        )
                                    },
                                )
                                    .takeUnless {
                                        state.isAddItemMode ||
                                            !state.isCipherInCollection ||
                                            !state.canAssociateToCollections
                                    },
                                OverflowMenuItemData(
                                    text = stringResource(id = Universal Password ManagerString.archive_verb),
                                    onClick = archiveClickAction,
                                )
                                    .takeIf { state.displayArchiveButton },
                                OverflowMenuItemData(
                                    text = stringResource(id = Universal Password ManagerString.unarchive),
                                    onClick = unarchiveClickAction,
                                )
                                    .takeIf { state.displayUnarchiveButton },
                                OverflowMenuItemData(
                                    text = stringResource(id = Universal Password ManagerString.delete),
                                    onClick = { pendingDeleteCipher = true },
                                )
                                    .takeUnless { state.isAddItemMode || !state.canDelete },
                            ),
                        )
                    },
                )
            },
            snackbarHost = {
                Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
            },
        ) {
            when (val viewState = state.viewState) {
                is VaultAddEditState.ViewState.Content -> {
                    VaultAddEditContent(
                        state = viewState,
                        isAddItemMode = state.isAddItemMode,
                        defaultUriMatchType = state.defaultUriMatchType,
                        loginItemTypeHandlers = loginItemTypeHandlers,
                        commonTypeHandlers = commonTypeHandlers,
                        permissionsManager = permissionsManager,
                        identityItemTypeHandlers = identityItemTypeHandlers,
                        cardItemTypeHandlers = cardItemTypeHandlers,
                        sshKeyItemTypeHandlers = sshKeyItemTypeHandlers,
                        bankAccountItemTypeHandlers = bankAccountItemTypeHandlers,
                        licenseItemTypeHandlers = licenseItemTypeHandlers,
                        passportItemTypeHandlers = passportItemTypeHandlers,
                        isCardScannerEnabled = state.isCardScannerEnabled,
                        cardHolderNameFocusRequester = cardHolderNameFocusRequester,
                        lazyListState = lazyListState,
                        onPreviousCoachMark = {
                            coroutineScope.launch {
                                coachMarkState.showPreviousCoachMark()
                            }
                        },
                        onNextCoachMark = {
                            coroutineScope.launch {
                                coachMarkState.showNextCoachMark()
                            }
                        },
                        onCoachMarkTourComplete = {
                            coachMarkState.coachingComplete(onComplete = scrollBackToTop)
                        },
                        onCoachMarkDismissed = scrollBackToTop,
                        shouldShowLearnAboutLoginsCard = state.shouldShowLearnAboutNewLogins,
                        modifier = Modifier
                            .fillMaxSize(),
                    )

                    BottomSheetViews(
                        bottomSheetState = state.bottomSheetState,
                        viewState = viewState.common,
                        handlers = commonTypeHandlers,
                    )
                }

                is VaultAddEditState.ViewState.Error -> {
                    Universal Password ManagerErrorContent(
                        message = viewState.message(),
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                VaultAddEditState.ViewState.Loading -> {
                    Universal Password ManagerLoadingContent(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun VaultAddEditItemDialogs(
    dialogState: VaultAddEditState.DialogState?,
    onDismissRequest: () -> Unit,
    onAutofillDismissRequest: () -> Unit,
    onCredentialErrorDismiss: (Text) -> Unit,
    onConfirmOverwriteExistingPasskey: () -> Unit,
    onSubmitMasterPasswordFido2Verification: (password: String) -> Unit,
    onRetryFido2PasswordVerification: () -> Unit,
    onSubmitPinFido2Verification: (pin: String) -> Unit,
    onRetryFido2PinVerification: () -> Unit,
    onSubmitPinSetUpFido2Verification: (pin: String) -> Unit,
    onRetryPinSetUpFido2Verification: () -> Unit,
    onDismissFido2Verification: () -> Unit,
    onUpgradeToPremiumClick: () -> Unit,
    onCameraPermissionSettingsClick: () -> Unit,
) {
    when (dialogState) {
        is VaultAddEditState.DialogState.ArchiveRequiresPremium -> {
            PremiumRequiredDialog(
                message = stringResource(id = Universal Password ManagerString.archiving_items_is_a_premium_feature),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = onUpgradeToPremiumClick,
                onDismissRequest = onDismissRequest,
            )
        }

        is VaultAddEditState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.label())
        }

        is VaultAddEditState.DialogState.Generic -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissRequest,
            )
        }

        is VaultAddEditState.DialogState.InitialAutofillPrompt -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.universalpasswordmanager_autofill_service),
                message = stringResource(id = Universal Password ManagerString.universalpasswordmanager_autofill_service_alert2),
                onDismissRequest = onAutofillDismissRequest,
            )
        }

        is VaultAddEditState.DialogState.CredentialError -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.an_error_has_occurred),
                message = dialogState.message(),
                onDismissRequest = { onCredentialErrorDismiss(dialogState.message) },
            )
        }

        is VaultAddEditState.DialogState.OverwritePasskeyConfirmationPrompt -> {
            @Suppress("MaxLineLength")
            Universal Password ManagerOverwriteCredentialConfirmationDialog(
                title = stringResource(id = Universal Password ManagerString.overwrite_passkey),
                message = stringResource(
                    id = Universal Password ManagerString
                        .this_item_already_contains_a_passkey_are_you_sure_you_want_to_overwrite_the_current_passkey,
                ),
                onConfirmClick = onConfirmOverwriteExistingPasskey,
                onDismissRequest = onDismissRequest,
            )
        }

        is VaultAddEditState.DialogState.Fido2MasterPasswordPrompt -> {
            Universal Password ManagerMasterPasswordDialog(
                onConfirmClick = onSubmitMasterPasswordFido2Verification,
                onDismissRequest = onDismissFido2Verification,
            )
        }

        is VaultAddEditState.DialogState.Fido2MasterPasswordError -> {
            Universal Password ManagerBasicDialog(
                title = null,
                message = stringResource(id = Universal Password ManagerString.invalid_master_password),
                onDismissRequest = onRetryFido2PasswordVerification,
            )
        }

        is VaultAddEditState.DialogState.Fido2PinPrompt -> {
            Universal Password ManagerPinDialog(
                onConfirmClick = onSubmitPinFido2Verification,
                onDismissRequest = onDismissFido2Verification,
            )
        }

        is VaultAddEditState.DialogState.Fido2PinError -> {
            Universal Password ManagerBasicDialog(
                title = null,
                message = stringResource(id = Universal Password ManagerString.invalid_pin),
                onDismissRequest = onRetryFido2PinVerification,
            )
        }

        is VaultAddEditState.DialogState.Fido2PinSetUpPrompt -> {
            PinInputDialog(
                onCancelClick = onDismissFido2Verification,
                onSubmitClick = onSubmitPinSetUpFido2Verification,
                onDismissRequest = onDismissFido2Verification,
            )
        }

        is VaultAddEditState.DialogState.Fido2PinSetUpError -> {
            Universal Password ManagerBasicDialog(
                title = null,
                message = stringResource(
                    id = Universal Password ManagerString.validation_field_required,
                    stringResource(id = Universal Password ManagerString.pin),
                ),
                onDismissRequest = onRetryPinSetUpFido2Verification,
            )
        }

        is VaultAddEditState.DialogState.CameraPermissionDenied -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.allow_camera_access),
                message = stringResource(
                    id = Universal Password ManagerString.to_scan_your_card_we_need_access_to_your_camera,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.go_to_settings),
                dismissButtonText = stringResource(id = Universal Password ManagerString.not_now),
                onConfirmClick = onCameraPermissionSettingsClick,
                onDismissClick = onDismissRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Composable
private fun BottomSheetViews(
    bottomSheetState: VaultAddEditState.BottomSheetState?,
    viewState: VaultAddEditState.ViewState.Content.Common,
    handlers: VaultAddEditCommonHandlers,
    modifier: Modifier = Modifier,
) {
    when (bottomSheetState) {
        is VaultAddEditState.BottomSheetState.FolderSelection -> {
            FolderSelectionBottomSheet(
                state = viewState,
                handlers = handlers,
                modifier = modifier,
            )
        }

        is VaultAddEditState.BottomSheetState.OwnerSelection -> {
            OwnerSelectionBottomSheet(
                state = viewState,
                handlers = handlers,
                modifier = modifier,
            )
        }

        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderSelectionBottomSheet(
    state: VaultAddEditState.ViewState.Content.Common,
    handlers: VaultAddEditCommonHandlers,
    modifier: Modifier = Modifier,
) {
    var selectedOptionState by rememberSaveable {
        mutableStateOf(state.selectedFolder?.name.orEmpty())
    }
    Universal Password ManagerModalBottomSheet(
        sheetTitle = stringResource(id = Universal Password ManagerString.my_folders),
        onDismiss = handlers.onDismissBottomSheet,
        topBarActions = { animatedOnDismiss ->
            Universal Password ManagerTextButton(
                label = stringResource(Universal Password ManagerString.save),
                onClick = {
                    handlers.onDismissBottomSheet()
                    state
                        .availableFolders
                        .firstOrNull {
                            it.name == selectedOptionState
                        }
                        ?.run {
                            handlers.onChangeToExistingFolder(this.id)
                        }
                        ?: run {
                            handlers.onOnAddFolder(selectedOptionState)
                        }
                    animatedOnDismiss()
                },
                isEnabled = selectedOptionState.isNotBlank(),
            )
        },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier.statusBarsPadding(),
    ) {
        FolderSelectionBottomSheetContent(
            options = state.availableFolders.map { it.name }.toImmutableList(),
            selectedOption = selectedOptionState,
            onOptionSelected = {
                selectedOptionState = it
            },
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun FolderSelectionBottomSheetContent(
    options: ImmutableList<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .standardHorizontalMargin(),
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        itemsIndexed(options) { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .cardStyle(
                        cardStyle = if (index == 0) CardStyle.Top() else CardStyle.Middle(),
                        paddingHorizontal = 16.dp,
                        onClick = { onOptionSelected(option) },
                    )
                    .semantics(mergeDescendants = true) {
                        this.selected = selectedOption == option
                        this.role = Role.RadioButton
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    style = Universal Password ManagerTheme.typography.bodyLarge,
                    modifier = Modifier.weight(weight = 1f),
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
                Universal Password ManagerRadioButton(
                    isSelected = selectedOption == option,
                    onClick = null,
                )
            }
        }
        item {
            var inEditMode by rememberSaveable {
                mutableStateOf(false)
            }
            var addFolderText by rememberSaveable {
                mutableStateOf("")
            }
            val cardStyle = if (options.isEmpty()) CardStyle.Full else CardStyle.Bottom
            val shape = if (options.isEmpty()) {
                Universal Password ManagerTheme.shapes.content
            } else {
                Universal Password ManagerTheme.shapes.contentBottom
            }
            if (inEditMode) {
                Universal Password ManagerTextField(
                    label = stringResource(Universal Password ManagerString.add_folder),
                    value = addFolderText,
                    onValueChange = {
                        addFolderText = it
                        onOptionSelected(it)
                    },
                    autoFocus = true,
                    cardStyle = cardStyle,
                    modifier = Modifier
                        .fillMaxWidth(),
                    actions = {
                        Universal Password ManagerRadioButton(
                            isSelected = selectedOption == addFolderText,
                            onClick = {
                                onOptionSelected(addFolderText)
                            },
                        )
                    },
                )
            } else {
                Universal Password ManagerClickableText(
                    label = stringResource(id = Universal Password ManagerString.add_folder),
                    onClick = {
                        onOptionSelected(addFolderText)
                        inEditMode = true
                    },
                    leadingIcon = painterResource(id = Universal Password ManagerDrawable.ic_plus_small),
                    style = Universal Password ManagerTheme.typography.labelMedium,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = shape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .cardStyle(cardStyle = cardStyle, paddingVertical = 0.dp),
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OwnerSelectionBottomSheet(
    state: VaultAddEditState.ViewState.Content.Common,
    handlers: VaultAddEditCommonHandlers,
    modifier: Modifier = Modifier,
) {

    var selectedOwner by rememberSaveable {
        mutableStateOf(state.selectedOwner)
    }
    Universal Password ManagerModalBottomSheet(
        sheetTitle = stringResource(id = Universal Password ManagerString.select_vault),
        onDismiss = handlers.onDismissBottomSheet,
        topBarActions = { animatedOnDismiss ->
            Universal Password ManagerTextButton(
                label = stringResource(Universal Password ManagerString.save),
                onClick = {
                    handlers.onDismissBottomSheet()
                    selectedOwner?.let { handlers.onOwnerSelected(it.id) }
                    animatedOnDismiss()
                },
                isEnabled = selectedOwner != null,
            )
        },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier.statusBarsPadding(),
    ) {
        OwnerSelectionBottomSheetContent(
            options = state.availableOwners,
            selectedOwner = selectedOwner,
            onOptionSelected = {
                selectedOwner = it
            },
        )
    }
}

@Composable
private fun OwnerSelectionBottomSheetContent(
    options: ImmutableList<VaultAddEditState.Owner>,
    selectedOwner: VaultAddEditState.Owner?,
    onOptionSelected: (VaultAddEditState.Owner) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .standardHorizontalMargin(),
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        itemsIndexed(options) { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .cardStyle(
                        cardStyle = options.toListItemCardStyle(index = index),
                        paddingHorizontal = 16.dp,
                        onClick = { onOptionSelected(option) },
                    )
                    .semantics(mergeDescendants = true) {
                        this.selected = selectedOwner == option
                        this.role = Role.RadioButton
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option.name(),
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    style = Universal Password ManagerTheme.typography.bodyLarge,
                    modifier = Modifier.weight(weight = 1f),
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
                Universal Password ManagerRadioButton(
                    isSelected = selectedOwner == option,
                    onClick = null,
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
