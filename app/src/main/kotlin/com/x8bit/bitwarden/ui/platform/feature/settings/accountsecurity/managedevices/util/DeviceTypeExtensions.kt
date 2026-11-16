package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices.util

import com.work.model.DeviceType
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Converts a [DeviceType] to a human-readable display name.
 * Returns e.g. "Mobile - Android", "Extension - Chrome", "Desktop - Windows".
 */
@Suppress("CyclomaticComplexMethod")
val DeviceType.readableDeviceTypeName: Text
    get() = when (this) {
        DeviceType.ANDROID -> Universal Password ManagerString.mobile_platform.asText("Android")
        DeviceType.IOS -> Universal Password ManagerString.mobile_platform.asText("iOS")
        DeviceType.CHROME_EXTENSION -> Universal Password ManagerString.extension_platform.asText("Chrome")
        DeviceType.FIREFOX_EXTENSION -> Universal Password ManagerString.extension_platform.asText("Firefox")
        DeviceType.OPERA_EXTENSION -> Universal Password ManagerString.extension_platform.asText("Opera")
        DeviceType.EDGE_EXTENSION -> Universal Password ManagerString.extension_platform.asText("Edge")
        DeviceType.WINDOWS_DESKTOP -> Universal Password ManagerString.desktop_platform.asText("Windows")
        DeviceType.MAC_OS_DESKTOP -> Universal Password ManagerString.desktop_platform.asText("MacOS")
        DeviceType.LINUX_DESKTOP -> Universal Password ManagerString.desktop_platform.asText("Linux")
        DeviceType.CHROME_BROWSER -> Universal Password ManagerString.web_platform.asText("Chrome")
        DeviceType.FIREFOX_BROWSER -> Universal Password ManagerString.web_platform.asText("Firefox")
        DeviceType.OPERA_BROWSER -> Universal Password ManagerString.web_platform.asText("Opera")
        DeviceType.EDGE_BROWSER -> Universal Password ManagerString.web_platform.asText("Edge")
        DeviceType.IE_BROWSER -> Universal Password ManagerString.web_platform.asText("IE")
        DeviceType.UNKNOWN_BROWSER -> Universal Password ManagerString.web_platform.asText("Unknown")
        DeviceType.ANDROID_AMAZON -> Universal Password ManagerString.mobile_platform.asText("Amazon")
        DeviceType.UWP -> Universal Password ManagerString.desktop_platform.asText("Windows UWP")
        DeviceType.SAFARI_BROWSER -> Universal Password ManagerString.web_platform.asText("Safari")
        DeviceType.VIVALDI_BROWSER -> Universal Password ManagerString.web_platform.asText("Vivaldi")
        DeviceType.VIVALDI_EXTENSION -> Universal Password ManagerString.extension_platform.asText("Vivaldi")
        DeviceType.SAFARI_EXTENSION -> Universal Password ManagerString.extension_platform.asText("Safari")
        DeviceType.SDK -> Universal Password ManagerString.sdk.asText()
        DeviceType.SERVER -> Universal Password ManagerString.server.asText()
        DeviceType.WINDOWS_CLI -> Universal Password ManagerString.cli_platform.asText("Windows")
        DeviceType.MAC_OS_CLI -> Universal Password ManagerString.cli_platform.asText("MacOS")
        DeviceType.LINUX_CLI -> Universal Password ManagerString.cli_platform.asText("Linux")
        DeviceType.DUCK_DUCK_GO_BROWSER -> Universal Password ManagerString.extension_platform.asText("DuckDuckGo")
        DeviceType.UNKNOWN -> Universal Password ManagerString.unknown_device.asText()
    }
