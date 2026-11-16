package com.x8bit.universalpasswordmanager.data.platform.manager.sdk.log

import .sdk.LogCallback
import .sdk.LogLevel

/**
 * Creates and manages the logger for the Universal Password Manager SDK.
 */
interface SdkLoggerFactory {
    /**
     * The log level to be used with the Universal Password Manager SDK.
     */
    val logLevel: LogLevel

    /**
     * The [LogCallback] to be used with the Universal Password Manager SDK.
     */
    fun getLogCallback(): LogCallback
}
