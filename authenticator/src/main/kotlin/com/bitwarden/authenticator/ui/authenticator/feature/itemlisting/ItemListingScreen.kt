package .authenticator.ui.authenticator.feature.itemlisting

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .authenticator.ui.platform.components.listitem.VaultVerificationCodeItem
import .authenticator.ui.platform.components.listitem.model.SharedCodesDisplayState
import .authenticator.ui.platform.components.listitem.model.VaultDropdownMenuAction
import .authenticator.ui.platform.components.listitem.model.VerificationCodeDisplayItem
import .authenticator.ui.platform.composition.LocalPermissionsManager
import .authenticator.ui.platform.manager.permissions.PermissionsManager
import .authenticator.ui.platform.util.startUniversal Password ManagerAccountSettings
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerMediumTopAppBar
import .ui.platform.components.appbar.action.Universal Password ManagerSearchActionItem
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.fab.Universal Password ManagerExpandableFloatingActionButton
import .ui.platform.components.fab.model.ExpandableFabIcon
import .ui.platform.components.fab.model.ExpandableFabOption
import .ui.platform.components.header.Universal Password ManagerExpandingListHeader
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.icon.model.IconData
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.util.startAppSettingsActivity
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import kotlinx.collections.immutable.persistentListOf

/**
 * Displays the item listing screen.
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListingScreen(
    viewModel: ItemListingViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
    permissionsManager: PermissionsManager = LocalPermissionsManager.current,
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToQrCodeScanner: () -> Unit,
    onNavigateToManualKeyEntry: () -> Unit,
    onNavigateToEditItemScreen: (id: String) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val launcher = permissionsManager.getLauncher { isGranted ->
        if (isGranted) {
            viewModel.trySendAction(ItemListingAction.ScanQrCodeClick)
        } else {
            viewModel.trySendAction(ItemListingAction.EnterSetupKeyClick)
        }
    }
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is ItemListingEvent.NavigateBack -> onNavigateBack()
            is ItemListingEvent.NavigateToSearch -> onNavigateToSearch()
            is ItemListingEvent.NavigateToQrCodeScanner -> onNavigateToQrCodeScanner()
            is ItemListingEvent.NavigateToManualAddItem -> onNavigateToManualKeyEntry()
            is ItemListingEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(snackbarData = event.data)
            }

            is ItemListingEvent.NavigateToEditItem -> onNavigateToEditItemScreen(event.id)
            is ItemListingEvent.NavigateToAppSettings -> {
                intentManager.startAppSettingsActivity()
            }

            ItemListingEvent.NavigateToUniversal Password ManagerListing -> {
                intentManager.launchUri(
                    "https://play.google.com/store/apps/details?id=com.x8bit.universalpasswordmanager".toUri(),
                )
            }

            ItemListingEvent.NavigateToSyncInformation -> {
                intentManager.launchUri("https:///help/totp-sync".toUri())
            }

            ItemListingEvent.NavigateToUniversal Password ManagerSettings -> {
                intentManager.startUniversal Password ManagerAccountSettings()
            }
        }
    }

    ItemListingDialogs(
        dialog = state.dialog,
        onDismissRequest = { viewModel.trySendAction(ItemListingAction.DialogDismiss) },
        onConfirmDeleteClick = {
            viewModel.trySendAction(ItemListingAction.ConfirmDeleteClick(it))
        },
    )

    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerMediumTopAppBar(
                title = stringResource(id = Universal Password ManagerString.verification_codes),
                scrollBehavior = scrollBehavior,
                actions = {
                    Universal Password ManagerSearchActionItem(
                        contentDescription = stringResource(id = Universal Password ManagerString.search_codes),
                        isDisplayed = state.shouldShowSearchIcon,
                        onClick = onNavigateToSearch,
                    )
                },
            )
        },
        floatingActionButton = {
            Universal Password ManagerExpandableFloatingActionButton(
                modifier = Modifier.testTag("AddItemButton"),
                items = persistentListOf(
                    ExpandableFabOption(
                        label = Universal Password ManagerString.scan_a_qr_code.asText(),
                        icon = IconData.Local(
                            iconRes = Universal Password ManagerDrawable.ic_camera_small,
                            contentDescription = null,
                            testTag = "ScanQRCodeButton",
                        ),
                        onFabOptionClick = { launcher.launch(Manifest.permission.CAMERA) },
                    ),
                    ExpandableFabOption(
                        label = Universal Password ManagerString.enter_key_manually.asText(),
                        icon = IconData.Local(
                            iconRes = Universal Password ManagerDrawable.ic_lock_encrypted_small,
                            contentDescription = null,
                            testTag = "EnterSetupKeyButton",
                        ),
                        onFabOptionClick = {
                            viewModel.trySendAction(ItemListingAction.EnterSetupKeyClick)
                        },
                    ),
                ),
                expandableFabIcon = ExpandableFabIcon(
                    icon = IconData.Local(
                        iconRes = Universal Password ManagerDrawable.ic_plus_large,
                        contentDescription = Universal Password ManagerString.add_item.asText(),
                        testTag = "AddItemButton",
                    ),
                    iconRotation = 45f,
                ),
            )
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState) },
    ) {
        when (val currentState = state.viewState) {
            is ItemListingState.ViewState.Content -> {
                ItemListingContent(
                    state = currentState,
                    onItemClick = { viewModel.trySendAction(ItemListingAction.ItemClick(it)) },
                    onDropdownMenuClick = { action, item ->
                        viewModel.trySendAction(
                            ItemListingAction.DropdownMenuClick(menuAction = action, item = item),
                        )
                    },
                    onDownloadUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.DownloadUniversal Password ManagerClick)
                    },
                    onDismissDownloadUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.DownloadUniversal Password ManagerDismiss)
                    },
                    onSyncWithUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.SyncWithUniversal Password ManagerClick)
                    },
                    onDismissSyncWithUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.SyncWithUniversal Password ManagerDismiss)
                    },
                    onSyncLearnMoreClick = {
                        viewModel.trySendAction(ItemListingAction.SyncLearnMoreClick)
                    },
                    onSectionExpandedClick = {
                        viewModel.trySendAction(ItemListingAction.SectionExpandedClick(it))
                    },
                )
            }

            ItemListingState.ViewState.Loading -> {
                Universal Password ManagerLoadingContent(modifier = Modifier.fillMaxSize())
            }

            is ItemListingState.ViewState.NoItems -> {
                EmptyItemListingContent(
                    actionCardState = currentState.actionCard,
                    onAddCodeClick = { launcher.launch(Manifest.permission.CAMERA) },
                    onDownloadUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.DownloadUniversal Password ManagerClick)
                    },
                    onDismissDownloadUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.DownloadUniversal Password ManagerDismiss)
                    },
                    onSyncWithUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.SyncWithUniversal Password ManagerClick)
                    },
                    onSyncLearnMoreClick = {
                        viewModel.trySendAction(ItemListingAction.SyncLearnMoreClick)
                    },
                    onDismissSyncWithUniversal Password ManagerClick = {
                        viewModel.trySendAction(ItemListingAction.SyncWithUniversal Password ManagerDismiss)
                    },
                )
            }
        }
    }
}

@Composable
private fun ItemListingDialogs(
    dialog: ItemListingState.DialogState?,
    onDismissRequest: () -> Unit,
    onConfirmDeleteClick: (itemId: String) -> Unit,
) {
    when (dialog) {
        ItemListingState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(
                text = stringResource(id = Universal Password ManagerString.syncing),
            )
        }

        is ItemListingState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        is ItemListingState.DialogState.DeleteConfirmationPrompt -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(id = Universal Password ManagerString.delete),
                message = dialog.message(),
                confirmButtonText = stringResource(id = Universal Password ManagerString.okay),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = {
                    onConfirmDeleteClick(dialog.itemId)
                },
                onDismissClick = onDismissRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Suppress("LongMethod")
@Composable
private fun ItemListingContent(
    state: ItemListingState.ViewState.Content,
    onItemClick: (String) -> Unit,
    onDropdownMenuClick: (VaultDropdownMenuAction, VerificationCodeDisplayItem) -> Unit,
    onDownloadUniversal Password ManagerClick: () -> Unit,
    onDismissDownloadUniversal Password ManagerClick: () -> Unit,
    onSyncWithUniversal Password ManagerClick: () -> Unit,
    onDismissSyncWithUniversal Password ManagerClick: () -> Unit,
    onSyncLearnMoreClick: () -> Unit,
    onSectionExpandedClick: (SharedCodesDisplayState.SharedCodesAccountSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isLocalHeaderExpanded by rememberSaveable { mutableStateOf(value = true) }
    LazyColumn(modifier = modifier.fillMaxSize()) {
        state.actionCard?.let {
            item(key = "action_card") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                ActionCard(
                    actionCardState = it,
                    onDownloadUniversal Password ManagerClick = onDownloadUniversal Password ManagerClick,
                    onDownloadUniversal Password ManagerDismissClick = onDismissDownloadUniversal Password ManagerClick,
                    onSyncWithUniversal Password ManagerClick = onSyncWithUniversal Password ManagerClick,
                    onSyncWithUniversal Password ManagerDismissClick = onDismissSyncWithUniversal Password ManagerClick,
                    onSyncLearnMoreClick = onSyncLearnMoreClick,
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        }

        if (state.favoriteItems.isNotEmpty()) {
            item(key = "favorites_header") {
                Spacer(modifier = Modifier.height(height = 12.dp))
                Universal Password ManagerListHeaderText(
                    label = stringResource(id = Universal Password ManagerString.favorites),
                    supportingLabel = state.favoriteItems.count().toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .padding(horizontal = 16.dp)
                        .testTag("FavoritesHeaderLabel")
                        .animateItem(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }

            itemsIndexed(
                items = state.favoriteItems,
                key = { _, it -> "favorite_item_${it.id}" },
            ) { index, item ->
                VaultVerificationCodeItem(
                    displayItem = item,
                    onItemClick = { onItemClick(item.authCode) },
                    onDropdownMenuClick = { action -> onDropdownMenuClick(action, item) },
                    cardStyle = state.favoriteItems.toListItemCardStyle(index = index),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        if (state.shouldShowLocalHeader) {
            item(key = "local_items_header") {
                Universal Password ManagerExpandingListHeader(
                    label = stringResource(id = Universal Password ManagerString.local_codes_header),
                    supportingLabel = state.itemList.size.toString(),
                    isExpanded = isLocalHeaderExpanded,
                    onClick = { isLocalHeaderExpanded = !isLocalHeaderExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin()
                        .animateItem(),
                )
            }
        } else if (state.sharedItems.isEmpty()) {
            item(key = "local_items_spacer") {
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
        }

        if (isLocalHeaderExpanded) {
            itemsIndexed(
                items = state.itemList,
                key = { _, it -> "local_item_${it.id}" },
            ) { index, item ->
                VaultVerificationCodeItem(
                    displayItem = item,
                    onItemClick = { onItemClick(item.authCode) },
                    onDropdownMenuClick = { action -> onDropdownMenuClick(action, item) },
                    cardStyle = state.itemList.toListItemCardStyle(index = index),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth()
                        .animateItem(),
                )
            }
        }

        when (state.sharedItems) {
            is SharedCodesDisplayState.Codes -> {
                state.sharedItems.sections.forEachIndexed { _, section ->
                    item(key = "sharedSection_${section.id}") {
                        Universal Password ManagerExpandingListHeader(
                            label = section.label(),
                            supportingLabel = section.codes.size.toString(),
                            isExpanded = section.isExpanded,
                            onClick = { onSectionExpandedClick(section) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .standardHorizontalMargin()
                                .animateItem(),
                        )
                    }
                    if (section.isExpanded) {
                        itemsIndexed(
                            items = section.codes,
                            key = { _, code -> "code_${code.id}" },
                        ) { index, item ->
                            VaultVerificationCodeItem(
                                displayItem = item,
                                onItemClick = { onItemClick(item.authCode) },
                                onDropdownMenuClick = { action ->
                                    onDropdownMenuClick(action, item)
                                },
                                cardStyle = section.codes.toListItemCardStyle(index = index),
                                modifier = Modifier
                                    .standardHorizontalMargin()
                                    .fillMaxWidth()
                                    .animateItem(),
                            )
                        }
                    }
                }
            }

            SharedCodesDisplayState.Error -> {
                item(key = "shared_codes_error") {
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Text(
                        text = stringResource(Universal Password ManagerString.shared_codes_error),
                        color = Universal Password ManagerTheme.colorScheme.text.secondary,
                        style = Universal Password ManagerTheme.typography.bodySmall,
                        modifier = Modifier
                            .standardHorizontalMargin()
                            .padding(horizontal = 16.dp)
                            .animateItem(),
                    )
                }
            }
        }

        // Add a spacer item to prevent the FAB from hiding verification codes at the
        // bottom of the list
        item {
            Spacer(modifier = Modifier.height(height = 88.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

/**
 * Displays the item listing screen with no existing items.
 */
