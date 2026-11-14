package com.universalpasswordmanager.ui.platform.components.scaffold.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * The state of the pull-to-refresh.
 */
data class UniversalPullToRefreshState(
    val isEnabled: Boolean,
    val isRefreshing: Boolean,
    val onRefresh: () -> Unit,
)

/**
 * Create and remember the default [UniversalPullToRefreshState].
 */
@Composable
fun rememberUniversalPullToRefreshState(
    isEnabled: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
): UniversalPullToRefreshState = remember(isEnabled, isRefreshing, onRefresh) {
    UniversalPullToRefreshState(
        isEnabled = isEnabled,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    )
}
