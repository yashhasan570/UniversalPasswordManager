package com.universalpasswordmanager.ui.auth.feature.resetpassword

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalOverflowActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.model.OverflowMenuItemData
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalInfoCalloutCard
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.text.UniversalHyperTextLink
import com.universalpasswordmanager.ui.platform.components.util.throttledClick
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.auth.datasource.disk.model.ForcePasswordResetReason
import com.universalpasswordmanager.ui.auth.feature.completeregistration.PasswordStrengthIndicator
import com.universalpasswordmanager.ui.auth.feature.completeregistration.PasswordStrengthState
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
            UniversalBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                onDismissRequest = { viewModel.trySendAction(ResetPasswordAction.DialogDismiss) },
            )
        }

        is ResetPasswordState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }

    var shouldShowLogoutConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    val onLogoutClicked = { viewModel.trySendAction(ResetPasswordAction.ConfirmLogoutClick) }
    if (shouldShowLogoutConfirmationDialog) {
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.log_out),
            message = stringResource(id = UniversalString.logout_confirmation),
            confirmButtonText = stringResource(id = UniversalString.yes),
            dismissButtonText = stringResource(id = UniversalString.cancel),
            onConfirmClick = {
                shouldShowLogoutConfirmationDialog = false
                onLogoutClicked()
            },
            onDismissClick = { shouldShowLogoutConfirmationDialog = false },
            onDismissRequest = { shouldShowLogoutConfirmationDialog = false },
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.update_master_password),
                navigationIcon = null,
                scrollBehavior = scrollBehavior,
                actions = {
                    if (state.viewState.shouldShowSaveButton) {
                        UniversalTextButton(
                            label = stringResource(id = UniversalString.save),
                            onClick = { viewModel.trySendAction(ResetPasswordAction.SaveClick) },
                            modifier = Modifier.testTag("SaveButton"),
                        )
                    }
                    UniversalOverflowActionItem(
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(UniversalString.log_out),
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
                UniversalErrorContent(
                    message = viewState.message(),
                    buttonData = UniversalButtonData(
                        label = UniversalString.try_again.asText(),
                        onClick = throttledClick {
                            viewModel.trySendAction(ResetPasswordAction.RetryClick)
                        },
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ResetPasswordState.ViewState.Loading -> {
                UniversalLoadingContent(
                    text = stringResource(id = UniversalString.syncing),
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
                UniversalString.update_weak_master_password_warning
            } else {
                UniversalString.update_master_password_warning
            }
        UniversalInfoCalloutCard(
            text = stringResource(id = instructionsTextId),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        state.policies.takeUnless { it.isEmpty() }?.let { policies ->
            val passwordPolicyContent = listOf(
                stringResource(id = UniversalString.master_password_policy_in_effect),
            )
                .plus(elements = policies.map { it() })
                .joinToString(separator = "\n  •  ")
            UniversalInfoCalloutCard(
                text = passwordPolicyContent,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }

        if (state.resetReason == ForcePasswordResetReason.WEAK_MASTER_PASSWORD_ON_LOGIN) {
            UniversalPasswordField(
                label = stringResource(id = UniversalString.current_master_password_required),
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
        UniversalPasswordField(
            label = stringResource(id = UniversalString.new_master_password_required),
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

        UniversalPasswordField(
            label = stringResource(id = UniversalString.retype_new_master_password_required),
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

        UniversalTextField(
            label = stringResource(id = UniversalString.new_master_password_hint),
            value = state.passwordHintInput,
            onValueChange = onPasswordHintInputChanged,
            supportingContent = {
                Column {
                    Text(
                        text = stringResource(
                            id = UniversalString
                                .bitwarden_cannot_reset_a_lost_or_forgotten_master_password,
                        ),
                        style = UniversalTheme.typography.bodySmall,
                        color = UniversalTheme.colorScheme.text.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    UniversalHyperTextLink(
                        annotatedResId =
                            UniversalString.learn_about_ways_to_prevent_account_lockout,
                        annotationKey = "onPreventAccountLockout",
                        accessibilityString = stringResource(
                            UniversalString.learn_about_ways_to_prevent_account_lockout,
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
    UniversalTheme {
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
                .background(UniversalTheme.colorScheme.background.primary),
        )
    }
}
