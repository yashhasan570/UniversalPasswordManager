package com.x8bit.universalpasswordmanager.ui.tools.feature.send.viewsend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.button.Universal Password ManagerOutlinedErrorButton
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.fab.Universal Password ManagerFloatingActionButton
import .ui.platform.components.field.Universal Password ManagerTextField
import .ui.platform.components.header.Universal Password ManagerExpandingHeader
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.icon.Universal Password ManagerIcon
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.stepper.Universal Password ManagerStepper
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.ModeType
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.viewsend.model.SendPolicyRestriction

/**
 * Displays view send screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewSendScreen(
    viewModel: ViewSendViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddEditSend: (route: VaultUnlockedGraph.AddEditSendRoute) -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is ViewSendEvent.NavigateBack -> onNavigateBack()
            is ViewSendEvent.NavigateToCopy -> {
                onNavigateToAddEditSend(
                    VaultUnlockedGraph.AddEditSendRoute(
                        sendType = event.sendType,
                        modeType = ModeType.COPY,
                        sendId = event.sendId,
                    ),
                )
            }

            is ViewSendEvent.NavigateToEdit -> {
                onNavigateToAddEditSend(
                    VaultUnlockedGraph.AddEditSendRoute(
                        sendType = event.sendType,
                        modeType = ModeType.EDIT,
                        sendId = event.sendId,
                    ),
                )
            }

            is ViewSendEvent.ShareText -> {
                intentManager.shareText(text = event.text(resources).toString())
            }

            is ViewSendEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    ViewSendDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(ViewSendAction.DialogDismiss) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.screenDisplayName(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                    onNavigationIconClick = { viewModel.trySendAction(ViewSendAction.CloseClick) },
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.isFabVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Universal Password ManagerFloatingActionButton(
                    onClick = { viewModel.trySendAction(ViewSendAction.EditClick) },
                    painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_pencil),
                    contentDescription = stringResource(id = Universal Password ManagerString.edit_send),
                    modifier = Modifier.testTag(tag = "EditItemButton"),
                )
            }
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
    ) {
        ViewSendScreenContent(
            state = state,
            modifier = Modifier.fillMaxSize(),
            onCopyClick = { viewModel.trySendAction(ViewSendAction.CopyClick) },
            onCopyNotesClick = { viewModel.trySendAction(ViewSendAction.CopyNotesClick) },
            onDeleteClick = { viewModel.trySendAction(ViewSendAction.DeleteClick) },
            onMakeACopyClick = { viewModel.trySendAction(ViewSendAction.MakeACopyClick) },
            onShareClick = { viewModel.trySendAction(ViewSendAction.ShareClick) },
        )
    }
}

@Composable
private fun ViewSendDialogs(
    dialogState: ViewSendState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is ViewSendState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                throwable = dialogState.throwable,
                onDismissRequest = onDismissRequest,
            )
        }

        is ViewSendState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.message())
        }

        null -> Unit
    }
}

@Composable
private fun ViewSendScreenContent(
    state: ViewSendState,
    onCopyClick: () -> Unit,
    onCopyNotesClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMakeACopyClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val viewState = state.viewState) {
        is ViewSendState.ViewState.Content -> {
            ViewStateContent(
                state = viewState,
                policyRestriction = state.policyRestriction,
                isSendDisabled = state.isSendDisabled,
                onCopyClick = onCopyClick,
                onCopyNotesClick = onCopyNotesClick,
                onDeleteClick = onDeleteClick,
                onMakeACopyClick = onMakeACopyClick,
                onShareClick = onShareClick,
                modifier = modifier,
            )
        }

        is ViewSendState.ViewState.Error -> {
            Universal Password ManagerErrorContent(
                message = viewState.message(),
                modifier = modifier,
            )
        }

        ViewSendState.ViewState.Loading -> {
            Universal Password ManagerLoadingContent(modifier = modifier)
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun ViewStateContent(
    state: ViewSendState.ViewState.Content,
    policyRestriction: SendPolicyRestriction?,
    isSendDisabled: Boolean,
    onCopyClick: () -> Unit,
    onCopyNotesClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMakeACopyClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        policyRestriction?.let {
            Universal Password ManagerActionCard(
                cardTitle = stringResource(id = Universal Password ManagerString.organization_policy_restriction),
                cardSubtitle = it.message(),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.make_a_copy.asText(),
                    onClick = onMakeACopyClick,
                    testTag = "SendMakeACopyButton",
                )
                    .takeIf { _ -> it.isCopyable },
                leadingContent = {
                    Universal Password ManagerIcon(
                        iconData = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_info_circle),
                        tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                    )
                },
                modifier = Modifier
                    .testTag(tag = "SendPolicyRestrictionBanner")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        if (!isSendDisabled) {
            ShareLinkSection(
                shareLink = state.shareLink,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            Universal Password ManagerFilledButton(
                label = stringResource(id = Universal Password ManagerString.copy),
                onClick = onCopyClick,
                icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_copy_small),
                cardStyle = CardStyle.Middle(hasDivider = false),
                cardInsets = PaddingValues(top = 16.dp, bottom = 6.dp, start = 16.dp, end = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag(tag = "ViewSendCopyButton"),
            )
            Universal Password ManagerOutlinedButton(
                label = stringResource(id = Universal Password ManagerString.share),
                onClick = onShareClick,
                icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_share_small),
                isExternalLink = true,
                cardStyle = CardStyle.Bottom,
                cardInsets = PaddingValues(top = 6.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin()
                    .testTag(tag = "ViewSendShareButton"),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }

        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.send_details),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.send_name_required),
            value = state.sendName,
            onValueChange = {},
            readOnly = true,
            cardStyle = CardStyle.Full,
            textFieldTestTag = "ViewSendNameField",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        when (val sendType = state.sendType) {
            is ViewSendState.ViewState.Content.SendType.FileType -> {
                FileSendContent(
                    fileType = sendType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }

            is ViewSendState.ViewState.Content.SendType.TextType -> {
                TextSendContent(
                    textType = sendType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
        }
        Spacer(modifier = Modifier.height(height = 8.dp))

        Universal Password ManagerTextField(
            label = stringResource(id = Universal Password ManagerString.deletion_date),
            value = state.deletionDate,
            onValueChange = {},
            readOnly = true,
            cardStyle = CardStyle.Full,
            textFieldTestTag = "ViewSendDeletionDateField",
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        AdditionalOptions(
            state = state,
            onCopyNotesClick = onCopyNotesClick,
        )

        DeleteButton(
            onDeleteClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .testTag("ViewSendDeleteButton"),
        )
        Spacer(modifier = Modifier.height(height = 88.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun DeleteButton(
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowDeleteConfirmationDialog by rememberSaveable { mutableStateOf(value = false) }
    if (shouldShowDeleteConfirmationDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.delete),
            message = stringResource(id = Universal Password ManagerString.are_you_sure_delete_send),
            confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                onDeleteClick()
                shouldShowDeleteConfirmationDialog = false
            },
            onDismissClick = { shouldShowDeleteConfirmationDialog = false },
            onDismissRequest = { shouldShowDeleteConfirmationDialog = false },
        )
    }
    Universal Password ManagerOutlinedErrorButton(
        label = stringResource(id = Universal Password ManagerString.delete_send),
        onClick = { shouldShowDeleteConfirmationDialog = true },
        icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_trash_small),
        modifier = modifier,
    )
}

/**
 * A default content block which displays a header with an optional subtitle and an icon.
 * Implemented to match design component.
 */
