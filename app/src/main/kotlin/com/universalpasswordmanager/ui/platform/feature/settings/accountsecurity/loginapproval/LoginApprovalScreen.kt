package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.loginapproval

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.nullableTestTag
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalExitManager
import com.universalpasswordmanager.ui.platform.manager.exit.ExitManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Displays the login approval screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun LoginApprovalScreen(
    viewModel: LoginApprovalViewModel,
    exitManager: ExitManager = LocalExitManager.current,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            LoginApprovalEvent.ExitApp -> exitManager.exitApplication()
            LoginApprovalEvent.NavigateBack -> onNavigateBack()
        }
    }

    LoginApprovalDialogs(
        state = state.dialogState,
        onDismissError = { viewModel.trySendAction(LoginApprovalAction.ErrorDialogDismiss) },
        onConfirmChangeAccount = {
            viewModel.trySendAction(LoginApprovalAction.ApproveAccountChangeClick)
        },
        onDismissChangeAccount = {
            viewModel.trySendAction(LoginApprovalAction.CancelAccountChangeClick)
        },
    )

    BackHandler(onBack = { viewModel.trySendAction(LoginApprovalAction.CloseClick) })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.access_requested),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = { viewModel.trySendAction(LoginApprovalAction.CloseClick) },
            )
        },
    ) {
        when (val viewState = state.viewState) {
            is LoginApprovalState.ViewState.Content -> {
                LoginApprovalContent(
                    state = viewState,
                    onConfirmLoginClick = {
                        viewModel.trySendAction(LoginApprovalAction.ApproveRequestClick)
                    },
                    onDeclineLoginClick = {
                        viewModel.trySendAction(LoginApprovalAction.DeclineRequestClick)
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is LoginApprovalState.ViewState.Error -> {
                UniversalErrorContent(
                    message = stringResource(id = UniversalString.generic_error_message),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is LoginApprovalState.ViewState.Loading -> {
                UniversalLoadingContent(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun LoginApprovalContent(
    state: LoginApprovalState.ViewState.Content,
    onConfirmLoginClick: () -> Unit,
    onDeclineLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 24.dp))
        Text(
            text = stringResource(id = UniversalString.are_you_trying_to_access_your_account),
            textAlign = TextAlign.Center,
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        Text(
            text = stringResource(
                id = UniversalString.access_attempt_by_x_on_y,
                state.email,
                state.domainUrl,
            ),
            textAlign = TextAlign.Center,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .testTag("LogInAttemptByLabel"),
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = UniversalString.fingerprint_phrase),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(
                    cardStyle = CardStyle.Top(hasDivider = false),
                    paddingBottom = 4.dp,
                )
                .padding(horizontal = 16.dp),
        )
        Text(
            text = state.fingerprint,
            textAlign = TextAlign.Start,
            color = UniversalTheme.colorScheme.text.codePink,
            style = UniversalTheme.typography.sensitiveInfoSmall,
            modifier = Modifier
                .testTag("FingerprintValueLabel")
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(
                    cardStyle = CardStyle.Middle(dividerPadding = 0.dp),
                    paddingTop = 0.dp,
                    paddingStart = 16.dp,
                    paddingEnd = 16.dp,
                ),
        )

        LoginApprovalInfoColumn(
            label = stringResource(id = UniversalString.device_type),
            value = state.deviceType,
            valueTestTag = "DeviceTypeValueLabel",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(cardStyle = CardStyle.Middle()),
        )

        LoginApprovalInfoColumn(
            label = stringResource(id = UniversalString.ip_address),
            value = state.ipAddress,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(cardStyle = CardStyle.Middle()),
        )

        LoginApprovalInfoColumn(
            label = stringResource(id = UniversalString.time),
            value = state.time,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .cardStyle(cardStyle = CardStyle.Bottom),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalFilledButton(
            label = stringResource(id = UniversalString.confirm_access),
            onClick = onConfirmLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .testTag("ConfirmLoginButton"),
        )

        Spacer(modifier = Modifier.height(8.dp))

        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.deny_access),
            onClick = onDeclineLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .testTag("DenyLoginButton"),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

/**
 * A view displaying information about this login approval request.
 */
@Composable
private fun LoginApprovalInfoColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueTestTag: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = UniversalTheme.typography.titleSmall,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 4.dp))

        Text(
            text = value,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .nullableTestTag(tag = valueTestTag),
        )
    }
}

@Composable
private fun LoginApprovalDialogs(
    state: LoginApprovalState.DialogState?,
    onDismissError: () -> Unit,
    onConfirmChangeAccount: () -> Unit,
    onDismissChangeAccount: () -> Unit,
) {
    when (state) {
        is LoginApprovalState.DialogState.ChangeAccount -> UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.access_requested),
            message = state.message(),
            confirmButtonText = stringResource(id = UniversalString.okay),
            dismissButtonText = stringResource(id = UniversalString.cancel),
            onConfirmClick = onConfirmChangeAccount,
            onDismissClick = onDismissChangeAccount,
            onDismissRequest = onDismissChangeAccount,
        )

        is LoginApprovalState.DialogState.Error -> UniversalBasicDialog(
            title = state.title?.invoke(),
            message = state.message(),
            throwable = state.error,
            onDismissRequest = onDismissError,
        )

        null -> Unit
    }
}
