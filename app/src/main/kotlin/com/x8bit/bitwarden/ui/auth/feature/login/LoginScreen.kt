package com.x8bit.universalpasswordmanager.ui.auth.feature.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.account.Universal Password ManagerAccountSwitcher
import .ui.platform.components.account.Universal Password ManagerPlaceholderAccountActionItem
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
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.R
import kotlinx.collections.immutable.persistentListOf

/**
 * The top level composable for the Login screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMasterPasswordHint: (String) -> Unit,
    onNavigateToEnterpriseSignOn: (String) -> Unit,
    onNavigateToLoginWithDevice: (emailAddress: String) -> Unit,
    onNavigateToTwoFactorLogin: (String, String?, Boolean) -> Unit,
    viewModel: LoginViewModel,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            LoginEvent.NavigateBack -> onNavigateBack()
            is LoginEvent.NavigateToMasterPasswordHint -> {
                onNavigateToMasterPasswordHint(event.emailAddress)
            }

            is LoginEvent.NavigateToEnterpriseSignOn -> {
                onNavigateToEnterpriseSignOn(event.emailAddress)
            }

            is LoginEvent.NavigateToLoginWithDevice -> {
                onNavigateToLoginWithDevice(event.emailAddress)
            }

            is LoginEvent.NavigateToTwoFactorLogin -> {
                onNavigateToTwoFactorLogin(
                    event.emailAddress,
                    event.password,
                    event.isNewDeviceVerification,
                )
            }
        }
    }

    LoginDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(LoginAction.ErrorDialogDismiss) },
    )

    val isAccountButtonVisible = state.accountSummaries.isNotEmpty()
    var isAccountMenuVisible by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = R.string.app_name),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = { viewModel.trySendAction(LoginAction.CloseButtonClick) },
                actions = {
                    if (isAccountButtonVisible) {
                        Universal Password ManagerPlaceholderAccountActionItem(
                            onClick = { isAccountMenuVisible = !isAccountMenuVisible },
                        )
                    }
                    Universal Password ManagerOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.get_password_hint),
                                onClick = {
                                    viewModel.trySendAction(LoginAction.MasterPasswordHintClick)
                                },
                            ),
                        ),
                    )
                },
            )
        },
        overlay = {
            Universal Password ManagerAccountSwitcher(
                isVisible = isAccountMenuVisible,
                accountSummaries = state.accountSummaries,
                onSwitchAccountClick = {
                    viewModel.trySendAction(LoginAction.SwitchAccountClick(it))
                },
                onLockAccountClick = { viewModel.trySendAction(LoginAction.LockAccountClick(it)) },
                onLogoutAccountClick = {
                    viewModel.trySendAction(LoginAction.LogoutAccountClick(it))
                },
                onAddAccountClick = { viewModel.trySendAction(LoginAction.AddAccountClick) },
                onDismissRequest = { isAccountMenuVisible = false },
                topAppBarScrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxSize(),
            )
        },
    ) {
        LoginScreenContent(
            state = state,
            onPasswordInputChanged = {
                viewModel.trySendAction(LoginAction.PasswordInputChanged(it))
            },
            onMasterPasswordClick = {
                viewModel.trySendAction(LoginAction.MasterPasswordHintClick)
            },
            onLoginButtonClick = {
                keyboardController?.hide()
                viewModel.trySendAction(LoginAction.LoginButtonClick)
            },
            onLoginWithDeviceClick = {
                viewModel.trySendAction(LoginAction.LoginWithDeviceButtonClick)
            },
            onSingleSignOnClick = { viewModel.trySendAction(LoginAction.SingleSignOnClick) },
            onNotYouButtonClick = { viewModel.trySendAction(LoginAction.NotYouButtonClick) },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun LoginDialogs(
    dialogState: LoginState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is LoginState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            throwable = dialogState.error,
            onDismissRequest = onDismissRequest,
        )

        is LoginState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}

@Suppress("LongMethod")
@Composable
private fun LoginScreenContent(
    state: LoginState,
    onPasswordInputChanged: (String) -> Unit,
    onMasterPasswordClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    onLoginWithDeviceClick: () -> Unit,
    onSingleSignOnClick: () -> Unit,
    onNotYouButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        Universal Password ManagerPasswordField(
            autoFocus = true,
            value = state.passwordInput,
            onValueChange = onPasswordInputChanged,
            label = stringResource(id = Universal Password ManagerString.master_password),
            showPasswordTestTag = "PasswordVisibilityToggle",
            supportingContentPadding = PaddingValues(),
            supportingContent = {
                Universal Password ManagerClickableText(
                    label = stringResource(id = Universal Password ManagerString.get_master_passwordword_hint),
                    onClick = onMasterPasswordClick,
                    style = Universal Password ManagerTheme.typography.labelMedium,
                    innerPadding = PaddingValues(all = 16.dp),
                    shape = Universal Password ManagerTheme.shapes.contentBottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(tag = "GetMasterPasswordHintLabel"),
                )
            },
            passwordFieldTestTag = "MasterPasswordEntry",
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.log_in_with_master_password),
            onClick = onLoginButtonClick,
            isEnabled = state.isLoginButtonEnabled,
            modifier = Modifier
                .testTag("LogInWithMasterPasswordButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.shouldShowLoginWithDevice) {
            Universal Password ManagerOutlinedButton(
                label = stringResource(id = Universal Password ManagerString.log_in_with_device),
                icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_mobile_small),
                onClick = onLoginWithDeviceClick,
                modifier = Modifier
                    .testTag("LogInWithAnotherDeviceButton")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.log_in_sso),
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_enterprise_small),
            onClick = onSingleSignOnClick,
            modifier = Modifier
                .testTag("LogInWithSsoButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(
                id = Universal Password ManagerString.logging_in_as_x_on_y,
                state.emailAddress,
                state.environmentLabel,
            ),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodySmall,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            modifier = Modifier
                .testTag("LoggingInAsLabel")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Universal Password ManagerClickableText(
            label = stringResource(id = Universal Password ManagerString.not_you),
            onClick = onNotYouButtonClick,
            style = Universal Password ManagerTheme.typography.labelMedium,
            innerPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            modifier = Modifier
                .standardHorizontalMargin()
                .align(alignment = Alignment.CenterHorizontally)
                .testTag("NotYouLabel"),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
