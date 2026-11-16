package com.x8bit.universalpasswordmanager.ui.auth.feature.setpassword

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * The top level composable for the Set Master Password screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPasswordScreen(
    viewModel: SetPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    SetPasswordDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(SetPasswordAction.DialogDismiss) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.set_master_password),
                navigationIcon = null,
                scrollBehavior = scrollBehavior,
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.cancel),
                        onClick = { viewModel.trySendAction(SetPasswordAction.CancelClick) },
                        modifier = Modifier.testTag("CancelButton"),
                    )
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.submit),
                        onClick = { viewModel.trySendAction(SetPasswordAction.SubmitClick) },
                        modifier = Modifier.testTag("SubmitButton"),
                    )
                },
            )
        },
    ) {
        SetPasswordScreenContent(
            state = state,
            onPasswordInputChanged = {
                viewModel.trySendAction(SetPasswordAction.PasswordInputChanged(it))
            },
            onRetypePasswordInputChanged = {
                viewModel.trySendAction(SetPasswordAction.RetypePasswordInputChanged(it))
            },
            onPasswordHintInputChanged = {
                viewModel.trySendAction(SetPasswordAction.PasswordHintInputChanged(it))
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun SetPasswordScreenContent(
    state: SetPasswordState,
    onPasswordInputChanged: (String) -> Unit,
    onRetypePasswordInputChanged: (String) -> Unit,
    onPasswordHintInputChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        Text(
            text = stringResource(
                id = Universal Password ManagerString.your_organization_requires_you_to_set_a_master_password,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Universal Password ManagerInfoCalloutCard(
            text = stringResource(id = Universal Password ManagerString.reset_password_auto_enroll_invite_warning),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.master_password),
            value = state.passwordInput,
            onValueChange = onPasswordInputChanged,
            showPassword = isPasswordVisible,
            showPasswordChange = { isPasswordVisible = it },
            supportingText = stringResource(id = Universal Password ManagerString.master_password_description),
            passwordFieldTestTag = "NewPasswordField",
            cardStyle = CardStyle.Top(dividerPadding = 0.dp),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Universal Password ManagerPasswordField(
            label = stringResource(id = Universal Password ManagerString.retype_master_password),
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
            label = stringResource(id = Universal Password ManagerString.master_password_hint),
            value = state.passwordHintInput,
            onValueChange = onPasswordHintInputChanged,
            supportingText = stringResource(id = Universal Password ManagerString.master_password_hint_description),
            textFieldTestTag = "MasterPasswordHintLabel",
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun SetPasswordDialogs(
    dialogState: SetPasswordState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is SetPasswordState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissRequest,
            )
        }

        is SetPasswordState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.message())
        }

        null -> Unit
    }
}
