package com.x8bit.universalpasswordmanager.ui.auth.feature.expiredregistrationlink

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Top level screen component for the ExpiredRegistrationLink screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiredRegistrationLinkScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToStartRegistration: () -> Unit,
    viewModel: ExpiredRegistrationLinkViewModel = hiltViewModel(),
) {
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            ExpiredRegistrationLinkEvent.NavigateBack -> onNavigateBack()
            ExpiredRegistrationLinkEvent.NavigateToLogin -> onNavigateToLogin()
            ExpiredRegistrationLinkEvent.NavigateToStartRegistration -> {
                onNavigateToStartRegistration()
            }
        }
    }
    val sendCloseClicked = { viewModel.trySendAction(ExpiredRegistrationLinkAction.CloseClicked) }
    BackHandler(onBack = sendCloseClicked)
    Universal Password ManagerScaffold(
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.create_account),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                    onNavigationIconClick = sendCloseClicked,
                ),
            )
        },
    ) {
        ExpiredRegistrationLinkContent(
            onNavigateToLogin = {
                viewModel.trySendAction(ExpiredRegistrationLinkAction.GoToLoginClicked)
            },
            onNavigateToStartRegistration = {
                viewModel.trySendAction(ExpiredRegistrationLinkAction.RestartRegistrationClicked)
            },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
private fun ExpiredRegistrationLinkContent(
    onNavigateToLogin: () -> Unit,
    onNavigateToStartRegistration: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Universal Password ManagerString.expired_link),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Universal Password ManagerString.please_restart_registration_or_try_logging_in),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(Universal Password ManagerString.restart_registration),
            onClick = onNavigateToStartRegistration,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.log_in_verb),
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpiredRegistrationLinkScreen_preview() {
    Universal Password ManagerTheme {
        ExpiredRegistrationLinkContent(
            onNavigateToLogin = {},
            onNavigateToStartRegistration = {},
        )
    }
}
