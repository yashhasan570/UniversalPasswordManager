package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.folders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.fab.Universal Password ManagerFloatingActionButton
import .ui.platform.components.row.Universal Password ManagerTextRow
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.folders.model.FolderDisplayItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Displays the folders screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddFolderScreen: () -> Unit,
    onNavigateToEditFolderScreen: (folderId: String) -> Unit,
    viewModel: FoldersViewModel = hiltViewModel(),
) {
    val state = viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is FoldersEvent.NavigateBack -> onNavigateBack()
            is FoldersEvent.NavigateToAddFolderScreen -> onNavigateToAddFolderScreen()
            is FoldersEvent.NavigateToEditFolderScreen -> {
                onNavigateToEditFolderScreen(event.folderId)
            }

            is FoldersEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.my_folders),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = { viewModel.trySendAction(FoldersAction.CloseButtonClick) },
            )
        },
        floatingActionButton = {
            Universal Password ManagerFloatingActionButton(
                onClick = { viewModel.trySendAction(FoldersAction.AddFolderButtonClick) },
                painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_plus_large),
                contentDescription = stringResource(id = Universal Password ManagerString.add_item),
                modifier = Modifier
                    .testTag(tag = "AddItemButton")
                    .navigationBarsPadding(),
            )
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
    ) {
        when (val viewState = state.value.viewState) {
            is FoldersState.ViewState.Content -> {
                FoldersContent(
                    foldersList = viewState.folderList.toImmutableList(),
                    onItemClick = { viewModel.trySendAction(FoldersAction.FolderClick(it)) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is FoldersState.ViewState.Error -> {
                Universal Password ManagerErrorContent(
                    message = viewState.message(),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is FoldersState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun FoldersContent(
    foldersList: ImmutableList<FolderDisplayItem>,
    onItemClick: (folderId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (foldersList.isEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = Universal Password ManagerString.no_folders_to_list),
                textAlign = TextAlign.Center,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                modifier = Modifier.testTag("NoFoldersLabel"),
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
        ) {
            item {
                Spacer(modifier = Modifier.height(height = 12.dp))
            }
            itemsIndexed(foldersList) { index, it ->
                Universal Password ManagerTextRow(
                    text = it.name,
                    onClick = { onItemClick(it.id) },
                    textTestTag = "FolderName",
                    cardStyle = foldersList.toListItemCardStyle(index = index),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .testTag(tag = "FolderCell"),
                )
            }
            item {
                Spacer(modifier = Modifier.height(height = 88.dp))
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}
