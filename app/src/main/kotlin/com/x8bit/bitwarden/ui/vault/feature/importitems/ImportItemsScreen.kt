package com.x8bit.universalpasswordmanager.ui.vault.feature.importitems

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .cxf.importer.CredentialExchangeImporter
import .cxf.ui.composition.LocalCredentialExchangeImporter
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.model.CardStyle
import .ui.platform.components.row.Universal Password ManagerTextRow
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarHostState
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.feature.importitems.ImportItemsAction.ImportCredentialSelectionReceive
import com.x8bit.universalpasswordmanager.ui.vault.feature.importitems.handlers.rememberImportItemsHandler
import kotlinx.coroutines.launch

/**
 * Top level component for the import items screen.
 */
@Suppress("LongMethod")
@Composable
fun ImportItemsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToImportFromComputer: () -> Unit,
    viewModel: ImportItemsViewModel = hiltViewModel(),
    credentialExchangeImporter: CredentialExchangeImporter =
        LocalCredentialExchangeImporter.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val handler = rememberImportItemsHandler(viewModel = viewModel)
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()

    EventsEffect(viewModel) { event ->
        when (event) {
            ImportItemsEvent.NavigateBack -> onNavigateBack()
            ImportItemsEvent.NavigateToImportFromComputer -> onNavigateToImportFromComputer()
            is ImportItemsEvent.ShowRegisteredImportSources -> {
                coroutineScope.launch {
                    viewModel.trySendAction(
                        action = ImportCredentialSelectionReceive(
                            selectionResult = credentialExchangeImporter
                                .importCredentials(
                                    credentialTypes = event.credentialTypes,
                                ),
                        ),
                    )
                }
            }

            is ImportItemsEvent.ShowBasicSnackbar -> {
                snackbarHostState.showSnackbar(event.data)
            }

            is ImportItemsEvent.ShowSyncFailedSnackbar -> {
                snackbarHostState.showSnackbar(
                    snackbarData = event.data,
                    onActionPerformed = handler.onSyncFailedTryAgainClick,
                )
            }
        }
    }

    ImportItemsDialogs(
        dialog = state.dialog,
        onDismissDialog = handler.onDismissDialog,
    )

    ImportItemsScaffold(
        onNavigateBack = handler.onNavigateBack,
        onImportFromComputerClick = handler.onImportFromComputerClick,
        onImportFromAnotherAppClick = handler.onImportFromAnotherAppClick,
        snackbarHostState = snackbarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImportItemsScaffold(
    onNavigateBack: () -> Unit,
    onImportFromComputerClick: () -> Unit,
    onImportFromAnotherAppClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: Universal Password ManagerSnackbarHostState = rememberUniversal Password ManagerSnackbarHostState(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(Universal Password ManagerString.import_items),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(Universal Password ManagerDrawable.ic_back),
                    onNavigationIconClick = onNavigateBack,
                    navigationIconContentDescription = stringResource(Universal Password ManagerString.back),
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(
                universalpasswordmanagerHostState = snackbarHostState,
            )
        },
    ) {
        ImportItemsContent(
            onImportFromComputerClick = onImportFromComputerClick,
            onImportFromAnotherAppClick = onImportFromAnotherAppClick,
            modifier = Modifier
                .fillMaxSize()
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun ImportItemsContent(
    onImportFromComputerClick: () -> Unit,
    onImportFromAnotherAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item { Spacer(Modifier.height(12.dp)) }

        item {
            Universal Password ManagerTextRow(
                text = stringResource(Universal Password ManagerString.import_from_computer),
                onClick = onImportFromComputerClick,
                cardStyle = CardStyle.Top(),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Universal Password ManagerTextRow(
                text = stringResource(Universal Password ManagerString.import_from_another_app),
                onClick = onImportFromAnotherAppClick,
                isExternalLink = true,
                cardStyle = CardStyle.Bottom,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item { Spacer(Modifier.height(16.dp)) }
        item { Spacer(Modifier.navigationBarsPadding()) }
    }
}

@Composable
private fun ImportItemsDialogs(
    dialog: ImportItemsState.DialogState?,
    onDismissDialog: () -> Unit,
) {
    when (dialog) {
        is ImportItemsState.DialogState.General -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                onDismissRequest = onDismissDialog,
                throwable = dialog.throwable,
            )
        }

        is ImportItemsState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }
}

//region Previews

@Preview(showBackground = true, name = "Initial state")
@Composable
private fun ImportItemsContent_preview() {
    Universal Password ManagerTheme {
        ImportItemsScaffold(
            onNavigateBack = {},
            onImportFromComputerClick = {},
            onImportFromAnotherAppClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading dialog")
@Composable
private fun ImportItemsDialogs_loading_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            ImportItemsDialogs(
                dialog = ImportItemsState.DialogState.Loading("Decoding items...".asText()),
                onDismissDialog = {},
            )
        }
    }
}
//endregion Previews
