package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.vault

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.mirrorIfRtl
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.badge.NotificationBadge
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.card.actionCardExitAnimation
import .ui.platform.components.model.CardStyle
import .ui.platform.components.row.Universal Password ManagerTextRow
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText

/**
 * Displays the vault settings screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultSettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExportVault: () -> Unit,
    onNavigateToFolders: () -> Unit,
    onNavigateToImportLogins: () -> Unit,
    onNavigateToImportItems: () -> Unit,
    viewModel: VaultSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            VaultSettingsEvent.NavigateBack -> onNavigateBack()
            VaultSettingsEvent.NavigateToExportVault -> onNavigateToExportVault()
            VaultSettingsEvent.NavigateToFolders -> onNavigateToFolders()
            is VaultSettingsEvent.NavigateToImportVault -> onNavigateToImportLogins()
            is VaultSettingsEvent.NavigateToImportItems -> onNavigateToImportItems()
            is VaultSettingsEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.vault),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = { viewModel.trySendAction(VaultSettingsAction.BackClick) },
            )
        },
        snackbarHost = {
            Universal Password ManagerSnackbarHost(
                universalpasswordmanagerHostState = snackbarHostState,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(
                visible = state.showImportActionCard,
                label = "ImportLoginsActionCard",
                exit = actionCardExitAnimation(),
            ) {
                Universal Password ManagerActionCard(
                    cardTitle = stringResource(id = Universal Password ManagerString.import_saved_logins),
                    actionButton = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.get_started.asText(),
                        onClick = {
                            viewModel.trySendAction(VaultSettingsAction.ImportLoginsCardCtaClick)
                        },
                    ),
                    cardSubtitle = stringResource(Universal Password ManagerString.use_a_computer_to_import_logins),
                    onDismissClick = {
                        viewModel.trySendAction(VaultSettingsAction.ImportLoginsCardDismissClick)
                    },
                    leadingContent = {
                        NotificationBadge(notificationCount = 1)
                    },
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .padding(bottom = 16.dp),
                )
            }
            Universal Password ManagerTextRow(
                text = stringResource(id = Universal Password ManagerString.my_folders),
                onClick = { viewModel.trySendAction(VaultSettingsAction.FoldersButtonClick) },
                withDivider = false,
                cardStyle = CardStyle.Top(),
                modifier = Modifier
                    .testTag("FoldersLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Universal Password ManagerTextRow(
                text = stringResource(Universal Password ManagerString.export_vault),
                onClick = { viewModel.trySendAction(VaultSettingsAction.ExportVaultClick) },
                withDivider = false,
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .testTag("ExportVaultLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            Universal Password ManagerTextRow(
                text = stringResource(Universal Password ManagerString.import_items),
                onClick = { viewModel.trySendAction(VaultSettingsAction.ImportItemsClick) },
                withDivider = false,
                cardStyle = CardStyle.Bottom,
                modifier = Modifier
                    .testTag("ImportItemsLinkItemView")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            ) {
                if (state.showImportItemsChevron) {
                    Icon(
                        painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_chevron_right),
                        contentDescription = null,
                        tint = Universal Password ManagerTheme.colorScheme.icon.primary,
                        modifier = Modifier
                            .mirrorIfRtl()
                            .size(size = 16.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
