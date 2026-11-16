package com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit

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
import .core.util.persistentListOfNotNull
import .ui.platform.base.util.EventsEffect
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.appbar.action.Universal Password ManagerOverflowActionItem
import .ui.platform.components.appbar.model.OverflowMenuItemData
import .ui.platform.components.appbar.model.TopAppBarDividerStyle
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
import .ui.platform.composition.LocalExitManager
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.exit.ExitManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager
import com.x8bit.universalpasswordmanager.ui.tools.feature.generator.model.GeneratorMode
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.addedit.handlers.AddEditSendHandlers

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

    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
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
                    onNavigationIconClick = {
                        viewModel.trySendAction(AddEditSendAction.CloseClick)
                    },
                )
                    .takeUnless { state.isShared },
                dividerStyle = TopAppBarDividerStyle.NONE,
                scrollBehavior = scrollBehavior,
                actions = {
                    Universal Password ManagerTextButton(
                        label = stringResource(id = Universal Password ManagerString.save),
                        isEnabled = !state.policyDisablesSend,
                        onClick = { viewModel.trySendAction(AddEditSendAction.SaveClick) },
                        modifier = Modifier.testTag("SaveButton"),
                    )
                    Universal Password ManagerOverflowActionItem(
                        isVisible = !state.isNewSend,
                        menuItemDataList = persistentListOfNotNull(
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.remove_password),
                                onClick = {
                                    viewModel.trySendAction(AddEditSendAction.RemovePasswordClick)
                                },
                            )
                                .takeIf { state.hasPassword && !state.policyDisablesSend },
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.copy_link),
                                onClick = {
                                    viewModel.trySendAction(AddEditSendAction.CopyLinkClick)
                                },
                            )
                                .takeIf { !state.policyDisablesSend },
                            OverflowMenuItemData(
                                text = stringResource(id = Universal Password ManagerString.share_link),
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
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
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

            is AddEditSendState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = viewState.message(),
                modifier = modifier,
            )

            AddEditSendState.ViewState.Loading -> Universal Password ManagerLoadingContent(
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

        is AddEditSendState.DialogState.Error -> Universal Password ManagerBasicDialog(
            title = dialogState.title?.invoke(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
            throwable = dialogState.throwable,
        )

        is AddEditSendState.DialogState.Loading -> Universal Password ManagerLoadingDialog(
            text = dialogState.message(),
        )

        null -> Unit
    }
}
