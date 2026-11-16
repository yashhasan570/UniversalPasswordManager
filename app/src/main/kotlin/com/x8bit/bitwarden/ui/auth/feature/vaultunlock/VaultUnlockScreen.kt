package com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.account.Universal Password ManagerAccountActionItem
import .ui.platform.components.account.Universal Password ManagerAccountSwitcher
import .ui.platform.components.account.dialog.Universal Password ManagerLogoutConfirmationDialog
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.support.Universal Password ManagerSupportingContent
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.MainViewModel
import com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util.inputFieldVisibilityToggleTestTag
import com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util.unlockScreenInputLabel
import com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util.unlockScreenInputTestTag
import com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util.unlockScreenKeyboardType
import com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util.unlockScreenMessage
import com.x8bit.universalpasswordmanager.ui.auth.feature.vaultunlock.util.unlockScreenTitle
import com.x8bit.universalpasswordmanager.ui.credentials.manager.CredentialProviderCompletionManager
import com.x8bit.universalpasswordmanager.ui.credentials.manager.model.AssertFido2CredentialResult
import com.x8bit.universalpasswordmanager.ui.credentials.manager.model.GetCredentialsResult
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalCredentialProviderCompletionManager
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import javax.crypto.Cipher
import kotlin.time.Duration.Companion.milliseconds

/**
 * Time slice to delay auto-focusing on the password/pin field. Because of the refresh that
 * takes place when switching accounts or changing the lock status we want to delay this
 * longer than the delay in place for sending those actions in [MainViewModel]
 * defined by `ANIMATION_REFRESH_DELAY`. We need to  ensure this value is
 * always greater.
 */
private const val AUTO_FOCUS_DELAY = 575L

