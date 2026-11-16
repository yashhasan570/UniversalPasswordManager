package com.x8bit.universalpasswordmanager.ui.platform.feature.cookieacquisition

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalAuthTabLaunchers
import com.x8bit.universalpasswordmanager.ui.platform.feature.cookieacquisition.handlers.CookieAcquisitionHandler
import com.x8bit.universalpasswordmanager.ui.platform.feature.cookieacquisition.handlers.rememberCookieAcquisitionHandler
import com.x8bit.universalpasswordmanager.ui.platform.model.AuthTabLaunchers

/**
 * Top-level composable for the Cookie Acquisition screen.
 */
@Composable
fun CookieAcquisitionScreen(
    onDismiss: () -> Unit,
    viewModel: CookieAcquisitionViewModel = hiltViewModel(),
    authTabLaunchers: AuthTabLaunchers = LocalAuthTabLaunchers.current,
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is CookieAcquisitionEvent.LaunchBrowser -> {
                intentManager.startAuthTab(
                    uri = event.uri.toUri(),
                    authTabData = event.authTabData,
                    launcher = authTabLaunchers.cookie,
                )
            }

            CookieAcquisitionEvent.NavigateBack -> onDismiss()
        }
    }

    val handler = rememberCookieAcquisitionHandler(viewModel = viewModel)

    // Route back through the ViewModel so the pending cookie request is cleared
    // before dismissing. A normal back-pop would leave the request active and
    // MainViewModel would immediately re-navigate to this screen.
    BackHandler {
        viewModel.trySendAction(CookieAcquisitionAction.ContinueWithoutSyncingClick)
    }

    CookieAcquisitionDialogs(
        dialogState = state.dialogState,
        onDismissRequest = handler.onDismissDialogClick,
    )

    Universal Password ManagerScaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .union(WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    ) {
        CookieAcquisitionContent(
            state = state,
            handler = handler,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun CookieAcquisitionDialogs(
    dialogState: CookieAcquisitionDialogState?,
    onDismissRequest: () -> Unit,
) {
    when (dialogState) {
        is CookieAcquisitionDialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                onDismissRequest = onDismissRequest,
            )
        }

        null -> Unit
    }
}

@Suppress("LongMethod")
@Composable
private fun CookieAcquisitionContent(
    state: CookieAcquisitionState,
    handler: CookieAcquisitionHandler,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_keyhole),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(100.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.sync_with_browser),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(
                id = Universal Password ManagerString.sync_with_browser_description,
                state.environmentUrl,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.launch_browser),
            onClick = handler.onLaunchBrowserClick,
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_external_link),
            isExternalLink = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.continue_without_syncing),
            onClick = handler.onContinueWithoutSyncingClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun CookieAcquisitionScreen_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            CookieAcquisitionContent(
                state = CookieAcquisitionState(
                    environmentUrl = "vault.",
                    hostname = "",
                    dialogState = null,
                ),
                handler = CookieAcquisitionHandler(
                    onLaunchBrowserClick = {},
                    onContinueWithoutSyncingClick = {},
                    onDismissDialogClick = {},
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CookieAcquisitionScreen_darkPreview() {
    Universal Password ManagerTheme(theme = AppTheme.DARK) {
        Universal Password ManagerScaffold {
            CookieAcquisitionContent(
                state = CookieAcquisitionState(
                    environmentUrl = "vault.",
                    hostname = "",
                    dialogState = null,
                ),
                handler = CookieAcquisitionHandler(
                    onLaunchBrowserClick = {},
                    onContinueWithoutSyncingClick = {},
                    onDismissDialogClick = {},
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
