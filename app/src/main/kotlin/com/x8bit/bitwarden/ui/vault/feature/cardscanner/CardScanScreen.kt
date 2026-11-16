package com.x8bit.universalpasswordmanager.ui.vault.feature.cardscanner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.ColorSchemeOverride
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.StatusBarsAppearanceAffect
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.camera.CameraPreview
import .ui.platform.components.camera.CardScanOverlay
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalCardTextAnalyzer
import .ui.platform.feature.cardscanner.util.CardTextAnalyzer
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.model.WindowSize
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.util.rememberWindowSize

/**
 * The screen to scan credit cards for the application.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScanScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardScanViewModel = hiltViewModel(),
    cardTextAnalyzer: CardTextAnalyzer = LocalCardTextAnalyzer.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    cardTextAnalyzer.onCardScanned = { cardScanData ->
        viewModel.trySendAction(
            CardScanAction.CardScanReceive(cardScanData = cardScanData),
        )
    }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is CardScanEvent.NavigateBack -> onNavigateBack()
        }
    }

    // This screen should always look like it's in dark mode
    ColorSchemeOverride(appTheme = AppTheme.DARK) {
        StatusBarsAppearanceAffect()
        Universal Password ManagerScaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Universal Password ManagerTopAppBar(
                    title = stringResource(id = Universal Password ManagerString.scan_card),
                    navigationIcon = rememberVectorPainter(
                        id = Universal Password ManagerDrawable.ic_close,
                    ),
                    navigationIconContentDescription = stringResource(
                        id = Universal Password ManagerString.close,
                    ),
                    onNavigationIconClick = {
                        viewModel.trySendAction(CardScanAction.CloseClick)
                    },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                        state = rememberTopAppBarState(),
                    ),
                )
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = stringResource(id = Universal Password ManagerString.scan_card_instruction),
                    textAlign = TextAlign.Center,
                    color = Universal Password ManagerTheme.colorScheme.text.primary,
                    style = Universal Password ManagerTheme.typography.bodyMedium,
                    modifier = Modifier
                        .testTag("CardScanInstruction")
                        .fillMaxWidth()
                        .padding(all = 12.dp),
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .testTag("CardScanFrame")
                        .fillMaxSize(),
                ) {
                    CameraPreview(
                        cameraErrorReceive = {
                            viewModel.trySendAction(
                                CardScanAction.CameraSetupErrorReceive,
                            )
                        },
                        analyzer = cardTextAnalyzer,
                        modifier = Modifier.fillMaxSize(),
                    )
                    CardScanOverlay(
                        overlayWidth = when (rememberWindowSize()) {
                            WindowSize.Compact -> 300.dp
                            WindowSize.Medium -> 250.dp
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                    AnimatedScanHintBanner(
                        visible = state.showHint,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

/**
 * Wraps [ScanHintBanner] in a fade-in/fade-out so the hint doesn't snap into place when the
 * timeout elapses or vanish abruptly once a scan succeeds.
 */
@Composable
private fun AnimatedScanHintBanner(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        ScanHintBanner(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        )
    }
}

/**
 * A timeout-driven hint shown over the camera preview when no successful card scan has been
 * received within the expected window. Wrapped in a polite live region so TalkBack announces it
 * to users when it appears.
 */
@Composable
private fun ScanHintBanner(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(
            id = Universal Password ManagerString.hold_steady_and_ensure_all_card_details_are_visible,
        ),
        textAlign = TextAlign.Center,
        color = Universal Password ManagerTheme.colorScheme.text.primary,
        style = Universal Password ManagerTheme.typography.bodyMedium,
        modifier = modifier
            .semantics { liveRegion = LiveRegionMode.Polite }
            .background(
                color = Universal Password ManagerTheme.colorScheme.background.scrim,
                shape = Universal Password ManagerTheme.shapes.content,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}
