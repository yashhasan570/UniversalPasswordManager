package com.universalpasswordmanager.ui.platform.feature.settings.vault

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.mirrorIfRtl
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.badge.NotificationBadge
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCard
import com.universalpasswordmanager.ui.platform.components.card.actionCardExitAnimation
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.row.UniversalTextRow
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText

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

    val snackbarHostState = rememberUniversalSnackbarHostState()
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.vault),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
                onNavigationIconClick = { viewModel.trySendAction(VaultSettingsAction.BackClick) },
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(
                bitwardenHostState = snackbarHostState,
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
                UniversalActionCard(
                    cardTitle = stringResource(id = UniversalString.import_saved_logins),
                    actionButton = UniversalButtonData(
                        label = UniversalString.get_started.asText(),
                        onClick = {
                            viewModel.trySendAction(VaultSettingsAction.ImportLoginsCardCtaClick)
                        },
                    ),
                    cardSubtitle = stringResource(UniversalString.use_a_computer_to_import_logins),
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
            UniversalTextRow(
                text = stringResource(id = UniversalString.my_folders),
                onClick = { viewModel.trySendAction(VaultSettingsAction.FoldersButtonClick) },
                withDivider = false,
                cardStyle = CardStyle.Top(),
                modifier = Modifier
                    .testTag("FoldersLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            UniversalTextRow(
                text = stringResource(UniversalString.export_vault),
                onClick = { viewModel.trySendAction(VaultSettingsAction.ExportVaultClick) },
                withDivider = false,
                cardStyle = CardStyle.Middle(),
                modifier = Modifier
                    .testTag("ExportVaultLabel")
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )

            UniversalTextRow(
                text = stringResource(UniversalString.import_items),
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
                        painter = rememberVectorPainter(id = UniversalDrawable.ic_chevron_right),
                        contentDescription = null,
                        tint = UniversalTheme.colorScheme.icon.primary,
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
