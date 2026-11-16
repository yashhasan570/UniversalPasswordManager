package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.blockautofill

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.fab.Universal Password ManagerFloatingActionButton
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import kotlinx.collections.immutable.persistentListOf

/**
 * Displays the block autofill screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockAutoFillScreen(
    onNavigateBack: () -> Unit,
    viewModel: BlockAutoFillViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            BlockAutoFillEvent.NavigateBack -> onNavigateBack.invoke()
        }
    }

    BlockAutoFillDialogs(
        dialogState = state.dialog,
        onUriTextChange = { viewModel.trySendAction(BlockAutoFillAction.UriTextChange(uri = it)) },
        onSaveClick = { newUri, originalUri ->
            viewModel.trySendAction(
                BlockAutoFillAction.SaveUri(newUri = newUri, originalUri = originalUri),
            )
        },
        onDismissRequest = { viewModel.trySendAction(BlockAutoFillAction.DismissDialog) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.block_auto_fill),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = { viewModel.trySendAction(BlockAutoFillAction.BackClick) },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Universal Password ManagerFloatingActionButton(
                    onClick = { viewModel.trySendAction(BlockAutoFillAction.AddUriClick) },
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_plus_large),
                    contentDescription = stringResource(id = Universal Password ManagerString.add_item),
                    modifier = Modifier.testTag(tag = "AddItemButton"),
                )
            }
        },
    ) {
        when (val viewState = state.viewState) {
            is BlockAutoFillState.ViewState.Content -> {
                BlockedAutofillContent(
                    viewState = viewState,
                    onEditUriClick = {
                        viewModel.trySendAction(BlockAutoFillAction.EditUriClick(it))
                    },
                    onRemoveClick = {
                        viewModel.trySendAction(BlockAutoFillAction.RemoveUriClick(it))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            BlockAutoFillState.ViewState.Empty -> {
                BlockAutoFillNoItems(
                    addItemClickAction = {
                        viewModel.trySendAction(BlockAutoFillAction.AddUriClick)
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun BlockedAutofillContent(
    viewState: BlockAutoFillState.ViewState.Content,
    onEditUriClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(height = 24.dp))
            Text(
                text = stringResource(
                    id = Universal Password ManagerString.auto_fill_will_not_be_offered_for_these_ur_is,
                ),
                textAlign = TextAlign.Center,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .animateItem(),
            )
            Spacer(modifier = Modifier.height(height = 24.dp))
        }

        itemsIndexed(
            items = viewState.blockedUris,
            key = { _, uri -> uri },
        ) { index, uri ->
            BlockAutoFillListItem(
                label = uri,
                onDeleteClick = { onRemoveClick(uri) },
                onEditClick = { onEditUriClick(uri) },
                cardStyle = viewState.blockedUris.toListItemCardStyle(index = index),
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth()
                    .animateItem(),
            )
        }
        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun BlockAutoFillDialogs(
    dialogState: BlockAutoFillState.DialogState? = null,
    onUriTextChange: (String) -> Unit,
    onSaveClick: (String, String?) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is BlockAutoFillState.DialogState.AddEdit -> {
            AddEditBlockedUriDialog(
                uri = dialogState.uri,
                isEdit = dialogState.isEdit,
                errorMessage = dialogState.errorMessage?.invoke(),
                onUriChange = onUriTextChange,
                onDismissRequest = onDismissRequest,
                onCancelClick = onDismissRequest,
                onSaveClick = { newUri -> onSaveClick(newUri, dialogState.originalUri) },
            )
        }

        null -> Unit
    }
}

/**
 * No items view for the [BlockAutoFillScreen].
 */
@Composable
private fun BlockAutoFillNoItems(
    addItemClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(height = 24.dp))
        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_network),
            contentDescription = null,
            modifier = Modifier
                .size(size = 124.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = Universal Password ManagerString.no_uris_blocked),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(
                id = Universal Password ManagerString.auto_fill_will_not_be_offered_for_these_ur_is,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.new_blocked_uri),
            onClick = addItemClickAction,
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_plus_small),
            modifier = Modifier
                .wrapContentWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun BlockAutoFillListItem(
    label: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                paddingStart = 16.dp,
                paddingEnd = 4.dp,
            ),
    ) {
        Text(
            text = label,
            style = Universal Password ManagerTheme.typography.bodyLarge,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(weight = 1f),
        )
        Universal Password ManagerOverflowActionItem(
            menuItemDataList = persistentListOf(
                OverflowMenuItemData(
                    text = stringResource(id = Universal Password ManagerString.edit),
                    onClick = onEditClick,
                ),
                OverflowMenuItemData(
                    text = stringResource(id = Universal Password ManagerString.delete),
                    onClick = onDeleteClick,
                ),
            ),
            vectorIconRes = Universal Password ManagerDrawable.ic_ellipsis_horizontal,
            testTag = "Options",
        )
    }
}
