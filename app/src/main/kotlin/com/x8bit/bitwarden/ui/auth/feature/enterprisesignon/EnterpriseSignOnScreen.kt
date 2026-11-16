package com.x8bit.universalpasswordmanager.ui.auth.feature.enterprisesignon

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.R
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAuthTabLaunchers
import com.x8bit.universalpasswordmanager.ui.platform.model.AuthTabLaunchers

/**
 * The top level composable for the Enterprise Single Sign On screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterpriseSignOnScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSetPassword: () -> Unit,
    onNavigateToTwoFactorLogin: (email: String, orgIdentifier: String) -> Unit,
    authTabLaunchers: AuthTabLaunchers = LocalAuthTabLaunchers.current,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: EnterpriseSignOnViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            EnterpriseSignOnEvent.NavigateBack -> onNavigateBack()

            is EnterpriseSignOnEvent.NavigateToSsoLogin -> {
                intentManager.startAuthTab(
                    uri = event.uri,
                    authTabData = event.authTabData,
                    launcher = authTabLaunchers.sso,
                )
            }

            is EnterpriseSignOnEvent.NavigateToSetPassword -> {
                onNavigateToSetPassword()
            }

            is EnterpriseSignOnEvent.NavigateToTwoFactorLogin -> {
                onNavigateToTwoFactorLogin(event.emailAddress, event.orgIdentifier)
            }
        }
    }

    EnterpriseSignOnDialogs(
        dialogState = state.dialogState,
        onConfirmKeyConnectorDomain = {
            viewModel.trySendAction(EnterpriseSignOnAction.ConfirmKeyConnectorDomainClick)
        },
        onDismissKeyConnectorDomain = {
            viewModel.trySendAction(EnterpriseSignOnAction.CancelKeyConnectorDomainClick)
        },
        onDismissRequest = { viewModel.trySendAction(EnterpriseSignOnAction.DialogDismiss) },
    )

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
                onNavigationIconClick = {
                    viewModel.trySendAction(EnterpriseSignOnAction.CloseButtonClick)
                },
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.log_in_verb),
                        onClick = { viewModel.trySendAction(EnterpriseSignOnAction.LogInClick) },
                        modifier = Modifier.testTag("LoginButton"),
                    )
                },
            )
        },
    ) {
        EnterpriseSignOnScreenContent(
            state = state,
            onOrgIdentifierInputChange = {
                viewModel.trySendAction(EnterpriseSignOnAction.OrgIdentifierInputChange(it))
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun EnterpriseSignOnScreenContent(
    state: EnterpriseSignOnState,
    onOrgIdentifierInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.log_in_sso_summary),
            textAlign = TextAlign.Start,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 8.dp))

        Universal Password ManagerTextField(
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
            value = state.orgIdentifierInput,
            onValueChange = onOrgIdentifierInputChange,
            label = stringResource(id = Universal Password ManagerString.org_identifier),
            textFieldTestTag = "OrgSSOIdentifierEntry",
            cardStyle = CardStyle.Full,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun EnterpriseSignOnDialogs(
    dialogState: EnterpriseSignOnState.DialogState?,
    onDismissRequest: () -> Unit,
    onConfirmKeyConnectorDomain: () -> Unit,
    onDismissKeyConnectorDomain: () -> Unit,
) {
    when (dialogState) {
        is EnterpriseSignOnState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissRequest,
            )
        }

        is EnterpriseSignOnState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.message())
        }

        is EnterpriseSignOnState.DialogState.KeyConnectorDomain -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.confirm_key_connector_domain),
                message = stringResource(
                    Universal Password ManagerString.please_confirm_domain_with_admin,
                    dialogState.keyConnectorDomain,
                ),
                confirmButtonText = stringResource(Universal Password ManagerString.confirm),
                dismissButtonText = stringResource(Universal Password ManagerString.cancel),
                onConfirmClick = onConfirmKeyConnectorDomain,
                onDismissRequest = onDismissKeyConnectorDomain,
                onDismissClick = onDismissKeyConnectorDomain,
            )
        }

        null -> Unit
    }
}
