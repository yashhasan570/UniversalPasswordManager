@file:Suppress("TooManyFunctions")

package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill

import android.content.res.Resources
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .core.util.persistentListOfNotNull
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.spanStyleOf
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toAnnotatedString
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.badge.NotificationBadge
import .ui.platform.components.button.model.Universal Password ManagerButtonData
import .ui.platform.components.button.model.Universal Password ManagerHelpButtonData
import .ui.platform.components.card.Universal Password ManagerActionCard
import .ui.platform.components.card.Universal Password ManagerActionCardSmall
import .ui.platform.components.card.actionCardExitAnimation
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.dropdown.model.MultiSelectOption
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.row.Universal Password ManagerExternalLinkRow
import .ui.platform.components.row.Universal Password ManagerPushRow
import .ui.platform.components.row.Universal Password ManagerTextRow
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.manager.util.startSystemAccessibilitySettingsActivity
import .ui.platform.manager.util.startSystemAutofillSettingsActivity
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.UriMatchType
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.browser.BrowserAutofillSettingsCard
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.handlers.AutoFillHandlers
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.util.displayLabel
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.util.isAdvancedMatching
import com.x8bit.universalpasswordmanager.ui.platform.manager.utils.startBrowserAutofillSettingsActivity
import kotlinx.collections.immutable.toImmutableList

/**
 * Displays the auto-fill screen.
 */
