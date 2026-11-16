package com.x8bit.universalpasswordmanager.ui.vault.feature.qrcodescan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import .ui.platform.base.util.ColorSchemeOverride
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.StatusBarsAppearanceAffect
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.camera.CameraPreview
import .ui.platform.components.camera.QrCodeSquare
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalQrCodeAnalyzer
import .ui.platform.feature.qrcodescan.util.QrCodeAnalyzer
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.model.WindowSize
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.util.rememberWindowSize

/**
 * The screen to scan QR codes for the application.
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScanScreen(
    onNavigateBack: () -> Unit,
    onNavigateToManualCodeEntryScreen: () -> Unit,
    viewModel: QrCodeScanViewModel = hiltViewModel(),
    qrCodeAnalyzer: QrCodeAnalyzer = LocalQrCodeAnalyzer.current,
) {
    qrCodeAnalyzer.onQrCodeScanned = {
        viewModel.trySendAction(QrCodeScanAction.QrCodeScanReceive(it))
    }

    val onEnterKeyManuallyClick = { viewModel.trySendAction(QrCodeScanAction.ManualEntryTextClick) }

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is QrCodeScanEvent.NavigateBack -> {
                onNavigateBack.invoke()
            }

            is QrCodeScanEvent.NavigateToManualCodeEntry -> {
                onNavigateToManualCodeEntryScreen.invoke()
            }
        }
    }
    // This screen should always look like it's in dark mode
    ColorSchemeOverride(appTheme = AppTheme.DARK) {
        StatusBarsAppearanceAffect()
        Universal Password ManagerScaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Universal Password ManagerTopAppBar(
                    title = stringResource(id = Universal Password ManagerString.scan_qr_code),
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                    navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                    onNavigationIconClick = {
                        viewModel.trySendAction(QrCodeScanAction.CloseClick)
                    },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                        state = rememberTopAppBarState(),
                    ),
                )
            },
        ) {
            CameraPreview(
                cameraErrorReceive = {
                    viewModel.trySendAction(QrCodeScanAction.CameraSetupErrorReceive)
                },
                analyzer = qrCodeAnalyzer,
                modifier = Modifier.fillMaxSize(),
            )
            when (rememberWindowSize()) {
                WindowSize.Compact -> {
                    QrCodeContentCompact(
                        onEnterKeyManuallyClick = onEnterKeyManuallyClick,
                    )
                }

                WindowSize.Medium -> {
                    QrCodeContentMedium(
                        onEnterKeyManuallyClick = onEnterKeyManuallyClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun QrCodeContentCompact(
    onEnterKeyManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        QrCodeSquare(
            squareOutlineSize = 250.dp,
            modifier = Modifier.weight(2f),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(color = Universal Password ManagerTheme.colorScheme.background.scrim)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(id = Universal Password ManagerString.point_your_camera_at_the_qr_code),
                textAlign = TextAlign.Center,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            EnterKeyManuallyText(
                onEnterKeyManuallyClick = onEnterKeyManuallyClick,
            )
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun QrCodeContentMedium(
    onEnterKeyManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        QrCodeSquare(
            squareOutlineSize = 200.dp,
            modifier = Modifier.weight(2f),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(color = Universal Password ManagerTheme.colorScheme.background.scrim)
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(id = Universal Password ManagerString.point_your_camera_at_the_qr_code),
                textAlign = TextAlign.Center,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                style = Universal Password ManagerTheme.typography.bodySmall,
            )

            EnterKeyManuallyText(
                onEnterKeyManuallyClick = onEnterKeyManuallyClick,
            )
        }
    }
}

@Composable
private fun EnterKeyManuallyText(
    onEnterKeyManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Universal Password ManagerHyperTextLink(
        annotatedResId = Universal Password ManagerString.cannot_scan_qr_code_enter_key_manually,
        annotationKey = "enterKeyManually",
        accessibilityString = stringResource(Universal Password ManagerString.enter_key_manually),
        onClick = onEnterKeyManuallyClick,
        style = Universal Password ManagerTheme.typography.bodySmall,
        color = Universal Password ManagerTheme.colorScheme.text.primary,
        modifier = modifier,
    )
}
