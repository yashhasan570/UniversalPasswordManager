package com.universalpasswordmanager.ui.platform.components.snackbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarHostState

/**
 * A custom Universal-themed snackbar.
 *
 * @param universalpasswordmanagerHostState The state of this snackbar.
 * @param modifier The [Modifier] to be applied to the [SnackbarHost].
 * @param windowInsets The insets to be applied to this composable. By default this will account for
 * the insets that are on the sides and bottom of the screen (Display Cutout and Navigation bars).
 */
@Composable
fun UniversalSnackbarHost(
    universalpasswordmanagerHostState: UniversalSnackbarHostState,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .only(sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        .union(insets = WindowInsets.navigationBars),
) {
    SnackbarHost(
        hostState = universalpasswordmanagerHostState.snackbarHostState,
        modifier = modifier
            .windowInsetsPadding(insets = windowInsets)
            .consumeWindowInsets(insets = windowInsets),
    ) { snackbarData ->
        val message = snackbarData.visuals.message
        val currentCustomSnackbarData = universalpasswordmanagerHostState.currentSnackbarData
        if (currentCustomSnackbarData?.key == message) {
            UniversalSnackbar(
                universalpasswordmanagerSnackbarData = currentCustomSnackbarData,
                onDismiss = snackbarData::dismiss,
                onActionClick = snackbarData::performAction,
            )
        }
    }
}