@Suppress("LongMethod")
@Composable
fun EmptyItemListingContent(
    actionCardState: ItemListingState.ActionCardState?,
    onAddCodeClick: () -> Unit,
    onDownloadUniversal Password ManagerClick: () -> Unit,
    onDismissDownloadUniversal Password ManagerClick: () -> Unit,
    onSyncWithUniversal Password ManagerClick: () -> Unit,
    onSyncLearnMoreClick: () -> Unit,
    onDismissSyncWithUniversal Password ManagerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = when (actionCardState) {
            null -> Arrangement.Center
            ItemListingState.ActionCardState.DownloadUniversal Password ManagerApp -> Arrangement.Top
            ItemListingState.ActionCardState.SyncWithUniversal Password Manager -> Arrangement.Top
        },
    ) {
        actionCardState?.let {
            Spacer(modifier = Modifier.height(height = 12.dp))
            ActionCard(
                actionCardState = it,
                onDownloadUniversal Password ManagerClick = onDownloadUniversal Password ManagerClick,
                onDownloadUniversal Password ManagerDismissClick = onDismissDownloadUniversal Password ManagerClick,
                onSyncWithUniversal Password ManagerClick = onSyncWithUniversal Password ManagerClick,
                onSyncWithUniversal Password ManagerDismissClick = onDismissSyncWithUniversal Password ManagerClick,
                onSyncLearnMoreClick = onSyncLearnMoreClick,
                modifier = Modifier.standardHorizontalMargin(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .standardHorizontalMargin(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_authenticator),
                contentDescription = stringResource(id = Universal Password ManagerString.empty_item_list),
                modifier = Modifier
                    .size(size = 100.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = Universal Password ManagerString.you_dont_have_items_to_display),
                style = Universal Password ManagerTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = Universal Password ManagerString.empty_item_list_instruction),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Universal Password ManagerFilledButton(
                modifier = Modifier
                    .testTag("AddCodeButton")
                    .fillMaxWidth(),
                label = stringResource(Universal Password ManagerString.add_code),
                onClick = onAddCodeClick,
            )

            Spacer(modifier = Modifier.height(height = 12.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun ActionCard(
    actionCardState: ItemListingState.ActionCardState,
    onDownloadUniversal Password ManagerClick: () -> Unit,
    onDownloadUniversal Password ManagerDismissClick: () -> Unit,
    onSyncWithUniversal Password ManagerClick: () -> Unit,
    onSyncWithUniversal Password ManagerDismissClick: () -> Unit,
    onSyncLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (actionCardState) {
        ItemListingState.ActionCardState.DownloadUniversal Password ManagerApp -> {
            Universal Password ManagerActionCard(
                modifier = modifier,
                cardSubtitle = stringResource(id = Universal Password ManagerString.download_universalpasswordmanager_card_message),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.download_now.asText(),
                    onClick = onDownloadUniversal Password ManagerClick,
                ),
                cardTitle = stringResource(id = Universal Password ManagerString.download_universalpasswordmanager_card_title),
                onDismissClick = onDownloadUniversal Password ManagerDismissClick,
                leadingContent = {
                    Icon(
                        painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_shield),
                        contentDescription = null,
                        tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                    )
                },
            )
        }

        ItemListingState.ActionCardState.SyncWithUniversal Password Manager -> {
            Universal Password ManagerActionCard(
                modifier = modifier,
                cardTitle = stringResource(id = Universal Password ManagerString.sync_with_the_universalpasswordmanager_app),
                actionButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.take_me_to_app_settings.asText(),
                    onClick = onSyncWithUniversal Password ManagerClick,
                ),
                cardSubtitle = stringResource(
                    id = Universal Password ManagerString.sync_with_universalpasswordmanager_action_card_message,
                ),
                onDismissClick = onSyncWithUniversal Password ManagerDismissClick,
                secondaryButton = Universal Password ManagerButtonData(
                    label = Universal Password ManagerString.learn_more.asText(),
                    onClick = onSyncLearnMoreClick,
                ),
                leadingContent = {
                    Icon(
                        painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_refresh),
                        contentDescription = null,
                        tint = Universal Password ManagerTheme.colorScheme.icon.secondary,
                    )
                },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyListingContentPreview() {
    EmptyItemListingContent(
        modifier = Modifier.padding(horizontal = 16.dp),
        onAddCodeClick = { },
        actionCardState = ItemListingState.ActionCardState.DownloadUniversal Password ManagerApp,
        onDownloadUniversal Password ManagerClick = { },
        onDismissDownloadUniversal Password ManagerClick = { },
        onSyncWithUniversal Password ManagerClick = { },
        onSyncLearnMoreClick = { },
        onDismissSyncWithUniversal Password ManagerClick = { },
    )
}

@Composable
@Preview(showBackground = true)
private fun ContentPreview() {
    val email = "longemailaddress+verification+codes@email.com"
    Universal Password ManagerTheme {
        ItemListingContent(
            state = ItemListingState.ViewState.Content(
                actionCard = null,
                favoriteItems = persistentListOf(),
                itemList = persistentListOf(
                    VerificationCodeDisplayItem(
                        id = "",
                        title = "Local item",
                        subtitle = "with a subtitle",
                        timeLeftSeconds = 20,
                        periodSeconds = 30,
                        alertThresholdSeconds = 15,
                        authCode = "123456",
                        favorite = false,
                        showMoveToUniversal Password Manager = true,
                        showOverflow = true,
                    ),
                ),
                sharedItems = SharedCodesDisplayState.Codes(
                    sections = persistentListOf(
                        SharedCodesDisplayState.SharedCodesAccountSection(
                            id = "id",
                            label = "$email | Bitawrden.eu (1)".asText(),
                            codes = persistentListOf(
                                VerificationCodeDisplayItem(
                                    id = "",
                                    title = "Shared item",
                                    subtitle = "with a subtitle",
                                    timeLeftSeconds = 15,
                                    periodSeconds = 30,
                                    alertThresholdSeconds = 15,
                                    authCode = "123456",
                                    favorite = false,
                                    showMoveToUniversal Password Manager = false,
                                    showOverflow = false,
                                ),
                            ),
                            isExpanded = true,
                            sortKey = email,
                        ),
                    ),
                ),
            ),
            onItemClick = { },
            onDropdownMenuClick = { _, _ -> },
            onDownloadUniversal Password ManagerClick = { },
            onDismissDownloadUniversal Password ManagerClick = { },
            onSyncWithUniversal Password ManagerClick = { },
            onDismissSyncWithUniversal Password ManagerClick = { },
            onSyncLearnMoreClick = { },
            onSectionExpandedClick = { },
        )
    }
}
