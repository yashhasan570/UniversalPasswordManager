package com.universalpasswordmanager.data.datasource.disk.model

import com.universalpasswordmanager.network.model.ConfigResponseJson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A higher-level wrapper around [ConfigResponseJson] that provides a timestamp
 * to check if a sync is necessary
 *
 * @property lastSync The [Long] of the last sync.
 * @property serverData The raw [ConfigResponseJson] that contains specific data of the
 * server configuration
 */
@Serializable
data class ServerConfig(
    @SerialName("lastSync")
    val lastSync: Long,

    @SerialName("serverData")
    val serverData: ConfigResponseJson,
) {
    /**
     * Whether the server is an official Universal server or not.
     */
    val isOfficialUniversalServer: Boolean
        get() = serverData.server == null
}
