package .authenticator.ui.authenticator.feature.manualcodeentry

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .authenticator.ui.platform.composition.LocalPermissionsManager
import .authenticator.ui.platform.manager.permissions.PermissionsManager
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerPasswordField
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.util.startAppSettingsActivity
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * The screen to manually add a totp code.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualCodeEntryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToQrCodeScreen: () -> Unit,
    viewModel: ManualCodeEntryViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
    permissionsManager: PermissionsManager = LocalPermissionsManager.current,
) {
    var shouldShowPermissionDialog by rememberSaveable { mutableStateOf(false) }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    val launcher = permissionsManager.getLauncher { isGranted ->
        if (isGranted) {
            viewModel.trySendAction(ManualCodeEntryAction.ScanQrCodeTextClick)
        } else {
            shouldShowPermissionDialog = true
        }
    }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is ManualCodeEntryEvent.NavigateToAppSettings -> {
                intentManager.startAppSettingsActivity()
            }

            is ManualCodeEntryEvent.NavigateToQrCodeScreen -> {
                onNavigateToQrCodeScreen.invoke()
            }

            is ManualCodeEntryEvent.NavigateBack -> {
                onNavigateBack.invoke()
            }
        }
    }

    if (shouldShowPermissionDialog) {
        Universal Password ManagerTwoButtonDialog(
            message = stringResource(
                id = Universal Password ManagerString.enable_camera_permission_to_use_the_scanner,
            ),
            confirmButtonText = stringResource(id = Universal Password ManagerString.settings),
            dismissButtonText = stringResource(id = Universal Password ManagerString.no_thanks),
            onConfirmClick = { viewModel.trySendAction(ManualCodeEntryAction.SettingsClick) },
            onDismissClick = { shouldShowPermissionDialog = false },
            onDismissRequest = { shouldShowPermissionDialog = false },
            title = null,
        )
    }

    ManualCodeEntryDialogs(
        dialog = state.dialog,
        onDismissRequest = remember(state) {
            { viewModel.trySendAction(ManualCodeEntryAction.DismissDialog) }
        },
    )

    Universal Password ManagerScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.create_verification_code),
                navigationIcon = painterResource(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(ManualCodeEntryAction.CloseClick)
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
            )
        },
    ) {
        ManualCodeEntryContent(
            state = state,
            onNameChange = { viewModel.trySendAction(ManualCodeEntryAction.IssuerTextChange(it)) },
            onKeyChange = { viewModel.trySendAction(ManualCodeEntryAction.CodeTextChange(it)) },
            onSaveLocallyClick = {
                viewModel.trySendAction(ManualCodeEntryAction.SaveLocallyClick)
            },
            onSaveToUniversal Password ManagerClick = {
                viewModel.trySendAction(ManualCodeEntryAction.SaveToUniversal Password ManagerClick)
            },
            onScanQrCodeClick = {
                if (permissionsManager.checkPermission(Manifest.permission.CAMERA)) {
                    viewModel.trySendAction(ManualCodeEntryAction.ScanQrCodeTextClick)
                } else {
                    launcher.launch(Manifest.permission.CAMERA)
                }
            },
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun ManualCodeEntryContent(
    state: ManualCodeEntryState,
    onNameChange: (name: String) -> Unit,
    onKeyChange: (key: String) -> Unit,
    onSaveLocallyClick: () -> Unit,
    onSaveToUniversal Password ManagerClick: () -> Unit,
    onScanQrCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(state = rememberScrollState())) {
        Spacer(modifier = Modifier.height(height = 24.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.enter_key_manually),
            style = Universal Password ManagerTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.name),
            value = state.issuer,
            onValueChange = onNameChange,
            cardStyle = CardStyle.Top(),
            textFieldTestTag = "NameTextField",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Universal Password ManagerPasswordField(
            singleLine = false,
            label = stringResource(id = Universal Password ManagerString.key),
            value = state.code,
            onValueChange = onKeyChange,
            cardStyle = CardStyle.Bottom,
            passwordFieldTestTag = "KeyTextField",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))
        SaveManualCodeButtons(
            state = state.buttonState,
            onSaveLocallyClick = onSaveLocallyClick,
            onSaveToUniversal Password ManagerClick = onSaveToUniversal Password ManagerClick,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.cannot_add_authenticator_key),
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Universal Password ManagerTextButton(
            label = stringResource(id = Universal Password ManagerString.scan_qr_code),
            onClick = onScanQrCodeClick,
            modifier = Modifier
                .wrapContentWidth()
                .align(alignment = Alignment.CenterHorizontally)
                .standardHorizontalMargin()
                .testTag("ScanQRCodeButton"),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun ManualCodeEntryDialogs(
    dialog: ManualCodeEntryState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialog) {
        is ManualCodeEntryState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        is ManualCodeEntryState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }
}
