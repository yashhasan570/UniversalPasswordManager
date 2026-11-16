@file:Suppress("TooManyFunctions")

package .authenticator.ui.platform.feature.settings

import android.content.Intent
import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .authenticator.data.platform.manager.lock.model.AppTimeout
import .authenticator.ui.platform.components.biometrics.BiometricChanges
import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .authenticator.ui.platform.feature.settings.security.util.displayLabel
import .authenticator.ui.platform.util.displayLabel
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.mirrorIfRtl
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerMediumTopAppBar
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.dropdown.Universal Password ManagerMultiSelectButton
import .ui.platform.components.header.Universal Password ManagerListHeaderText
import .ui.platform.components.model.CardStyle
import .ui.platform.components.row.Universal Password ManagerExternalLinkRow
import .ui.platform.components.row.Universal Password ManagerPushRow
import .ui.platform.components.row.Universal Password ManagerTextRow
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.support.Universal Password ManagerSupportingText
import .ui.platform.components.toggle.Universal Password ManagerSwitch
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalBiometricsManager
import .ui.platform.composition.LocalIntentManager
import .ui.platform.feature.settings.appearance.model.AppLanguage
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.manager.IntentManager
import .ui.platform.manager.biometrics.BiometricsManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.util.displayLabel
import .ui.util.Text
import .ui.util.asText
import kotlinx.collections.immutable.toImmutableList
import javax.crypto.Cipher

