package .authenticator.ui.authenticator.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .authenticator.ui.authenticator.feature.search.handlers.SearchHandlers
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.bottomDivider
import .ui.platform.components.appbar.Universal Password ManagerSearchTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString

/**
 * The search screen for authenticator items.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchScreen(
    viewModel: ItemSearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val searchHandlers = remember(viewModel) { SearchHandlers.create(viewModel) }
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is ItemSearchEvent.NavigateBack -> onNavigateBack()
            is ItemSearchEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            is ItemSearchEvent.NavigateToEditItem -> onNavigateToEdit(event.itemId)
        }
    }

    ItemSearchDialogs(
        dialogState = state.dialog,
        searchHandlers = searchHandlers,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerSearchTopAppBar(
                modifier = Modifier
                    .testTag("SearchFieldEntry")
                    .bottomDivider(),
                searchTerm = state.searchTerm,
                placeholder = stringResource(id = Universal Password ManagerString.search_codes),
                onSearchTermChange = searchHandlers.onSearchTermChange,
                scrollBehavior = scrollBehavior,
                clearIconContentDescription = stringResource(id = Universal Password ManagerString.clear),
                navigationIcon = NavigationIcon(
                    navigationIcon = painterResource(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                    onNavigationIconClick = searchHandlers.onBackClick,
                ),
            )
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
    ) {
        when (val viewState = state.viewState) {
            is ItemSearchState.ViewState.Content -> {
                ItemSearchContent(
                    viewState = viewState,
                    searchHandlers = searchHandlers,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is ItemSearchState.ViewState.Empty -> {
                ItemSearchEmptyContent(
                    viewState = viewState,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ItemSearchDialogs(
    dialogState: ItemSearchState.DialogState?,
    searchHandlers: SearchHandlers,
) {
    when (dialogState) {
        is ItemSearchState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                throwable = dialogState.throwable,
                onDismissRequest = searchHandlers.onDismissDialog,
            )
        }

        is ItemSearchState.DialogState.DeleteConfirmationPrompt -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.delete),
                message = dialogState.message(),
                confirmButtonText = stringResource(id = Universal Password ManagerString.okay),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = { searchHandlers.onConfirmDeleteClick(dialogState.itemId) },
                onDismissClick = searchHandlers.onDismissDialog,
                onDismissRequest = searchHandlers.onDismissDialog,
            )
        }

        ItemSearchState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = stringResource(id = Universal Password ManagerString.loading))
        }

        null -> Unit
    }
}
