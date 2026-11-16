package .ui.platform.components.snackbar.model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import .ui.util.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * A custom state holder for [Universal Password ManagerSnackbarData] and manging a snackbar host with the
 * passed in [SnackbarHostState].
 */
@Stable
data class Universal Password ManagerSnackbarHostState(
    val snackbarHostState: SnackbarHostState,
    val scope: CoroutineScope,
) {
    /**
     * The current snackbar data to be displayed.
     */
    var currentSnackbarData: Universal Password ManagerSnackbarData? by mutableStateOf(null)
        private set

    /**
     * Shows a snackbar with the given [snackbarData]. Passes the [Universal Password ManagerSnackbarData.key]
     * through the message parameter of the [SnackbarHostState.showSnackbar] method. This key
     * can be used to identify the correct snackbar data to show in the host.
     */
    fun showSnackbar(
        snackbarData: Universal Password ManagerSnackbarData,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onActionPerformed: () -> Unit = { },
        onDismiss: () -> Unit = { },
    ) {
        scope.launch {
            currentSnackbarData = snackbarData
            snackbarHostState
                .showSnackbar(message = snackbarData.key, duration = duration)
                .also {
                    currentSnackbarData = null
                    when (it) {
                        SnackbarResult.Dismissed -> onDismiss()
                        SnackbarResult.ActionPerformed -> onActionPerformed()
                    }
                }
        }
    }
}

/**
 * Models possible data to show in a custom universalpasswordmanager snackbar.
 * @property message The text to show in the snackbar.
 * @property messageHeader The optional title text to show.
 * @property actionLabel The optional text to show in the action button.
 * @property withDismissAction Whether to show the dismiss action.
 * @property key The unique key for the [Universal Password ManagerSnackbarData].
 */
@Immutable
@Parcelize
data class Universal Password ManagerSnackbarData(
    val message: Text,
    val messageHeader: Text? = null,
    val actionLabel: Text? = null,
    val withDismissAction: Boolean = false,
) : Parcelable {
    val key: String get() = this.hashCode().toString()
}

/**
 * Creates a [Universal Password ManagerSnackbarHostState] that is remembered across compositions.
 */
@Composable
fun rememberUniversal Password ManagerSnackbarHostState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
): Universal Password ManagerSnackbarHostState = remember {
    Universal Password ManagerSnackbarHostState(snackbarHostState = snackbarHostState, scope = scope)
}