/**
 * Display the settings screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    intentManager: IntentManager = LocalIntentManager.current,
    onNavigateToTutorial: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToImport: () -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val snackbarState = rememberUniversal Password ManagerSnackbarHostState()
    var showBiometricsPrompt by rememberSaveable { mutableStateOf(false) }
    val unlockWithBiometricToggle: (cipher: Cipher) -> Unit = {
        viewModel.trySendAction(SettingsAction.SecurityClick.UnlockWithBiometricToggleEnabled(it))
    }
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            SettingsEvent.NavigateToTutorial -> onNavigateToTutorial()
            SettingsEvent.NavigateToExport -> onNavigateToExport()
            SettingsEvent.NavigateToImport -> onNavigateToImport()
            SettingsEvent.NavigateToBackup -> {
                intentManager.launchUri(
                    uri = "https://support.google.com/android/answer/2819582".toUri(),
                )
            }

            SettingsEvent.NavigateToHelpCenter -> {
                intentManager.launchUri("https:///help".toUri())
            }

            SettingsEvent.NavigateToPrivacyPolicy -> {
                intentManager.launchUri("https:///privacy".toUri())
            }

            SettingsEvent.NavigateToSyncInformation -> {
                intentManager.launchUri("https:///help/totp-sync".toUri())
            }

            SettingsEvent.NavigateToUniversal Password ManagerApp -> {
                intentManager.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "settings/account_security".toUri(),
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    },
                )
            }

            SettingsEvent.NavigateToUniversal Password ManagerPlayStoreListing -> {
                intentManager.launchUri(
                    "https://play.google.com/store/apps/details?id=com.x8bit.universalpasswordmanager".toUri(),
                )
            }

            is SettingsEvent.ShowSnackbar -> snackbarState.showSnackbar(event.data)

            is SettingsEvent.ShowBiometricsPrompt -> {
                showBiometricsPrompt = true
                biometricsManager.promptBiometrics(
                    onSuccess = {
                        unlockWithBiometricToggle(it)
                        showBiometricsPrompt = false
                    },
                    onCancel = { showBiometricsPrompt = false },
                    onLockOut = { showBiometricsPrompt = false },
                    onError = { showBiometricsPrompt = false },
                    cipher = event.cipher,
                )
            }
        }
    }

    BiometricChanges(
        biometricsManager = biometricsManager,
        onBiometricSupportChange = {
            viewModel.trySendAction(SettingsAction.BiometricSupportChanged(it))
        },
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerMediumTopAppBar(
                title = stringResource(id = Universal Password ManagerString.settings),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
        ) {
            SecuritySettings(
                state = state,
                onBiometricToggle = {
                    viewModel.trySendAction(
                        SettingsAction.SecurityClick.UnlockWithBiometricToggle(it),
                    )
                },
                onScreenCaptureChange = {
                    viewModel.trySendAction(
                        SettingsAction.SecurityClick.AllowScreenCaptureToggle(it),
                    )
                },
                onAppTimeoutChange = {
                    viewModel.trySendAction(SettingsAction.SecurityClick.AppTimeoutChange(it))
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            VaultSettings(
                onExportClick = { viewModel.trySendAction(SettingsAction.DataClick.ExportClick) },
                onImportClick = { viewModel.trySendAction(SettingsAction.DataClick.ImportClick) },
                onBackupClick = { viewModel.trySendAction(SettingsAction.DataClick.BackupClick) },
                onSyncWithUniversal Password ManagerClick = {
                    viewModel.trySendAction(SettingsAction.DataClick.SyncWithUniversal Password ManagerClick)
                },
                onSyncLearnMoreClick = {
                    viewModel.trySendAction(SettingsAction.DataClick.SyncLearnMoreClick)
                },
                onDefaultSaveOptionUpdated = {
                    viewModel.trySendAction(SettingsAction.DataClick.DefaultSaveOptionUpdated(it))
                },
                onShowNextCodeToggle = {
                    viewModel.trySendAction(SettingsAction.DataClick.ShowNextCodeToggle(it))
                },
                defaultSaveOption = state.defaultSaveOption,
                isShowNextCodeEnabled = state.isShowNextCodeEnabled,
                shouldShowDefaultSaveOptions = state.showDefaultSaveOptionRow,
                shouldShowSyncWithUniversal Password ManagerApp = state.showSyncWithUniversal Password Manager,
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppearanceSettings(
                state = state.appearance,
                onLanguageSelection = {
                    viewModel.trySendAction(SettingsAction.AppearanceChange.LanguageChange(it))
                },
                onThemeSelection = {
                    viewModel.trySendAction(SettingsAction.AppearanceChange.ThemeChange(it))
                },
                onDynamicColorChange = {
                    viewModel.trySendAction(SettingsAction.AppearanceChange.DynamicColorChange(it))
                },
            )
            Spacer(Modifier.height(16.dp))
            HelpSettings(
                onTutorialClick = {
                    viewModel.trySendAction(SettingsAction.HelpClick.ShowTutorialClick)
                },
                onHelpCenterClick = {
                    viewModel.trySendAction(SettingsAction.HelpClick.HelpCenterClick)
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            AboutSettings(
                state = state,
                onSubmitCrashLogsCheckedChange = {
                    viewModel.trySendAction(SettingsAction.AboutClick.SubmitCrashLogsClick(it))
                },
                onPrivacyPolicyClick = {
                    viewModel.trySendAction(SettingsAction.AboutClick.PrivacyPolicyClick)
                },
                onVersionClick = {
                    viewModel.trySendAction(SettingsAction.AboutClick.VersionClick)
                },
            )
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = state.copyrightInfo.invoke(),
                    style = Universal Password ManagerTheme.typography.bodySmall,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

//region Security settings

@Composable
private fun ColumnScope.SecuritySettings(
    state: SettingsState,
    onBiometricToggle: (Boolean) -> Unit,
    onScreenCaptureChange: (Boolean) -> Unit,
    onAppTimeoutChange: (AppTimeout.Type) -> Unit,
    resources: Resources = LocalResources.current,
) {
    Spacer(modifier = Modifier.height(height = 12.dp))
    Universal Password ManagerListHeaderText(
        modifier = Modifier
            .standardHorizontalMargin()
            .padding(horizontal = 16.dp),
        label = stringResource(id = Universal Password ManagerString.security),
    )

    if (state.hasBiometricsSupport) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        Universal Password ManagerSwitch(
            cardStyle = CardStyle.Top(),
            label = stringResource(id = Universal Password ManagerString.lock_app),
            isChecked = state.isUnlockWithBiometricsEnabled,
            onCheckedChange = { onBiometricToggle(it) },
            modifier = Modifier
                .testTag("UnlockWithBiometricsSwitch")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Universal Password ManagerMultiSelectButton(
            label = stringResource(id = Universal Password ManagerString.session_timeout),
            options = AppTimeout.Type.entries.map { it.displayLabel() }.toImmutableList(),
            selectedOption = state.appTimeout.type.displayLabel(),
            onOptionSelected = { selectedType ->
                val selectedOption = AppTimeout.Type.entries.first {
                    it.displayLabel.toString(resources) == selectedType
                }
                onAppTimeoutChange(selectedOption)
            },
            isEnabled = state.isUnlockWithBiometricsEnabled,
            textFieldTestTag = "SessionTimeoutStatusLabel",
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .testTag("AppTimeoutSwitch")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Universal Password ManagerSupportingText(
            text = stringResource(
                id = Universal Password ManagerString.use_your_devices_lock_method_to_unlock_the_app,
            ),
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }

    Spacer(modifier = Modifier.height(height = 8.dp))
    ScreenCaptureRow(
        currentValue = state.allowScreenCapture,
        cardStyle = CardStyle.Full,
        onValueChange = onScreenCaptureChange,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag = "AllowScreenCaptureSwitch")
            .standardHorizontalMargin(),
    )
}
//endregion

//region Data settings

@Composable
@Suppress("LongMethod")
private fun ColumnScope.VaultSettings(
    defaultSaveOption: DefaultSaveOption,
    isShowNextCodeEnabled: Boolean,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onBackupClick: () -> Unit,
    onSyncWithUniversal Password ManagerClick: () -> Unit,
    onSyncLearnMoreClick: () -> Unit,
    onDefaultSaveOptionUpdated: (DefaultSaveOption) -> Unit,
    onShowNextCodeToggle: (Boolean) -> Unit,
    shouldShowSyncWithUniversal Password ManagerApp: Boolean,
    shouldShowDefaultSaveOptions: Boolean,
) {
    Universal Password ManagerListHeaderText(
        modifier = Modifier
            .standardHorizontalMargin()
            .padding(horizontal = 16.dp),
        label = stringResource(id = Universal Password ManagerString.data),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerPushRow(
        text = stringResource(id = Universal Password ManagerString.import_vault),
        onClick = onImportClick,
        cardStyle = CardStyle.Top(),
        modifier = Modifier
            .standardHorizontalMargin()
            .testTag("Import"),
    )
    Universal Password ManagerPushRow(
        text = stringResource(id = Universal Password ManagerString.export),
        onClick = onExportClick,
        cardStyle = CardStyle.Middle(),
        modifier = Modifier
            .standardHorizontalMargin()
            .testTag("Export"),
    )
    Universal Password ManagerExternalLinkRow(
        text = stringResource(Universal Password ManagerString.backup),
        onConfirmClick = onBackupClick,
        modifier = Modifier
            .standardHorizontalMargin()
            .testTag("Backup"),
        withDivider = false,
        dialogTitle = stringResource(Universal Password ManagerString.data_backup_title),
        dialogMessage = stringResource(Universal Password ManagerString.data_backup_message),
        dialogConfirmButtonText = stringResource(Universal Password ManagerString.learn_more),
        dialogDismissButtonText = stringResource(Universal Password ManagerString.okay),
        cardStyle = CardStyle.Middle(),
    )
    if (shouldShowSyncWithUniversal Password ManagerApp) {
        val learnMore = stringResource(id = Universal Password ManagerString.learn_more_link)
        Universal Password ManagerTextRow(
            text = stringResource(id = Universal Password ManagerString.sync_with_universalpasswordmanager_app),
            description = annotatedStringResource(
                id = Universal Password ManagerString.learn_more_link,
                onAnnotationClick = {
                    when (it) {
                        "learnMore" -> onSyncLearnMoreClick()
                    }
                },
            ),
            onClick = onSyncWithUniversal Password ManagerClick,
            modifier = Modifier
                .semantics {
                    customActions = listOf(
                        CustomAccessibilityAction(
                            label = learnMore,
                            action = {
                                onSyncLearnMoreClick()
                                true
                            },
                        ),
                    )
                }
                .standardHorizontalMargin(),
            cardStyle = CardStyle.Middle(),
            content = {
                Icon(
                    modifier = Modifier.mirrorIfRtl(),
                    painter = painterResource(id = Universal Password ManagerDrawable.ic_external_link),
                    contentDescription = stringResource(id = Universal Password ManagerString.external_link),
                    tint = Universal Password ManagerTheme.colorScheme.icon.primary,
                )
            },
        )
    }
    if (shouldShowDefaultSaveOptions) {
        DefaultSaveOptionSelectionRow(
            currentSelection = defaultSaveOption,
            onSaveOptionUpdated = onDefaultSaveOptionUpdated,
            modifier = Modifier.standardHorizontalMargin(),
        )
    }
    Universal Password ManagerSwitch(
        label = stringResource(id = Universal Password ManagerString.show_next_code),
        subtext = stringResource(id = Universal Password ManagerString.see_upcoming_codes_in_the_list),
        isChecked = isShowNextCodeEnabled,
        onCheckedChange = onShowNextCodeToggle,
        cardStyle = CardStyle.Bottom,
        modifier = Modifier
            .testTag("ShowNextCodeToggle")
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
}

@Composable
private fun DefaultSaveOptionSelectionRow(
    currentSelection: DefaultSaveOption,
    onSaveOptionUpdated: (DefaultSaveOption) -> Unit,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.default_save_option),
        dialogSubtitle = stringResource(id = Universal Password ManagerString.default_save_options_subtitle),
        options = DefaultSaveOption.entries.map { it.displayLabel() }.toImmutableList(),
        selectedOption = currentSelection.displayLabel(),
        onOptionSelected = { selectedOptionLabel ->
            val selectedOption = DefaultSaveOption
                .entries
                .first { it.displayLabel(resources) == selectedOptionLabel }
            onSaveOptionUpdated(selectedOption)
        },
        cardStyle = CardStyle.Middle(),
        modifier = modifier,
    )
}

@Composable
private fun ScreenCaptureRow(
    currentValue: Boolean,
    cardStyle: CardStyle,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldShowScreenCaptureConfirmDialog by remember { mutableStateOf(false) }

    Universal Password ManagerSwitch(
        label = stringResource(id = Universal Password ManagerString.allow_screen_capture),
        isChecked = currentValue,
        onCheckedChange = {
            if (currentValue) {
                onValueChange(false)
            } else {
                shouldShowScreenCaptureConfirmDialog = true
            }
        },
        cardStyle = cardStyle,
        modifier = modifier,
    )

    if (shouldShowScreenCaptureConfirmDialog) {
        Universal Password ManagerTwoButtonDialog(
            title = stringResource(Universal Password ManagerString.allow_screen_capture),
            message = stringResource(
                id = Universal Password ManagerString.are_you_sure_you_want_to_enable_screen_capture,
            ),
            confirmButtonText = stringResource(Universal Password ManagerString.yes),
            dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
            onConfirmClick = {
                onValueChange(true)
                shouldShowScreenCaptureConfirmDialog = false
            },
            onDismissClick = { shouldShowScreenCaptureConfirmDialog = false },
            onDismissRequest = { shouldShowScreenCaptureConfirmDialog = false },
        )
    }
}

//endregion Data settings

//region Appearance settings

@Composable
private fun ColumnScope.AppearanceSettings(
    state: SettingsState.Appearance,
    onLanguageSelection: (language: AppLanguage) -> Unit,
    onThemeSelection: (theme: AppTheme) -> Unit,
    onDynamicColorChange: (isEnabled: Boolean) -> Unit,
    resources: Resources = LocalResources.current,
) {
    Universal Password ManagerListHeaderText(
        modifier = Modifier
            .standardHorizontalMargin()
            .padding(horizontal = 16.dp),
        label = stringResource(id = Universal Password ManagerString.appearance),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.language),
        options = AppLanguage.entries.map { it.text() }.toImmutableList(),
        selectedOption = state.language.text(),
        onOptionSelected = { language ->
            onLanguageSelection(
                AppLanguage.entries.first { language == it.text.toString(resources) },
            )
        },
        cardStyle = CardStyle.Full,
        modifier = Modifier
            .testTag(tag = "LanguageChooser")
            .standardHorizontalMargin()
            .fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    ThemeSelectionRow(
        currentSelection = state.theme,
        onThemeSelection = onThemeSelection,
        cardStyle = if (state.isDynamicColorsSupported) CardStyle.Top() else CardStyle.Full,
        modifier = Modifier
            .testTag("ThemeChooser")
            .standardHorizontalMargin()
            .fillMaxWidth(),
    )
    if (state.isDynamicColorsSupported) {
        Universal Password ManagerSwitch(
            label = stringResource(id = Universal Password ManagerString.use_dynamic_colors),
            isChecked = state.isDynamicColorsEnabled,
            onCheckedChange = onDynamicColorChange,
            cardStyle = CardStyle.Bottom,
            modifier = Modifier
                .testTag(tag = "DynamicColorsSwitch")
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun ThemeSelectionRow(
    currentSelection: AppTheme,
    onThemeSelection: (AppTheme) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
    resources: Resources = LocalResources.current,
) {
    Universal Password ManagerMultiSelectButton(
        label = stringResource(id = Universal Password ManagerString.theme),
        options = AppTheme.entries.map { it.displayLabel() }.toImmutableList(),
        selectedOption = currentSelection.displayLabel(),
        onOptionSelected = { selectedOptionLabel ->
            val selectedOption = AppTheme
                .entries
                .first { it.displayLabel(resources) == selectedOptionLabel }
            onThemeSelection(selectedOption)
        },
        cardStyle = cardStyle,
        modifier = modifier,
    )
}

//endregion Appearance settings

//region Help settings

@Composable
private fun ColumnScope.HelpSettings(
    onTutorialClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
) {
    Universal Password ManagerListHeaderText(
        modifier = Modifier
            .standardHorizontalMargin()
            .padding(horizontal = 16.dp),
        label = stringResource(id = Universal Password ManagerString.help),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerTextRow(
        text = stringResource(id = Universal Password ManagerString.launch_tutorial),
        onClick = onTutorialClick,
        modifier = Modifier
            .testTag("LaunchTutorial")
            .standardHorizontalMargin(),
        cardStyle = CardStyle.Top(),
    )
    Universal Password ManagerExternalLinkRow(
        text = stringResource(id = Universal Password ManagerString.universalpasswordmanager_help_center),
        onConfirmClick = onHelpCenterClick,
        modifier = Modifier
            .standardHorizontalMargin()
            .testTag("Universal Password ManagerHelpCenter"),
        withDivider = false,
        dialogTitle = stringResource(id = Universal Password ManagerString.continue_to_help_center),
        dialogMessage = stringResource(
            Universal Password ManagerString.learn_more_about_how_to_use_universalpasswordmanager_authenticator_on_the_help_center,
        ),
        cardStyle = CardStyle.Bottom,
    )
}

//endregion Help settings

//region About settings
@Composable
private fun ColumnScope.AboutSettings(
    state: SettingsState,
    onSubmitCrashLogsCheckedChange: (Boolean) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onVersionClick: () -> Unit,
) {
    Universal Password ManagerListHeaderText(
        modifier = Modifier
            .standardHorizontalMargin()
            .padding(horizontal = 16.dp),
        label = stringResource(id = Universal Password ManagerString.about),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Universal Password ManagerSwitch(
        modifier = Modifier
            .standardHorizontalMargin()
            .testTag("SubmitCrashLogs"),
        label = stringResource(id = Universal Password ManagerString.submit_crash_logs),
        isChecked = state.isSubmitCrashLogsEnabled,
        onCheckedChange = onSubmitCrashLogsCheckedChange,
        cardStyle = CardStyle.Top(),
    )
    Universal Password ManagerExternalLinkRow(
        text = stringResource(id = Universal Password ManagerString.privacy_policy),
        modifier = Modifier
            .standardHorizontalMargin()
            .testTag("PrivacyPolicy"),
        withDivider = false,
        onConfirmClick = onPrivacyPolicyClick,
        dialogTitle = stringResource(id = Universal Password ManagerString.continue_to_privacy_policy),
        dialogMessage = stringResource(
            id = Universal Password ManagerString.privacy_policy_description_long,
        ),
        cardStyle = CardStyle.Middle(),
    )
    CopyRow(
        text = state.version,
        onClick = onVersionClick,
        modifier = Modifier.standardHorizontalMargin(),
    )
}

@Composable
private fun CopyRow(
    text: Text,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerTextRow(
        text = text(),
        onClick = onClick,
        cardStyle = CardStyle.Bottom,
        modifier = modifier,
    ) {
        Icon(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_copy),
            contentDescription = stringResource(id = Universal Password ManagerString.copy),
            tint = Universal Password ManagerTheme.colorScheme.icon.primary,
            modifier = Modifier.mirrorIfRtl(),
        )
    }
}

//endregion About settings

@Preview
@Composable
private fun CopyRow_preview() {
    Universal Password ManagerTheme {
        CopyRow(
            text = "Copyable Text".asText(),
            onClick = { },
        )
    }
}
