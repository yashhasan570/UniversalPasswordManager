package com.universalpasswordmanager.ui.platform.feature.settings.other

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.dropdown.UniversalMultiSelectButton
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.data.platform.repository.model.ClearClipboardFrequency
import com.universalpasswordmanager.data.platform.repository.util.displayLabel
import kotlinx.collections.immutable.toImmutableList

/**
 * Displays the other screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen(
    onNavigateBack: () -> Unit,
    viewModel: OtherViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = rememberUniversalSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            OtherEvent.NavigateBack -> onNavigateBack.invoke()
            is OtherEvent.ShowSnackbar -> snackbarHostState.showSnackbar(snackbarData = event.data)
        }
    }

    OtherDialogs(
        dialogState = state.dialogState,
        onDismissRequest = { viewModel.trySendAction(OtherAction.DismissDialog) },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.other),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
                onNavigationIconClick = { viewModel.trySendAction(OtherAction.BackClick) },
            )
        },
        snackbarHost = {
            UniversalSnackbarHost(bitwardenHostState = snackbarHostState)
        },
    ) {
        OtherContent(
            state = state,
            onEnableSyncCheckChange = { viewModel.trySendAction(OtherAction.AllowSyncToggle(it)) },
            onSyncClick = { viewModel.trySendAction(OtherAction.SyncNowButtonClick) },
            onClipboardFrequencyChange = {
                viewModel.trySendAction(OtherAction.ClearClipboardFrequencyChange(it))
            },
            onScreenCaptureChange = {
                viewModel.trySendAction(OtherAction.AllowScreenCaptureToggle(it))
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun OtherContent(
    state: OtherState,
    onEnableSyncCheckChange: (Boolean) -> Unit,
    onSyncClick: () -> Unit,
    onClipboardFrequencyChange: (ClearClipboardFrequency) -> Unit,
    onScreenCaptureChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        if (!state.isPreAuth) {
            UniversalSwitch(
                label = stringResource(id = UniversalString.enable_sync_on_refresh),
                supportingText = stringResource(
                    id = UniversalString.enable_sync_on_refresh_description,
                ),
                isChecked = state.allowSyncOnRefresh,
                onCheckedChange = onEnableSyncCheckChange,
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(tag = "SyncOnRefreshSwitch")
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            UniversalOutlinedButton(
                onClick = onSyncClick,
                label = stringResource(id = UniversalString.sync_now),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(tag = "SyncNowButton")
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(tag = "LastSyncLabel")
                    .standardHorizontalMargin()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = UniversalString.last_sync),
                    style = UniversalTheme.typography.bodySmall,
                    color = UniversalTheme.colorScheme.text.secondary,
                    modifier = Modifier.padding(end = 2.dp),
                )
                Text(
                    text = state.lastSyncTime,
                    style = UniversalTheme.typography.bodySmall,
                    color = UniversalTheme.colorScheme.text.secondary,
                )
            }

            Spacer(modifier = Modifier.height(height = 16.dp))

            ClearClipboardFrequencyRow(
                currentSelection = state.clearClipboardFrequency,
                onFrequencySelection = onClipboardFrequencyChange,
                modifier = Modifier
                    .testTag(tag = "ClearClipboardChooser")
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )

            Spacer(modifier = Modifier.height(height = 8.dp))
        }

        ScreenCaptureRow(
            currentValue = state.allowScreenCapture,
            onValueChange = onScreenCaptureChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag = "AllowScreenCaptureSwitch")
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun ScreenCaptureRow(
    currentValue: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowScreenCaptureConfirmDialog by remember { mutableStateOf(false) }

    UniversalSwitch(
        label = stringResource(id = UniversalString.allow_screen_capture),
        isChecked = currentValue,
        onCheckedChange = {
            if (currentValue) {
                onValueChange(false)
            } else {
                shouldShowScreenCaptureConfirmDialog = true
            }
        },
        cardStyle = CardStyle.Full,
        modifier = modifier,
    )

    if (shouldShowScreenCaptureConfirmDialog) {
        UniversalTwoButtonDialog(
            title = stringResource(UniversalString.allow_screen_capture),
            message = stringResource(
                UniversalString.are_you_sure_you_want_to_enable_screen_capture,
            ),
            confirmButtonText = stringResource(UniversalString.yes),
            dismissButtonText = stringResource(id = UniversalString.cancel),
            onConfirmClick = {
                onValueChange(true)
                shouldShowScreenCaptureConfirmDialog = false
            },
            onDismissClick = { shouldShowScreenCaptureConfirmDialog = false },
            onDismissRequest = { shouldShowScreenCaptureConfirmDialog = false },
        )
    }
}

@Composable
private fun ClearClipboardFrequencyRow(
    currentSelection: ClearClipboardFrequency,
    onFrequencySelection: (ClearClipboardFrequency) -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    UniversalMultiSelectButton(
        label = stringResource(id = UniversalString.clear_clipboard),
        supportingText = stringResource(id = UniversalString.clear_clipboard_description),
        options = ClearClipboardFrequency.entries.map { it.displayLabel() }.toImmutableList(),
        selectedOption = currentSelection.displayLabel(),
        onOptionSelected = { selectedFrequency ->
            onFrequencySelection(
                ClearClipboardFrequency
                    .entries
                    .first { it.displayLabel.toString(resources) == selectedFrequency },
            )
        },
        textFieldTestTag = "ClearClipboardAfterLabel",
        cardStyle = CardStyle.Full,
        modifier = modifier,
    )
}

@Composable
private fun OtherDialogs(
    dialogState: OtherState.DialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is OtherState.DialogState.Loading -> UniversalLoadingDialog(
            text = dialogState.message(),
        )

        is OtherState.DialogState.Error -> UniversalBasicDialog(
            title = dialogState.title.invoke(),
            message = dialogState.message(),
            onDismissRequest = onDismissRequest,
        )

        null -> Unit
    }
}
