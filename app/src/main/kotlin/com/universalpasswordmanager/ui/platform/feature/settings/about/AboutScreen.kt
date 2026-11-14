package com.universalpasswordmanager.ui.platform.feature.settings.about

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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.mirrorIfRtl
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalHelpButtonData
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.row.UniversalExternalLinkRow
import com.universalpasswordmanager.ui.platform.components.row.UniversalPushRow
import com.universalpasswordmanager.ui.platform.components.row.UniversalTextRow
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.toggle.UniversalSwitch
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Displays the about screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFlightRecorder: () -> Unit,
    onNavigateToRecordedLogs: () -> Unit,
    viewModel: AboutViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is AboutEvent.NavigateToWebVault -> intentManager.launchUri(event.vaultUrl.toUri())
            AboutEvent.NavigateBack -> onNavigateBack.invoke()
            AboutEvent.NavigateToFlightRecorder -> onNavigateToFlightRecorder()
            AboutEvent.NavigateToRecordedLogs -> onNavigateToRecordedLogs()

            AboutEvent.NavigateToFlightRecorderHelp -> {
                intentManager.launchUri("https:///help/flight-recorder".toUri())
            }

            AboutEvent.NavigateToHelpCenter -> {
                intentManager.launchUri("https:///help".toUri())
            }

            AboutEvent.NavigateToPrivacyPolicy -> {
                intentManager.launchUri("https:///privacy".toUri())
            }

            AboutEvent.NavigateToLearnAboutOrganizations -> {
                intentManager.launchUri("https:///help/about-organizations".toUri())
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.about),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = UniversalString.back),
                onNavigationIconClick = { viewModel.trySendAction(AboutAction.BackClick) },
            )
        },
    ) {
        AboutScreenContent(
            state = state,
            modifier = Modifier.fillMaxSize(),
            onHelpCenterClick = { viewModel.trySendAction(AboutAction.HelpCenterClick) },
            onPrivacyPolicyClick = { viewModel.trySendAction(AboutAction.PrivacyPolicyClick) },
            onLearnAboutOrgsClick = {
                viewModel.trySendAction(AboutAction.LearnAboutOrganizationsClick)
            },
            onSubmitCrashLogsCheckedChange = {
                viewModel.trySendAction(AboutAction.SubmitCrashLogsClick(it))
            },
            onFlightRecorderCheckedChange = {
                viewModel.trySendAction(AboutAction.FlightRecorderCheckedChange(it))
            },
            onFlightRecorderTooltipClick = {
                viewModel.trySendAction(AboutAction.FlightRecorderTooltipClick)
            },
            onViewRecordedLogsClick = {
                viewModel.trySendAction(AboutAction.ViewRecordedLogsClick)
            },
            onVersionClick = { viewModel.trySendAction(AboutAction.VersionClick) },
            onWebVaultClick = { viewModel.trySendAction(AboutAction.WebVaultClick) },
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun AboutScreenContent(
    state: AboutState,
    onHelpCenterClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onLearnAboutOrgsClick: () -> Unit,
    onSubmitCrashLogsCheckedChange: (Boolean) -> Unit,
    onFlightRecorderCheckedChange: (Boolean) -> Unit,
    onFlightRecorderTooltipClick: () -> Unit,
    onViewRecordedLogsClick: () -> Unit,
    onVersionClick: () -> Unit,
    onWebVaultClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        CrashLogsCard(
            isVisible = state.shouldShowCrashLogsButton,
            isEnabled = state.isSubmitCrashLogsEnabled,
            onSubmitCrashLogsCheckedChange = onSubmitCrashLogsCheckedChange,
        )
        FlightRecorderCard(
            isFlightRecorderEnabled = state.isFlightRecorderEnabled,
            logExpiration = state.flightRecorderSubtext,
            onFlightRecorderCheckedChange = onFlightRecorderCheckedChange,
            onFlightRecorderTooltipClick = onFlightRecorderTooltipClick,
            onViewRecordedLogsClick = onViewRecordedLogsClick,
        )
        UniversalExternalLinkRow(
            text = stringResource(id = UniversalString.universalpasswordmanager_help_center),
            onConfirmClick = onHelpCenterClick,
            dialogTitle = stringResource(id = UniversalString.continue_to_help_center),
            dialogMessage = stringResource(
                id = UniversalString.learn_more_about_how_to_use_universalpasswordmanager_on_the_help_center,
            ),
            withDivider = false,
            cardStyle = CardStyle.Top(),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag(tag = "UniversalHelpCenterRow"),
        )
        UniversalExternalLinkRow(
            text = stringResource(id = UniversalString.privacy_policy),
            onConfirmClick = onPrivacyPolicyClick,
            dialogTitle = stringResource(id = UniversalString.continue_to_privacy_policy),
            dialogMessage = stringResource(
                id = UniversalString.privacy_policy_description_long,
            ),
            withDivider = false,
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag(tag = "PrivacyPolicyRow"),
        )
        UniversalExternalLinkRow(
            text = stringResource(id = UniversalString.web_vault),
            onConfirmClick = onWebVaultClick,
            dialogTitle = stringResource(id = UniversalString.continue_to_web_app),
            dialogMessage = stringResource(
                id = UniversalString.explore_more_features_of_your_universalpasswordmanager_account_on_the_web_app,
            ),
            withDivider = false,
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag(tag = "UniversalWebVaultRow"),
        )
        UniversalExternalLinkRow(
            text = stringResource(id = UniversalString.learn_org),
            onConfirmClick = onLearnAboutOrgsClick,
            dialogTitle = stringResource(id = UniversalString.continue_to_x, ""),
            dialogMessage = stringResource(
                id = UniversalString.learn_about_organizations_description_long,
            ),
            withDivider = false,
            cardStyle = CardStyle.Middle(),
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag(tag = "LearnAboutOrganizationsRow"),
        )
        CopyRow(
            text = state.version,
            onClick = onVersionClick,
            modifier = Modifier
                .standardHorizontalMargin()
                .fillMaxWidth()
                .testTag("CopyAboutInfoRow"),
        )
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 60.dp)
                .standardHorizontalMargin()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = state.copyrightInfo.invoke(),
                style = UniversalTheme.typography.bodySmall,
                color = UniversalTheme.colorScheme.text.primary,
            )
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun ColumnScope.CrashLogsCard(
    isVisible: Boolean,
    isEnabled: Boolean,
    onSubmitCrashLogsCheckedChange: (Boolean) -> Unit,
) {
    if (!isVisible) return
    UniversalSwitch(
        label = stringResource(id = UniversalString.submit_crash_logs),
        contentDescription = stringResource(id = UniversalString.submit_crash_logs),
        isChecked = isEnabled,
        onCheckedChange = onSubmitCrashLogsCheckedChange,
        cardStyle = CardStyle.Full,
        modifier = Modifier
            .testTag(tag = "SubmitCrashLogsSwitch")
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
}

@Composable
private fun ColumnScope.FlightRecorderCard(
    isFlightRecorderEnabled: Boolean,
    logExpiration: Text?,
    onFlightRecorderCheckedChange: (Boolean) -> Unit,
    onFlightRecorderTooltipClick: () -> Unit,
    onViewRecordedLogsClick: () -> Unit,
) {
    UniversalSwitch(
        label = stringResource(id = UniversalString.flight_recorder),
        isChecked = isFlightRecorderEnabled,
        onCheckedChange = onFlightRecorderCheckedChange,
        helpData = UniversalHelpButtonData(
            contentDescription = stringResource(id = UniversalString.flight_recorder_help),
            onClick = onFlightRecorderTooltipClick,
            isExternalLink = true,
        ),
        subtext = logExpiration?.invoke(),
        cardStyle = CardStyle.Top(),
        modifier = Modifier
            .testTag(tag = "FlightRecorderSwitch")
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
    UniversalPushRow(
        text = stringResource(id = UniversalString.view_recorded_logs),
        onClick = onViewRecordedLogsClick,
        cardStyle = CardStyle.Bottom,
        modifier = Modifier
            .testTag(tag = "ViewRecordedLogs")
            .fillMaxWidth()
            .standardHorizontalMargin(),
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
}

@Composable
private fun CopyRow(
    text: Text,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UniversalTextRow(
        text = text(),
        onClick = onClick,
        withDivider = false,
        cardStyle = CardStyle.Bottom,
        modifier = modifier,
    ) {
        Icon(
            painter = rememberVectorPainter(id = UniversalDrawable.ic_copy),
            contentDescription = stringResource(id = UniversalString.copy),
            tint = UniversalTheme.colorScheme.icon.primary,
            modifier = Modifier.mirrorIfRtl(),
        )
    }
}

@Preview
@Composable
private fun AboutScreenContent_preview() {
    UniversalTheme {
        AboutScreenContent(
            state = AboutState(
                version = "Version: 1.0.0 (1)".asText(),
                sdkVersion = "\uD83E\uDD80 SDK: 1.0.0-20250708.105256-238".asText(),
                serverData = "\uD83C\uDF29 Server: 2025.7.1 @ US".asText(),
                deviceData = "device_data".asText(),
                ciData = "ci_data".asText(),
                isSubmitCrashLogsEnabled = false,
                copyrightInfo = "".asText(),
                shouldShowCrashLogsButton = true,
                isFlightRecorderEnabled = true,
                flightRecorderSubtext = "Expires 3/21/25 at 3:20 PM".asText(),
            ),
            onHelpCenterClick = {},
            onPrivacyPolicyClick = {},
            onLearnAboutOrgsClick = {},
            onSubmitCrashLogsCheckedChange = { },
            onFlightRecorderCheckedChange = { },
            onFlightRecorderTooltipClick = {},
            onViewRecordedLogsClick = {},
            onVersionClick = {},
            onWebVaultClick = {},
        )
    }
}
