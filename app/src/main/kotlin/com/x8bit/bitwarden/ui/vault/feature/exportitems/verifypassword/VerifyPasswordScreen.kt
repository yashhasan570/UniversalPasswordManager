package com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword

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
import .cxf.manager.CredentialExchangeCompletionManager
import .cxf.manager.model.ExportCredentialsResult
import .cxf.ui.composition.LocalCredentialExchangeCompletionManager
import .cxf.ui.composition.LocalCredentialExchangeRequestValidator
import .cxf.validator.CredentialExchangeRequestValidator
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarHostState
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.component.AccountSummaryListItem
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.component.ExportItemsScaffold
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.model.AccountSelectionListItem
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword.handlers.VerifyPasswordHandlers
import com.x8bit.universalpasswordmanager.ui.vault.feature.exportitems.verifypassword.handlers.rememberVerifyPasswordHandler

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
    snackbarHostState: Universal Password ManagerSnackbarHostState = rememberUniversal Password ManagerSnackbarHostState(),
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
                Universal Password ManagerDrawable.ic_back
            } else {
                Universal Password ManagerDrawable.ic_close
            },
        ),
        onNavigationIconClick = handler.onNavigateBackClick,
        navigationIconContentDescription = stringResource(Universal Password ManagerString.back),
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
                Universal Password ManagerErrorContent(
                    message = viewState.message(),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            VerifyPasswordState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(
                    text = stringResource(id = Universal Password ManagerString.loading),
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
            Universal Password ManagerBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                throwable = dialog.error,
                onDismissRequest = onDismiss,
            )
        }

        is VerifyPasswordState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
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
            style = Universal Password ManagerTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        viewState.subtext?.let { subtext ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = subtext(),
                textAlign = TextAlign.Center,
                style = Universal Password ManagerTheme.typography.bodyMedium,
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
            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.verification_code),
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
            Universal Password ManagerPasswordField(
                label = stringResource(Universal Password ManagerString.master_password),
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
                supportingText = stringResource(Universal Password ManagerString.vault_locked_master_password),
                passwordFieldTestTag = "MasterPasswordEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        Spacer(Modifier.height(16.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(Universal Password ManagerString.continue_text),
            onClick = handler.onContinueClick,
            isEnabled = viewState.isContinueButtonEnabled,
            modifier = Modifier
                .testTag("ContinueImportButton")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        if (viewState.showResendCodeButton) {
            Universal Password ManagerOutlinedButton(
                label = stringResource(Universal Password ManagerString.resend_code),
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
        navIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
        onNavigationIconClick = {},
        navigationIconContentDescription = stringResource(Universal Password ManagerString.back),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        modifier = Modifier.fillMaxSize(),
    ) {
        VerifyPasswordContent(
            viewState = VerifyPasswordState.ViewState.Content(
                title = Universal Password ManagerString.verify_your_master_password.asText(),
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
        navIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
        onNavigationIconClick = {},
        navigationIconContentDescription = stringResource(Universal Password ManagerString.back),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        modifier = Modifier.fillMaxSize(),
    ) {
        VerifyPasswordContent(
            viewState = VerifyPasswordState.ViewState.Content(
                title = Universal Password ManagerString.verify_your_account_email_address.asText(),
                subtext = Universal Password ManagerString
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
