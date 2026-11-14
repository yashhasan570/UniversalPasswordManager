package com.universalpasswordmanager.ui.platform.feature.premium.upgraded

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
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.composition.LocalIntentManager
import com.universalpasswordmanager.ui.platform.feature.settings.appearance.model.AppTheme
import com.universalpasswordmanager.ui.platform.manager.IntentManager
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme

/**
 * Top-level composable for the "Upgraded to Premium" screen.
 */
@Composable
fun UpgradedToPremiumScreen(
    onDismiss: () -> Unit,
    viewModel: UpgradedToPremiumViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
) {
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            UpgradedToPremiumEvent.NavigateBack -> onDismiss()
            is UpgradedToPremiumEvent.NavigateToUrl -> {
                intentManager.launchUri(event.url.toUri())
            }
        }
    }

    // Route system back through the close handler so the acknowledgment is consumed before
    // the destination pops. A default Navigation Compose pop would leave pending=true on disk
    // and the action card would still surface on the next root-nav visit.
    BackHandler {
        viewModel.trySendAction(UpgradedToPremiumAction.CloseClick)
    }

    UniversalScaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .union(WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    ) {
        UpgradedToPremiumContent(
            onLearnMoreClick = {
                viewModel.trySendAction(UpgradedToPremiumAction.LearnMoreClick)
            },
            onCloseClick = {
                viewModel.trySendAction(UpgradedToPremiumAction.CloseClick)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun UpgradedToPremiumContent(
    onLearnMoreClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = rememberVectorPainter(id = UniversalDrawable.ill_celebration),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(100.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = UniversalString.upgraded_to_premium),
            style = UniversalTheme.typography.titleLarge,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(
                id = UniversalString
                    .you_now_have_access_to_advanced_security_features_learn_more_about_premium,
            ),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        UniversalFilledButton(
            label = stringResource(id = UniversalString.learn_more),
            onClick = onLearnMoreClick,
            icon = rememberVectorPainter(id = UniversalDrawable.ic_external_link),
            isExternalLink = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.close),
            onClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun UpgradedToPremiumContent_preview() {
    UniversalTheme {
        UniversalScaffold {
            UpgradedToPremiumContent(
                onLearnMoreClick = {},
                onCloseClick = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpgradedToPremiumContent_darkPreview() {
    UniversalTheme(theme = AppTheme.DARK) {
        UniversalScaffold {
            UpgradedToPremiumContent(
                onLearnMoreClick = {},
                onCloseClick = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
