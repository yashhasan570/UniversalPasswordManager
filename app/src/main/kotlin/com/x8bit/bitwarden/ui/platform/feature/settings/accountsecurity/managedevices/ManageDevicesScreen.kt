package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import .ui.platform.base.util.EventsEffect
import .ui.platform.base.util.LifecycleEventEffect
import .ui.platform.base.util.annotatedStringResource
import .ui.platform.base.util.cardStyle
import .ui.platform.base.util.mirrorIfRtl
import .ui.platform.base.util.standardHorizontalMargin
import .ui.platform.base.util.toListItemCardStyle
import .ui.platform.components.appbar.Universal Password ManagerTopAppBar
import .ui.platform.components.badge.Universal Password ManagerStatusBadge
import .ui.platform.components.bottomsheet.Universal Password ManagerModalBottomSheet
import .ui.platform.components.button.Universal Password ManagerFilledButton
import .ui.platform.components.button.Universal Password ManagerOutlinedButton
import .ui.platform.components.content.Universal Password ManagerErrorContent
import .ui.platform.components.content.Universal Password ManagerLoadingContent
import .ui.platform.components.model.CardStyle
import .ui.platform.components.scaffold.Universal Password ManagerScaffold
import .ui.platform.components.scaffold.model.rememberUniversal Password ManagerPullToRefreshState
import .ui.platform.components.snackbar.Universal Password ManagerSnackbarHost
import .ui.platform.components.snackbar.model.rememberUniversal Password ManagerSnackbarHostState
import .ui.platform.components.util.rememberVectorPainter
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.util.Text
import com.x8bit.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager

/**
 * Displays the Manage Devices screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun ManageDevicesScreen(
    viewModel: ManageDevicesViewModel = hiltViewModel(),
    permissionsManager: PermissionsManager = LocalPermissionsManager.current,
    onNavigateBack: () -> Unit,
    onNavigateToLoginApproval: (fingerprint: String) -> Unit,
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberUniversal Password ManagerPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(ManageDevicesAction.RefreshPull) },
    )
    val snackbarHostState = rememberUniversal Password ManagerSnackbarHostState()
    EventsEffect(viewModel = viewModel) { event ->
        when (event) {
            ManageDevicesEvent.NavigateBack -> onNavigateBack()
            is ManageDevicesEvent.NavigateToLoginApproval -> {
                onNavigateToLoginApproval(event.fingerprint)
            }

            is ManageDevicesEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.data)
            }
        }
    }

    LifecycleEventEffect { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.trySendAction(ManageDevicesAction.LifecycleResume)
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
        onDismiss = { viewModel.trySendAction(ManageDevicesAction.HideBottomSheet) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier.statusBarsPadding(),
    ) { animatedOnDismiss ->
        ManageDevicesBottomSheetContent(
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
                title = stringResource(id = Universal Password ManagerString.devices),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = Universal Password ManagerString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(ManageDevicesAction.CloseClick)
                },
            )
        },
        pullToRefreshState = pullToRefreshState,
        snackbarHost = {
            Universal Password ManagerSnackbarHost(universalpasswordmanagerHostState = snackbarHostState)
        },
    ) {
        when (val viewState = state.viewState) {
            is ManageDevicesState.ViewState.Content -> {
                ManageDevicesContent(
                    modifier = Modifier.fillMaxSize(),
                    state = viewState,
                    onNavigateToLoginApproval = {
                        viewModel.trySendAction(ManageDevicesAction.PendingRequestRowClick(it))
                    },
                )
            }

            ManageDevicesState.ViewState.Error -> Universal Password ManagerErrorContent(
                message = stringResource(
                    id = Universal Password ManagerString.generic_error_message,
                ),
                modifier = Modifier.fillMaxSize(),
            )

            ManageDevicesState.ViewState.Loading -> Universal Password ManagerLoadingContent(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

/**
 * Models the list content for the Manage Devices screen.
 */
