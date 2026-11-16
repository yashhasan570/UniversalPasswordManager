package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.folders.addedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * Displays the screen for adding or editing a folder item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun FolderAddEditScreen(
    onNavigateBack: () -> Unit,
    viewModel: FolderAddEditViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    var shouldShowConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is FolderAddEditEvent.NavigateBack -> onNavigateBack.invoke()
        }
    }

    FolderAddEditItemDialogs(
        dialogState = state.dialog,
        onDismissRequest = { viewModel.trySendAction(FolderAddEditAction.DismissDialog) },
    )

    if (shouldShowConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = null,
            message = stringResource(id = Universal Password ManagerString.do_you_really_want_to_delete),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            confirmButtonText = stringResource(id = Universal Password ManagerString.delete),
            onDismissClick = { shouldShowConfirmationDialog = false },
            onConfirmClick = {
                shouldShowConfirmationDialog = false
                viewModel.trySendAction(FolderAddEditAction.DeleteClick)
            },
            onDismissRequest = { shouldShowConfirmationDialog = false },
            confirmTextColor = Universal Password ManagerTheme.colorScheme.status.error,
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.screenDisplayName.invoke(),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = { viewModel.trySendAction(FolderAddEditAction.CloseClick) },
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.save),
                        onClick = { viewModel.trySendAction(FolderAddEditAction.SaveClick) },
                        modifier = Modifier.testTag("SaveButton"),
                    )
                    Universal Password ManagerOverflowActionItem(
                        isVisible = state.shouldShowOverflowMenu,
                        menuItemDataList = persistentListOf(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.delete),
                                onClick = { shouldShowConfirmationDialog = true },
                            ),
                        ),
                    )
                },
            )
        },
    ) {
        when (val viewState = state.viewState) {
            is FolderAddEditState.ViewState.Content -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.height(height = 12.dp))
                    Universal Password ManagerTextField(
                        label = stringResource(id = Universal Password ManagerString.name),
                        value = viewState.folderName,
                        onValueChange = {
                            viewModel.trySendAction(FolderAddEditAction.NameTextChange(it))
                        },
                        textFieldTestTag = "FolderNameField",
                        cardStyle = CardStyle.Full,
                        modifier = Modifier
                            .standardHorizontalMargin()
                            .fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }

            is FolderAddEditState.ViewState.Error -> {
                Universal Password ManagerErrorContent(
                    message = viewState.message(),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is FolderAddEditState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun FolderAddEditItemDialogs(
    dialogState: FolderAddEditState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is FolderAddEditState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.label())
        }

        is FolderAddEditState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = stringResource(id = Universal Password ManagerString.an_error_has_occurred),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
            throwable = dialogState.throwable,
        )

        null -> Unit
    }
}
