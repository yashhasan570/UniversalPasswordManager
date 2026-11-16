package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity

import android.content.res.Resources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .core.data.manager.biometrics.model.BiometricSupportStatus
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.account.dialog.Universal Password ManagerLogoutConfirmationDialog
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.badge.NotificationBadge
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.card.actionCardExitAnimation
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.dropdown.Universal Password ManagerTimePickerButton
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.row.Universal Password ManagerExternalLinkRow
import .ui.platform.components.row.Universal Password ManagerTextRow
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.support.Universal Password ManagerSupportingText
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.manager.util.startAppSettingsActivity
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.auth.repository.model.PolicyInformation
import com.x8bit.universalpasswordmanager.data.platform.repository.model.VaultTimeout
import com.x8bit.universalpasswordmanager.data.platform.repository.model.VaultTimeoutAction
import com.x8bit.universalpasswordmanager.ui.platform.components.toggle.Universal Password ManagerUnlockWithBiometricsSwitch
import com.x8bit.universalpasswordmanager.ui.platform.components.toggle.Universal Password ManagerUnlockWithPinSwitch
import com.x8bit.universalpasswordmanager.ui.platform.util.displayLabel
import com.x8bit.universalpasswordmanager.ui.platform.util.minutes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.crypto.Cipher

/**
 * Displays the account security screen.
 */
