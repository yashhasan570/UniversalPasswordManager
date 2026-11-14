package com.universalpasswordmanager.data.platform.manager.sdk.log

import com.universalpasswordmanager.sdk.LogCallback
import com.universalpasswordmanager.sdk.LogLevel

/**
 * Creates and manages the logger for the Universal SDK.
 */
interface SdkLoggerFactory {
    /**
     * The log level to be used with the Universal SDK.
     */
    val logLevel: LogLevel

    /**
     * The [LogCallback] to be used with the Universal SDK.
     */
    fun getLogCallback(): LogCallback
}
