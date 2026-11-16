package com.x8bit.universalpasswordmanager.ui.auth.feature.resetpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.components.util.throttledClick
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.auth.datasource.disk.model.ForcePasswordResetReason
import com.x8bit.universalpasswordmanager.ui.auth.feature.completeregistration.PasswordStrengthIndicator
import com.x8bit.universalpasswordmanager.ui.auth.feature.completeregistration.PasswordStrengthState
import kotlinx.collections.immutable.persistentListOf

/**
 * The top level composable for the Reset Password screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun ResetPasswordScreen(
    onNavigateToPreventAccountLockOut: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            ResetPasswordEvent.NavigateToPreventAccountLockout -> {
                onNavigateToPreventAccountLockOut()
            }
        }
    }

    when (val dialog = state.dialogState) {
        is ResetPasswordState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                onDismissRequest = { viewModel.trySendAction(ResetPasswordAction.DialogDismiss) },
            )
        }

        is ResetPasswordState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }

    var shouldShowLogoutConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    val onLogoutClicked = { viewModel.trySendAction(ResetPasswordAction.ConfirmLogoutClick) }
    if (shouldShowLogoutConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.log_out),
            message = stringResource(id = Universal Password ManagerString.logout_confirmation),
            confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                shouldShowLogoutConfirmationDialog = false
                onLogoutClicked()
            },
            onDismissClick = { shouldShowLogoutConfirmationDialog = false },
            onDismissRequest = { shouldShowLogoutConfirmationDialog = false },
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.update_master_password),
                navigationIcon = null,
                scrollBehavior = scrollBehavior,
                actions = {
                    if (state.viewState.shouldShowSaveButton) {
                        Universal Password ManagerTextButton(
                            label = stringResource(id = Universal Password ManagerString.save),
                            onClick = { viewModel.trySendAction(ResetPasswordAction.SaveClick) },
                            modifier = Modifier.testTag("SaveButton"),
                        )
                    }
                    Universal Password ManagerOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(Universal Password ManagerString.log_out),
                                onClick = { shouldShowLogoutConfirmationDialog = true },
                            ),
                        ),
                    )
                },
            )
        },
    ) {
        when (val viewState = state.viewState) {
            is ResetPasswordState.ViewState.Content -> {
                ResetPasswordScreenContent(
                    state = viewState,
                    onCurrentPasswordInputChanged = {
                        viewModel.trySendAction(ResetPasswordAction.CurrentPasswordInputChanged(it))
                    },
                    onPasswordInputChanged = {
                        viewModel.trySendAction(ResetPasswordAction.PasswordInputChanged(it))
                    },
                    onRetypePasswordInputChanged = {
                        viewModel.trySendAction(ResetPasswordAction.RetypePasswordInputChanged(it))
                    },
                    onPasswordHintInputChanged = {
                        viewModel.trySendAction(ResetPasswordAction.PasswordHintInputChanged(it))
                    },
                    onLearnToPreventLockout = {
                        viewModel.trySendAction(ResetPasswordAction.LearnHowPreventLockoutClick)
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is ResetPasswordState.ViewState.Error -> {
                Universal Password ManagerErrorContent(
                    message = viewState.message(),
                    buttonData = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.try_again.asText(),
                        onClick = throttledClick {
                            viewModel.trySendAction(ResetPasswordAction.RetryClick)
                        },
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ResetPasswordState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(
                    text = stringResource(id = Universal Password ManagerString.syncing),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
@Suppress("LongMethod")
private fun ResetPasswordScreenContent(
    state: ResetPasswordState.ViewState.Content,
    onCurrentPasswordInputChanged: (String) -> Unit,
    onPasswordInputChanged: (String) -> Unit,
    onRetypePasswordInputChanged: (String) -> Unit,
    onPasswordHintInputChanged: (String) -> Unit,
    onLearnToPreventLockout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        val instructionsTextId =
            if (state.resetReason == ForcePasswordResetReason.WEAK_MASTER_PASSWORD_ON_LOGIN) {
                Universal Password ManagerString.update_weak_master_password_warning
            } else {
                Universal Password ManagerString.update_master_password_warning
            }
        Universal Password ManagerInfoCalloutCard(
            text = stringResource(id = instructionsTextId),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        state.policies.takeUnless { it.isEmpty() }?.let { policies ->
            val passwordPolicyContent = listOf(
                stringResource(id = Universal Password ManagerString.master_password_policy_in_effect),
            )
                .plus(elements = policies.map { it() })
                .joinToString(separator = "\n  •  ")
            Universal Password ManagerInfoCalloutCard(
                text = passwordPolicyContent,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }

        if (state.resetReason == ForcePasswordResetReason.WEAK_MASTER_PASSWORD_ON_LOGIN) {
            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.current_master_password_required),
                value = state.currentPasswordInput,
                onValueChange = onCurrentPasswordInputChanged,
                passwordFieldTestTag = "MasterPasswordField",
                cardStyle = CardStyle.Top(dividerPadding = 0.dp),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.new_master_password_required),
            value = state.passwordInput,
            onValueChange = onPasswordInputChanged,
            showPassword = isPasswordVisible,
            showPasswordChange = { isPasswordVisible = it },
            passwordFieldTestTag = "NewPasswordField",
            supportingContent = {
                PasswordStrengthIndicator(
                    state = state.passwordStrengthState,
                    currentCharacterCount = state.passwordInput.length,
                    minimumCharacterCount = state.minimumPasswordLength,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            cardStyle = if (
                state.resetReason == ForcePasswordResetReason.WEAK_MASTER_PASSWORD_ON_LOGIN) {
                CardStyle.Middle(dividerPadding = 0.dp)
            } else {
                CardStyle.Top(dividerPadding = 0.dp)
            },
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.retype_new_master_password_required),
            value = state.retypePasswordInput,
            onValueChange = onRetypePasswordInputChanged,
            showPassword = isPasswordVisible,
            showPasswordChange = { isPasswordVisible = it },
            passwordFieldTestTag = "RetypePasswordField",
            cardStyle = CardStyle.Middle(dividerPadding = 0.dp),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.new_master_password_hint),
            value = state.passwordHintInput,
            onValueChange = onPasswordHintInputChanged,
            supportingContent = {
                Column {
                    Text(
                        text = stringResource(
                            id = Universal Password ManagerString
                                .universalpasswordmanager_cannot_reset_a_lost_or_forgotten_master_password,
                        ),
                        style = Universal Password ManagerTheme.typography.bodySmall,
                        color = Universal Password ManagerTheme.colorScheme.text.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Universal Password ManagerHyperTextLink(
                        annotatedResId =
                            Universal Password ManagerString.learn_about_ways_to_prevent_account_lockout,
                        annotationKey = "onPreventAccountLockout",
                        accessibilityString = stringResource(
                            Universal Password ManagerString.learn_about_ways_to_prevent_account_lockout,
                        ),
                        onClick = onLearnToPreventLockout,
                    )
                }
            },
            textFieldTestTag = "MasterPasswordHintLabel",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview
@Composable
private fun ResetPasswordScreenContent_preview() {
    Universal Password ManagerTheme {
        ResetPasswordScreenContent(
            state = ResetPasswordState.ViewState.Content(
                resetReason = null,
                policies = persistentListOf(),
                minimumPasswordLength = 12,
                currentPasswordInput = "",
                passwordInput = "",
                retypePasswordInput = "",
                passwordHintInput = "",
                passwordStrengthState = PasswordStrengthState.GOOD,
            ),
            onCurrentPasswordInputChanged = {},
            onPasswordInputChanged = {},
            onRetypePasswordInputChanged = {},
            onPasswordHintInputChanged = {},
            onLearnToPreventLockout = {},
            modifier = Modifier
                .fillMaxSize()
                .background(Universal Password ManagerTheme.colorScheme.background.primary),
        )
    }
}
