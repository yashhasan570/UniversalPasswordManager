package com.universalpasswordmanager.ui.auth.feature.accountsetup

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.text.UniversalClickableText
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.data.autofill.model.browser.BrowserPackage
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.browser.BrowserAutofillSettingsCard
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.browser.model.BrowserAutofillSettingsOption
import com.universalpasswordmanager.ui.platform.manager.utils.startBrowserAutofillSettingsActivity
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
                    "https://bitwarden.com/help/auto-fill-android/#browser-integrations/".toUri(),
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(
                    id = if (state.isInitialSetup) {
                        UniversalString.account_setup
                    } else {
                        UniversalString.autofill_setup
                    },
                ),
                scrollBehavior = scrollBehavior,
                navigationIcon = if (state.isInitialSetup) {
                    null
                } else {
                    NavigationIcon(
                        navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                        navigationIconContentDescription = stringResource(UniversalString.close),
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
            text = stringResource(id = UniversalString.turn_on_browser_autofill_integration),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(Modifier.height(height = 8.dp))
        Text(
            text = stringResource(
                id = if (state.browserCount > 1) {
                    UniversalString.youre_using_a_browser_that_requires_special_permissions_plural
                } else {
                    UniversalString.youre_using_a_browser_that_requires_special_permissions_singular
                },
            ),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        UniversalClickableText(
            label = stringResource(id = UniversalString.why_is_this_step_required),
            style = UniversalTheme.typography.labelMedium,
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
        UniversalFilledButton(
            label = stringResource(id = UniversalString.continue_text),
            onClick = onContinueClick,
            isEnabled = state.isContinueEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        if (state.isInitialSetup) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            UniversalOutlinedButton(
                label = stringResource(UniversalString.turn_on_later),
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
            UniversalTwoButtonDialog(
                title = stringResource(UniversalString.turn_on_browser_autofill_integration_later),
                message = stringResource(
                    id = UniversalString.return_to_complete_this_step_anytime_in_settings,
                ),
                confirmButtonText = stringResource(id = UniversalString.confirm),
                dismissButtonText = stringResource(id = UniversalString.cancel),
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
    UniversalTheme {
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