@Suppress("LongMethod", "LongParameterList", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSecurityScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDeleteAccount: () -> Unit,
    onNavigateToPendingRequests: () -> Unit,
    onNavigateToSetupUnlockScreen: () -> Unit,
    onNavigateToManageDevices: () -> Unit,
    viewModel: AccountSecurityViewModel = hiltViewModel(),
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    var showBiometricsPrompt by rememberSaveable { mutableStateOf(false) }
    val unlockWithBiometricToggle: (cipher: Cipher) -> Unit = {
        viewModel.trySendAction(AccountSecurityAction.UnlockWithBiometricToggleEnabled(it))
    }
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            AccountSecurityEvent.NavigateBack -> onNavigateBack()

            AccountSecurityEvent.NavigateToApplicationDataSettings -> {
                intentManager.startAppSettingsActivity()
            }

            AccountSecurityEvent.NavigateToDeleteAccount -> onNavigateToDeleteAccount()

            AccountSecurityEvent.NavigateToFingerprintPhrase -> {
                intentManager.launchUri("http:///help/fingerprint-phrase".toUri())
            }

            AccountSecurityEvent.NavigateToPendingRequests -> onNavigateToPendingRequests()

            is AccountSecurityEvent.NavigateToTwoStepLogin -> {
                intentManager.launchUri(event.url.toUri())
            }

            is AccountSecurityEvent.NavigateToChangeMasterPassword -> {
                intentManager.launchUri(event.url.toUri())
            }

            is AccountSecurityEvent.NavigateToManageDevices -> onNavigateToManageDevices()

            is AccountSecurityEvent.ShowBiometricsPrompt -> {
                showBiometricsPrompt = true
                biometricsManager.promptBiometrics(
                    onSuccess = {
                        unlockWithBiometricToggle(it)
                        showBiometricsPrompt = false
                    },
                    onCancel = { showBiometricsPrompt = false },
                    onLockOut = { showBiometricsPrompt = false },
                    onError = { showBiometricsPrompt = false },
                    cipher = event.cipher,
                )
            }

            AccountSecurityEvent.NavigateToSetupUnlockScreen -> onNavigateToSetupUnlockScreen()
        }
    }

    AccountSecurityDialogs(
        state = state,
        onDismissRequest = { viewModel.trySendAction(AccountSecurityAction.DismissDialog) },
        onConfirmLogoutClick = {
            viewModel.trySendAction(AccountSecurityAction.ConfirmLogoutClick)
        },
        onFingerprintLearnMore = {
            viewModel.trySendAction(AccountSecurityAction.FingerPrintLearnMoreClick)
        },
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.account_security),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = {
                    viewModel.trySendAction(AccountSecurityAction.BackClick)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            AnimatedVisibility(
                visible = state.shouldShowUnlockActionCard,
                label = "UnlockActionCard",
                exit = actionCardExitAnimation(),
            ) {
                Universal Password ManagerActionCard(
                    cardTitle = stringResource(id = Universal Password ManagerString.set_up_unlock),
                    actionButton = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.get_started.asText(),
                        onClick = {
                            viewModel.trySendAction(AccountSecurityAction.UnlockActionCardCtaClick)
                        },
                    ),
                    onDismissClick = {
                        viewModel.trySendAction(AccountSecurityAction.UnlockActionCardDismiss)
                    },
                    leadingContent = {
                        NotificationBadge(notificationCount = 1)
                    },
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .padding(bottom = 16.dp),
                )
            }

            if (!state.isManageDevicesEnabled) {
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.account_access_requests),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                Universal Password ManagerTextRow(
                    text = stringResource(id = Universal Password ManagerString.pending_access_requests),
                    onClick = {
                        viewModel.trySendAction(AccountSecurityAction.PendingLoginRequestsClick)
                    },
                    cardStyle = CardStyle.Full,
                    modifier = Modifier
                        .testTag("PendingLogInRequestsLabel")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }

            val biometricSupportStatus = biometricsManager.biometricSupportStatus
            if (biometricSupportStatus != BiometricSupportStatus.NOT_SUPPORTED ||
                !state.removeUnlockWithPinPolicyEnabled ||
                state.isUnlockWithPinEnabled
            ) {
                if (!state.isManageDevicesEnabled) {
                    Spacer(Modifier.height(16.dp))
                }
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.unlock_options),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
            Universal Password ManagerUnlockWithBiometricsSwitch(
                biometricSupportStatus = biometricSupportStatus,
                isChecked = state.isUnlockWithBiometricsEnabled || showBiometricsPrompt,
                onDisableBiometrics = {
                    viewModel.trySendAction(AccountSecurityAction.UnlockWithBiometricToggleDisabled)
                },
                onEnableBiometrics = {
                    viewModel.trySendAction(AccountSecurityAction.EnableBiometricsClick)
                },
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .testTag("UnlockWithBiometricsSwitch")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            if (!state.removeUnlockWithPinPolicyEnabled || state.isUnlockWithPinEnabled) {
                Spacer(modifier = Modifier.height(height = 8.dp))
                Universal Password ManagerUnlockWithPinSwitch(
                    isUnlockWithPasswordEnabled = state.isUnlockWithPasswordEnabled,
                    isUnlockWithPinEnabled = state.isUnlockWithPinEnabled,
                    onUnlockWithPinToggleAction = {
                        viewModel.trySendAction(AccountSecurityAction.UnlockWithPinToggle(it))
                    },
                    cardStyle = CardStyle.Full,
                    modifier = Modifier
                        .testTag("UnlockWithPinSwitch")
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
            Spacer(Modifier.height(16.dp))
            if (state.shouldShowEnableAuthenticatorSync) {
                SyncWithAuthenticatorRow(
                    isChecked = state.isAuthenticatorSyncChecked,
                    onCheckedChange = {
                        viewModel.trySendAction(AccountSecurityAction.AuthenticatorSyncToggle(it))
                    },
                )
                Spacer(Modifier.height(16.dp))
            }
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.session_timeout),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            SessionTimeoutRow(
                vaultTimeoutPolicy = state.vaultTimeoutPolicy,
                selectedVaultTimeoutType = state.vaultTimeout.type,
                onVaultTimeoutTypeSelect = {
                    viewModel.trySendAction(AccountSecurityAction.VaultTimeoutTypeSelect(it))
                },
                modifier = Modifier
                    .testTag("VaultTimeoutChooser")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            (state.vaultTimeout as? VaultTimeout.Custom)?.let { customTimeout ->
                SessionCustomTimeoutRow(
                    vaultTimeoutPolicy = state.vaultTimeoutPolicy,
                    customVaultTimeout = customTimeout,
                    onCustomVaultTimeoutSelect = {
                        viewModel.trySendAction(AccountSecurityAction.CustomVaultTimeoutSelect(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
            state.sessionTimeoutSupportText?.let { text ->
                Universal Password ManagerSupportingText(
                    text = text(),
                    cardStyle = CardStyle.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
            SessionTimeoutActionRow(
                isEnabled = state.isSessionTimeoutActionEnabled,
                selectedVaultTimeoutAction = state.vaultTimeoutAction,
                onVaultTimeoutActionSelect = {
                    viewModel.trySendAction(AccountSecurityAction.VaultTimeoutActionSelect(it))
                },
                supportingText = state.sessionTimeoutActionSupportingText?.invoke(),
                cardStyle = if (state.sessionTimeoutSupportText == null) {
                    CardStyle.Bottom
                } else {
                    CardStyle.Full
                },
                modifier = Modifier
                    .testTag("VaultTimeoutActionChooser")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(Modifier.height(16.dp))
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.other),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            Universal Password ManagerTextRow(
                text = stringResource(id = Universal Password ManagerString.account_fingerprint_phrase),
                onClick = {
                    viewModel.trySendAction(AccountSecurityAction.AccountFingerprintPhraseClick)
                },
                cardStyle = CardStyle.Top(),
                modifier = Modifier
                    .testTag("AccountFingerprintPhraseLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Universal Password ManagerExternalLinkRow(
                text = stringResource(id = Universal Password ManagerString.two_step_login),
                onConfirmClick = {
                    viewModel.trySendAction(AccountSecurityAction.TwoStepLoginClick)
                },
                withDivider = false,
                dialogTitle = stringResource(id = Universal Password ManagerString.continue_to_web_app),
                dialogMessage = stringResource(
                    id = Universal Password ManagerString.two_step_login_description_long,
                ),
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .testTag("TwoStepLoginLinkItemView")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            if (state.isManageDevicesEnabled) {
                Universal Password ManagerTextRow(
                    text = stringResource(id = Universal Password ManagerString.devices),
                    onClick = {
                        viewModel.trySendAction(AccountSecurityAction.ManageDevicesClick)
                    },
                    cardStyle = CardStyle.Middle(),
                    modifier = Modifier
                        .testTag("ManageDevicesLabel")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            if (state.isUnlockWithPasswordEnabled) {
                Universal Password ManagerExternalLinkRow(
                    text = stringResource(id = Universal Password ManagerString.change_master_password),
                    onConfirmClick = {
                        viewModel.trySendAction(AccountSecurityAction.ChangeMasterPasswordClick)
                    },
                    withDivider = false,
                    dialogTitle = stringResource(id = Universal Password ManagerString.continue_to_web_app),
                    dialogMessage = stringResource(
                        id = Universal Password ManagerString.change_master_password_description_long,
                    ),
                    cardStyle = CardStyle.Middle(),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            if (state.hasUnlockMechanism) {
                Universal Password ManagerTextRow(
                    text = stringResource(id = Universal Password ManagerString.lock_now),
                    onClick = { viewModel.trySendAction(AccountSecurityAction.LockNowClick) },
                    cardStyle = CardStyle.Middle(),
                    modifier = Modifier
                        .testTag("LockNowLabel")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            Universal Password ManagerTextRow(
                text = stringResource(id = Universal Password ManagerString.log_out),
                onClick = { viewModel.trySendAction(AccountSecurityAction.LogoutClick) },
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .testTag("LogOutLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Universal Password ManagerTextRow(
                text = stringResource(id = Universal Password ManagerString.delete_account),
                onClick = { viewModel.trySendAction(AccountSecurityAction.DeleteAccountClick) },
                cardStyle = CardStyle.Bottom,
                modifier = Modifier
                    .testTag("DeleteAccountLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun AccountSecurityDialogs(
    state: AccountSecurityState,
    onDismissRequest: () -> Unit,
    onConfirmLogoutClick: () -> Unit,
    onFingerprintLearnMore: () -> Unit,
) {
    when (val dialogState = state.dialog) {
        AccountSecurityDialog.ConfirmLogout -> Universal Password ManagerLogoutConfirmationDialog(
            onDismissRequest = onDismissRequest,
            onConfirmClick = onConfirmLogoutClick,
        )

        AccountSecurityDialog.FingerprintPhrase -> FingerPrintPhraseDialog(
            fingerprintPhrase = state.fingerprintPhrase,
            onDismissRequest = onDismissRequest,
            onLearnMore = onFingerprintLearnMore,
        )

        is AccountSecurityDialog.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        is AccountSecurityDialog.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}

@Composable
private fun SessionTimeoutRow(
    vaultTimeoutPolicy: VaultTimeoutPolicy?,
    selectedVaultTimeoutType: VaultTimeout.Type,
    onVaultTimeoutTypeSelect: (VaultTimeout.Type) -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    var shouldShowNeverTimeoutConfirmationDialog by remember { mutableStateOf(false) }
    val vaultTimeoutOptions = rememberSessionTimeoutOptions(vaultTimeoutPolicy)
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.session_timeout),
        options = vaultTimeoutOptions.map { it.displayLabel() }.toImmutableList(),
        selectedOption = selectedVaultTimeoutType.displayLabel(),
        onOptionSelected = { selectedType ->
            val selectedOption = vaultTimeoutOptions.first {
                it.displayLabel.toString(resources) == selectedType
            }
            if (selectedOption == VaultTimeout.Type.NEVER) {
                shouldShowNeverTimeoutConfirmationDialog = true
            } else {
                onVaultTimeoutTypeSelect(selectedOption)
            }
        },
        isEnabled = vaultTimeoutOptions.size > 1,
        textFieldTestTag = "SessionTimeoutStatusLabel",
        cardStyle = CardStyle.Top(),
        modifier = modifier,
    )

    if (shouldShowNeverTimeoutConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.warning),
            message = stringResource(id = Universal Password ManagerString.never_lock_warning),
            confirmButtonText = stringResource(id = Universal Password ManagerString.okay),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                shouldShowNeverTimeoutConfirmationDialog = false
                onVaultTimeoutTypeSelect(VaultTimeout.Type.NEVER)
            },
            onDismissClick = { shouldShowNeverTimeoutConfirmationDialog = false },
            onDismissRequest = { shouldShowNeverTimeoutConfirmationDialog = false },
        )
    }
}

@Composable
private fun SessionCustomTimeoutRow(
    vaultTimeoutPolicy: VaultTimeoutPolicy?,
    customVaultTimeout: VaultTimeout.Custom,
    onCustomVaultTimeoutSelect: (VaultTimeout.Custom) -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowViolatesPoliciesDialog by remember { mutableStateOf(false) }
    Universal Password ManagerTimePickerButton(
        label = stringResource(id = Universal Password ManagerString.custom_timeout),
        totalMinutes = customVaultTimeout.vaultTimeoutInMinutes,
        onTimeSelect = { minutes ->
            if (vaultTimeoutPolicy?.minutes != null && minutes > vaultTimeoutPolicy.minutes) {
                shouldShowViolatesPoliciesDialog = true
            } else {
                onCustomVaultTimeoutSelect(VaultTimeout.Custom(minutes))
            }
        },
        is24Hour = true,
        supportingContent = null,
        cardStyle = CardStyle.Middle(),
        modifier = modifier,
    )

    if (shouldShowViolatesPoliciesDialog) {
        Universal Password ManagerBasicDialog(
            title = stringResource(id = Universal Password ManagerString.warning),
            message = stringResource(id = Universal Password ManagerString.vault_timeout_to_large),
            onDismissRequest = {
                shouldShowViolatesPoliciesDialog = false
                vaultTimeoutPolicy?.minutes?.let {
                    onCustomVaultTimeoutSelect(VaultTimeout.Custom(it))
                }
            },
        )
    }
}

@Composable
private fun SessionTimeoutActionRow(
    isEnabled: Boolean,
    selectedVaultTimeoutAction: VaultTimeoutAction,
    onVaultTimeoutActionSelect: (VaultTimeoutAction) -> Unit,
    supportingText: String?,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    var shouldShowLogoutActionConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    Universal Password ManagerMultiSelectButton(
        isEnabled = isEnabled,
        label = stringResource(id = Universal Password ManagerString.session_timeout_action),
        options = VaultTimeoutAction.entries.map { it.displayLabel() }.toImmutableList(),
        selectedOption = selectedVaultTimeoutAction.displayLabel(),
        onOptionSelected = { action ->
            val selectedAction = VaultTimeoutAction.entries.first {
                it.displayLabel.toString(resources) == action
            }
            if (selectedAction == VaultTimeoutAction.LOGOUT) {
                shouldShowLogoutActionConfirmationDialog = true
            } else {
                onVaultTimeoutActionSelect(selectedAction)
            }
        },
        supportingText = supportingText,
        textFieldTestTag = "SessionTimeoutActionStatusLabel",
        cardStyle = cardStyle,
        modifier = modifier,
    )

    if (shouldShowLogoutActionConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.vault_timeout_log_out_confirmation_title),
            message = stringResource(
                id = Universal Password ManagerString.vault_timeout_log_out_confirmation_message,
            ),
            confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                shouldShowLogoutActionConfirmationDialog = false
                onVaultTimeoutActionSelect(VaultTimeoutAction.LOGOUT)
            },
            onDismissClick = {
                shouldShowLogoutActionConfirmationDialog = false
            },
            onDismissRequest = {
                shouldShowLogoutActionConfirmationDialog = false
            },
        )
    }
}

@Composable
private fun ColumnScope.SyncWithAuthenticatorRow(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Universal Password ManagerListHeaderText(
        label = stringResource(Universal Password ManagerString.authenticator_sync),
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin()
            .padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerSwitch(
        label = stringResource(Universal Password ManagerString.allow_universalpasswordmanager_authenticator_syncing),
        onCheckedChange = onCheckedChange,
        isChecked = isChecked,
        cardStyle = CardStyle.Full,
        modifier = Modifier
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
}

@Composable
private fun FingerPrintPhraseDialog(
    fingerprintPhrase: Text,
    onDismissRequest: () -> Unit,
    onLearnMore: () -> Unit,
) {
    Universal Password ManagerTwoButtonDialog(
        title = stringResource(id = Universal Password ManagerString.fingerprint_phrase),
        message = buildAnnotatedString {
            append("${stringResource(id = Universal Password ManagerString.your_accounts_fingerprint)}:\n\n")
            withStyle(
                style = Universal Password ManagerTheme.typography.sensitiveInfoSmall
                    .toSpanStyle()
                    .copy(color = Universal Password ManagerTheme.colorScheme.text.codePink),
            ) {
                append(fingerprintPhrase())
            }
        },
        confirmButtonText = stringResource(id = Universal Password ManagerString.learn_more),
        dismissButtonText = stringResource(id = Universal Password ManagerString.close),
        onConfirmClick = onLearnMore,
        onDismissClick = onDismissRequest,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun rememberSessionTimeoutOptions(
    vaultTimeoutPolicy: VaultTimeoutPolicy?,
): ImmutableList<VaultTimeout.Type> = remember(vaultTimeoutPolicy) {
    VaultTimeout.Type
        .entries
        .filter { timeoutType ->
            when (vaultTimeoutPolicy?.type) {
                PolicyInformation.VaultTimeout.Type.NEVER -> {
                    // We allow everything here.
                    true
                }

                PolicyInformation.VaultTimeout.Type.ON_APP_RESTART,
                PolicyInformation.VaultTimeout.Type.ON_SYSTEM_LOCK,
                    -> {
                    // We allow everything but never here.
                    timeoutType != VaultTimeout.Type.NEVER
                }

                PolicyInformation.VaultTimeout.Type.IMMEDIATELY -> {
                    // Only allow immediately, everything else is blocked.
                    timeoutType == VaultTimeout.Type.IMMEDIATELY
                }

                PolicyInformation.VaultTimeout.Type.CUSTOM -> {
                    // Filter out all values above the specified amount. Custom set timeouts
                    // that exceed the max value will be constrained in the VM.
                    timeoutType.minutes <= (vaultTimeoutPolicy.minutes ?: Int.MAX_VALUE)
                }

                null -> {
                    // If the type is null, there could still be a policy in place from a
                    // legacy server that is not sending a type. So we still filter as though
                    // it were a custom type in that scenario, otherwise we allow everything.
                    vaultTimeoutPolicy
                        ?.minutes
                        ?.let { minutes -> timeoutType.minutes <= minutes }
                        ?: true
                }
            }
        }
        .toImmutableList()
}
