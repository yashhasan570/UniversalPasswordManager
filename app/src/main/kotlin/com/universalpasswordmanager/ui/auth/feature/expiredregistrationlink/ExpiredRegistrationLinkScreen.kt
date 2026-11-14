package com.universalpasswordmanager.ui.auth.feature.expiredregistrationlink

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

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
    UniversalScaffold(
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.create_account),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                    navigationIconContentDescription = stringResource(id = UniversalString.close),
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
            text = stringResource(UniversalString.expired_link),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(UniversalString.please_restart_registration_or_try_logging_in),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        UniversalFilledButton(
            label = stringResource(UniversalString.restart_registration),
            onClick = onNavigateToStartRegistration,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.log_in_verb),
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
    UniversalTheme {
        ExpiredRegistrationLinkContent(
            onNavigateToLogin = {},
            onNavigateToStartRegistration = {},
        )
    }
}
