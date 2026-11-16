package com.x8bit.universalpasswordmanager.ui.vault.feature.migratetomyitems

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
import .ui.platform.components.button.Universal Password ManagerTextButton
import .ui.platform.components.dialog.Universal Password ManagerBasicDialog
import .ui.platform.components.dialog.Universal Password ManagerLoadingDialog
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.text.Universal Password ManagerHyperTextLink
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.composition.LocalIntentManager
import .ui.platform.manager.IntentManager
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.vault.feature.migratetomyitems.handler.rememberMigrateToMyItemsHandler

/**
 * Top level screen component for the MigrateToMyItems screen.
 */
@Composable
fun MigrateToMyItemsScreen(
    onNavigateToLeaveOrganization: (organizationId: String, organizationName: String) -> Unit,
    viewModel: MigrateToMyItemsViewModel = hiltViewModel(),
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handlers = rememberMigrateToMyItemsHandler(viewModel)

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            is MigrateToMyItemsEvent.NavigateToLeaveOrganization -> {
                onNavigateToLeaveOrganization(event.organizationId, event.organizationName)
            }

            is MigrateToMyItemsEvent.LaunchUri -> intentManager.launchUri(event.uri.toUri())
        }
    }

    MigrateToMyItemsDialogs(
        dialog = state.dialog,
        onDismissRequest = handlers.onDismissDialog,
        onDismissNoNetworkRequest = handlers.onDismissNoNetworkDialog,
    )

    Universal Password ManagerScaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .union(WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    ) {
        MigrateToMyItemsContent(
            state = state,
            onAcceptClick = handlers.onAcceptClick,
            onDeclineClick = handlers.onDeclineClick,
            onHelpClick = handlers.onHelpClick,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
private fun MigrateToMyItemsDialogs(
    dialog: MigrateToMyItemsState.DialogState?,
    onDismissRequest: () -> Unit,
    onDismissNoNetworkRequest: () -> Unit,
) {
    when (dialog) {
        is MigrateToMyItemsState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                throwable = dialog.throwable,
                onDismissRequest = onDismissRequest,
            )
        }

        is MigrateToMyItemsState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(text = dialog.message())
        }

        is MigrateToMyItemsState.DialogState.NoNetwork -> {
            Universal Password ManagerBasicDialog(
                title = dialog.title(),
                message = dialog.message(),
                throwable = dialog.throwable,
                onDismissRequest = onDismissNoNetworkRequest,
            )
        }

        null -> Unit
    }
}

@Composable
private fun MigrateToMyItemsContent(
    state: MigrateToMyItemsState,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_business_lock),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(100.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        MigrateToMyItemsTextContent(
            organizationName = state.organizationName,
            onLearnMoreClick = onHelpClick,
        )
        Spacer(modifier = Modifier.height(24.dp))
        MigrateToMyItemsActions(
            onContinueClick = onAcceptClick,
            onDeclineClick = onDeclineClick,
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun MigrateToMyItemsTextContent(
    organizationName: String,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(
                id = Universal Password ManagerString.transfer_items_to_org,
                organizationName,
            ),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerHyperTextLink(
            annotatedResId = Universal Password ManagerString.transfer_items_description,
            args = arrayOf(organizationName),
            annotationKey = "learnMore",
            accessibilityString = stringResource(Universal Password ManagerString.learn_more),
            onClick = onLearnMoreClick,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            isExternalLink = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Composable
private fun MigrateToMyItemsActions(
    onContinueClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.accept),
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Universal Password ManagerTextButton(
            label = stringResource(id = Universal Password ManagerString.decline_and_leave),
            onClick = onDeclineClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MigrateToMyItemsScreen_preview() {
    Universal Password ManagerTheme {
        Universal Password ManagerScaffold {
            MigrateToMyItemsContent(
                state = MigrateToMyItemsState(
                    organizationId = "test-org-id",
                    organizationName = "Universal Password Manager",
                    dialog = null,
                ),
                onAcceptClick = {},
                onDeclineClick = {},
                onHelpClick = {},
            )
        }
    }
}
