package com.x8bit.universalpasswordmanager.ui.auth.feature.accountsetup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.appbar.NavigationIcon
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.text.Universal Password ManagerClickableText
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.data.autofill.model.browser.BrowserPackage
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.browser.BrowserAutofillSettingsCard
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.browser.model.BrowserAutofillSettingsOption
import com.x8bit.universalpasswordmanager.ui.platform.manager.utils.startBrowserAutofillSettingsActivity
import kotlinx.collections.immutable.persistentListOf

/**
 * Top level composable for the Setup Browser Autofill screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun SetupBrowserAutofillScreen(
    viewModel: SetupBrowserAutofillViewModel,
    intentManager: IntentManager = LocalIntentManager.current,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            SetupBrowserAutofillEvent.NavigateBack -> onNavigateBack()
            is SetupBrowserAutofillEvent.NavigateToBrowserAutofillSettings -> {
                intentManager.startBrowserAutofillSettingsActivity(
                    browserPackage = event.browserPackage,
                )
            }

            SetupBrowserAutofillEvent.NavigateToBrowserIntegrationsInfo -> {
                intentManager.launchUri(
                    "https:///help/auto-fill-android/#browser-integrations/".toUri(),
                )
            }
        }
    }
    SetupBrowserAutofillDialogs(
        dialogState = state.dialogState,
        onDismissDialog = { viewModel.trySendAction(SetupBrowserAutofillAction.DismissDialog) },
        onTurnOnLaterConfirm = {
            viewModel.trySendAction(SetupBrowserAutofillAction.TurnOnLaterConfirmClick)
        },
    )
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
                        navigationIconContentDescription = stringResource(Universal Password ManagerString.close),
                        onNavigationIconClick = {
                            viewModel.trySendAction(SetupBrowserAutofillAction.CloseClick)
                        },
                    )
                },
            )
        },
    ) {
        SetupBrowserAutofillContent(
            state = state,
            onWhyIsThisStepRequiredClick = {
                viewModel.trySendAction(SetupBrowserAutofillAction.WhyIsThisStepRequiredClick)
            },
            onBrowserClick = {
                viewModel.trySendAction(SetupBrowserAutofillAction.BrowserIntegrationClick(it))
            },
            onContinueClick = { viewModel.trySendAction(SetupBrowserAutofillAction.ContinueClick) },
            onTurnOnLaterClick = {
                viewModel.trySendAction(SetupBrowserAutofillAction.TurnOnLaterClick)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun SetupBrowserAutofillContent(
    state: SetupBrowserAutofillState,
    onWhyIsThisStepRequiredClick: () -> Unit,
    onBrowserClick: (BrowserPackage) -> Unit,
    onContinueClick: () -> Unit,
    onTurnOnLaterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(height = 24.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.turn_on_browser_autofill_integration),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.height(height = 8.dp))
        Text(
            text = stringResource(
                id = if (state.browserCount > 1) {
                    Universal Password ManagerString.youre_using_a_browser_that_requires_special_permissions_plural
                } else {
                    Universal Password ManagerString.youre_using_a_browser_that_requires_special_permissions_singular
                },
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Universal Password ManagerClickableText(
            label = stringResource(id = Universal Password ManagerString.why_is_this_step_required),
            style = Universal Password ManagerTheme.typography.labelMedium,
            onClick = onWhyIsThisStepRequiredClick,
            isExternalLink = true,
            modifier = Modifier
                .wrapContentWidth()
                .align(alignment = Alignment.CenterHorizontally)
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        BrowserAutofillSettingsCard(
            options = state.browserAutofillSettingsOptions,
            onOptionClicked = onBrowserClick,
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.continue_text),
            onClick = onContinueClick,
            isEnabled = state.isContinueEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        if (state.isInitialSetup) {
            Spacer(modifier = Modifier.height(height = 12.dp))
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
private fun SetupBrowserAutofillDialogs(
    dialogState: SetupBrowserAutofillState.DialogState?,
    onTurnOnLaterConfirm: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    when (dialogState) {
        SetupBrowserAutofillState.DialogState.TurnOnLaterDialog -> {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.turn_on_browser_autofill_integration_later),
                message = stringResource(
                    id = Universal Password ManagerString.return_to_complete_this_step_anytime_in_settings,
                ),
                confirmButtonText = stringResource(id = Universal Password ManagerString.confirm),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = onTurnOnLaterConfirm,
                onDismissClick = onDismissDialog,
                onDismissRequest = onDismissDialog,
            )
        }

        null -> Unit
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SetupBrowserAutofillContent_preview() {
    Universal Password ManagerTheme {
        SetupBrowserAutofillContent(
            state = SetupBrowserAutofillState(
                dialogState = null,
                isInitialSetup = true,
                browserAutofillSettingsOptions = persistentListOf(
                    BrowserAutofillSettingsOption.BraveStable(enabled = true),
                    BrowserAutofillSettingsOption.ChromeStable(enabled = false),
                    BrowserAutofillSettingsOption.ChromeBeta(enabled = true),
                ),
            ),
            onWhyIsThisStepRequiredClick = { },
            onBrowserClick = { },
            onContinueClick = { },
            onTurnOnLaterClick = { },
        )
    }
}
