package com.universalpasswordmanager.ui.auth.feature.trusteddevice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.remember
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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.auth.feature.trusteddevice.handlers.TrustedDeviceHandlers

/**
 * The top level composable for the Reset Password screen.
 */
@Composable
fun TrustedDeviceScreen(
    onNavigateToAdminApproval: (emailAddress: String) -> Unit,
    onNavigateToLoginWithOtherDevice: (emailAddress: String) -> Unit,
    onNavigateToLock: (emailAddress: String) -> Unit,
    viewModel: TrustedDeviceViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handlers = remember(viewModel) { TrustedDeviceHandlers.create(viewModel = viewModel) }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is TrustedDeviceEvent.NavigateToApproveWithAdmin -> {
                onNavigateToAdminApproval(event.email)
            }

            is TrustedDeviceEvent.NavigateToApproveWithDevice -> {
                onNavigateToLoginWithOtherDevice(event.email)
            }

            is TrustedDeviceEvent.NavigateToLockScreen -> {
                onNavigateToLock(event.email)
            }
        }
    }

    TrustedDeviceDialogs(
        dialogState = state.dialogState,
        handlers = handlers,
    )

    TrustedDeviceScaffold(
        state = state,
        handlers = handlers,
    )
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrustedDeviceScaffold(
    state: TrustedDeviceState,
    handlers: TrustedDeviceHandlers,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.log_in_initiated),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                    navigationIconContentDescription = stringResource(id = UniversalString.close),
                    onNavigationIconClick = handlers.onBackClick,
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            UniversalSwitch(
                label = stringResource(id = UniversalString.remember_this_device),
                supportingText = stringResource(id = UniversalString.turn_off_using_public_device),
                isChecked = state.isRemembered,
                onCheckedChange = handlers.onRememberToggle,
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .testTag("RememberThisDeviceSwitch")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (state.showContinueButton) {
                UniversalFilledButton(
                    label = stringResource(id = UniversalString.continue_text),
                    onClick = handlers.onContinueClick,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.showOtherDeviceButton) {
                UniversalFilledButton(
                    label = stringResource(id = UniversalString.approve_with_my_other_device),
                    onClick = handlers.onApproveWithDeviceClick,
                    modifier = Modifier
                        .testTag("ApproveWithOtherDeviceButton")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.showRequestAdminButton) {
                UniversalOutlinedButton(
                    label = stringResource(id = UniversalString.request_admin_approval),
                    onClick = handlers.onApproveWithAdminClick,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .testTag("RequestAdminApprovalButton"),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.showMasterPasswordButton) {
                UniversalOutlinedButton(
                    label = stringResource(id = UniversalString.approve_with_master_password),
                    onClick = handlers.onApproveWithPasswordClick,
                    modifier = Modifier
                        .testTag("ApproveWithMasterPasswordButton")
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(
                    id = UniversalString.logging_in_as_x_on_y,
                    state.emailAddress,
                    state.environmentLabel,
                ),
                style = UniversalTheme.typography.bodyMedium,
                color = UniversalTheme.colorScheme.text.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .testTag("LoggingInAsLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            UniversalClickableText(
                label = stringResource(id = UniversalString.not_you),
                onClick = handlers.onNotYouButtonClick,
                style = UniversalTheme.typography.labelLarge,
                innerPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .testTag("NotYouLabel"),
            )

            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun TrustedDeviceDialogs(
    dialogState: TrustedDeviceState.DialogState?,
    handlers: TrustedDeviceHandlers,
) {
    when (dialogState) {
        is TrustedDeviceState.DialogState.Error -> UniversalBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            throwable = dialogState.error,
            onDismissRequest = handlers.onDismissDialog,
        )

        is TrustedDeviceState.DialogState.Loading -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}

@Preview
@Composable
private fun TrustedDeviceScaffold_preview() {
    UniversalTheme {
        TrustedDeviceScaffold(
            state = TrustedDeviceState(
                dialogState = null,
                isRemembered = false,
                emailAddress = "email@",
                environmentLabel = "vault.",
                showContinueButton = false,
                showOtherDeviceButton = true,
                showRequestAdminButton = true,
                showMasterPasswordButton = true,
            ),
            handlers = TrustedDeviceHandlers(
                onBackClick = {},
                onDismissDialog = {},
                onRememberToggle = {},
                onContinueClick = {},
                onApproveWithAdminClick = {},
                onApproveWithDeviceClick = {},
                onApproveWithPasswordClick = {},
                onNotYouButtonClick = {},
            ),
        )
    }
}