/**
 * The top level composable for the Vault Unlock screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun VaultUnlockScreen(
    viewModel: VaultUnlockViewModel,
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    focusManager: FocusManager = LocalFocusManager.current,
    credentialProviderCompletionManager: CredentialProviderCompletionManager =
        LocalCredentialProviderCompletionManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(state.requiresBiometricsLogin) {
        if (state.requiresBiometricsLogin && !biometricsManager.isBiometricsSupported) {
            viewModel.trySendAction(VaultUnlockAction.BiometricsNoLongerSupported)
        }
    }

    val onBiometricsUnlockSuccess: (cipher: Cipher) -> Unit = {
        viewModel.trySendAction(VaultUnlockAction.BiometricsUnlockSuccess(it))
    }
    val onBiometricsLockOut: () -> Unit = {
        viewModel.trySendAction(VaultUnlockAction.BiometricsLockOut)
    }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is VaultUnlockEvent.PromptForBiometrics -> {
                biometricsManager.promptBiometrics(
                    onSuccess = onBiometricsUnlockSuccess,
                    onCancel = {
                        // no-op
                    },
                    onError = {
                        // no-op
                    },
                    onLockOut = onBiometricsLockOut,
                    cipher = event.cipher,
                )
            }

            is VaultUnlockEvent.Fido2CredentialAssertionError -> {
                credentialProviderCompletionManager.completeFido2Assertion(
                    result = AssertFido2CredentialResult.Error(message = event.message),
                )
            }

            is VaultUnlockEvent.GetCredentialsError -> {
                credentialProviderCompletionManager.completeProviderGetCredentialsRequest(
                    result = GetCredentialsResult.Error(message = event.message),
                )
            }
        }
    }

    var accountMenuVisible by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { !accountMenuVisible },
    )

    // Dynamic dialogs
    when (val dialog = state.dialog) {
        is VaultUnlockState.VaultUnlockDialog.Error -> Universal Password ManagerBasicDialog(
            title = dialog.title(),
            message = dialog.message(),
            onDismissRequest = { viewModel.trySendAction(VaultUnlockAction.DismissDialog) },
            throwable = dialog.throwable,
        )

        VaultUnlockState.VaultUnlockDialog.Loading -> Universal Password ManagerLoadingDialog(
            text = stringResource(id = Universal Password ManagerString.loading),
        )

        VaultUnlockState.VaultUnlockDialog.BiometricsNoLongerSupported -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.biometrics_no_longer_supported_title),
                message = stringResource(id = Universal Password ManagerString.biometrics_no_longer_supported),
                onDismissRequest = remember {
                    {
                        viewModel.trySendAction(
                            VaultUnlockAction.DismissBiometricsNoLongerSupportedDialog,
                        )
                    }
                },
            )
        }

        null -> Unit
    }

    // Static dialogs
    var showLogoutConfirmationDialog by remember { mutableStateOf(false) }
    if (showLogoutConfirmationDialog) {
        Universal Password ManagerLogoutConfirmationDialog(
            onDismissRequest = { showLogoutConfirmationDialog = false },
            onConfirmClick = {
                showLogoutConfirmationDialog = false
                viewModel.trySendAction(VaultUnlockAction.ConfirmLogoutClick)
            },
        )
    }

    // Content
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(force = true) },
                )
            },
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.vaultUnlockType.unlockScreenTitle(),
                scrollBehavior = scrollBehavior,
                navigationIcon = null,
                actions = {
                    if (state.showAccountMenu) {
                        Universal Password ManagerAccountActionItem(
                            initials = state.initials,
                            color = state.avatarColor,
                            onClick = {
                                focusManager.clearFocus()
                                accountMenuVisible = !accountMenuVisible
                            },
                        )
                    }
                    Universal Password ManagerOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.log_out),
                                onClick = { showLogoutConfirmationDialog = true },
                            ),
                        ),
                    )
                },
            )
        },
        overlay = {
            Universal Password ManagerAccountSwitcher(
                isVisible = accountMenuVisible,
                accountSummaries = state.accountSummaries,
                onSwitchAccountClick = {
                    viewModel.trySendAction(VaultUnlockAction.SwitchAccountClick(it))
                },
                onLockAccountClick = {
                    viewModel.trySendAction(VaultUnlockAction.LockAccountClick(it))
                },
                onLogoutAccountClick = {
                    viewModel.trySendAction(VaultUnlockAction.LogoutAccountClick(it))
                },
                onAddAccountClick = { viewModel.trySendAction(VaultUnlockAction.AddAccountClick) },
                onDismissRequest = { accountMenuVisible = false },
                topAppBarScrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxSize(),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            if (!state.hideInput) {
                // When switching from an unlocked account to a locked account, the
                // current activity is recreated and therefore the composition takes place
                // twice. Adding this delay prevents the MP or Pin field
                // from auto focusing on the first composition which creates a visual jank where
                // the keyboard shows, disappears, and then shows again.
                var autoFocusDelayCompleted by rememberSaveable {
                    mutableStateOf(false)
                }
                LaunchedEffect(Unit) {
                    delay(AUTO_FOCUS_DELAY.milliseconds)
                    autoFocusDelayCompleted = true
                }
                Universal Password ManagerPasswordField(
                    label = state.vaultUnlockType.unlockScreenInputLabel(),
                    value = state.input,
                    onValueChange = { viewModel.trySendAction(VaultUnlockAction.InputChanged(it)) },
                    keyboardType = state.vaultUnlockType.unlockScreenKeyboardType,
                    showPasswordTestTag = state
                        .vaultUnlockType
                        .inputFieldVisibilityToggleTestTag,
                    autoFocus = state.showKeyboard && autoFocusDelayCompleted,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.trySendAction(VaultUnlockAction.UnlockClick) },
                    ),
                    passwordFieldTestTag = state.vaultUnlockType.unlockScreenInputTestTag,
                    cardStyle = CardStyle.Top(),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            Universal Password ManagerSupportingContent(
                cardStyle = if (state.hideInput) CardStyle.Full else CardStyle.Bottom,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            ) {
                if (!state.hideInput) {
                    Text(
                        text = state.vaultUnlockType.unlockScreenMessage(),
                        style = Universal Password ManagerTheme.typography.bodySmall,
                        color = Universal Password ManagerTheme.colorScheme.text.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                }
                Text(
                    text = stringResource(
                        id = Universal Password ManagerString.logged_in_as_on,
                        formatArgs = arrayOf(state.email, state.environmentUrl),
                    ),
                    style = Universal Password ManagerTheme.typography.bodySmall,
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    modifier = Modifier
                        .testTag(tag = "UserAndEnvironmentDataLabel")
                        .fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (state.showBiometricLogin && biometricsManager.isBiometricsSupported) {
                Universal Password ManagerOutlinedButton(
                    label = stringResource(id = Universal Password ManagerString.use_biometrics_to_unlock),
                    onClick = { viewModel.trySendAction(VaultUnlockAction.BiometricsUnlockClick) },
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else if (state.showBiometricInvalidatedMessage) {
                Text(
                    text = stringResource(Universal Password ManagerString.account_biometric_invalidated),
                    textAlign = TextAlign.Start,
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    color = Universal Password ManagerTheme.colorScheme.status.error,
                    modifier = Modifier.standardHorizontalMargin(),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!state.hideInput) {
                Universal Password ManagerFilledButton(
                    label = stringResource(id = Universal Password ManagerString.unlock),
                    onClick = { viewModel.trySendAction(VaultUnlockAction.UnlockClick) },
                    isEnabled = state.input.isNotEmpty(),
                    modifier = Modifier
                        .testTag("UnlockVaultButton")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
