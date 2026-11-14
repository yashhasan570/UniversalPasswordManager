@file:Suppress("TooManyFunctions")

package com.universalpasswordmanager.ui.platform.feature.settings.autofill

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
import com.universalpasswordmanager.core.util.persistentListOfNotNull
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.spanStyleOf
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toAnnotatedString
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.badge.NotificationBadge
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalHelpButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCard
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCardSmall
import com.universalpasswordmanager.ui.platform.components.card.actionCardExitAnimation
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.dropdown.UniversalMultiSelectButton
import com.universalpasswordmanager.ui.platform.components.dropdown.model.MultiSelectOption
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.row.UniversalExternalLinkRow
import com.universalpasswordmanager.ui.platform.components.row.UniversalPushRow
import com.universalpasswordmanager.ui.platform.components.row.UniversalTextRow
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.manager.util.startSystemAccessibilitySettingsActivity
import com.universalpasswordmanager.ui.platform.manager.util.startSystemAutofillSettingsActivity
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.UriMatchType
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.browser.BrowserAutofillSettingsCard
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.handlers.AutoFillHandlers
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.util.displayLabel
import com.universalpasswordmanager.ui.platform.feature.settings.autofill.util.isAdvancedMatching
import com.universalpasswordmanager.ui.platform.manager.utils.startBrowserAutofillSettingsActivity
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
                    "https://bitwarden.com/help/fill-assist/".toUri(),
                )
            }

            AutoFillEvent.NavigateToLearnMore -> {
                intentManager.launchUri("https://bitwarden.com/help/uri-match-detection/".toUri())
            }

            AutoFillEvent.NavigateToAutofillHelp -> {
                intentManager.launchUri(
                    uri = "https://bitwarden.com/help/auto-fill-android-troubleshooting/".toUri(),
                )
            }
        }
    }

    if (shouldShowAutofillFallbackDialog) {
        UniversalBasicDialog(
            title = null,
            message = stringResource(id = UniversalString.bitwarden_autofill_go_to_settings),
            onDismissRequest = { shouldShowAutofillFallbackDialog = false },
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val autoFillHandlers = remember(viewModel) { AutoFillHandlers.create(viewModel = viewModel) }
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.autofill_noun),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
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
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.autofill_noun),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        val autofillServicesLabel = stringResource(id = UniversalString.autofill_services)
        UniversalSwitch(
            label = autofillServicesLabel,
            supportingText = stringResource(
                id = UniversalString.autofill_services_explanation_long,
            ),
            contentDescription = if (state.isAutoFillServicesEnabled) {
                autofillServicesLabel
            } else {
                stringResource(
                    id = UniversalString.external_link_format,
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
                        id = UniversalString
                            .improves_login_filling_for_supported_websites_on_selected_browsers,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (state.showPasskeyManagementRow) {
            UniversalExternalLinkRow(
                text = stringResource(id = UniversalString.passkey_management),
                description = UniversalString
                    .passkey_management_explanation_long
                    .toAnnotatedString(),
                onConfirmClick = autoFillHandlers.onPasskeyManagementClick,
                dialogTitle = stringResource(id = UniversalString.continue_to_device_settings),
                dialogMessage = stringResource(
                    id = UniversalString.set_bitwarden_as_passkey_manager_description,
                ),
                withDivider = false,
                cardStyle = CardStyle.Top(),
                modifier = Modifier
                    .fillMaxWidth()
                    .standardHorizontalMargin(),
            )
            UniversalTextRow(
                text = stringResource(UniversalString.privileged_apps),
                onClick = autoFillHandlers.onPrivilegedAppsClick,
                helpData = UniversalHelpButtonData(
                    contentDescription = stringResource(
                        id = UniversalString.learn_more_about_privileged_apps,
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
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.additional_options),
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        if (state.showFillAssistOption) {
            Spacer(modifier = Modifier.height(8.dp))
            UniversalSwitch(
                label = stringResource(id = UniversalString.turn_on_fill_assist),
                supportingText = stringResource(
                    id = UniversalString.fill_assist_improves_autofill_accuracy_on_supported_sites,
                ),
                isChecked = state.isFillAssistEnabled,
                onCheckedChange = autoFillHandlers.onFillAssistToggleClick,
                helpData = UniversalHelpButtonData(
                    onClick = autoFillHandlers.onFillAssistInfoClick,
                    contentDescription = stringResource(
                        id = UniversalString.learn_more,
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
        UniversalSwitch(
            label = stringResource(id = UniversalString.copy_totp_automatically),
            supportingText = stringResource(
                id = UniversalString.copy_totp_automatically_description,
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
        UniversalSwitch(
            label = stringResource(id = UniversalString.ask_to_add_item),
            supportingText = stringResource(id = UniversalString.ask_to_add_item_description),
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
        UniversalPushRow(
            text = stringResource(id = UniversalString.block_auto_fill),
            description = stringResource(
                id = UniversalString.auto_fill_will_not_be_offered_for_these_ur_is,
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
                UniversalActionCard(
                    cardTitle = stringResource(id = UniversalString.turn_on_autofill),
                    actionButton = UniversalButtonData(
                        label = UniversalString.get_started.asText(),
                        onClick = autoFillHandlers.onAutofillActionCardClick,
                    ),
                    onDismissClick = autoFillHandlers.onAutofillActionCardDismissClick,
                    leadingContent = { NotificationBadge(notificationCount = 1) },
                )
            }

            CtaState.BROWSER_AUTOFILL -> {
                val subTitleRes = if (state.browserCount > 1) {
                    UniversalString.browser_requires_special_permissions_for_bitwarden_plural
                } else {
                    UniversalString.browser_requires_special_permissions_for_bitwarden_singular
                }
                UniversalActionCard(
                    cardTitle = stringResource(
                        id = UniversalString.turn_on_browser_autofill_integration,
                    ),
                    cardSubtitle = stringResource(id = subTitleRes),
                    actionButton = UniversalButtonData(
                        label = UniversalString.get_started.asText(),
                        onClick = autoFillHandlers.onBrowserAutofillActionCardClick,
                    ),
                    onDismissClick = autoFillHandlers.onBrowserAutofillActionCardDismissClick,
                    leadingContent = { NotificationBadge(notificationCount = 1) },
                )
            }

            CtaState.DEFAULT -> {
                UniversalActionCardSmall(
                    actionIcon = rememberVectorPainter(id = UniversalDrawable.ic_question_circle),
                    actionText = stringResource(id = UniversalString.having_trouble_with_autofill),
                    callToActionText = stringResource(
                        id = UniversalString.access_help_and_troubleshooting_documentation_here,
                    ),
                    onCardClicked = autoFillHandlers.onHelpCardClick,
                    trailingContent = {
                        Icon(
                            painter = rememberVectorPainter(UniversalDrawable.ic_chevron_right),
                            contentDescription = stringResource(id = UniversalString.external_link),
                            tint = UniversalTheme.colorScheme.icon.primary,
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
    UniversalMultiSelectButton(
        label = stringResource(id = UniversalString.display_autofill_suggestions),
        supportingText = stringResource(id = UniversalString.use_inline_autofill_explanation_long),
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
    UniversalSwitch(
        label = stringResource(id = UniversalString.accessibility),
        supportingText = stringResource(id = UniversalString.accessibility_description5),
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
        UniversalTwoButtonDialog(
            title = stringResource(id = UniversalString.accessibility_service_disclosure),
            message = stringResource(id = UniversalString.accessibility_disclosure_text),
            confirmButtonText = stringResource(id = UniversalString.accept),
            dismissButtonText = stringResource(id = UniversalString.decline),
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
                UniversalString.advanced_option_with_increased_risk_of_exposing_credentials
            }

            UriMatchType.REGULAR_EXPRESSION -> {
                UniversalString.advanced_option_increased_risk_exposing_credentials_used_incorrectly
            }

            UriMatchType.HOST,
            UriMatchType.DOMAIN,
            UriMatchType.EXACT,
            UriMatchType.NEVER,
                -> {
                error("Unexpected value $pendingOption on AdvancedMatchDetectionWarningDialog")
            }
        }

    UniversalTwoButtonDialog(
        title = stringResource(
            id = UniversalString.are_you_sure_you_want_to_use,
            formatArgs = arrayOf(
                pendingOption.displayLabel(),
            ),
        ),
        message = stringResource(
            id = descriptionStringResId,
        ),
        confirmButtonText = stringResource(id = UniversalString.yes),
        dismissButtonText = stringResource(id = UniversalString.cancel),
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
                title = stringResource(id = UniversalString.advanced_options),
                testTag = "AdvancedOptionsSection",
            )
        } else {
            null
        },
        *advancedOptions
            .map { MultiSelectOption.Row(it.displayLabel()) }
            .toTypedArray(),
    )

    UniversalMultiSelectButton(
        label = stringResource(id = UniversalString.default_uri_match_detection),
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
    UniversalTwoButtonDialog(
        title = stringResource(id = UniversalString.keep_your_credential_secure),
        message = stringResource(
            id = UniversalString.learn_more_about_how_to_keep_credentirals_secure,
            formatArgs = arrayOf(uriMatchType.displayLabel()),
        ),
        confirmButtonText = stringResource(id = UniversalString.learn_more),
        dismissButtonText = stringResource(id = UniversalString.close),
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
                UniversalString.default_uri_match_detection_description_advanced_options
            }

            UriMatchType.REGULAR_EXPRESSION -> {
                UniversalString.default_uri_match_detection_description_advanced_options_incorrectly
            }

            UriMatchType.HOST,
            UriMatchType.DOMAIN,
            UriMatchType.EXACT,
            UriMatchType.NEVER,
                -> {
                UniversalString.default_uri_match_detection_description
            }
        }

    val supportingAnnotatedString =
        annotatedStringResource(
            id = stringResId,
            emphasisHighlightStyle = spanStyleOf(
                textStyle = UniversalTheme.typography.bodyMediumEmphasis,
                color = UniversalTheme.colorScheme.text.secondary,
            ),
            style = spanStyleOf(
                textStyle = UniversalTheme.typography.bodySmall,
                color = UniversalTheme.colorScheme.text.secondary,
            ),
        )

    Text(
        text = supportingAnnotatedString,
        style = UniversalTheme.typography.bodySmall,
        color = UniversalTheme.colorScheme.text.secondary,
        modifier = Modifier.fillMaxWidth(),
    )
}
