package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices

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
import com.universalpasswordmanager.ui.platform.base.util.EventsEffect
import com.universalpasswordmanager.ui.platform.base.util.LifecycleEventEffect
import com.universalpasswordmanager.ui.platform.base.util.annotatedStringResource
import com.universalpasswordmanager.ui.platform.base.util.cardStyle
import com.universalpasswordmanager.ui.platform.base.util.mirrorIfRtl
import com.universalpasswordmanager.ui.platform.base.util.standardHorizontalMargin
import com.universalpasswordmanager.ui.platform.base.util.toListItemCardStyle
import com.universalpasswordmanager.ui.platform.components.appbar.UniversalTopAppBar
import com.universalpasswordmanager.ui.platform.components.badge.UniversalStatusBadge
import com.universalpasswordmanager.ui.platform.components.bottomsheet.UniversalModalBottomSheet
import com.universalpasswordmanager.ui.platform.components.button.UniversalFilledButton
import com.universalpasswordmanager.ui.platform.components.button.UniversalOutlinedButton
import com.universalpasswordmanager.ui.platform.components.content.UniversalErrorContent
import com.universalpasswordmanager.ui.platform.components.content.UniversalLoadingContent
import com.universalpasswordmanager.ui.platform.components.model.CardStyle
import com.universalpasswordmanager.ui.platform.components.scaffold.UniversalScaffold
import com.universalpasswordmanager.ui.platform.components.scaffold.model.rememberUniversalPullToRefreshState
import com.universalpasswordmanager.ui.platform.components.snackbar.UniversalSnackbarHost
import com.universalpasswordmanager.ui.platform.components.snackbar.model.rememberUniversalSnackbarHostState
import com.universalpasswordmanager.ui.platform.components.util.rememberVectorPainter
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.platform.composition.LocalPermissionsManager
import com.universalpasswordmanager.ui.platform.manager.permissions.PermissionsManager

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
    val pullToRefreshState = rememberUniversalPullToRefreshState(
        isEnabled = state.isPullToRefreshEnabled,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.trySendAction(ManageDevicesAction.RefreshPull) },
    )
    val snackbarHostState = rememberUniversalSnackbarHostState()
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
    UniversalModalBottomSheet(
        showBottomSheet = !hideBottomSheet,
        sheetTitle = stringResource(UniversalString.enable_notifications),
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
    UniversalScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UniversalTopAppBar(
                title = stringResource(id = UniversalString.devices),
                scrollBehavior = scrollBehavior,
                navigationIcon = rememberVectorPainter(id = UniversalDrawable.ic_close),
                navigationIconContentDescription = stringResource(id = UniversalString.close),
                onNavigationIconClick = {
                    viewModel.trySendAction(ManageDevicesAction.CloseClick)
                },
            )
        },
        pullToRefreshState = pullToRefreshState,
        snackbarHost = {
            UniversalSnackbarHost(bitwardenHostState = snackbarHostState)
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

            ManageDevicesState.ViewState.Error -> UniversalErrorContent(
                message = stringResource(
                    id = UniversalString.generic_error_message,
                ),
                modifier = Modifier.fillMaxSize(),
            )

            ManageDevicesState.ViewState.Loading -> UniversalLoadingContent(
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
            UniversalStatusBadge(
                label = stringResource(id = UniversalString.pending_request),
                colors = UniversalTheme.colorScheme.statusBadge.warning,
            )
            Spacer(Modifier.height(height = 8.dp))
            Text(
                text = platform,
                style = UniversalTheme.typography.titleMedium,
                color = UniversalTheme.colorScheme.text.primary,
                textAlign = TextAlign.Start,
            )
            if (isTrusted) {
                Text(
                    text = stringResource(id = UniversalString.trusted),
                    style = UniversalTheme.typography.bodySmall,
                    color = UniversalTheme.colorScheme.text.secondary,
                    textAlign = TextAlign.Start,
                )
            }
            DeviceInfoAnnotatedLabel(
                id = UniversalString.first_login_date,
                arg = firstLoginDate,
            )
        }
        Icon(
            painter = rememberVectorPainter(id = UniversalDrawable.ic_chevron_right),
            contentDescription = null,
            tint = UniversalTheme.colorScheme.icon.primary,
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
            UniversalStatusBadge(
                label = stringResource(id = UniversalString.current_session),
                colors = UniversalTheme.colorScheme.statusBadge.primary,
            )
            Spacer(Modifier.height(height = 8.dp))
        }
        Text(
            text = platform,
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Start,
        )
        if (isTrusted) {
            Text(
                text = stringResource(id = UniversalString.trusted),
                style = UniversalTheme.typography.bodySmall,
                color = UniversalTheme.colorScheme.text.secondary,
                textAlign = TextAlign.Start,
            )
        }
        if (status != DeviceSessionStatus.Current) {
            lastActivityLabel?.let {
                DeviceInfoAnnotatedLabel(
                    id = UniversalString.recently_active,
                    arg = it(),
                )
            }
        }
        DeviceInfoAnnotatedLabel(
            id = UniversalString.first_login_date,
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
                color = UniversalTheme.colorScheme.text.secondary,
                fontSize = UniversalTheme.typography.bodySmall.fontSize,
                fontWeight = FontWeight.Bold,
            ),
            style = SpanStyle(
                color = UniversalTheme.colorScheme.text.secondary,
                fontSize = UniversalTheme.typography.bodySmall.fontSize,
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
            painter = rememberVectorPainter(id = UniversalDrawable.ill_2fa),
            contentDescription = null,
            modifier = Modifier
                .standardHorizontalMargin()
                .size(size = 132.dp)
                .align(alignment = Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Text(
            text = stringResource(id = UniversalString.log_in_quickly_and_easily_across_devices),
            style = UniversalTheme.typography.titleMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        @Suppress("MaxLineLength")
        Text(
            text = stringResource(
                id = UniversalString.bitwarden_can_notify_you_each_time_you_receive_a_new_login_request_from_another_device,
            ),
            style = UniversalTheme.typography.bodyMedium,
            color = UniversalTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        UniversalFilledButton(
            label = stringResource(id = UniversalString.enable_notifications),
            onClick = {
                @SuppressLint("InlinedApi")
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        UniversalOutlinedButton(
            label = stringResource(id = UniversalString.skip_for_now),
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .standardHorizontalMargin(),
        )
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
