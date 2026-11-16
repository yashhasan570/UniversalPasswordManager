package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.exportvault

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.card.Universal Password ManagerInfoCalloutCard
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.auth.feature.completeregistration.PasswordStrengthIndicator
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.exportvault.model.ExportVaultFormat
import com.x8bit.universalpasswordmanager.ui.platform.util.displayLabel
import kotlinx.collections.immutable.toImmutableList

/**
 * The top level composable for the Export Vault screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun ExportVaultScreen(
    onNavigateBack: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: ExportVaultViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val exportVaultLocationReceived: (Uri) -> Unit = remember {
        { viewModel.trySendAction(ExportVaultAction.ExportLocationReceive(it)) }
    }
    val fileSaverLauncher = intentManager.getActivityResultLauncher { activityResult ->
        intentManager.getFileDataFromActivityResult(activityResult)?.let {
            exportVaultLocationReceived.invoke(it.uri)
        }
    }
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            ExportVaultEvent.NavigateBack -> onNavigateBack()
            is ExportVaultEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            is ExportVaultEvent.NavigateToSelectExportDataLocation -> {
                fileSaverLauncher.launch(
                    intentManager.createDocumentIntent(
                        fileName = event.fileName,
                    ),
                )
            }
        }
    }

    var shouldShowConfirmationDialog by remember { mutableStateOf(false) }
    val confirmExportVaultClicked = {
        viewModel.trySendAction(ExportVaultAction.ConfirmExportVaultClicked)
    }
    if (shouldShowConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.export_vault_confirmation_title),
            message = if (state.exportFormat == ExportVaultFormat.JSON_ENCRYPTED) {
                stringResource(id = Universal Password ManagerString.export_vault_file_pw_protect_info)
            } else {
                stringResource(
                    id = Universal Password ManagerString.export_vault_warning,
                )
            },
            confirmButtonText = stringResource(id = Universal Password ManagerString.export_vault),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                shouldShowConfirmationDialog = false
                confirmExportVaultClicked()
            },
            onDismissClick = { shouldShowConfirmationDialog = false },
            onDismissRequest = { shouldShowConfirmationDialog = false },
        )
    }

    when (val dialog = state.dialogState) {
        is ExportVaultState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                throwable = dialog.error,
                onDismissRequest = { viewModel.trySendAction(ExportVaultAction.DialogDismiss) },
            )
        }

        is ExportVaultState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.export_vault),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(ExportVaultAction.CloseButtonClick)
                },
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        ExportVaultScreenContent(
            state = state,
            onConfirmFilePasswordInputChanged = {
                viewModel.trySendAction(ExportVaultAction.ConfirmFilePasswordInputChange(it))
            },
            onExportFormatOptionSelected = {
                viewModel.trySendAction(ExportVaultAction.ExportFormatOptionSelect(it))
            },
            onFilePasswordInputChanged = {
                viewModel.trySendAction(ExportVaultAction.FilePasswordInputChange(it))
            },
            onPasswordInputChanged = {
                viewModel.trySendAction(ExportVaultAction.PasswordInputChanged(it))
            },
            onSendCodeClicked = { viewModel.trySendAction(ExportVaultAction.SendCodeClick) },
            onExportVaultClick = { shouldShowConfirmationDialog = true },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun ExportVaultScreenContent(
    state: ExportVaultState,
    onConfirmFilePasswordInputChanged: (String) -> Unit,
    onExportFormatOptionSelected: (ExportVaultFormat) -> Unit,
    onFilePasswordInputChanged: (String) -> Unit,
    onPasswordInputChanged: (String) -> Unit,
    onSendCodeClicked: () -> Unit,
    onExportVaultClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        if (state.policyPreventsExport) {
            Universal Password ManagerInfoCalloutCard(
                text = stringResource(
                    id = Universal Password ManagerString.disable_personal_vault_export_policy_in_effect,
                ),
                modifier = Modifier
                    .testTag("DisablePrivateVaultPolicyLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        val resources = LocalResources.current
        Universal Password ManagerMultiSelectButton(
            label = stringResource(id = Universal Password ManagerString.file_format),
            options = ExportVaultFormat.entries.map { it.displayLabel() }.toImmutableList(),
            selectedOption = state.exportFormat.displayLabel(),
            onOptionSelected = { selectedOptionLabel ->
                val selectedOption = ExportVaultFormat
                    .entries
                    .first { it.displayLabel(resources) == selectedOptionLabel }
                onExportFormatOptionSelected(selectedOption)
            },
            isEnabled = !state.policyPreventsExport,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .testTag("FileFormatPicker")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        if (state.exportFormat == ExportVaultFormat.JSON_ENCRYPTED) {
            Spacer(modifier = Modifier.height(8.dp))
            var showPassword by rememberSaveable { mutableStateOf(false) }
            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.file_password),
                value = state.filePasswordInput,
                onValueChange = onFilePasswordInputChanged,
                showPassword = showPassword,
                showPasswordChange = { showPassword = it },
                supportingContent = {
                    PasswordStrengthIndicator(
                        state = state.passwordStrengthState,
                        currentCharacterCount = state.passwordInput.length,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = stringResource(id = Universal Password ManagerString.password_used_to_export),
                        style = Universal Password ManagerTheme.typography.bodySmall,
                        color = Universal Password ManagerTheme.colorScheme.text.secondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                showPasswordTestTag = "FilePasswordEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.confirm_file_password),
                value = state.confirmFilePasswordInput,
                onValueChange = onConfirmFilePasswordInputChanged,
                showPassword = showPassword,
                showPasswordChange = { showPassword = it },
                passwordFieldTestTag = "ConfirmFilePasswordEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        if (state.showSendCodeButton) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = Universal Password ManagerString.send_verification_code_to_email),
                textAlign = TextAlign.Start,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Universal Password ManagerOutlinedButton(
                label = stringResource(Universal Password ManagerString.send_code),
                onClick = onSendCodeClicked,
                isEnabled = !state.policyPreventsExport,
                modifier = Modifier
                    .testTag("SendTOTPCodeButton")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.verification_code),
                value = state.passwordInput,
                readOnly = state.policyPreventsExport,
                supportingText = stringResource(id = Universal Password ManagerString.confirm_your_identity),
                onValueChange = onPasswordInputChanged,
                keyboardType = KeyboardType.Number,
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        } else {
            Spacer(modifier = Modifier.height(height = 8.dp))
            Universal Password ManagerPasswordField(
                label = stringResource(id = Universal Password ManagerString.master_password),
                supportingText = stringResource(
                    id = Universal Password ManagerString.export_vault_master_password_description,
                ),
                value = state.passwordInput,
                readOnly = state.policyPreventsExport,
                onValueChange = onPasswordInputChanged,
                passwordFieldTestTag = "MasterPasswordEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.export_vault),
            onClick = onExportVaultClick,
            isEnabled = !state.policyPreventsExport,
            modifier = Modifier
                .testTag("ExportVaultButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
    }
}
