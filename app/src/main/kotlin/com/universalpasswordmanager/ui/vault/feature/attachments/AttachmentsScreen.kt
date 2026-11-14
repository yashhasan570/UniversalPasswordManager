package com.universalpasswordmanager.ui.vault.feature.attachments

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.button.UniversalTextButton
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.PremiumRequiredDialog
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.feature.vaultunlocked.VaultUnlockedGraph
import com.universalpasswordmanager.ui.vault.feature.attachments.handlers.AttachmentsHandlers

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
    val snackbarHostState = rememberUniversalSnackbarHostState()
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
    UniversalScaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.attachments),
                scrollBehavior = scrollBehavior,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = UniversalString.back),
                    onNavigationIconClick = attachmentsHandlers.onBackClick,
                ),
                actions = {
                    UniversalTextButton(
                        label = stringResource(id = UniversalString.save),
                        onClick = attachmentsHandlers.onSaveClick,
                        modifier = Modifier.testTag("SaveButton"),
                    )
                },
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        when (val viewState = state.viewState) {
            is AttachmentsState.ViewState.Content -> AttachmentsContent(
                viewState = viewState,
                attachmentsHandlers = attachmentsHandlers,
                isAttachmentUpdatesEnabled = state.isAttachmentUpdatesEnabled,
                modifier = Modifier.fillMaxSize(),
            )

            is AttachmentsState.ViewState.Error -> UniversalErrorContent(
                message = viewState.message(),
                modifier = Modifier.fillMaxSize(),
            )

            AttachmentsState.ViewState.Loading -> UniversalLoadingContent(
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
            message = stringResource(id = UniversalString.attachments_are_a_premium_feature),
            isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
            onUpgradeToPremiumClick = attachmentsHandlers.onUpgradeToPremiumClick,
            onDismissRequest = attachmentsHandlers.onDismissRequest,
        )

        is AttachmentsState.DialogState.Error -> UniversalBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = attachmentsHandlers.onDismissRequest,
            throwable = dialogState.throwable,
        )

        is AttachmentsState.DialogState.Loading -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}
