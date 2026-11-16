package com.x8bit.universalpasswordmanager.ui.auth.feature.loginwithdevice

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
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.card.Universal Password ManagerContentCard
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.content.model.ContentBlockData
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * The top level composable for the Login with Device screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginWithDeviceScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTwoFactorLogin: (emailAddress: String) -> Unit,
    viewModel: LoginWithDeviceViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            LoginWithDeviceEvent.NavigateBack -> onNavigateBack()

            is LoginWithDeviceEvent.NavigateToTwoFactorLogin -> {
                onNavigateToTwoFactorLogin(event.emailAddress)
            }
        }
    }

    LoginWithDeviceDialogs(
        state = state.dialogState,
        onDismissDialog = { viewModel.trySendAction(LoginWithDeviceAction.DismissDialog) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.toolbarTitle(),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(LoginWithDeviceAction.CloseButtonClick)
                },
            )
        },
    ) {
        when (val viewState = state.viewState) {
            is LoginWithDeviceState.ViewState.Content -> {
                LoginWithDeviceScreenContent(
                    state = viewState,
                    onResendNotificationClick = {
                        viewModel.trySendAction(LoginWithDeviceAction.ResendNotificationClick)
                    },
                    onViewAllLogInOptionsClick = {
                        viewModel.trySendAction(LoginWithDeviceAction.ViewAllLogInOptionsClick)
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            LoginWithDeviceState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun LoginWithDeviceScreenContent(
    state: LoginWithDeviceState.ViewState.Content,
    onResendNotificationClick: () -> Unit,
    onViewAllLogInOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            text = state.title(),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = state.subTitle(),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = state.description(),
            textAlign = TextAlign.Center,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Universal Password ManagerContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(id = Universal Password ManagerString.fingerprint_phrase),
                    subtitleText = state.fingerprintPhrase,
                ),
            ),
            contentHeaderTextStyle = Universal Password ManagerTheme.typography.titleMedium,
            contentSubtitleTextStyle = Universal Password ManagerTheme.typography.sensitiveInfoSmall,
            contentSubtitleColor = Universal Password ManagerTheme.colorScheme.text.codePink,
            modifier = Modifier
                .testTag(tag = "FingerprintPhraseValue")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        if (state.allowsResend) {
            Spacer(modifier = Modifier.height(height = 24.dp))
            Universal Password ManagerOutlinedButton(
                label = stringResource(id = Universal Password ManagerString.resend_notification),
                onClick = onResendNotificationClick,
                modifier = Modifier
                    .testTag(tag = "ResendNotificationButton")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerHyperTextLink(
            annotatedResId = Universal Password ManagerString.need_another_option_view_all_login_options,
            annotationKey = "viewAll",
            accessibilityString = stringResource(id = Universal Password ManagerString.view_all_login_options),
            onClick = onViewAllLogInOptionsClick,
            style = Universal Password ManagerTheme.typography.bodySmall,
            modifier = Modifier
                .testTag(tag = "ViewAllLoginOptionsButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun LoginWithDeviceDialogs(
    state: LoginWithDeviceState.DialogState?,
    onDismissDialog: () -> Unit,
) {
    when (state) {
        is LoginWithDeviceState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = state.message(),
        )

        is LoginWithDeviceState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = state.title?.invoke(),
            message = state.message(),
            throwable = state.error,
            onDismissRequest = onDismissDialog,
        )

        null -> Unit
    }
}
