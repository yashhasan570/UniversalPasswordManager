package .ui.platform.components.scaffold.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * The state of the pull-to-refresh.
 */
data class Universal Password ManagerPullToRefreshState(
    val isEnabled: Boolean,
    val isRefreshing: Boolean,
    val onRefresh: () -> Unit,
)

/**
 * Create and remember the default [Universal Password ManagerPullToRefreshState].
 */
@Composable
fun rememberUniversal Password ManagerPullToRefreshState(
    isEnabled: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
): Universal Password ManagerPullToRefreshState = remember(isEnabled, isRefreshing, onRefresh) {
    Universal Password ManagerPullToRefreshState(
        isEnabled = isEnabled,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    )
}
