package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.deleteaccountconfirmation

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledErrorButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Displays the delete account confirmation screen.
 */
@Composable
fun DeleteAccountConfirmationScreen(
    viewModel: DeleteAccountConfirmationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            DeleteAccountConfirmationEvent.NavigateBack -> onNavigateBack()
        }
    }

    DeleteAccountConfirmationDialogs(
        dialogState = state.dialog,
        onDeleteAccountAcknowledge = {
            viewModel.trySendAction(DeleteAccountConfirmationAction.DeleteAccountAcknowledge)
        },
        onDismissDialog = {
            viewModel.trySendAction(DeleteAccountConfirmationAction.DismissDialog)
        },
    )

    DeleteAccountConfirmationScaffold(
        state = state,
        onCloseClick = { viewModel.trySendAction(DeleteAccountConfirmationAction.CloseClick) },
        onDeleteAccountClick = {
            viewModel.trySendAction(DeleteAccountConfirmationAction.DeleteAccountClick)
        },
        onResendCodeClick = {
            viewModel.trySendAction(DeleteAccountConfirmationAction.ResendCodeClick)
        },
        onVerificationCodeTextChange = {
            viewModel.trySendAction(DeleteAccountConfirmationAction.VerificationCodeTextChange(it))
        },
    )
}

@Composable
private fun DeleteAccountConfirmationDialogs(
    dialogState: DeleteAccountConfirmationState.DeleteAccountConfirmationDialog?,
    onDismissDialog: () -> Unit,
    onDeleteAccountAcknowledge: () -> Unit,
) {
    when (dialogState) {
        is DeleteAccountConfirmationState.DeleteAccountConfirmationDialog.DeleteSuccess -> {
            UniversalBasicDialog(
                title = null,
                message = dialogState.message(),
                onDismissRequest = onDeleteAccountAcknowledge,
            )
        }

        is DeleteAccountConfirmationState.DeleteAccountConfirmationDialog.Error -> {
            UniversalBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissDialog,
            )
        }

        is DeleteAccountConfirmationState.DeleteAccountConfirmationDialog.Loading -> {
            UniversalLoadingDialog(text = dialogState.title())
        }

        null -> Unit
    }
}

@Composable
private fun DeleteAccountConfirmationContent(
    state: DeleteAccountConfirmationState,
    onDeleteAccountClick: () -> Unit,
    onResendCodeClick: () -> Unit,
    onVerificationCodeTextChange: (verificationCode: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(id = UniversalString.a_verification_code_was_sent_to_your_email),
            textAlign = TextAlign.Start,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        UniversalPasswordField(
            value = state.verificationCode,
            onValueChange = onVerificationCodeTextChange,
            label = stringResource(id = UniversalString.verification_code),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            autoFocus = true,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = UniversalString.confirm_your_identity),
            textAlign = TextAlign.Start,
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        UniversalFilledErrorButton(
            label = stringResource(id = UniversalString.delete_account),
            onClick = onDeleteAccountClick,
            isEnabled = state.verificationCode.isNotBlank(),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.resend_code),
            onClick = onResendCodeClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteAccountConfirmationScaffold(
    state: DeleteAccountConfirmationState,
    onCloseClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onResendCodeClick: () -> Unit,
    onVerificationCodeTextChange: (verificationCode: String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.verification_code),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = onCloseClick,
            )
        },
    ) {
        DeleteAccountConfirmationContent(
            state = state,
            onDeleteAccountClick = onDeleteAccountClick,
            onResendCodeClick = onResendCodeClick,
            onVerificationCodeTextChange = onVerificationCodeTextChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteAccountConfirmationScreen_preview() {
    UniversalTheme {
        DeleteAccountConfirmationScaffold(
            state = DeleteAccountConfirmationState(
                dialog = null,
                verificationCode = "123456",
            ),
            onCloseClick = {},
            onDeleteAccountClick = {},
            onResendCodeClick = {},
            onVerificationCodeTextChange = {},
        )
    }
}
