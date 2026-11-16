package com.x8bit.universalpasswordmanager.ui.vault.feature.manualcodeentry

import android.Manifest
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.util.startAppSettingsActivity
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager

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
                id = Universal Password ManagerString.enable_camer_permission_to_use_the_scanner,
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
        state = state.dialog,
        onDismissRequest = { viewModel.trySendAction(ManualCodeEntryAction.DialogDismiss) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.authenticator_key_scanner),
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(ManualCodeEntryAction.CloseClick)
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(height = 24.dp))
            Text(
                text = stringResource(id = Universal Password ManagerString.enter_key_manually),
                style = Universal Password ManagerTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth()
                    .testTag("EnterKeyManuallyButton"),
            )

            Spacer(modifier = Modifier.height(height = 12.dp))
            Text(
                text = stringResource(id = Universal Password ManagerString.once_the_key_is_successfully_entered),
                style = Universal Password ManagerTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 24.dp))
            Universal Password ManagerTextField(
                singleLine = false,
                label = stringResource(id = Universal Password ManagerString.authenticator_key_scanner),
                value = state.code,
                onValueChange = {
                    viewModel.trySendAction(ManualCodeEntryAction.CodeTextChange(it))
                },
                textFieldTestTag = "AddManualTOTPField",
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 24.dp))
            Universal Password ManagerFilledButton(
                label = stringResource(id = Universal Password ManagerString.add_totp),
                onClick = { viewModel.trySendAction(ManualCodeEntryAction.CodeSubmit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag("AddManualTOTPButton"),
            )

            Spacer(modifier = Modifier.height(height = 24.dp))
            Universal Password ManagerHyperTextLink(
                annotatedResId = Universal Password ManagerString.cannot_add_authenticator_key_scan_qr_code,
                annotationKey = "scanQrCode",
                accessibilityString = stringResource(id = Universal Password ManagerString.scan_qr_code),
                onClick = {
                    if (permissionsManager.checkPermission(Manifest.permission.CAMERA)) {
                        viewModel.trySendAction(ManualCodeEntryAction.ScanQrCodeTextClick)
                    } else {
                        launcher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun ManualCodeEntryDialogs(
    state: ManualCodeEntryState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (state) {
        is ManualCodeEntryState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = state.title?.invoke(),
                message = state.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}