@Composable
private fun ManageDevicesContent(
    state: ManageDevicesState.ViewState.Content,
    onNavigateToLoginApproval: (fingerprint: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Spacer(modifier = Modifier.height(height = 12.dp))
        }
        itemsIndexed(state.items) { index, item ->
            when (item.status) {
                DeviceSessionStatus.Pending -> item.fingerprintPhrase?.let {
                    PendingRequestItem(
                        fingerprintPhrase = item.fingerprintPhrase,
                        platform = item.typeName(),
                        firstLoginDate = item.firstLoginDate,
                        isTrusted = item.isTrusted,
                        onNavigateToLoginApproval = onNavigateToLoginApproval,
                        cardStyle = state.items.toListItemCardStyle(
                            index = index,
                            dividerPadding = 0.dp,
                        ),
                        modifier = Modifier
                            .testTag("LoginRequestCell")
                            .fillMaxWidth()
                            .standardHorizontalMargin(),
                    )
                }

                DeviceSessionStatus.None,
                DeviceSessionStatus.Current,
                    -> {
                    SessionItem(
                        platform = item.typeName(),
                        firstLoginDate = item.firstLoginDate,
                        lastActivityLabel = item.lastActivityLabel,
                        status = item.status,
                        isTrusted = item.isTrusted,
                        cardStyle = state.items.toListItemCardStyle(
                            index = index,
                            dividerPadding = 0.dp,
                        ),
                        modifier = Modifier
                            .testTag("CurrentItemCell")
                            .fillMaxWidth()
                            .standardHorizontalMargin(),
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

/**
 * Represents a pending request item to display in the list.
 */
@Composable
private fun PendingRequestItem(
    fingerprintPhrase: String,
    platform: String,
    firstLoginDate: String,
    isTrusted: Boolean,
    onNavigateToLoginApproval: (fingerprint: String) -> Unit,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                onClick = { onNavigateToLoginApproval(fingerprintPhrase) },
                paddingHorizontal = 16.dp,
            ),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Universal Password ManagerStatusBadge(
                label = stringResource(id = Universal Password ManagerString.pending_request),
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.warning,
            )
            Spacer(Modifier.height(height = 8.dp))
            Text(
                text = platform,
                style = Universal Password ManagerTheme.typography.titleMedium,
                color = Universal Password ManagerTheme.colorScheme.text.primary,
                textAlign = TextAlign.Start,
            )
            if (isTrusted) {
                Text(
                    text = stringResource(id = Universal Password ManagerString.trusted),
                    style = Universal Password ManagerTheme.typography.bodySmall,
                    color = Universal Password ManagerTheme.colorScheme.text.secondary,
                    textAlign = TextAlign.Start,
                )
            }
            DeviceInfoAnnotatedLabel(
                id = Universal Password ManagerString.first_login_date,
                arg = firstLoginDate,
            )
        }
        Icon(
            painter = rememberVectorPainter(id = Universal Password ManagerDrawable.ic_chevron_right),
            contentDescription = null,
            tint = Universal Password ManagerTheme.colorScheme.icon.primary,
            modifier = Modifier
                .mirrorIfRtl()
                .size(size = 16.dp),
        )
    }
}

/**
 * Represents a registered session item to display in the list.
 */
@Composable
private fun SessionItem(
    platform: String,
    firstLoginDate: String,
    lastActivityLabel: Text?,
    status: DeviceSessionStatus,
    isTrusted: Boolean,
    cardStyle: CardStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .cardStyle(
                cardStyle = cardStyle,
                paddingHorizontal = 16.dp,
            ),
        horizontalAlignment = Alignment.Start,
    ) {
        if (status == DeviceSessionStatus.Current) {
            Universal Password ManagerStatusBadge(
                label = stringResource(id = Universal Password ManagerString.current_session),
                colors = Universal Password ManagerTheme.colorScheme.statusBadge.primary,
            )
            Spacer(Modifier.height(height = 8.dp))
        }
        Text(
            text = platform,
            style = Universal Password ManagerTheme.typography.titleMedium,
            color = Universal Password ManagerTheme.colorScheme.text.primary,
            textAlign = TextAlign.Start,
        )
        if (isTrusted) {
            Text(
                text = stringResource(id = Universal Password ManagerString.trusted),
                style = Universal Password ManagerTheme.typography.bodySmall,
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                textAlign = TextAlign.Start,
            )
        }
        if (status != DeviceSessionStatus.Current) {
            lastActivityLabel?.let {
                DeviceInfoAnnotatedLabel(
                    id = Universal Password ManagerString.recently_active,
                    arg = it(),
                )
            }
        }
        DeviceInfoAnnotatedLabel(
            id = Universal Password ManagerString.first_login_date,
            arg = firstLoginDate,
        )
    }
}

/**
 * Displays an annotated string resource with [arg] substituted in, using secondary text color and
 * a bold emphasis style for the dynamic portion.
 */
@Composable
private fun DeviceInfoAnnotatedLabel(
    @StringRes id: Int,
    arg: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = annotatedStringResource(
            id = id,
            args = arrayOf(arg),
            emphasisHighlightStyle = SpanStyle(
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                fontSize = Universal Password ManagerTheme.typography.bodySmall.fontSize,
                fontWeight = FontWeight.Bold,
            ),
            style = SpanStyle(
                color = Universal Password ManagerTheme.colorScheme.text.secondary,
                fontSize = Universal Password ManagerTheme.typography.bodySmall.fontSize,
            ),
        ),
        textAlign = TextAlign.Start,
        modifier = modifier,
    )
}

@Composable
private fun ManageDevicesBottomSheetContent(
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
