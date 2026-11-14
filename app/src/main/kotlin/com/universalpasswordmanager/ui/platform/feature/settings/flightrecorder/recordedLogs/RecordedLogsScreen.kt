package com.universalpasswordmanager.ui.platform.feature.settings.flightrecorder.recordedLogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toListItemCardStyle
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalOverflowActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.model.OverflowMenuItemData
import com.universalpasswordmanager.ui.platform.components.content.UniversalEmptyContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * Displays the flight recorder recorded logs screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun RecordedLogsScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecordedLogsViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = rememberUniversalSnackbarHostState()
    EventsEffect(viewModel) { event ->
        when (event) {
            RecordedLogsEvent.NavigateBack -> onNavigateBack()
            is RecordedLogsEvent.ShareLog -> {
                intentManager.shareFile(fileUri = event.uri.toUri())
            }

            is RecordedLogsEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    snackbarData = UniversalSnackbarData(message = event.text),
                )
            }
        }
    }

    RecordedLogsDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(RecordedLogsAction.DismissDialog) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.recorded_logs_title),
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = { viewModel.trySendAction(RecordedLogsAction.BackClick) },
                scrollBehavior = scrollBehavior,
                actions = {
                    RecordedLogsOverflowMenu(
                        isOverflowEnabled = state.viewState.isOverflowEnabled,
                        onDeleteAllClick = {
                            viewModel.trySendAction(RecordedLogsAction.DeleteAllClick)
                        },
                        onShareAllClick = {
                            viewModel.trySendAction(RecordedLogsAction.ShareAllClick)
                        },
                    )
                },
            )
        },
        snackbarHost = { UniversalSnackbarHost(bitwardenHostState = snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        when (val viewState = state.viewState) {
            is RecordedLogsState.ViewState.Content -> {
                RecordedLogsContent(
                    viewState = viewState,
                    onShareItemClick = {
                        viewModel.trySendAction(RecordedLogsAction.ShareClick(it))
                    },
                    onDeleteItemClick = {
                        viewModel.trySendAction(RecordedLogsAction.DeleteClick(it))
                    },
                )
            }

            RecordedLogsState.ViewState.Empty -> {
                UniversalEmptyContent(
                    text = stringResource(id = UniversalString.no_logs_recorded),
                    illustrationData = IconData.Local(
                        iconRes = UniversalDrawable.ill_secure_devices,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            RecordedLogsState.ViewState.Loading -> {
                UniversalLoadingContent(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun RecordedLogsOverflowMenu(
    isOverflowEnabled: Boolean,
    onDeleteAllClick: () -> Unit,
    onShareAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeletionDialog by rememberSaveable { mutableStateOf(value = false) }
    if (showDeletionDialog) {
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.delete_logs),
            message = stringResource(
                id = UniversalString.do_you_really_want_to_delete_all_recorded_logs,
            ),
            confirmButtonText = stringResource(id = UniversalString.yes),
            dismissButtonText = stringResource(id = UniversalString.cancel),
            onDismissRequest = { showDeletionDialog = false },
            onDismissClick = { showDeletionDialog = false },
            onConfirmClick = {
                onDeleteAllClick()
                showDeletionDialog = false
            },
        )
    }
    UniversalOverflowActionItem(
        modifier = modifier,
        menuItemDataList = persistentListOf(
            OverflowMenuItemData(
                text = stringResource(id = UniversalString.share_all),
                onClick = onShareAllClick,
                isEnabled = isOverflowEnabled,
            ),
            OverflowMenuItemData(
                text = stringResource(id = UniversalString.delete_all),
                onClick = { showDeletionDialog = true },
                isEnabled = isOverflowEnabled,
                color = UniversalTheme.colorScheme.status.error,
            ),
        ),
    )
}

@Composable
private fun RecordedLogsDialogs(
    dialogState: RecordedLogsState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is RecordedLogsState.DialogState.Error -> {
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Composable
private fun RecordedLogsContent(
    viewState: RecordedLogsState.ViewState.Content,
    onDeleteItemClick: (RecordedLogsState.DisplayItem) -> Unit,
    onShareItemClick: (RecordedLogsState.DisplayItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item(key = "top_spacer") {
            Spacer(modifier = Modifier.height(height = 12.dp))
        }

        itemsIndexed(
            items = viewState.items,
            key = { _, item -> item.toString() },
        ) { index, item ->
            LogRow(
                displayableItem = item,
                cardStyle = viewState.items.toListItemCardStyle(index = index),
                onDeleteItemClick = onDeleteItemClick,
                onShareItemClick = onShareItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun LogRow(
    displayableItem: RecordedLogsState.DisplayItem,
    onDeleteItemClick: (RecordedLogsState.DisplayItem) -> Unit,
    onShareItemClick: (RecordedLogsState.DisplayItem) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    var showDeletionDialog by rememberSaveable { mutableStateOf(value = false) }
    if (showDeletionDialog) {
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.delete_log),
            message = stringResource(id = UniversalString.do_you_really_want_to_delete_this_log),
            confirmButtonText = stringResource(id = UniversalString.yes),
            dismissButtonText = stringResource(id = UniversalString.cancel),
            onDismissRequest = { showDeletionDialog = false },
            onDismissClick = { showDeletionDialog = false },
            onConfirmClick = {
                onDeleteItemClick(displayableItem)
                showDeletionDialog = false
            },
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                paddingTop = 12.dp,
                paddingBottom = 12.dp,
                paddingStart = 16.dp,
                paddingEnd = 4.dp,
            )
            .testTag("LogRow"),
    ) {
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = displayableItem.title(),
                style = UniversalTheme.typography.bodyLarge,
                color = UniversalTheme.colorScheme.text.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("LogNameLabel"),
            )
            Spacer(modifier = Modifier.height(height = 2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = displayableItem.subtextStart(),
                    style = UniversalTheme.typography.bodyMedium,
                    color = UniversalTheme.colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(weight = 1f)
                        .testTag("LogSizeLabel"),
                )
                displayableItem.subtextEnd?.let {
                    Spacer(modifier = Modifier.width(width = 4.dp))
                    Text(
                        text = it(),
                        style = UniversalTheme.typography.bodyMedium,
                        color = UniversalTheme.colorScheme.text.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .weight(weight = 1f)
                            .testTag("LogExpirationLabel"),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(width = 12.dp))
        UniversalOverflowActionItem(
            menuItemDataList = persistentListOf(
                OverflowMenuItemData(
                    text = stringResource(id = UniversalString.share),
                    onClick = { onShareItemClick(displayableItem) },
                    testTag = "LogShareButton",
                ),
                OverflowMenuItemData(
                    text = stringResource(id = UniversalString.delete),
                    onClick = { showDeletionDialog = true },
                    color = UniversalTheme.colorScheme.status.error,
                    isEnabled = displayableItem.isDeletedEnabled,
                    testTag = "LogDeleteButton",
                ),
            ),
            vectorIconRes = UniversalDrawable.ic_ellipsis_horizontal,
            testTag = "Options",
        )
    }
}
