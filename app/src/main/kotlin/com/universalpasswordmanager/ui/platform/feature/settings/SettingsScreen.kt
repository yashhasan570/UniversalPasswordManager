package com.universalpasswordmanager.ui.platform.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toListItemCardStyle
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalMediumTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.button.model.UniversalButtonData
import com.universalpasswordmanager.ui.platform.components.card.UniversalActionCard
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.components.row.UniversalPushRow
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.asText

/**
 * Displays the settings screen.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToAccountSecurity: () -> Unit,
    onNavigateToAppearance: () -> Unit,
    onNavigateToAutoFill: () -> Unit,
    onNavigateToOther: () -> Unit,
    onNavigateToVault: () -> Unit,
    onNavigateToPlan: () -> Unit,
    viewModel: SettingsViewModel,
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            SettingsEvent.NavigateBack -> onNavigateBack()
            SettingsEvent.NavigateAbout -> onNavigateToAbout()
            SettingsEvent.NavigateAccountSecurity -> onNavigateToAccountSecurity()
            SettingsEvent.NavigateAppearance -> onNavigateToAppearance()
            SettingsEvent.NavigateAutoFill -> onNavigateToAutoFill()
            SettingsEvent.NavigateOther -> onNavigateToOther()
            SettingsEvent.NavigateVault -> onNavigateToVault()
            SettingsEvent.NavigateAccountSecurityShortcut -> onNavigateToAccountSecurity()
            SettingsEvent.NavigatePlan -> onNavigateToPlan()
            is SettingsEvent.NavigateToUrl -> intentManager.launchUri(event.url.toUri())
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    UniversalScaffold(
        topBar = {
            UniversalMediumTopAppBar(
                title = stringResource(id = UniversalString.settings),
                scrollBehavior = scrollBehavior,
                navigationIcon = if (state.shouldShowCloseButton) {
                    NavigationIcon(
                        navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                        navigationIconContentDescription = stringResource(
                            id = UniversalString.close,
                        ),
                        onNavigationIconClick = {
                            viewModel.trySendAction(SettingsAction.CloseClick)
                        },
                    )
                } else {
                    null
                },
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            if (state.shouldShowUpgradedToPremiumCard) {
                UniversalActionCard(
                    cardTitle = stringResource(id = UniversalString.upgraded_to_premium),
                    cardSubtitle = stringResource(
                        id = UniversalString.you_now_have_access_to_all_advanced_security_features,
                    ),
                    actionButton = UniversalButtonData(
                        label = UniversalString.learn_more.asText(),
                        onClick = {
                            viewModel.trySendAction(SettingsAction.UpgradedToPremiumCardClick)
                        },
                        isExternalLink = true,
                    ),
                    leadingContent = {
                        Icon(
                            painter = rememberVectorPainter(id = UniversalDrawable.ic_star),
                            contentDescription = null,
                            tint = UniversalTheme.colorScheme.icon.secondary,
                        )
                    },
                    onDismissClick = {
                        viewModel.trySendAction(SettingsAction.UpgradedToPremiumCardDismiss)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
                Spacer(modifier = Modifier.height(height = 12.dp))
            }
            state.settingRows.forEachIndexed { index, settingEntry ->
                UniversalPushRow(
                    text = settingEntry.text(),
                    onClick = {
                        viewModel.trySendAction(SettingsAction.SettingsClick(settingEntry))
                    },
                    notificationCount = state.notificationBadgeCountMap.getOrDefault(
                        key = settingEntry,
                        defaultValue = 0,
                    ),
                    cardStyle = state.settingRows.toListItemCardStyle(
                        index = index,
                        // Start padding, plus icon, plus spacing between text.
                        dividerPadding = 48.dp,
                    ),
                    leadingIcon = IconData.Local(iconRes = settingEntry.vectorIconRes),
                    modifier = Modifier
                        .testTag(tag = settingEntry.testTag)
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
