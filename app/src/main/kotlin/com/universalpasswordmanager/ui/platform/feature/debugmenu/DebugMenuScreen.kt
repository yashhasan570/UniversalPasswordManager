package com.universalpasswordmanager.ui.platform.feature.debugmenu

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.core.data.manager.model.FlagKey
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.scrolledContainerBottomDivider
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toListItemCardStyle
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.appbar.NavigationIcon
import com.universalpasswordmanager.ui.platform.components.appbar.model.TopAppBarDividerStyle
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.content.UniversalEmptyContent
import com.universalpasswordmanager.ui.platform.components.debug.ListItemContent
import com.universalpasswordmanager.ui.platform.components.divider.UniversalHorizontalDivider
import com.universalpasswordmanager.ui.platform.components.header.UniversalListHeaderText
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.segment.UniversalSegmentedButton
import com.universalpasswordmanager.ui.platform.components.segment.SegmentedButtonState
import com.universalpasswordmanager.ui.platform.components.segment.transition.segmentedContentTransform
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.feature.debugmenu.handler.DebugMenuHandler
import com.universalpasswordmanager.ui.platform.feature.debugmenu.handler.rememberDebugMenuHandler
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Top level screen for the debug menu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun DebugMenuScreen(
    onNavigateBack: () -> Unit,
    intentManager: IntentManager = LocalIntentManager.current,
    viewModel: DebugMenuViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handler = rememberDebugMenuHandler(viewModel = viewModel)
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            DebugMenuEvent.NavigateBack -> onNavigateBack()
            is DebugMenuEvent.ShareText -> intentManager.shareText(text = event.text)
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.debug_menu),
                scrollBehavior = scrollBehavior,
                dividerStyle = TopAppBarDividerStyle.NONE,
                navigationIcon = NavigationIcon(
                    navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_back),
                    navigationIconContentDescription = stringResource(id = UniversalString.back),
                    onNavigationIconClick = handler.onNavigateBack,
                ),
            )
        },
        utilityBar = {
            UniversalSegmentedButton(
                options = DebugMenuState
                    .MainTypeOption
                    .entries
                    .map {
                        SegmentedButtonState(
                            text = it.label(),
                            onClick = { handler.onMainTypeOptionClick(it) },
                            isChecked = it == state.mainTypeOption,
                            testTag = it.testTag,
                        )
                    }
                    .toImmutableList(),
                modifier = Modifier
                    .scrolledContainerBottomDivider(topAppBarScrollBehavior = scrollBehavior)
                    .fillMaxWidth(),
            )
        },
    ) {
        AnimatedContent(
            targetState = state.mainTypeOption,
            transitionSpec = { this.segmentedContentTransform() },
            label = "DebugContentTransition",
        ) {
            when (it) {
                DebugMenuState.MainTypeOption.FLAGS -> {
                    FeatureFlagContent(
                        featureFlagMap = state.featureFlags,
                        handler = handler,
                    )
                }

                DebugMenuState.MainTypeOption.OPTIONS -> {
                    DebugOptionsContent(handler = handler)
                }
            }
        }
    }
}

@Composable
private fun FeatureFlagContent(
    featureFlagMap: ImmutableMap<FlagKey<Any>, Any>,
    handler: DebugMenuHandler,
    modifier: Modifier = Modifier,
) {
    if (featureFlagMap.isEmpty()) {
        UniversalEmptyContent(
            text = stringResource(id = UniversalString.no_feature_flags),
            modifier = modifier,
        )
    } else {
        Column(modifier = modifier.verticalScroll(state = rememberScrollState())) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            featureFlagMap.forEach { featureFlag ->
                featureFlag.key.ListItemContent(
                    currentValue = featureFlag.value,
                    onValueChange = handler.onUpdateFeatureFlag,
                    cardStyle = featureFlagMap.keys.toListItemCardStyle(
                        index = featureFlagMap.keys.indexOf(element = featureFlag.key),
                    ),
                    modifier = Modifier
                        .standardHorizontalMargin()
                        .fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
            UniversalFilledButton(
                label = stringResource(id = UniversalString.reset_values),
                onClick = handler.onResetFeatureFlagValues,
                modifier = Modifier
                    .standardHorizontalMargin()
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun DebugOptionsContent(
    handler: DebugMenuHandler,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(state = rememberScrollState())) {
        Spacer(modifier = Modifier.height(height = 12.dp))
        OnboardingSection(handler = handler)

        Spacer(modifier = Modifier.height(height = 16.dp))
        CookiesSection(handler = handler)

        Spacer(modifier = Modifier.height(height = 16.dp))
        PremiumSection(handler = handler)

        Spacer(modifier = Modifier.height(height = 16.dp))
        ReportsSection(handler = handler)

        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Suppress("LongMethod")
@Composable
private fun OnboardingSection(
    handler: DebugMenuHandler,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.onboarding_override),
            modifier = Modifier
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        ButtonWithExplanation(
            label = stringResource(id = UniversalString.restart_onboarding_cta),
            explanation = stringResource(id = UniversalString.restart_onboarding_details),
            onClick = handler.onRestartOnboarding,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        ButtonWithExplanation(
            label = stringResource(id = UniversalString.restart_onboarding_carousel),
            explanation = stringResource(id = UniversalString.restart_onboarding_carousel_details),
            onClick = handler.onRestartOnboardingCarousel,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.reset_accessibility_disclaimer),
            onClick = handler.onResetAccessibilityDisclaimer,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.reset_coach_mark_tour_status),
            onClick = handler.onResetCoachMarkTourStatuses,
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        UniversalHorizontalDivider()
    }
}

@Composable
private fun CookiesSection(
    handler: DebugMenuHandler,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.cookies),
            modifier = Modifier
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.trigger_cookie_acquisition),
            onClick = handler.onTriggerCookieAcquisition,
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.clear_sso_cookies),
            onClick = handler.onClearSsoCookies,
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        UniversalHorizontalDivider()
    }
}

@Composable
private fun PremiumSection(
    handler: DebugMenuHandler,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.premium),
            modifier = Modifier
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.reset_premium_upgrade_banner),
            onClick = handler.onResetPremiumUpgradeBanner,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.show_upgraded_to_premium_card),
            onClick = handler.onShowUpgradedToPremiumCard,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        UniversalHorizontalDivider()
    }
}

@Composable
private fun ReportsSection(
    handler: DebugMenuHandler,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        UniversalListHeaderText(
            label = stringResource(id = UniversalString.error_reports),
            modifier = Modifier
                .standardHorizontalMargin()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.share_settings),
            onClick = handler.onShareSettingsClick,
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.generate_error_report),
            onClick = handler.onGenerateErrorReportClick,
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.generate_crash),
            onClick = handler.onGenerateCrashClick,
            isEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun ButtonWithExplanation(
    label: String,
    explanation: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        UniversalFilledButton(
            label = label,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text = explanation,
            style = UniversalTheme.typography.bodySmall,
            color = UniversalTheme.colorScheme.text.primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureFlagContent_preview() {
    UniversalTheme {
        FeatureFlagContent(
            featureFlagMap = persistentMapOf(
                FlagKey.DummyBoolean to true,
            ),
            handler = DebugMenuHandler.createEmpty(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DebugOptionsContent_preview() {
    UniversalTheme {
        DebugOptionsContent(handler = DebugMenuHandler.createEmpty())
    }
}
