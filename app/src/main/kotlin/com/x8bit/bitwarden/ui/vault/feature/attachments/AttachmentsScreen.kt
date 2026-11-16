package com.x8bit.universalpasswordmanager.ui.vault.feature.attachments

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
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.PremiumRequiredDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.x8bit.universalpasswordmanager.ui.vault.feature.attachments.handlers.AttachmentsHandlers

/**
 * Displays the attachments screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentsScreen(
    viewModel: AttachmentsViewModel,
    intentManager: IntentManager = LocalIntentManager.current,
    onNavigateBack: () -> Unit,
    onNavigateToPreview: (route: VaultUnlockedGraph.PreviewAttachmentRoute) -> Unit,
    onNavigateToPlan: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val attachmentsHandlers = remember(viewModel) { AttachmentsHandlers.create(viewModel) }
    val fileChooserLauncher = intentManager.getActivityResultLauncher { activityResult ->
        intentManager.getFileDataFromActivityResult(activityResult)?.let {
            attachmentsHandlers.onFileChoose(it)
        }
    }
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            AttachmentsEvent.NavigateBack -> onNavigateBack()
            AttachmentsEvent.NavigateToPlanModal -> onNavigateToPlan()
            is AttachmentsEvent.NavigateToUri -> intentManager.launchUri(event.uri.toUri())
            AttachmentsEvent.ShowChooserSheet -> {
                fileChooserLauncher.launch(
                    intentManager.createFileChooserIntent(withCameraIntents = false),
                )
            }

            is AttachmentsEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
            is AttachmentsEvent.NavigateToPreview -> {
                onNavigateToPreview(
                    VaultUnlockedGraph.PreviewAttachmentRoute(
                        cipherId = event.cipherId,
                        attachmentId = event.attachmentId,
                        fileName = event.fileName,
                        displaySize = event.displaySize,
                        isLargeFile = event.isLargeFile,
                    ),
                )
            }
        }
    }

    AttachmentsDialogs(
        dialogState = state.dialogState,
        attachmentsHandlers = attachmentsHandlers,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.attachments),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                    onNavigationIconClick = attachmentsHandlers.onBackClick,
                ),
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.save),
                        onClick = attachmentsHandlers.onSaveClick,
                        modifier = Modifier.testTag("SaveButton"),
                    )
                },
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        when (val viewState = state.viewState) {
            is AttachmentsState.ViewState.Content -> AttachmentsContent(
                viewState = viewState,
                attachmentsHandlers = attachmentsHandlers,
                isAttachmentUpdatesEnabled = state.isAttachmentUpdatesEnabled,
                modifier = Modifier.fillMaxSize(),
            )

            is AttachmentsState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = viewState.message(),
                modifier = Modifier.fillMaxSize(),
            )

            AttachmentsState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun AttachmentsDialogs(
    dialogState: AttachmentsState.DialogState?,
    attachmentsHandlers: AttachmentsHandlers,
) {
    when (dialogState) {
        is AttachmentsState.DialogState.RequiresPremium -> PremiumRequiredDialog(
            message = stringResource(id = Universal Password ManagerString.attachments_are_a_premium_feature),
            isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
            onUpgradeToPremiumClick = attachmentsHandlers.onUpgradeToPremiumClick,
            onDismissRequest = attachmentsHandlers.onDismissRequest,
        )

        is AttachmentsState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = attachmentsHandlers.onDismissRequest,
            throwable = dialogState.throwable,
        )

        is AttachmentsState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}
