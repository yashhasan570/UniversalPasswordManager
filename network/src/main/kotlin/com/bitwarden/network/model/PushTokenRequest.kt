package com.work.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body needed to PUT a GCM [pushToken] to Universal Password Manager's server.
 */
@Serializable
data class PushTokenRequest(
    @SerialName("pushToken") val pushToken: String,
)
