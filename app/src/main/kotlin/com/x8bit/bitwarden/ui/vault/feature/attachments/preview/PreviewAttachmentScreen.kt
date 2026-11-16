package com.x8bit.universalpasswordmanager.ui.vault.feature.attachments.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.spanStyleOf
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerStandardIconButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.preview.ImagePreviewContent
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText

/**
 * Displays the preview attachment screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewAttachmentScreen(
    viewModel: PreviewAttachmentViewModel,
    intentManager: IntentManager = LocalIntentManager.current,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val fileChooserLauncher = intentManager.getActivityResultLauncher { activityResult ->
        val action = intentManager
            .getFileDataFromActivityResult(activityResult)
            ?.let { PreviewAttachmentAction.AttachmentFileLocationReceive(it.uri) }
            ?: PreviewAttachmentAction.NoAttachmentFileLocationReceive
        viewModel.trySendAction(action)
    }
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            PreviewAttachmentEvent.NavigateBack -> onNavigateBack()
            is PreviewAttachmentEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            is PreviewAttachmentEvent.NavigateToSelectAttachmentSaveLocation -> {
                fileChooserLauncher.launch(intentManager.createDocumentIntent(event.fileName))
            }
        }
    }

    PreviewAttachmentDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(PreviewAttachmentAction.DismissDialog) },
        onCloseClick = { viewModel.trySendAction(PreviewAttachmentAction.CloseClick) },
        onConfirmDownloadClick = {
            viewModel.trySendAction(PreviewAttachmentAction.ConfirmDownloadClick)
        },
    )

    Universal Password ManagerScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = state.fileName,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                    onNavigationIconClick = {
                        viewModel.trySendAction(PreviewAttachmentAction.BackClick)
                    },
                ),
                actions = {
                    Universal Password ManagerStandardIconButton(
                        vectorIconRes = Universal Password ManagerDrawable.ic_download,
                        contentDescription = stringResource(id = Universal Password ManagerString.download),
                        onClick = {
                            viewModel.trySendAction(PreviewAttachmentAction.DownloadClick)
                        },
                        isExternalLink = true,
                        modifier = Modifier.testTag("ToolbarDownloadButton"),
                    )
                },
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        when (val viewState = state.viewState) {
            is PreviewAttachmentState.ViewState.Content -> {
                ImagePreviewContent(
                    file = viewState.file,
                    onMissingFile = {
                        viewModel.trySendAction(PreviewAttachmentAction.FileMissing)
                    },
                    onLoaded = {
                        viewModel.trySendAction(PreviewAttachmentAction.BitmapRenderComplete)
                    },
                    onError = {
                        viewModel.trySendAction(PreviewAttachmentAction.BitmapRenderError)
                    },
                )
            }

            is PreviewAttachmentState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = viewState.message(),
                illustrationData = IconData.Local(iconRes = viewState.illustrationRes),
                buttonData = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.download_file.asText(),
                    icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_download),
                    onClick = { viewModel.trySendAction(PreviewAttachmentAction.DownloadClick) },
                    isExternalLink = true,
                    testTag = "ErrorStateDownloadButton",
                ),
                modifier = Modifier.fillMaxSize(),
            )

            is PreviewAttachmentState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                text = viewState.message(),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun PreviewAttachmentDialogs(
    dialogState: PreviewAttachmentState.DialogState?,
    onDismissRequest: () -> Unit,
    onCloseClick: () -> Unit,
    onConfirmDownloadClick: () -> Unit,
) {
    when (dialogState) {
        is PreviewAttachmentState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        is PreviewAttachmentState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialogState.message())
        }

        PreviewAttachmentState.DialogState.PreviewUnavailable -> {
            Universal Password ManagerBasicDialog(
                title = stringResource(id = Universal Password ManagerString.preview_unavailable),
                message = stringResource(
                    id = Universal Password ManagerString
                        .universalpasswordmanager_could_not_decrypt_this_file_so_the_preview_cannot_be_displayed,
                ),
                confirmButtonLabel = stringResource(id = Universal Password ManagerString.close),
                onDismissRequest = onCloseClick,
            )
        }

        is PreviewAttachmentState.DialogState.DownloadLargeFileConfirmation -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.download_attachment),
                message = annotatedStringResource(
                    id = Universal Password ManagerString.attachment_large_warning,
                    args = arrayOf(dialogState.displaySize),
                    style = spanStyleOf(
                        color = Universal Password ManagerTheme.colorScheme.text.primary,
                        textStyle = Universal Password ManagerTheme.typography.bodyMedium,
                    ),
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
                dismissButtonText = stringResource(id = Universal Password ManagerString.no),
                onConfirmClick = onConfirmDownloadClick,
                onDismissClick = onDismissRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}
