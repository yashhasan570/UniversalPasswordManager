package com.universalpasswordmanager.ui.auth.feature.environment

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.field.UniversalTextField
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalClientCertificateDialog
import com.universalpasswordmanager.ui.platform.composition.LocalKeyChainManager
import com.universalpasswordmanager.ui.platform.manager.keychain.KeyChainManager
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
    val snackbarHostState = rememberUniversalSnackbarHostState()
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
            UniversalBasicDialog(
                title = stringResource(id = UniversalString.an_error_has_occurred),
                message = dialog.message(),
                onDismissRequest = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
                throwable = dialog.throwable,
            )
        }

        is EnvironmentState.DialogState.SetCertificateData -> {
            UniversalClientCertificateDialog(
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
            UniversalTwoButtonDialog(
                title = stringResource(UniversalString.warning),
                message = stringResource(
                    UniversalString.system_certificates_are_not_as_secure_as_importing_certificates_to_bitwarden,
                ),
                confirmButtonText = stringResource(UniversalString.continue_text),
                onConfirmClick = {
                    viewModel.trySendAction(EnvironmentAction.ConfirmChooseSystemCertificateClick)
                },
                dismissButtonText = stringResource(UniversalString.cancel),
                onDismissClick = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
                onDismissRequest = { viewModel.trySendAction(EnvironmentAction.DialogDismiss) },
            )
        }

        is EnvironmentState.DialogState.ConfirmOverwriteAlias -> {
            UniversalTwoButtonDialog(
                title = dialog.title(),
                message = dialog.message(),
                confirmButtonText = stringResource(UniversalString.replace_certificate),
                dismissButtonText = stringResource(UniversalString.cancel),
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.settings),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = { viewModel.trySendAction(EnvironmentAction.CloseClick) },
                actions = {
                    UniversalTextButton(
                        label = stringResource(id = UniversalString.save),
                        onClick = { viewModel.trySendAction(EnvironmentAction.SaveClick) },
                        modifier = Modifier.testTag("SaveButton"),
                    )
                },
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(bitwardenHostState = snackbarHostState)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            UniversalListHeaderText(
                label = stringResource(id = UniversalString.self_hosted_environment),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            UniversalTextField(
                label = stringResource(id = UniversalString.server_url),
                value = state.serverUrl,
                placeholder = "ex. https://bitwarden.company.com",
                supportingText = stringResource(
                    id = UniversalString.self_hosted_environment_footer,
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

            UniversalListHeaderText(
                label = stringResource(id = UniversalString.custom_environment),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            UniversalTextField(
                label = stringResource(id = UniversalString.web_vault_url),
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

            UniversalTextField(
                label = stringResource(id = UniversalString.api_url),
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

            UniversalTextField(
                label = stringResource(id = UniversalString.identity_url),
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

            UniversalTextField(
                label = stringResource(id = UniversalString.icons_url),
                value = state.iconsServerUrl,
                onValueChange = {
                    viewModel.trySendAction(EnvironmentAction.IconsServerUrlChange(it))
                },
                supportingText = stringResource(id = UniversalString.custom_environment_footer),
                keyboardType = KeyboardType.Uri,
                textFieldTestTag = "IconsUrlEntry",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            UniversalListHeaderText(
                label = stringResource(id = UniversalString.client_certificate_mtls),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))

            UniversalTextField(
                label = stringResource(id = UniversalString.certificate_alias),
                value = state.keyAlias,
                supportingText = stringResource(
                    id = UniversalString.certificate_used_for_client_authentication,
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

            UniversalFilledButton(
                label = stringResource(id = UniversalString.import_certificate),
                onClick = { viewModel.trySendAction(EnvironmentAction.ImportCertificateClick) },
                isExternalLink = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("ImportCertificateButton"),
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            UniversalOutlinedButton(
                label = stringResource(id = UniversalString.choose_system_certificate),
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
