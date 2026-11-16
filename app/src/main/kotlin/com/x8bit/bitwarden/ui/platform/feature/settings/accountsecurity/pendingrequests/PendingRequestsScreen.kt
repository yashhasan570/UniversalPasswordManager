package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.pendingrequests

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.LifecycleEventEffect
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.bottomsheet.Universal Password ManagerModalBottomSheet
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.dialog.Universal Password ManagerTwoButtonDialog
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.scaffold.model.rememberUniversal Password ManagerPullToRefreshState
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager

/**
 * Displays the pending login requests screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun PendingRequestsScreen(
    viewModel: PendingRequestsViewModel = hiltViewModel(),
    permissionsManager: PermissionsManager = LocalPermissionsManager.current,
    onNavigateBack: () -> Unit,
    onNavigateToLoginApproval: (fingerprint: String) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberUniversal Password ManagerPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(PendingRequestsAction.RefreshPull) },
    )
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            PendingRequestsEvent.NavigateBack -> onNavigateBack()
            is PendingRequestsEvent.NavigateToLoginApproval -> {
                onNavigateToLoginApproval(event.fingerprint)
            }

            is PendingRequestsEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.data)
        }
    }

    LifecycleEventEffect { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.trySendAction(PendingRequestsAction.LifecycleResume)
            }

            else -> Unit
        }
    }

    val hideBottomSheet = state.hideBottomSheet ||
        permissionsManager.checkPermission(Manifest.permission.POST_NOTIFICATIONS) ||
        permissionsManager.shouldShowRequestPermissionRationale(
            permission = Manifest.permission.POST_NOTIFICATIONS,
        )
    Universal Password ManagerModalBottomSheet(
        showBottomSheet = !hideBottomSheet,
        sheetTitle = stringResource(Universal Password ManagerString.enable_notifications),
        onDismiss = { viewModel.trySendAction(PendingRequestsAction.HideBottomSheet) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier.statusBarsPadding(),
    ) { animatedOnDismiss ->
        PendingRequestsBottomSheetContent(
            permissionsManager = permissionsManager,
            onDismiss = animatedOnDismiss,
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Universal Password ManagerScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Universal Password ManagerTopAppBar(
                title = stringResource(id = Universal Password ManagerString.pending_access_requests),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(PendingRequestsAction.CloseClick)
                },
            )
        },
        pullToRefreshState = pullToRefreshState,
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        when (val viewState = state.viewState) {
            is PendingRequestsState.ViewState.Content -> {
                PendingRequestsContent(
                    modifier = Modifier.fillMaxSize(),
                    state = viewState,
                    onDeclineAllRequestsConfirm = {
                        viewModel.trySendAction(PendingRequestsAction.DeclineAllRequestsConfirm)
                    },
                    onNavigateToLoginApproval = {
                        viewModel.trySendAction(PendingRequestsAction.PendingRequestRowClick(it))
                    },
                )
            }

            is PendingRequestsState.ViewState.Empty -> PendingRequestsEmpty(
                modifier = Modifier.fillMaxSize(),
            )

            PendingRequestsState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = stringResource(Universal Password ManagerString.generic_error_message),
                modifier = Modifier.fillMaxSize(),
            )

            PendingRequestsState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

/**
 * Models the list content for the Pending Requests screen.
 */
@Composable
private fun PendingRequestsContent(
    state: PendingRequestsState.ViewState.Content,
    onDeclineAllRequestsConfirm: () -> Unit,
    onNavigateToLoginApproval: (fingerprint: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        var shouldShowDeclineAllRequestsConfirm by remember { mutableStateOf(false) }

        if (shouldShowDeclineAllRequestsConfirm) {
            Universal Password ManagerTwoButtonDialog(
                title = stringResource(Universal Password ManagerString.decline_all_requests),
                message = stringResource(
                    id = Universal Password ManagerString
                        .are_you_sure_you_want_to_decline_all_pending_log_in_requests,
                ),
                confirmButtonText = stringResource(Universal Password ManagerString.yes),
                dismissButtonText = stringResource(id = Universal Password ManagerString.cancel),
                onConfirmClick = {
                    onDeclineAllRequestsConfirm()
                    shouldShowDeclineAllRequestsConfirm = false
                },
                onDismissClick = { shouldShowDeclineAllRequestsConfirm = false },
                onDismissRequest = { shouldShowDeclineAllRequestsConfirm = false },
            )
        }

        LazyColumn(
            modifier = Modifier.weight(weight = 1f, fill = false),
        ) {
            item {
                Spacer(modifier = Modifier.height(height = 12.dp))
            }
            itemsIndexed(state.requests) { index, request ->
                PendingRequestItem(
                    fingerprintPhrase = request.fingerprintPhrase,
                    platform = request.platform,
                    timestamp = request.timestamp,
                    onNavigateToLoginApproval = onNavigateToLoginApproval,
                    cardStyle = state.requests.toListItemCardStyle(
                        index = index,
                        dividerPadding = 0.dp,
                    ),
                    modifier = Modifier
                        .testTag("LoginRequestCell")
                        .fillMaxWidth()
                        .standardHorizontalMargin(),
                )
            }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))

        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.decline_all_requests),
            icon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_trash),
            onClick = { shouldShowDeclineAllRequestsConfirm = true },
            modifier = Modifier
                .testTag("DeclineAllRequestsButton")
                .standardHorizontalMargin()
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(height = 16.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

/**
 * Represents a pending request item to display in the list.
 */
@Composable
private fun PendingRequestItem(
    fingerprintPhrase: String,
    platform: String,
    timestamp: String,
    onNavigateToLoginApproval: (fingerprintPhrase: String) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                onClick = { onNavigateToLoginApproval(fingerprintPhrase) },
                paddingHorizontal = 16.dp,
            ),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(id = Universal Password ManagerString.fingerprint_phrase),
            style = Universal Password ManagerTheme.typography.labelMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = fingerprintPhrase,
            color = Universal Password ManagerTheme.colorScheme.text.codePink,
            style = Universal Password ManagerTheme.typography.sensitiveInfoSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .testTag("FingerprintValueLabel")
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = platform,
                style = Universal Password ManagerTheme.typography.bodyMedium,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Text(
                text = timestamp,
                style = Universal Password ManagerTheme.typography.labelSmall,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                textAlign = TextAlign.End,
            )
        }
    }
}

/**
 * Models the empty state for the Pending Requests screen.
 */
@Composable
private fun PendingRequestsEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_devices),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(size = 124.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = Universal Password ManagerString.no_pending_requests),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.navigationBarsPadding())
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun PendingRequestsBottomSheetContent(
    permissionsManager: PermissionsManager,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val notificationPermissionLauncher = permissionsManager.getLauncher {
        onDismiss()
    }
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(height = 24.dp))
        Image(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ill_2fa),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(size = 132.dp)
                .align(alignment = Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Text(
            text = stringResource(id = Universal Password ManagerString.log_in_quickly_and_easily_across_devices),
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        @Suppress("MaxLineLength")
        Text(
            text = stringResource(
                id = Universal Password ManagerString.universalpasswordmanager_can_notify_you_each_time_you_receive_a_new_login_request_from_another_device,
            ),
            style = Universal Password ManagerTheme.typography.bodyMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Universal Password ManagerFilledButton(
            label = stringResource(id = Universal Password ManagerString.enable_notifications),
            onClick = {
                @SuppressLint("InlinedApi")
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        Universal Password ManagerOutlinedButton(
            label = stringResource(id = Universal Password ManagerString.skip_for_now),
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
