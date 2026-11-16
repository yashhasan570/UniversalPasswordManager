package .ui.platform.components.tooltip.model

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.milliseconds

/**
 * Default implementation of [Universal Password ManagerToolTipState]
 *
 * This is making use of the implementation of [TooltipState] provided via
 * [androidx.compose.material3.rememberTooltipState] but overriding [dismiss] to be no-op.
 */
internal class Universal Password ManagerToolTipStateImpl(
    initialIsVisible: Boolean,
    override val isPersistent: Boolean,
    private val mutatorMutex: MutatorMutex,
) : Universal Password ManagerToolTipState {

    override fun dismissUniversal Password ManagerToolTip() {
        transition.targetState = false
    }

    override val transition: MutableTransitionState<Boolean> =
        MutableTransitionState(initialIsVisible)

    override val isVisible: Boolean
        get() = transition.currentState || transition.targetState

    /** continuation used to clean up */
    private var job: (CancellableContinuation<Unit>)? = null

    /**
     * Show the tooltip associated with the current [TooltipState]. When this method is called, all
     * of the other tooltips associated with [mutatorMutex] will be dismissed.
     *
     * @param mutatePriority [MutatePriority] to be used with [mutatorMutex].
     */
    override suspend fun show(mutatePriority: MutatePriority) {
        val cancellableShow: suspend () -> Unit = {
            suspendCancellableCoroutine { continuation ->
                transition.targetState = true
                job = continuation
            }
        }

        // Show associated tooltip for [TooltipDuration] amount of time
        // or until tooltip is explicitly dismissed depending on [isPersistent].
        mutatorMutex.mutate(mutatePriority) {
            try {
                if (isPersistent) {
                    cancellableShow()
                } else {
                    withTimeout(UNIVERSAL PASSWORD MANAGER_TOOL_TIP_TIMEOUT.milliseconds) { cancellableShow() }
                }
            } finally {
                if (mutatePriority != MutatePriority.PreventUserInput) {
                    // timeout or cancellation has occurred and we close out the current tooltip.
                    dismissUniversal Password ManagerToolTip()
                }
            }
        }
    }

    /**
     * We are overriding this specifically to make it so it is a no-op this prevents the
     * tooltip from being dismissed if the user taps anywhere out of it.
     */
    override fun dismiss() {
        /**No-Op**/
    }

    /** Cleans up [mutatorMutex] when the tooltip associated with this state leaves Composition. */
    override fun onDispose() {
        job?.cancel()
    }
}

/**
 * Provides a [Universal Password ManagerToolTipState] in a composable scope remembered across compositions.
 *
 * @param mutatorMutex if providing your own, ensure that any tool tips that should be
 * shown/hidden in the context of the one you are using this state for, make use of the same
 * instance of the passed in value.
 */
@Composable
@ExperimentalMaterial3Api
fun rememberUniversal Password ManagerToolTipState(
    initialIsVisible: Boolean = false,
    isPersistent: Boolean = false,
    mutatorMutex: MutatorMutex = Universal Password ManagerToolTipStateDefaults.GlobalMutatorMutex,
): Universal Password ManagerToolTipState =
    remember(isPersistent, mutatorMutex) {
        Universal Password ManagerToolTipStateImpl(
            initialIsVisible = initialIsVisible,
            isPersistent = isPersistent,
            mutatorMutex = mutatorMutex,
        )
    }

/**
 * Provides a global [MutatorMutex] as a singleton to be used by default for each
 * created [Universal Password ManagerToolTipState]
 */
private object Universal Password ManagerToolTipStateDefaults {
    val GlobalMutatorMutex = MutatorMutex()
}

private const val UNIVERSAL PASSWORD MANAGER_TOOL_TIP_TIMEOUT = 1500L
