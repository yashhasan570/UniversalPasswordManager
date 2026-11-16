package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.deleteaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledErrorButton
import .ui.platform.components.button.Universal Password ManagerOutlinedErrorButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.components.dialog.Universal Password ManagerMasterPasswordDialog

/**
 * Displays the delete account screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(
    viewModel: DeleteAccountViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDeleteAccountConfirmation: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            DeleteAccountEvent.NavigateBack -> onNavigateBack()
            DeleteAccountEvent.NavigateToDeleteAccountConfirmationScreen -> {
                onNavigateToDeleteAccountConfirmation()
            }
        }
    }

    when (val dialog = state.dialog) {
        DeleteAccountState.DeleteAccountDialog.DeleteSuccess -> Universal Password ManagerBasicDialog(
            title = null,
            message = stringResource(
                id = Universal Password ManagerString.your_account_has_been_permanently_deleted,
            ),
            onDismissRequest = {
                viewModel.trySendAction(DeleteAccountAction.AccountDeletionConfirm)
            },
        )

        is DeleteAccountState.DeleteAccountDialog.Error -> Universal Password ManagerBasicDialog(
            title = stringResource(id = Universal Password ManagerString.an_error_has_occurred),
            message = dialog.message(),
            throwable = dialog.error,
            onDismissRequest = { viewModel.trySendAction(DeleteAccountAction.DismissDialog) },
        )

        DeleteAccountState.DeleteAccountDialog.Loading -> Universal Password ManagerLoadingDialog(
            text = stringResource(id = Universal Password ManagerString.loading),
        )

        null -> Unit
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.delete_account),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = { viewModel.trySendAction(DeleteAccountAction.CloseClick) },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (state.isUserManagedByOrganization) {
                WarningMessageCard(
                    headerText = stringResource(id = Universal Password ManagerString.cannot_delete_your_account),
                    subtitleText = stringResource(
                        id = Universal Password ManagerString.cannot_delete_your_account_explanation,
                    ),
                    modifier = Modifier.standardHorizontalMargin(),
                )
            } else {
                WarningMessageCard(
                    headerText = stringResource(
                        id = Universal Password ManagerString.deleting_your_account_is_permanent,
                    ),
                    subtitleText = stringResource(id = Universal Password ManagerString.delete_account_explanation),
                    modifier = Modifier.standardHorizontalMargin(),
                )
                Spacer(modifier = Modifier.height(24.dp))
                DeleteAccountButton(
                    onDeleteAccountConfirmDialogClick = {
                        viewModel.trySendAction(
                            DeleteAccountAction.DeleteAccountConfirmDialogClick(it),
                        )
                    },
                    onDeleteAccountClick = {
                        viewModel.trySendAction(DeleteAccountAction.DeleteAccountClick)
                    },
                    isUnlockWithPasswordEnabled = state.isUnlockWithPasswordEnabled,
                    modifier = Modifier
                        .testTag("DELETE ACCOUNT")
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Universal Password ManagerOutlinedErrorButton(
                    label = stringResource(id = Universal Password ManagerString.cancel),
                    onClick = { viewModel.trySendAction(DeleteAccountAction.CancelClick) },
                    modifier = Modifier
                        .testTag("CANCEL")
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun DeleteAccountButton(
    onDeleteAccountConfirmDialogClick: (masterPassword: String) -> Unit,
    onDeleteAccountClick: () -> Unit,
    isUnlockWithPasswordEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    var showPasswordDialog by remember { mutableStateOf(false) }
    if (showPasswordDialog) {
        Universal Password ManagerMasterPasswordDialog(
            onConfirmClick = {
                showPasswordDialog = false
                onDeleteAccountConfirmDialogClick(it)
            },
            onDismissRequest = { showPasswordDialog = false },
        )
    }

    Universal Password ManagerFilledErrorButton(
        label = stringResource(id = Universal Password ManagerString.delete_account),
        onClick = {
            if (isUnlockWithPasswordEnabled) {
                showPasswordDialog = true
            } else {
                onDeleteAccountClick()
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun WarningMessageCard(
    headerText: String,
    subtitleText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.cardStyle(
            cardStyle = CardStyle.Full,
            paddingHorizontal = 12.dp,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_warning),
            contentDescription = null,
            tint = Universal Password ManagerTheme.colorScheme.status.weak1,
        )
        Spacer(Modifier.width(width = 12.dp))
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = headerText,
                style = Universal Password ManagerTheme.typography.titleSmall,
                color = Universal Password ManagerTheme.colorScheme.status.weak1,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
                text = subtitleText,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.width(width = 4.dp))
    }
}

@Preview
@Composable
private fun WarningMessageCard_preview() {
    WarningMessageCard(
        headerText = stringResource(id = Universal Password ManagerString.cannot_delete_your_account),
        subtitleText = stringResource(id = Universal Password ManagerString.cannot_delete_your_account_explanation),
    )
}
