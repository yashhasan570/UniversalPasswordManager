package com.x8bit.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.image.Universal Password ManagerGifImage
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.util.startSystemAutofillSettingsActivity
import .ui.platform.model.WindowSize
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.util.rememberWindowSize
import com.x8bit.universalpasswordmanager.ui.auth.feature.accountsetup.handlers.rememberSetupAutoFillHandler

/**
 * Top level composable for the Auto-fill setup screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupAutoFillScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBrowserAutofill: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: SetupAutoFillViewModel,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberSetupAutoFillHandler(viewModel = viewModel)
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            SetupAutoFillEvent.NavigateToAutofillSettings -> {
                val showFallback = !intentManager.startSystemAutofillSettingsActivity()
                if (showFallback) {
                    handler.sendAutoFillServiceFallback.invoke()
                }
            }

            SetupAutoFillEvent.NavigateBack -> onNavigateBack()
            SetupAutoFillEvent.NavigateToBrowserAutofill -> onNavigateToBrowserAutofill()
        }
    }
    when (state.dialogState) {
        is SetupAutoFillDialogState.AutoFillFallbackDialog -> {
            Universal Password ManagerBasicDialog(
                title = null,
                message = stringResource(id = Universal Password ManagerString.universalpasswordmanager_autofill_go_to_settings),
                onDismissRequest = handler.onDismissDialog,
            )
        }

        is SetupAutoFillDialogState.TurnOnLaterDialog -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.turn_on_autofill_later),
                message = stringResource(
                    id = Universal Password ManagerString.return_to_complete_this_step_anytime_in_settings,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.confirm),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = handler.onConfirmTurnOnLaterClick,
                onDismissClick = handler.onDismissDialog,
                onDismissRequest = handler.onDismissDialog,
            )
        }

        null -> Unit
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(
                    id = if (state.isInitialSetup) {
                        Universal Password ManagerString.account_setup
                    } else {
                        Universal Password ManagerString.autofill_setup
                    },
                ),
                scrollBehavior = scrollBehavior,
                navigationIcon = if (state.isInitialSetup) {
                    null
                } else {
                    NavigationIcon(
                        navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                        navigationIconContentDescription = stringResource(
                            id = Universal Password ManagerString.close,
                        ),
                        onNavigationIconClick = {
                            viewModel.trySendAction(SetupAutoFillAction.CloseClick)
                        },
                    )
                },
            )
        },
    ) {
        SetupAutoFillContent(
            state = state,
            onAutofillServiceChanged = { handler.onAutofillServiceChanged(it) },
            onContinueClick = handler.onContinueClick,
            onTurnOnLaterClick = handler.onTurnOnLaterClick,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun SetupAutoFillContent(
    state: SetupAutoFillState,
    onAutofillServiceChanged: (Boolean) -> Unit,
    onContinueClick: () -> Unit,
    onTurnOnLaterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(Modifier.height(8.dp))
        SetupAutoFillContentHeader(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Universal Password ManagerSwitch(
            label = stringResource(
                Universal Password ManagerString.autofill_services,
            ),
            isChecked = state.autofillEnabled,
            onCheckedChange = onAutofillServiceChanged,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.continue_text),
            onClick = onContinueClick,
            isEnabled = state.autofillEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (state.isInitialSetup) {
            Universal Password ManagerOutlinedButton(
                label = stringResource(Universal Password ManagerString.turn_on_later),
                onClick = onTurnOnLaterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun SetupAutoFillContentHeader(
    modifier: Modifier = Modifier,
) {
    when (rememberWindowSize()) {
        WindowSize.Compact -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OrderedHeaderContent()
            }
        }

        WindowSize.Medium -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OrderedHeaderContent()
            }
        }
    }
}

@Composable
private fun OrderedHeaderContent() {
    Universal Password ManagerGifImage(
        resId = Universal Password ManagerDrawable.gif_setup_autofill,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp,
                ),
            )
            .size(
                width = 230.dp,
                height = 280.dp,
            ),
    )
    Spacer(modifier = Modifier.size(24.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Universal Password ManagerString.turn_on_autofill),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Universal Password ManagerString.use_autofill_to_log_into_your_accounts),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            // Apply similar line breaks to design
            modifier = Modifier.sizeIn(maxWidth = 300.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SetupAutoFillContentDisabled_preview() {
    Universal Password ManagerTheme {
        SetupAutoFillContent(
            state = SetupAutoFillState(
                userId = "disputationi",
                dialogState = null,
                autofillEnabled = false,
                isInitialSetup = true,
            ),
            onAutofillServiceChanged = {},
            onContinueClick = {},
            onTurnOnLaterClick = {},
        )
    }
}

@PreviewScreenSizes
@Composable
private fun SetupAutoFillContentEnabled_preview() {
    Universal Password ManagerTheme {
        SetupAutoFillContent(
            state = SetupAutoFillState(
                userId = "disputationi",
                dialogState = null,
                autofillEnabled = true,
                isInitialSetup = true,
            ),
            onAutofillServiceChanged = {},
            onContinueClick = {},
            onTurnOnLaterClick = {},
        )
    }
}