@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoFillScreen(
    onNavigateBack: () -> Unit,
    viewModel: AutoFillViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
    onNavigateToBlockAutoFillScreen: () -> Unit,
    onNavigateToSetupAutofill: () -> Unit,
    onNavigateToSetupBrowserAutofill: () -> Unit,
    onNavigateToAboutPrivilegedAppsScreen: () -> Unit,
    onNavigateToPrivilegedAppsList: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    var shouldShowAutofillFallbackDialog by rememberSaveable { mutableStateOf(false) }
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            AutoFillEvent.NavigateBack -> onNavigateBack.invoke()

            AutoFillEvent.NavigateToAccessibilitySettings -> {
                intentManager.startSystemAccessibilitySettingsActivity()
            }

            AutoFillEvent.NavigateToAutofillSettings -> {
                val isSuccess = intentManager.startSystemAutofillSettingsActivity()
                shouldShowAutofillFallbackDialog = !isSuccess
            }

            AutoFillEvent.NavigateToBlockAutoFill -> {
                onNavigateToBlockAutoFillScreen()
            }

            AutoFillEvent.NavigateToSettings -> {
                intentManager.startCredentialManagerSettings()
            }

            AutoFillEvent.NavigateToSetupAutofill -> onNavigateToSetupAutofill()
            AutoFillEvent.NavigateToSetupBrowserAutofill -> onNavigateToSetupBrowserAutofill()
            is AutoFillEvent.NavigateToBrowserAutofillSettings -> {
                intentManager.startBrowserAutofillSettingsActivity(
                    browserPackage = event.browserPackage,
                )
            }

            AutoFillEvent.NavigateToAboutPrivilegedAppsScreen -> {
                onNavigateToAboutPrivilegedAppsScreen()
            }

            AutoFillEvent.NavigateToPrivilegedAppsListScreen -> {
                onNavigateToPrivilegedAppsList()
            }

            AutoFillEvent.NavigateToFillAssistHelp -> {
                intentManager.launchUri(
                    "https:///help/fill-assist/".toUri(),
                )
            }

            AutoFillEvent.NavigateToLearnMore -> {
                intentManager.launchUri("https:///help/uri-match-detection/".toUri())
            }

            AutoFillEvent.NavigateToAutofillHelp -> {
                intentManager.launchUri(
                    uri = "https:///help/auto-fill-android-troubleshooting/".toUri(),
                )
            }
        }
    }

    if (shouldShowAutofillFallbackDialog) {
        Universal Password ManagerBasicDialog(
            title = null,
            message = stringResource(
                id = Universal Password ManagerString.we_were_unable_to_automatically_open_the_autofill_settings,
            ),
            onDismissRequest = { shouldShowAutofillFallbackDialog = false },
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val autoFillHandlers = remember(viewModel) { AutoFillHandlers.create(viewModel = viewModel) }
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.autofill_noun),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = { viewModel.trySendAction(AutoFillAction.BackClick) },
            )
        },
    ) {
        AutoFillScreenContent(
            state = state,
            autoFillHandlers = autoFillHandlers,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun AutoFillScreenContent(
    state: AutoFillState,
    autoFillHandlers: AutoFillHandlers,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        AutofillCallToActionCard(
            state = state,
            autoFillHandlers = autoFillHandlers,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.autofill_noun),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        val autofillServicesLabel = stringResource(id = Universal Password ManagerString.autofill_services)
        Universal Password ManagerSwitch(
            label = autofillServicesLabel,
            supportingText = stringResource(
                id = Universal Password ManagerString.autofill_services_explanation_long,
            ),
            contentDescription = if (state.isAutoFillServicesEnabled) {
                autofillServicesLabel
            } else {
                stringResource(
                    id = Universal Password ManagerString.external_link_format,
                    formatArgs = arrayOf(autofillServicesLabel),
                )
            },
            isChecked = state.isAutoFillServicesEnabled,
            onCheckedChange = autoFillHandlers.onAutofillServicesClick,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("AutofillServicesSwitch")
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))

        AnimatedVisibility(visible = state.showInlineAutofill) {
            Column {
                FillStyleSelector(
                    selectedStyle = state.autofillStyle,
                    onStyleChange = autoFillHandlers.onAutofillStyleChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
            }
        }

        AnimatedVisibility(visible = state.showBrowserSettingOptions) {
            Column {
                BrowserAutofillSettingsCard(
                    options = state.browserAutofillSettingsOptions,
                    onOptionClicked = autoFillHandlers.onBrowserAutofillSelected,
                    supportingText = stringResource(
                        id = Universal Password ManagerString
                            .improves_login_filling_for_supported_websites_on_selected_browsers,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (state.showPasskeyManagementRow) {
            Universal Password ManagerExternalLinkRow(
                text = stringResource(id = Universal Password ManagerString.passkey_management),
                description = Universal Password ManagerString
                    .passkey_management_explanation_long
                    .toAnnotatedString(),
                onConfirmClick = autoFillHandlers.onPasskeyManagementClick,
                dialogTitle = stringResource(id = Universal Password ManagerString.continue_to_device_settings),
                dialogMessage = stringResource(
                    id = Universal Password ManagerString.set_universalpasswordmanager_as_passkey_manager_description,
                ),
                withDivider = false,
                cardStyle = CardStyle.Top(),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            Universal Password ManagerTextRow(
                text = stringResource(Universal Password ManagerString.privileged_apps),
                onClick = autoFillHandlers.onPrivilegedAppsClick,
                helpData = Universal Password ManagerHelpButtonData(
                    contentDescription = stringResource(
                        id = Universal Password ManagerString.learn_more_about_privileged_apps,
                    ),
                    onClick = autoFillHandlers.onPrivilegedAppsHelpLinkClick,
                    isExternalLink = false,
                ),
                cardStyle = CardStyle.Bottom,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
        }
        AccessibilityAutofillSwitch(
            isAccessibilityAutoFillEnabled = state.isAccessibilityAutofillEnabled,
            onCheckedChange = autoFillHandlers.onUseAccessibilityServiceClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Universal Password ManagerListHeaderText(
            label = stringResource(id = Universal Password ManagerString.additional_options),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        if (state.showFillAssistOption) {
            Spacer(modifier = Modifier.height(8.dp))
            Universal Password ManagerSwitch(
                label = stringResource(id = Universal Password ManagerString.turn_on_fill_assist),
                supportingText = stringResource(
                    id = Universal Password ManagerString.fill_assist_improves_autofill_accuracy_on_supported_sites,
                ),
                isChecked = state.isFillAssistEnabled,
                onCheckedChange = autoFillHandlers.onFillAssistToggleClick,
                helpData = Universal Password ManagerHelpButtonData(
                    onClick = autoFillHandlers.onFillAssistInfoClick,
                    contentDescription = stringResource(
                        id = Universal Password ManagerString.learn_more,
                    ),
                    isExternalLink = true,
                ),
                cardStyle = CardStyle.Full,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("FillAssistSwitch")
                    .standardHorizontalMargin(),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Universal Password ManagerSwitch(
            label = stringResource(id = Universal Password ManagerString.copy_totp_automatically),
            supportingText = stringResource(
                id = Universal Password ManagerString.copy_totp_automatically_description,
            ),
            isChecked = state.isCopyTotpAutomaticallyEnabled,
            onCheckedChange = autoFillHandlers.onCopyTotpAutomaticallyClick,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("CopyTotpAutomaticallySwitch")
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Universal Password ManagerSwitch(
            label = stringResource(id = Universal Password ManagerString.ask_to_add_item),
            supportingText = stringResource(id = Universal Password ManagerString.ask_to_add_item_description),
            isChecked = state.isAskToAddLoginEnabled,
            onCheckedChange = autoFillHandlers.onAskToAddLoginClick,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("AskToAddLoginSwitch")
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        DefaultUriMatchTypeRow(
            selectedUriMatchType = state.defaultUriMatchType,
            onUriMatchTypeSelect = autoFillHandlers.onDefaultUriMatchTypeSelect,
            onNavigateToLearnMore = autoFillHandlers.onLearnMoreClick,
            modifier = Modifier
                .testTag("DefaultUriMatchDetectionChooser")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Universal Password ManagerPushRow(
            text = stringResource(id = Universal Password ManagerString.block_auto_fill),
            description = stringResource(
                id = Universal Password ManagerString.auto_fill_will_not_be_offered_for_these_ur_is,
            ),
            onClick = autoFillHandlers.onBlockAutoFillClick,
            cardStyle = CardStyle.Full,
            modifier = Modifier
                .testTag("BlockAutoFillButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun AutofillCallToActionCard(
    state: AutoFillState,
    autoFillHandlers: AutoFillHandlers,
    modifier: Modifier,
) {
    AnimatedContent(
        targetState = state.ctaState,
        label = "AutofillActionCard",
        transitionSpec = { EnterTransition.None.togetherWith(actionCardExitAnimation()) },
        modifier = modifier,
    ) {
        when (it) {
            CtaState.AUTOFILL -> {
                Universal Password ManagerActionCard(
                    cardTitle = stringResource(id = Universal Password ManagerString.turn_on_autofill),
                    actionButton = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.get_started.asText(),
                        onClick = autoFillHandlers.onAutofillActionCardClick,
                    ),
                    onDismissClick = autoFillHandlers.onAutofillActionCardDismissClick,
                    leadingContent = { NotificationBadge(notificationCount = 1) },
                )
            }

            CtaState.BROWSER_AUTOFILL -> {
                val subTitleRes = if (state.browserCount > 1) {
                    Universal Password ManagerString.browser_requires_special_permissions_for_universalpasswordmanager_plural
                } else {
                    Universal Password ManagerString.browser_requires_special_permissions_for_universalpasswordmanager_singular
                }
                Universal Password ManagerActionCard(
                    cardTitle = stringResource(
                        id = Universal Password ManagerString.turn_on_browser_autofill_integration,
                    ),
                    cardSubtitle = stringResource(id = subTitleRes),
                    actionButton = Universal Password ManagerButtonData(
                        label = Universal Password ManagerString.get_started.asText(),
                        onClick = autoFillHandlers.onBrowserAutofillActionCardClick,
                    ),
                    onDismissClick = autoFillHandlers.onBrowserAutofillActionCardDismissClick,
                    leadingContent = { NotificationBadge(notificationCount = 1) },
                )
            }

            CtaState.DEFAULT -> {
                Universal Password ManagerActionCardSmall(
                    actionIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_question_circle),
                    actionText = stringResource(id = Universal Password ManagerString.having_trouble_with_autofill),
                    callToActionText = stringResource(
                        id = Universal Password ManagerString.access_help_and_troubleshooting_documentation_here,
                    ),
                    onCardClicked = autoFillHandlers.onHelpCardClick,
                    trailingContent = {
                        Icon(
                            painter = rememberVectorPainter(Universal Password ManagerDrawable.ic_chevron_right),
                            contentDescription = stringResource(id = Universal Password ManagerString.external_link),
                            tint = Universal Password ManagerTheme.colorScheme.icon.primary,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun FillStyleSelector(
    selectedStyle: AutofillStyle,
    onStyleChange: (AutofillStyle) -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.display_autofill_suggestions),
        supportingText = stringResource(id = Universal Password ManagerString.use_inline_autofill_explanation_long),
        options = AutofillStyle.entries.map { it.label() }.toImmutableList(),
        selectedOption = selectedStyle.label(),
        onOptionSelected = {
            onStyleChange(AutofillStyle.entries.first { style -> style.label(resources) == it })
        },
        cardStyle = CardStyle.Full,
        modifier = modifier.testTag(tag = "InlineAutofillSelector"),
    )
}

@Composable
private fun AccessibilityAutofillSwitch(
    isAccessibilityAutoFillEnabled: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowDialog by rememberSaveable { mutableStateOf(value = false) }
    Universal Password ManagerSwitch(
        label = stringResource(id = Universal Password ManagerString.accessibility),
        supportingText = stringResource(id = Universal Password ManagerString.accessibility_description5),
        isChecked = isAccessibilityAutoFillEnabled,
        onCheckedChange = {
            if (isAccessibilityAutoFillEnabled) {
                onCheckedChange()
            } else {
                shouldShowDialog = true
            }
        },
        cardStyle = CardStyle.Full,
        modifier = modifier.testTag(tag = "AccessibilityAutofillSwitch"),
    )

    if (shouldShowDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(id = Universal Password ManagerString.accessibility_service_disclosure),
            message = stringResource(id = Universal Password ManagerString.accessibility_disclosure_text),
            confirmButtonText = stringResource(id = Universal Password ManagerString.accept),
            dismissButtonText = stringResource(id = Universal Password ManagerString.decline),
            onConfirmClick = {
                onCheckedChange()
                shouldShowDialog = false
            },
            onDismissClick = { shouldShowDialog = false },
            onDismissRequest = { shouldShowDialog = false },
        )
    }
}

@Composable
private fun DefaultUriMatchTypeRow(
    selectedUriMatchType: UriMatchType,
    onUriMatchTypeSelect: (UriMatchType) -> Unit,
    onNavigateToLearnMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAdvancedDialog by rememberSaveable { mutableStateOf(false) }
    var optionPendingConfirmation by rememberSaveable { mutableStateOf<UriMatchType?>(null) }
    var shouldShowLearnMoreMatchDetectionDialog by rememberSaveable { mutableStateOf(false) }

    UriMatchSelectionButton(
        selectedUriMatchType = selectedUriMatchType,
        onOptionSelected = { selectedOption ->
            if (selectedOption.isAdvancedMatching()) {
                optionPendingConfirmation = selectedOption
                showAdvancedDialog = true
            } else {
                onUriMatchTypeSelect(selectedOption)
                optionPendingConfirmation = null
                showAdvancedDialog = false
            }
        },
        modifier = modifier,
    )

    val currentOptionToConfirm = optionPendingConfirmation
    if (showAdvancedDialog && currentOptionToConfirm != null) {
        AdvancedMatchDetectionWarningDialog(
            pendingOption = currentOptionToConfirm,
            onDialogConfirm = {
                onUriMatchTypeSelect(currentOptionToConfirm)
                showAdvancedDialog = false
                optionPendingConfirmation = null
                shouldShowLearnMoreMatchDetectionDialog = true
            },
            onDialogDismiss = {
                showAdvancedDialog = false
                optionPendingConfirmation = null
            },
        )
    }

    if (shouldShowLearnMoreMatchDetectionDialog) {
        MatchDetectionLearnMoreDialog(
            uriMatchType = selectedUriMatchType,
            onDialogConfirm = {
                onNavigateToLearnMore()
                shouldShowLearnMoreMatchDetectionDialog = false
            },
            onDialogDismiss = {
                shouldShowLearnMoreMatchDetectionDialog = false
            },
        )
    }
}

@Composable
private fun AdvancedMatchDetectionWarningDialog(
    pendingOption: UriMatchType,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
) {
    val descriptionStringResId =
        when (pendingOption) {
            UriMatchType.STARTS_WITH -> {
                Universal Password ManagerString.advanced_option_with_increased_risk_of_exposing_credentials
            }

            UriMatchType.REGULAR_EXPRESSION -> {
                Universal Password ManagerString.advanced_option_increased_risk_exposing_credentials_used_incorrectly
            }

            UriMatchType.HOST,
            UriMatchType.DOMAIN,
            UriMatchType.EXACT,
            UriMatchType.NEVER,
                -> {
                error("Unexpected value $pendingOption on AdvancedMatchDetectionWarningDialog")
            }
        }

    Universal Password ManagerTwoButtonDialog(
        title = stringResource(
            id = Universal Password ManagerString.are_you_sure_you_want_to_use,
            formatArgs = arrayOf(
                pendingOption.displayLabel(),
            ),
        ),
        message = stringResource(
            id = descriptionStringResId,
        ),
        confirmButtonText = stringResource(id = Universal Password ManagerString.yes),
        dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
        onConfirmClick = onDialogConfirm,
        onDismissClick = onDialogDismiss,
        onDismissRequest = onDialogDismiss,
    )
}

@Composable
private fun UriMatchSelectionButton(
    selectedUriMatchType: UriMatchType,
    onOptionSelected: (UriMatchType) -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    val advancedOptions = UriMatchType.entries.filter { it.isAdvancedMatching() }
    val options = persistentListOfNotNull(
        *UriMatchType
            .entries
            .filter { !it.isAdvancedMatching() }
            .map { MultiSelectOption.Row(it.displayLabel()) }
            .toTypedArray(),
        if (advancedOptions.isNotEmpty()) {
            MultiSelectOption.Header(
                title = stringResource(id = Universal Password ManagerString.advanced_options),
                testTag = "AdvancedOptionsSection",
            )
        } else {
            null
        },
        *advancedOptions
            .map { MultiSelectOption.Row(it.displayLabel()) }
            .toTypedArray(),
    )

    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.default_uri_match_detection),
        options = options,
        selectedOption = MultiSelectOption.Row(selectedUriMatchType.displayLabel()),
        onOptionSelected = { row ->
            val newSelectedType = UriMatchType
                .entries
                .first { it.displayLabel(resources) == row.title }
            onOptionSelected(newSelectedType)
        },
        cardStyle = CardStyle.Full,
        supportingContent = { SupportingTextForMatchDetection(selectedUriMatchType) },
        modifier = modifier,
    )
}

@Composable
private fun MatchDetectionLearnMoreDialog(
    uriMatchType: UriMatchType,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
) {
    Universal Password ManagerTwoButtonDialog(
        title = stringResource(id = Universal Password ManagerString.keep_your_credential_secure),
        message = stringResource(
            id = Universal Password ManagerString.learn_more_about_how_to_keep_credentirals_secure,
            formatArgs = arrayOf(uriMatchType.displayLabel()),
        ),
        confirmButtonText = stringResource(id = Universal Password ManagerString.learn_more),
        dismissButtonText = stringResource(id = Universal Password ManagerString.close),
        onConfirmClick = onDialogConfirm,
        onDismissClick = onDialogDismiss,
        onDismissRequest = onDialogDismiss,
    )
}

@Composable
private fun SupportingTextForMatchDetection(
    uriMatchType: UriMatchType,
) {
    val stringResId =
        when (uriMatchType) {
            UriMatchType.STARTS_WITH -> {
                Universal Password ManagerString.default_uri_match_detection_description_advanced_options
            }

            UriMatchType.REGULAR_EXPRESSION -> {
                Universal Password ManagerString.default_uri_match_detection_description_advanced_options_incorrectly
            }

            UriMatchType.HOST,
            UriMatchType.DOMAIN,
            UriMatchType.EXACT,
            UriMatchType.NEVER,
                -> {
                Universal Password ManagerString.default_uri_match_detection_description
            }
        }

    val supportingAnnotatedString =
        annotatedStringResource(
            id = stringResId,
            emphasisHighlightStyle = spanStyleOf(
                textStyle = Universal Password ManagerTheme.typography.bodyMediumEmphasis,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
            ),
            style = spanStyleOf(
                textStyle = Universal Password ManagerTheme.typography.bodySmall,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
            ),
        )

    Text(
        text = supportingAnnotatedString,
        style = Universal Password ManagerTheme.typography.bodySmall,
        color = Universal Password ManagerTheme.colorScheme.text.secondary,
        modifier = Modifier.fillMaxWidth(),
    )
}
