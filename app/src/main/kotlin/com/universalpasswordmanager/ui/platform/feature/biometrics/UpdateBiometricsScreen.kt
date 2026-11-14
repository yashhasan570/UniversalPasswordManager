package com.universalpasswordmanager.ui.platform.feature.biometrics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalBasicDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalLoadingDialog
import com.universalpasswordmanager.ui.platform.components.dialog.UniversalTwoButtonDialog
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalBiometricsManager
import com.universalpasswordmanager.ui.platform.manager.biometrics.BiometricsManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Top-level composable for the Update Biometrics screen.
 */
@Composable
fun UpdateBiometricsScreen(
    onDismiss: () -> Unit,
    biometricsManager: BiometricsManager = LocalBiometricsManager.current,
    viewModel: UpdateBiometricsViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            UpdateBiometricsEvent.NavigateBack -> onDismiss()
            is UpdateBiometricsEvent.ShowBiometricsPrompt -> {
                biometricsManager.promptBiometrics(
                    onSuccess = {
                        viewModel.trySendAction(
                            UpdateBiometricsAction.BiometricCipherReceived(cipher = it),
                        )
                    },
                    onCancel = { },
                    onLockOut = { },
                    onError = { },
                    cipher = event.cipher,
                )
            }
        }
    }
    BackHandler { viewModel.trySendAction(UpdateBiometricsAction.TurnOffBiometricsClick) }
    UpdateBiometricsDialogs(
        dialogState = state.dialogState,
        onDismissClick = { viewModel.trySendAction(UpdateBiometricsAction.DismissDialog) },
        onConfirmTurnOffClick = {
            viewModel.trySendAction(UpdateBiometricsAction.ConfirmTurnOffClick)
        },
    )

    UniversalScaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .union(insets = WindowInsets.displayCutout)
            .only(sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    ) {
        UpdateBiometricsContent(
            onConfirmBiometricsClick = {
                viewModel.trySendAction(UpdateBiometricsAction.ConfirmBiometricsClick)
            },
            onTurnOffClick = {
                viewModel.trySendAction(UpdateBiometricsAction.TurnOffBiometricsClick)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun UpdateBiometricsDialogs(
    dialogState: UpdateBiometricsState.DialogState?,
    onDismissClick: () -> Unit,
    onConfirmTurnOffClick: () -> Unit,
) {
    when (dialogState) {
        UpdateBiometricsState.DialogState.DisableBiometricsAndClose -> {
            UniversalTwoButtonDialog(
                title = stringResource(id = UniversalString.turn_off_biometric_question),
                message = stringResource(
                    id = UniversalString.unlocking_will_require_your_master_password,
                ),
                confirmButtonText = stringResource(id = UniversalString.turn_off),
                dismissButtonText = stringResource(id = UniversalString.cancel),
                onDismissClick = onDismissClick,
                onDismissRequest = onDismissClick,
                onConfirmClick = onConfirmTurnOffClick,
                confirmTextColor = UniversalTheme.colorScheme.status.error,
            )
        }

        is UpdateBiometricsState.DialogState.Loading -> {
            UniversalLoadingDialog(text = dialogState.message())
        }

        is UpdateBiometricsState.DialogState.Error -> {
            UniversalBasicDialog(
                title = dialogState.title?.invoke(),
                message = dialogState.message(),
                onDismissRequest = onDismissClick,
            )
        }

        null -> Unit
    }
}

@Composable
private fun UpdateBiometricsContent(
    onConfirmBiometricsClick: () -> Unit,
    onTurnOffClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(height = 32.dp))

        Image(
            painter = rememberVectorPainter(id = UniversalDrawable.ill_fingerprint_unlock),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(size = 100.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            text = stringResource(id = UniversalString.keep_unlocking_with_biometrics),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = stringResource(id = UniversalString.weve_made_a_security_update),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalFilledButton(
            label = stringResource(id = UniversalString.confirm),
            onClick = onConfirmBiometricsClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.turn_off),
            onClick = onTurnOffClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
