package com.universalpasswordmanager.ui.auth.feature.removepassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.icon.UniversalIcon
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText

/**
 * The top level composable for the Remove Password screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemovePasswordScreen(
    viewModel: RemovePasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    RemovePasswordDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(RemovePasswordAction.DialogDismiss) },
        onConfirmLeaveClick = {
            viewModel.trySendAction(RemovePasswordAction.ConfirmLeaveOrganizationClick)
        },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.remove_master_password),
                scrollBehavior = scrollBehavior,
                navigationIcon = null,
            )
        },
    ) {
        RemovePasswordScreenContent(
            state = state,
            onContinueClick = { viewModel.trySendAction(RemovePasswordAction.ContinueClick) },
            onInputChanged = { viewModel.trySendAction(RemovePasswordAction.InputChanged(it)) },
            onLeaveOrganizationClick = {
                viewModel.trySendAction(RemovePasswordAction.LeaveOrganizationClick)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun RemovePasswordScreenContent(
    state: RemovePasswordState,
    onContinueClick: () -> Unit,
    onInputChanged: (String) -> Unit,
    onLeaveOrganizationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            text = state.description(),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))
        RemovePasswordInfoColumn(
            label = state.labelOrg(),
            value = state.orgName?.invoke().orEmpty(),
            icon = IconData.Local(iconRes = UniversalDrawable.ic_organization),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(
                    cardStyle = CardStyle.Top(dividerPadding = 56.dp),
                    paddingBottom = 4.dp,
                ),
        )
        RemovePasswordInfoColumn(
            label = state.labelDomain(),
            value = state.domainName?.invoke().orEmpty(),
            icon = IconData.Local(iconRes = UniversalDrawable.ic_globe),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(cardStyle = CardStyle.Bottom),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))

        UniversalPasswordField(
            label = stringResource(id = UniversalString.master_password),
            value = state.input,
            onValueChange = onInputChanged,
            showPasswordTestTag = "PasswordVisibilityToggle",
            passwordFieldTestTag = "MasterPasswordEntry",
            autoFocus = true,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))

        UniversalFilledButton(
            label = stringResource(id = UniversalString.continue_text),
            onClick = onContinueClick,
            isEnabled = state.input.isNotEmpty(),
            modifier = Modifier
                .testTag(tag = "ContinueButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.leave_organization),
            onClick = onLeaveOrganizationClick,
            modifier = Modifier
                .testTag("LeaveOrganizationButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun RemovePasswordInfoColumn(
    label: String,
    value: String,
    icon: IconData,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.defaultMinSize(minHeight = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(width = 12.dp))
        UniversalIcon(
            iconData = icon,
            tint = UniversalTheme.colorScheme.icon.secondary,
        )
        Spacer(modifier = Modifier.width(width = 12.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                style = UniversalTheme.typography.titleSmall,
                color = UniversalTheme.colorScheme.text.primary,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(height = 4.dp))

            Text(
                text = value,
                style = UniversalTheme.typography.bodyMedium,
                color = UniversalTheme.colorScheme.text.secondary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun RemovePasswordDialogs(
    dialogState: RemovePasswordState.DialogState?,
    onDismissRequest: () -> Unit,
    onConfirmLeaveClick: () -> Unit,
) {
    when (dialogState) {
        is RemovePasswordState.DialogState.Error -> {
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        is RemovePasswordState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialogState.title())
        }

        is RemovePasswordState.DialogState.LeaveConfirmationPrompt -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.leave_organization),
                message = dialogState.message.invoke(),
                confirmButtonText = stringResource(id = UniversalString.confirm),
                dismissButtonText = stringResource(id = UniversalString.cancel),
                onConfirmClick = onConfirmLeaveClick,
                onDismissClick = onDismissRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Preview(showBackground = true)
@Composable
private fun RemovePasswordScreen_preview() {
    UniversalTheme {
        RemovePasswordScreenContent(
            state = RemovePasswordState(
                input = "",
                description =
                    ("A master password is no longer required " +
                        "for members of the following organization. " +
                        "Please confirm the domain below with your " +
                        "organization administrator.").asText(),
                labelOrg = "Organization name".asText(),
                orgName = "Organization name".asText(),
                labelDomain = "Key Connector domain".asText(),
                domainName = "http://localhost:8080".asText(),
                dialogState = null,
                organizationId = null,
            ),
            onContinueClick = { },
            onInputChanged = { },
            onLeaveOrganizationClick = { },
        )
    }
}
