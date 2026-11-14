package com.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.providerevents.exception.ImportCredentialsCancellationException
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.cxf.manager.CredentialExchangeCompletionManager
import com.universalpasswordmanager.cxf.manager.model.ExportCredentialsResult
import com.universalpasswordmanager.cxf.ui.composition.LocalCredentialExchangeCompletionManager
import com.universalpasswordmanager.cxf.ui.composition.LocalCredentialExchangeRequestValidator
import com.universalpasswordmanager.cxf.validator.CredentialExchangeRequestValidator
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalPasswordField
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.feature.exportitems.component.AccountSummaryListItem
import com.universalpasswordmanager.ui.vault.feature.exportitems.component.ExportItemsScaffold
import com.universalpasswordmanager.ui.vault.feature.exportitems.model.AccountSelectionListItem
import com.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword.handlers.VerifyPasswordHandlers
import com.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword.handlers.rememberVerifyPasswordHandler

/**
 * Top level composable for the Verify Password screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun VerifyPasswordScreen(
    onNavigateBack: () -> Unit,
    onPasswordVerified: (userId: String) -> Unit,
    viewModel: VerifyPasswordViewModel,
    credentialExchangeCompletionManager: CredentialExchangeCompletionManager =
        LocalCredentialExchangeCompletionManager.current,
    credentialExchangeRequestValidator: CredentialExchangeRequestValidator =
        LocalCredentialExchangeRequestValidator.current,
    snackbarHostState: UniversalSnackbarHostState = rememberUniversalSnackbarHostState(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val handler = rememberVerifyPasswordHandler(viewModel)

    EventsEffect(viewModel) { event ->
        when (event) {
            VerifyPasswordEvent.NavigateBack -> onNavigateBack()
            VerifyPasswordEvent.CancelExport -> {
                credentialExchangeCompletionManager
                    .completeCredentialExport(
                        exportResult = ExportCredentialsResult.Failure(
                            error = ImportCredentialsCancellationException(
                                errorMessage = "User cancelled import.",
                            ),
                        ),
                    )
            }

            is VerifyPasswordEvent.ValidateImportRequest -> {
                viewModel.trySendAction(
                    VerifyPasswordAction.ValidateImportRequestResultReceive(
                        isValid = credentialExchangeRequestValidator.validate(
                            importCredentialsRequestData = event.importCredentialsRequestData,
                        ),
                    ),
                )
            }

            is VerifyPasswordEvent.PasswordVerified -> {
                onPasswordVerified(event.userId)
            }

            is VerifyPasswordEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.data)
            }
        }
    }

    VerifyPasswordDialogs(
        dialog = state.dialog,
        onDismiss = handler.onDismissDialog,
    )

    ExportItemsScaffold(
        navIcon = rememberVectorPainter(
            id = if (state.hasOtherAccounts) {
                UniversalDrawable.ic_back
            } else {
                UniversalDrawable.ic_close
            },
        ),
        onNavigationIconClick = handler.onNavigateBackClick,
        navigationIconContentDescription = stringResource(UniversalString.back),
        scrollBehavior = scrollBehavior,
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val viewState = state.viewState) {
            is VerifyPasswordState.ViewState.Content -> {
                VerifyPasswordContent(
                    viewState = viewState,
                    accountSummaryListItem = state.accountSummaryListItem,
                    handler = handler,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is VerifyPasswordState.ViewState.Error -> {
                UniversalErrorContent(
                    message = viewState.message(),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            VerifyPasswordState.ViewState.Loading -> {
                UniversalLoadingContent(
                    text = stringResource(id = UniversalString.loading),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun VerifyPasswordDialogs(
    dialog: VerifyPasswordState.DialogState?,
    onDismiss: () -> Unit,
) {
    when (dialog) {
        is VerifyPasswordState.DialogState.General -> {
            UniversalBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                throwable = dialog.error,
                onDismissRequest = onDismiss,
            )
        }

        is VerifyPasswordState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }
}

@Suppress("LongMethod")
@Composable
private fun VerifyPasswordContent(
    viewState: VerifyPasswordState.ViewState.Content,
    accountSummaryListItem: AccountSelectionListItem,
    handler: VerifyPasswordHandlers,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = viewState.title(),
            textAlign = TextAlign.Center,
            style = UniversalTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        viewState.subtext?.let { subtext ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = subtext(),
                textAlign = TextAlign.Center,
                style = UniversalTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(Modifier.height(16.dp))

        AccountSummaryListItem(
            item = accountSummaryListItem,
            cardStyle = CardStyle.Full,
            clickable = false,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(Modifier.height(16.dp))

        if (viewState.showResendCodeButton) {
            UniversalPasswordField(
                label = stringResource(id = UniversalString.verification_code),
                value = viewState.input,
                onValueChange = handler.onInputChanged,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (viewState.isContinueButtonEnabled) {
                            handler.onContinueClick()
                        } else {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    },
                ),
                autoFocus = true,
                cardStyle = CardStyle.Full,
                passwordFieldTestTag = "VerificationCodeEntry",
                showPasswordTestTag = "VerificationCodeVisibilityToggle",
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        } else {
            UniversalPasswordField(
                label = stringResource(UniversalString.master_password),
                value = viewState.input,
                onValueChange = handler.onInputChanged,
                showPasswordTestTag = "PasswordVisibilityToggle",
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (viewState.isContinueButtonEnabled) {
                            handler.onContinueClick()
                        } else {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    },
                ),
                autoFocus = true,
                supportingText = stringResource(UniversalString.vault_locked_master_password),
                passwordFieldTestTag = "MasterPasswordEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(Modifier.height(16.dp))

        UniversalFilledButton(
            label = stringResource(UniversalString.continue_text),
            onClick = handler.onContinueClick,
            isEnabled = viewState.isContinueButtonEnabled,
            modifier = Modifier
                .testTag("ContinueImportButton")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        if (viewState.showResendCodeButton) {
            UniversalOutlinedButton(
                label = stringResource(UniversalString.resend_code),
                onClick = handler.onSendCodeClick,
                modifier = Modifier
                    .testTag("ResendTOTPCodeButton")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(Modifier.height(12.dp))
        Spacer(Modifier.navigationBarsPadding())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun VerifyPasswordContent_MasterPassword_preview() {
    ExportItemsScaffold(
        navIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
        onNavigationIconClick = {},
        navigationIconContentDescription = stringResource(UniversalString.back),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        modifier = Modifier.fillMaxSize(),
    ) {
        VerifyPasswordContent(
            viewState = VerifyPasswordState.ViewState.Content(
                title = UniversalString.verify_your_master_password.asText(),
                subtext = null,
                showResendCodeButton = false,
            ),
            accountSummaryListItem = AccountSelectionListItem(
                userId = "userId",
                isItemRestricted = false,
                avatarColorHex = "#FF0000",
                initials = "JD",
                email = "john.doe@example.com",
            ),
            handler = VerifyPasswordHandlers.createEmpty(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun VerifyPasswordContent_Otp_preview() {
    ExportItemsScaffold(
        navIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
        onNavigationIconClick = {},
        navigationIconContentDescription = stringResource(UniversalString.back),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        modifier = Modifier.fillMaxSize(),
    ) {
        VerifyPasswordContent(
            viewState = VerifyPasswordState.ViewState.Content(
                title = UniversalString.verify_your_account_email_address.asText(),
                subtext = UniversalString
                    .enter_the_6_digit_code_that_was_emailed_to_the_address_below
                    .asText(),
                showResendCodeButton = true,
            ),
            accountSummaryListItem = AccountSelectionListItem(
                userId = "userId",
                isItemRestricted = false,
                avatarColorHex = "#FF0000",
                initials = "JD",
                email = "john.doe@example.com",
            ),
            handler = VerifyPasswordHandlers.createEmpty(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