@Composable
private fun ShareLinkSection(
    shareLink: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .cardStyle(
                cardStyle = CardStyle.Top(dividerPadding = 0.dp),
                paddingHorizontal = 16.dp,
                paddingVertical = 12.dp,
            ),
    ) {
        Text(
            text = stringResource(id = Universal Password ManagerString.send_link),
            style = Universal Password ManagerTheme.typography.titleSmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text = shareLink,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag = "ViewSendShareLinkText"),
        )
    }
}

@Composable
private fun FileSendContent(
    fileType: ViewSendState.ViewState.Content.SendType.FileType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = CardStyle.Full,
                paddingHorizontal = 16.dp,
                paddingVertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = fileType.fileName,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            style = Universal Password ManagerTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(weight = 1f)
                .testTag("ViewSendFileNameText"),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = fileType.fileSize,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            style = Universal Password ManagerTheme.typography.bodyLarge,
            modifier = Modifier.testTag("ViewSendFileSizeText"),
        )
    }
}

@Composable
private fun TextSendContent(
    textType: ViewSendState.ViewState.Content.SendType.TextType,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerTextField(
        label = stringResource(id = Universal Password ManagerString.text_to_share),
        value = textType.textToShare,
        onValueChange = {},
        readOnly = true,
        cardStyle = CardStyle.Full,
        textFieldTestTag = "ViewSendContentText",
        modifier = modifier,
    )
}

@Suppress("LongMethod")
@Composable
private fun ColumnScope.AdditionalOptions(
    state: ViewSendState.ViewState.Content,
    onCopyNotesClick: () -> Unit,
) {
    if (state.maxAccessCount == null && state.notes == null) {
        Spacer(modifier = Modifier.height(height = 16.dp))
        return
    }
    var isExpanded by rememberSaveable { mutableStateOf(value = false) }
    Universal Password ManagerExpandingHeader(
        isExpanded = isExpanded,
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier
            .testTag(tag = "ViewSendAdditionalOptions")
            .standardHorizontalMargin()
            .fillMaxWidth(),
    )
    // Hide all content if not expanded:
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
        modifier = Modifier.clipToBounds(),
    ) {
        Column {
            state.maxAccessCount?.let {
                Universal Password ManagerStepper(
                    label = stringResource(id = Universal Password ManagerString.maximum_access_count),
                    value = it,
                    supportingText = Universal Password ManagerString.current_access_count
                        .asText(state.currentAccessCount)
                        .invoke(),
                    onValueChange = {},
                    isDecrementEnabled = false,
                    isIncrementEnabled = false,
                    range = 0..Int.MAX_VALUE,
                    cardStyle = CardStyle.Full,
                    modifier = Modifier
                        .testTag(tag = "ViewSendMaxAccessCount")
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
            state.notes?.let {
                if (state.maxAccessCount != null) {
                    // If the stepper is present, we need a spacer between these 2 items
                    Spacer(modifier = Modifier.height(height = 8.dp))
                }
                Universal Password ManagerTextField(
                    label = stringResource(id = Universal Password ManagerString.private_notes),
                    readOnly = true,
                    value = it,
                    actions = {
                        Universal Password ManagerStandardIconButton(
                            vectorIconRes = Universal Password ManagerDrawable.ic_copy,
                            contentDescription = stringResource(id = Universal Password ManagerString.copy_notes),
                            onClick = onCopyNotesClick,
                            modifier = Modifier.testTag(tag = "ViewSendNotesCopyButton"),
                        )
                    },
                    singleLine = false,
                    onValueChange = {},
                    cardStyle = CardStyle.Full,
                    modifier = Modifier
                        .testTag(tag = "ViewSendNotes")
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}
