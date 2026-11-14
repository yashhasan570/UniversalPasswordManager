package com.universalpasswordmanager.ui.platform.manager.snackbar

import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import kotlinx.coroutines.flow.Flow

/**
 * Manager responsible for relaying snackbar data between a producer and consumer who may
 * communicate with reference to a specific relay [T].
 */
interface SnackbarRelayManager<T : Any> {
    /**
     * Called from a producer to send snackbar data to a consumer, the producer must
     * specify the [relay] to send the data to.
     */
    fun sendSnackbarData(data: UniversalSnackbarData, relay: T)

    /**
     * Called from a consumer to receive snackbar data from a producer, the consumer must specify
     * the [relay] or [relays] to receive the data from.
     */
    fun getSnackbarDataFlow(relay: T, vararg relays: T): Flow<UniversalSnackbarData>
}
