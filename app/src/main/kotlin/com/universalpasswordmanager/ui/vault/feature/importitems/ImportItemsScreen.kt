package com.universalpasswordmanager.ui.vault.feature.importitems

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
import com.universalpasswordmanager.cxf.importer.CredentialExchangeImporter
import com.universalpasswordmanager.cxf.ui.composition.LocalCredentialExchangeImporter
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.row.UniversalTextRow
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.feature.importitems.ImportItemsAction.ImportCredentialSelectionReceive
import com.universalpasswordmanager.ui.vault.feature.importitems.handlers.rememberImportItemsHandler
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
    val snackbarHostState = rememberUniversalSnackbarHostState()

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
    snackbarHostState: UniversalSnackbarHostState = rememberUniversalSnackbarHostState(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(UniversalString.import_items),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(UniversalDrawable.ic_back),
                    onNavigationIconClick = onNavigateBack,
                    navigationIconContentDescription = stringResource(UniversalString.back),
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(
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
            UniversalTextRow(
                text = stringResource(UniversalString.import_from_computer),
                onClick = onImportFromComputerClick,
                cardStyle = CardStyle.Top(),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            UniversalTextRow(
                text = stringResource(UniversalString.import_from_another_app),
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
            UniversalBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                onDismissRequest = onDismissDialog,
                throwable = dialog.throwable,
            )
        }

        is ImportItemsState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialog.message())
        }

        null -> Unit
    }
}

//region Previews

@Preview(showBackground = true, name = "Initial state")
@Composable
private fun ImportItemsContent_preview() {
    UniversalTheme {
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
    UniversalTheme {
        UniversalScaffold {
            ImportItemsDialogs(
                dialog = ImportItemsState.DialogState.Loading("Decoding items...".asText()),
                onDismissDialog = {},
            )
        }
    }
}
//endregion Previews
