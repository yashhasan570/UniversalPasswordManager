package com.x8bit.universalpasswordmanager.ui.vault.feature.leaveorganization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.button.Universal Password ManagerFilledErrorButton
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
import com.x8bit.universalpasswordmanager.ui.vault.feature.leaveorganization.handlers.rememberLeaveOrganizationHandler

/**
 * Top-level composable for the Leave Organization screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveOrganizationScreen(
    onNavigateBack: () -> Unit,
    viewModel: LeaveOrganizationViewModel,
    intentManager: IntentManager = LocalIntentManager.current,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val handlers = rememberLeaveOrganizationHandler(viewModel)

    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            LeaveOrganizationEvent.NavigateBack -> onNavigateBack()
            is LeaveOrganizationEvent.LaunchUri -> {
                intentManager.launchUri(event.uri.toUri())
            }
        }
    }

    LeaveOrganizationDialogs(
        dialogState = state.dialogState,
        onDismissRequest = handlers.onDismissDialog,
        onDismissNoNetworkRequest = handlers.onDismissNoNetworkDialog,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.leave_organization),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.back),
                onNavigationIconClick = handlers.onBackClick,
            )
        },
    ) {
        LeaveOrganizationContent(
            state = state,
            onLeaveClick = handlers.onLeaveClick,
            onHelpLinkClick = handlers.onHelpClick,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun LeaveOrganizationDialogs(
    dialogState: LeaveOrganizationState.DialogState?,
    onDismissRequest: () -> Unit,
    onDismissNoNetworkRequest: () -> Unit,
) {
    when (dialogState) {
        LeaveOrganizationState.DialogState.Loading -> {
            Universal Password ManagerLoadingDialog(
                text = stringResource(id = Universal Password ManagerString.loading),
            )
        }

        is LeaveOrganizationState.DialogState.Error -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissRequest,
            )
        }

        is LeaveOrganizationState.DialogState.NoNetwork -> {
            Universal Password ManagerBasicDialog(
                title = dialogState.title(),
                message = dialogState.message(),
                throwable = dialogState.error,
                onDismissRequest = onDismissNoNetworkRequest,
            )
        }

        null -> Unit
    }
}

@Suppress("LongMethod")
@Composable
private fun LeaveOrganizationContent(
    state: LeaveOrganizationState,
    onLeaveClick: () -> Unit,
    onHelpLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_business_warning),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(100.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(
                id = Universal Password ManagerString.are_you_sure_you_want_to_leave_organization,
            ),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Universal Password ManagerHyperTextLink(
            annotatedResId = Universal Password ManagerString.leave_organization_warning,
            annotationKey = "learnMore",
            accessibilityString = stringResource(Universal Password ManagerString.learn_more),
            onClick = onHelpLinkClick,
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            isExternalLink = true,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(
                id = Universal Password ManagerString.contact_your_admin_to_regain_access,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Universal Password ManagerFilledErrorButton(
            label = stringResource(
                id = Universal Password ManagerString.leave_organization_button,
                state.organizationName,
            ),
            onClick = onLeaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LeaveOrganizationScreen_preview() {
    Universal Password ManagerTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Universal Password ManagerScaffold(
            topBar = {
                Universal Password ManagerTopAppBar(
                    title = "Leave organization",
                    scrollBehavior = scrollBehavior,
                    navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_back),
                    navigationIconContentDescription = "Back",
                    onNavigationIconClick = {},
                )
            },
        ) {
            LeaveOrganizationContent(
                state = LeaveOrganizationState(
                    organizationId = "",
                    organizationName = "Test Organization",
                    dialogState = null,
                ),
                onLeaveClick = {},
                onHelpLinkClick = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
