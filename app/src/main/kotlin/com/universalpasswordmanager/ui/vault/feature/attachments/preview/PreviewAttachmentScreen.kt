package com.universalpasswordmanager.ui.vault.feature.attachments.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.spanStyleOf
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.button.UniversalStandardIconButton
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.preview.ImagePreviewContent
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText

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
    val snackbarHostState = rememberUniversalSnackbarHostState()
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

    UniversalScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniversalTopAppBar(
                title = state.fileName,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = UniversalString.back),
                    onNavigationIconClick = {
                        viewModel.trySendAction(PreviewAttachmentAction.BackClick)
                    },
                ),
                actions = {
                    UniversalStandardIconButton(
                        vectorIconRes = UniversalDrawable.ic_download,
                        contentDescription = stringResource(id = UniversalString.download),
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
            UniversalSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
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

            is PreviewAttachmentState.ViewState.Error -> UniversalErrorContent(
                message = viewState.message(),
                illustrationData = IconData.Local(iconRes = viewState.illustrationRes),
                buttonData = UniversalButtonData(
                    label = UniversalString.download_file.asText(),
                    icon = rememberVectorPainter(id = UniversalDrawable.ic_download),
                    onClick = { viewModel.trySendAction(PreviewAttachmentAction.DownloadClick) },
                    isExternalLink = true,
                    testTag = "ErrorStateDownloadButton",
                ),
                modifier = Modifier.fillMaxSize(),
            )

            is PreviewAttachmentState.ViewState.Loading -> UniversalLoadingContent(
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
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        is PreviewAttachmentState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialogState.message())
        }

        PreviewAttachmentState.DialogState.PreviewUnavailable -> {
            UniversalBasicDialog(
                title = stringResource(id = UniversalString.preview_unavailable),
                message = stringResource(
                    id = UniversalString
                        .universalpasswordmanager_could_not_decrypt_this_file_so_the_preview_cannot_be_displayed,
                ),
                confirmButtonLabel = stringResource(id = UniversalString.close),
                onDismissRequest = onCloseClick,
            )
        }

        is PreviewAttachmentState.DialogState.DownloadLargeFileConfirmation -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.download_attachment),
                message = annotatedStringResource(
                    id = UniversalString.attachment_large_warning,
                    args = arrayOf(dialogState.displaySize),
                    style = spanStyleOf(
                        color = UniversalTheme.colorScheme.text.primary,
                        textStyle = UniversalTheme.typography.bodyMedium,
                    ),
                ),
                confirmButtonText = stringResource(id = UniversalString.yes),
                dismissButtonText = stringResource(id = UniversalString.no),
                onConfirmClick = onConfirmDownloadClick,
                onDismissClick = onDismissRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}
