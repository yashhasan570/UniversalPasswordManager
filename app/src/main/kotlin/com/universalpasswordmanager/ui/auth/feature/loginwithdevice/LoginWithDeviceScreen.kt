package com.universalpasswordmanager.ui.auth.feature.loginwithdevice

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.card.UniversalContentCard
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.content.model.ContentBlockData
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.text.UniversalHyperTextLink
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = state.toolbarTitle(),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
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

            LoginWithDeviceState.ViewState.Loading -> UniversalLoadingContent(
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
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = state.subTitle(),
            textAlign = TextAlign.Center,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = state.description(),
            textAlign = TextAlign.Center,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalContentCard(
            contentItems = persistentListOf(
                ContentBlockData(
                    headerText = stringResource(id = UniversalString.fingerprint_phrase),
                    subtitleText = state.fingerprintPhrase,
                ),
            ),
            contentHeaderTextStyle = UniversalTheme.typography.titleMedium,
            contentSubtitleTextStyle = UniversalTheme.typography.sensitiveInfoSmall,
            contentSubtitleColor = UniversalTheme.colorScheme.text.codePink,
            modifier = Modifier
                .testTag(tag = "FingerprintPhraseValue")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        if (state.allowsResend) {
            Spacer(modifier = Modifier.height(height = 24.dp))
            UniversalOutlinedButton(
                label = stringResource(id = UniversalString.resend_notification),
                onClick = onResendNotificationClick,
                modifier = Modifier
                    .testTag(tag = "ResendNotificationButton")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(height = 24.dp))

        UniversalHyperTextLink(
            annotatedResId = UniversalString.need_another_option_view_all_login_options,
            annotationKey = "viewAll",
            accessibilityString = stringResource(id = UniversalString.view_all_login_options),
            onClick = onViewAllLogInOptionsClick,
            style = UniversalTheme.typography.bodySmall,
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
        is LoginWithDeviceState.DialogState.Loading -> UniversalLoadingDialog(
            text = state.message(),
        )

        is LoginWithDeviceState.DialogState.Error -> UniversalBasicDialog(
            title = state.title?.invoke(),
            message = state.message(),
            throwable = state.error,
            onDismissRequest = onDismissDialog,
        )

        null -> Unit
    }
}
