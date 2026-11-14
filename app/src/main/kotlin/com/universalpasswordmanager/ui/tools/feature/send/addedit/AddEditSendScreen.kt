package com.universalpasswordmanager.ui.tools.feature.send.addedit

import androidx.activity.compose.BackHandler
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
import com.universalpasswordmanager.core.util.persistentListOfNotNull
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.appbar.action.UniversalOverflowActionItem
import com.universalpasswordmanager.ui.platform.components.appbar.model.OverflowMenuItemData
import com.universalpasswordmanager.ui.platform.components.appbar.model.TopAppBarDividerStyle
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
import com.universalpasswordmanager.ui.platform.composition.LocalExitManager
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.manager.exit.ExitManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager
import com.universalpasswordmanager.ui.tools.feature.generator.model.GeneratorMode
import com.universalpasswordmanager.ui.tools.feature.send.addedit.handlers.AddEditSendHandlers

/**
 * Displays new send UX.
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSendScreen(
    viewModel: AddEditSendViewModel,
    exitManager: ExitManager = LocalExitManager.current,
    intentManager: IntentManager = LocalIntentManager.current,
    permissionsManager: PermissionsManager = LocalPermissionsManager.current,
    onNavigateBack: () -> Unit,
    onNavigateUpToSearchOrRoot: () -> Unit,
    onNavigateToGeneratorModal: (GeneratorMode.Modal) -> Unit,
    onNavigateToPlan: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val addSendHandlers = remember(viewModel) { AddEditSendHandlers.create(viewModel) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val fileChooserLauncher = intentManager.getActivityResultLauncher { activityResult ->
        intentManager.getFileDataFromActivityResult(activityResult)?.let {
            addSendHandlers.onFileChoose(it)
        }
    }

    val snackbarHostState = rememberUniversalSnackbarHostState()
    BackHandler(
        onBack = { viewModel.trySendAction(AddEditSendAction.CloseClick) },
    )
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            AddEditSendEvent.ExitApp -> exitManager.exitApplication()

            is AddEditSendEvent.NavigateBack -> onNavigateBack()

            is AddEditSendEvent.NavigateUpToSearchOrRoot -> onNavigateUpToSearchOrRoot()

            is AddEditSendEvent.ShowChooserSheet -> {
                fileChooserLauncher.launch(
                    intentManager.createFileChooserIntent(event.withCameraOption),
                )
            }

            is AddEditSendEvent.ShowShareSheet -> {
                intentManager.shareText(event.message)
            }

            is AddEditSendEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)

            is AddEditSendEvent.NavigateToGeneratorModal -> {
                onNavigateToGeneratorModal(event.generatorMode)
            }

            is AddEditSendEvent.NavigateToPremium -> {
                intentManager.launchUri(uri = event.uri.toUri())
            }

            AddEditSendEvent.NavigateToPlanModal -> onNavigateToPlan()
        }
    }

    AddEditSendDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(AddEditSendAction.DismissDialogClick) },
        onUpgradeToPremiumClick = {
            viewModel.trySendAction(AddEditSendAction.UpgradeToPremiumClick)
        },
    )
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = state.screenDisplayName(),
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                    navigationIconContentDescription = stringResource(id = UniversalString.close),
                    onNavigationIconClick = {
                        viewModel.trySendAction(AddEditSendAction.CloseClick)
                    },
                )
                    .takeUnless { state.isShared },
                dividerStyle = TopAppBarDividerStyle.NONE,
                scrollBehavior = scrollBehavior,
                actions = {
                    UniversalTextButton(
                        label = stringResource(id = UniversalString.save),
                        isEnabled = !state.policyDisablesSend,
                        onClick = { viewModel.trySendAction(AddEditSendAction.SaveClick) },
                        modifier = Modifier.testTag("SaveButton"),
                    )
                    UniversalOverflowActionItem(
                        isVisible = !state.isNewSend,
                        menuItemDataList = persistentListOfNotNull(
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.remove_password),
                                onClick = {
                                    viewModel.trySendAction(AddEditSendAction.RemovePasswordClick)
                                },
                            )
                                .takeIf { state.hasPassword && !state.policyDisablesSend },
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.copy_link),
                                onClick = {
                                    viewModel.trySendAction(AddEditSendAction.CopyLinkClick)
                                },
                            )
                                .takeIf { !state.policyDisablesSend },
                            OverflowMenuItemData(
                                text = stringResource(id = UniversalString.share_link),
                                isExternalLink = true,
                                onClick = {
                                    viewModel.trySendAction(AddEditSendAction.ShareLinkClick)
                                },
                            )
                                .takeIf { !state.policyDisablesSend },
                        ),
                    )
                },
            )
        },
        snackbarHost = { UniversalSnackbarHost(bitwardenHostState = snackbarHostState) },
    ) {
        val modifier = Modifier
            .fillMaxSize()

        when (val viewState = state.viewState) {
            is AddEditSendState.ViewState.Content -> AddEditSendContent(
                state = viewState,
                enforcedDeletionHours = state.enforcedDeletionHours,
                enforcedWhoCanAccess = state.enforcedWhoCanAccess,
                policyDisablesSend = state.policyDisablesSend,
                policySendOptionsInEffect = state.shouldDisplayPolicyWarning,
                shouldHideEmailAddressToggle = state.shouldHideEmailAddressToggle,
                isNewSend = state.isNewSend,
                isShared = state.isShared,
                addSendHandlers = addSendHandlers,
                permissionsManager = permissionsManager,
                modifier = modifier,
            )

            is AddEditSendState.ViewState.Error -> UniversalErrorContent(
                message = viewState.message(),
                modifier = modifier,
            )

            AddEditSendState.ViewState.Loading -> UniversalLoadingContent(
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun AddEditSendDialogs(
    dialogState: AddEditSendState.DialogState?,
    onDismissRequest: () -> Unit,
    onUpgradeToPremiumClick: () -> Unit,
) {
    when (dialogState) {
        is AddEditSendState.DialogState.PremiumRequired -> {
            PremiumRequiredDialog(
                message = dialogState.message(),
                isInAppUpgradeAvailable = dialogState.isInAppUpgradeAvailable,
                onUpgradeToPremiumClick = onUpgradeToPremiumClick,
                onDismissRequest = onDismissRequest,
            )
        }

        is AddEditSendState.DialogState.Error -> UniversalBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
            throwable = dialogState.throwable,
        )

        is AddEditSendState.DialogState.Loading -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}
