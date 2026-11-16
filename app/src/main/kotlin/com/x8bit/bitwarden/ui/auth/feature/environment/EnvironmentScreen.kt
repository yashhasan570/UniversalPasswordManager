package com.x8bit.universalpasswordmanager.ui.auth.feature.environment

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerClientCertificateDialog
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalKeyChainManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.keychain.KeyChainManager
import kotlinx.coroutines.launch

/**
 * Displays the about self-hosted/custom environment screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentScreen(
    onNavigateBack: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    keyChainManager: KeyChainManager = LocalKeyChainManager.current,
    viewModel: EnvironmentViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val certificateImportFilePickerLauncher = intentManager.getActivityResultLauncher { result ->
        intentManager.getFileDataFromActivityResult(result)?.let {
            viewModel.trySendAction(EnvironmentAction.ImportCertificateFilePickerResultReceive(it))
        }
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is EnvironmentEvent.NavigateBack -> onNavigateBack.invoke()
            is EnvironmentEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            is EnvironmentEvent.ShowCertificateImportFileChooser -> {
                certificateImportFilePickerLauncher.launch(
                    intentManager.createFileChooserIntent(withCameraIntents = false),
                )
            }

            is EnvironmentEvent.ShowSystemCertificateSelectionDialog -> {
                scope.launch {
                    val result = keyChainManager.choosePrivateKeyAlias(
                        currentServerUrl = event.serverUrl?.takeUnless { it.isEmpty() },
                    )
                    viewModel.trySendAction(
                        action = EnvironmentAction.SystemCertificateSelectionResultReceive(result),
                    )
                }
            }
        }
    }

    when (val dialog = state.dialog) {
        is EnvironmentState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.an_error_has_occurred),
                message = dialog.message(),
                onDismissRequest = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
                throwable = dialog.throwable,
            )
        }

        is EnvironmentState.DialogState.SetCertificateData -> {
            Universal Password ManagerClientCertificateDialog(
                onConfirmClick = { alias, password ->
                    viewModel.trySendAction(
                        EnvironmentAction.SetCertificateInfoResultReceive(
                            certificateFileData = dialog.certificateBytes,
                            password = password,
                            alias = alias,
                        ),
                    )
                },
                onDismissRequest = {
                    viewModel.trySendAction(EnvironmentAction.SetCertificatePasswordDialogDismiss)
                },
            )
        }

        is EnvironmentState.DialogState.SystemCertificateWarningDialog -> {
            @Suppress("MaxLineLength")
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.warning),
                message = stringResource(
                    Universal Password ManagerString.system_certificates_are_not_as_secure_as_importing_certificates_to_universalpasswordmanager,
                ),
                confirmButtonText = stringResource(Universal Password ManagerString.continue_text),
                onConfirmClick = {
                    viewModel.trySendAction(EnvironmentAction.ConfirmChooseSystemCertificateClick)
                },
                dismissButtonText = stringResource(Universal Password ManagerString.cancel),
                onDismissClick = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
                onDismissRequest = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
            )
        }

        is EnvironmentState.DialogState.ConfirmOverwriteAlias -> {
            Universal Password ManagerTwoButtonDialog(
                title = dialog.title(),
                message = dialog.message(),
                confirmButtonText = stringResource(Universal Password ManagerString.replace_certificate),
                dismissButtonText = stringResource(Universal Password ManagerString.cancel),
                onConfirmClick = {
                    viewModel.trySendAction(
                        EnvironmentAction.ConfirmOverwriteCertificateClick(
                            triggeringAction = dialog.triggeringAction,
                        ),
                    )
                },
                onDismissClick = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
                onDismissRequest = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
            )
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
                title = stringResource(id = Universal Password ManagerString.settings),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = { viewModel.trySendAction(EnvironmentAction.CloseClick) },
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.save),
                        onClick = { viewModel.trySendAction(EnvironmentAction.SaveClick) },
                        modifier = Modifier.testTag("SaveButton"),
                    )
                },
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.self_hosted_environment),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.server_url),
                value = state.serverUrl,
                placeholder = "ex. https://pany.com",
                supportingText = stringResource(
                    id = Universal Password ManagerString.self_hosted_environment_footer,
                ),
                onValueChange = { viewModel.trySendAction(EnvironmentAction.ServerUrlChange(it)) },
                autoCompleteOptions = state.autocompleteEnvironments,
                keyboardType = KeyboardType.Uri,
                textFieldTestTag = "ServerUrlEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.custom_environment),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.web_vault_url),
                value = state.webVaultServerUrl,
                onValueChange = {
                    viewModel.trySendAction(EnvironmentAction.WebVaultServerUrlChange(it))
                },
                keyboardType = KeyboardType.Uri,
                textFieldTestTag = "WebVaultUrlEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.api_url),
                value = state.apiServerUrl,
                onValueChange = {
                    viewModel.trySendAction(EnvironmentAction.ApiServerUrlChange(it))
                },
                keyboardType = KeyboardType.Uri,
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ApiUrlEntry")
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.identity_url),
                value = state.identityServerUrl,
                onValueChange = {
                    viewModel.trySendAction(EnvironmentAction.IdentityServerUrlChange(it))
                },
                keyboardType = KeyboardType.Uri,
                textFieldTestTag = "IdentityUrlEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.icons_url),
                value = state.iconsServerUrl,
                onValueChange = {
                    viewModel.trySendAction(EnvironmentAction.IconsServerUrlChange(it))
                },
                supportingText = stringResource(id = Universal Password ManagerString.custom_environment_footer),
                keyboardType = KeyboardType.Uri,
                textFieldTestTag = "IconsUrlEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            Universal Password ManagerListHeaderText(
                label = stringResource(id = Universal Password ManagerString.client_certificate_mtls),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))

            Universal Password ManagerTextField(
                label = stringResource(id = Universal Password ManagerString.certificate_alias),
                value = state.keyAlias,
                supportingText = stringResource(
                    id = Universal Password ManagerString.certificate_used_for_client_authentication,
                ),
                onValueChange = {},
                readOnly = true,
                cardStyle = CardStyle.Full,
                textFieldTestTag = "KeyAliasEntry",
                modifier = Modifier
                    .fillMaxWidth()
                    .focusProperties { canFocus = false }
                    .standardHorizontalMargin(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))

            Universal Password ManagerFilledButton(
                label = stringResource(id = Universal Password ManagerString.import_certificate),
                onClick = { viewModel.trySendAction(EnvironmentAction.ImportCertificateClick) },
                isExternalLink = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("ImportCertificateButton"),
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            Universal Password ManagerOutlinedButton(
                label = stringResource(id = Universal Password ManagerString.choose_system_certificate),
                onClick = {
                    viewModel.trySendAction(EnvironmentAction.ChooseSystemCertificateClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("ChooseSystemCertificateButton"),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
