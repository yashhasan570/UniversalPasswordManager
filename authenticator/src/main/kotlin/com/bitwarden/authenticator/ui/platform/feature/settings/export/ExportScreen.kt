package .authenticator.ui.platform.feature.settings.export

import android.net.Uri
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .authenticator.ui.platform.feature.settings.export.model.ExportVaultFormat
import .authenticator.ui.platform.util.displayLabel
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.toImmutableList

/**
 * Top level composable for the export data screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    viewModel: ExportViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val exportLocationReceive: (Uri) -> Unit = {
        viewModel.trySendAction(ExportAction.ExportLocationReceive(it))
    }
    val fileSaveLauncher = intentManager.getActivityResultLauncher { activityResult ->
        intentManager.getFileDataFromActivityResult(activityResult)?.let {
            exportLocationReceive.invoke(it.uri)
        }
    }
    val snackbarState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            ExportEvent.NavigateBack -> onNavigateBack()
            is ExportEvent.ShowSnackBar -> {
                snackbarState.showSnackbar(Universal Password ManagerSnackbarData(message = event.message))
            }

            is ExportEvent.NavigateToSelectExportDestination -> {
                fileSaveLauncher.launch(
                    intentManager.createDocumentIntent(
                        fileName = event.fileName,
                    ),
                )
            }
        }
    }

    var shouldShowConfirmationPrompt by remember { mutableStateOf(false) }
    val confirmExportClick = { viewModel.trySendAction(ExportAction.ConfirmExportClick) }
    if (shouldShowConfirmationPrompt) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.export_confirmation_title),
            message = stringResource(
                id = Universal Password ManagerString.export_vault_warning,
            ),
            confirmButtonText = stringResource(id = Universal Password ManagerString.export),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                shouldShowConfirmationPrompt = false
                confirmExportClick()
            },
            onDismissClick = { shouldShowConfirmationPrompt = false },
            onDismissRequest = { shouldShowConfirmationPrompt = false },
        )
    }

    when (val dialog = state.dialogState) {
        is ExportState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title?.invoke(),
                message = dialog.message(),
                onDismissRequest = { viewModel.trySendAction(ExportAction.DialogDismiss) },
            )
        }

        is ExportState.DialogState.Loading -> {
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
                title = stringResource(id = Universal Password ManagerString.export),
                scrollBehavior = scrollBehavior,
                navigationIcon = painterResource(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = { viewModel.trySendAction(ExportAction.CloseButtonClick) },
            )
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(snackbarState) },
    ) {
        ExportScreenContent(
            modifier = Modifier.fillMaxSize(),
            state = state,
            onExportFormatOptionSelected = {
                viewModel.trySendAction(ExportAction.ExportFormatOptionSelect(it))
            },
            onExportClick = { shouldShowConfirmationPrompt = true },
        )
    }
}

@Composable
private fun ExportScreenContent(
    modifier: Modifier = Modifier,
    state: ExportState,
    onExportFormatOptionSelected: (ExportVaultFormat) -> Unit,
    onExportClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        val resources = LocalResources.current
        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.included_in_this_export),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = stringResource(
                id = Universal Password ManagerString.only_codes_stored_locally_on_this_device_will_be_exported,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerMultiSelectButton(
            label = stringResource(id = Universal Password ManagerString.file_format),
            options = ExportVaultFormat.entries.map { it.displayLabel() }.toImmutableList(),
            selectedOption = state.exportVaultFormat.displayLabel(),
            onOptionSelected = { selectedOptionLabel ->
                val selectedOption = ExportVaultFormat
                    .entries
                    .first { it.displayLabel(resources) == selectedOptionLabel }
                onExportFormatOptionSelected(selectedOption)
            },
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .testTag("FileFormatPicker")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.export),
            onClick = onExportClick,
            isExternalLink = true,
            modifier = Modifier
                .testTag("ExportVaultButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
