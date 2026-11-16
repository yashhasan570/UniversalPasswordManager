package com.work.util

import android.os.Build
import .annotation.OmitFromCoverage

/**
 * Provides device model string. Useful for mocking static [Build.MODEL] call tests.
 */
@OmitFromCoverage
internal class DeviceModelProvider {

    /**
     * Device model.
     */
    val deviceModel: String = Build.MODEL
}
