package com.x8bit.universalpasswordmanager.ui.platform.feature.accessibilitydisclosure

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalExitManager
import .ui.platform.manager.exit.ExitManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme

/**
 * Top-level composable for the Accessibility Disclosure screen.
 */
@Composable
fun AccessibilityDisclosureScreen(
    onDismiss: () -> Unit,
    viewModel: AccessibilityDisclosureViewModel = hiltViewModel(),
    exitManager: ExitManager = LocalExitManager.current,
) {
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is AccessibilityDisclosureEvent.Dismiss -> onDismiss()
            is AccessibilityDisclosureEvent.CloseApp -> exitManager.exitApplication()
        }
    }

    BackHandler { viewModel.trySendAction(AccessibilityDisclosureAction.CloseAppClick) }
    Universal Password ManagerScaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .union(WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    ) {
        AccessibilityDisclosureContent(
            iUnderstandClick = {
                viewModel.trySendAction(AccessibilityDisclosureAction.IUnderstandClick)
            },
            onCloseAppClick = {
                viewModel.trySendAction(AccessibilityDisclosureAction.CloseAppClick)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun AccessibilityDisclosureContent(
    iUnderstandClick: () -> Unit,
    onCloseAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(height = 32.dp))

        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_autofill),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(size = 100.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.accessibility_service_disclosure),
            style = Universal Password ManagerTheme.typography.headlineSmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.accessibility_disclosure_start_up_text_1),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.accessibility_disclosure_start_up_text_2),
            style = Universal Password ManagerTheme.typography.bodySmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.accessibility_disclosure_start_up_text_3),
            style = Universal Password ManagerTheme.typography.bodySmall,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.i_understand),
            onClick = iUnderstandClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.close_app),
            onClick = onCloseAppClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun AccessibilityDisclosureContent_preview() {
    Universal Password ManagerTheme {
        AccessibilityDisclosureContent(
            iUnderstandClick = {},
            onCloseAppClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
