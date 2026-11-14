package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices.util

import com.universalpasswordmanager.network.model.DeviceType
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Converts a [DeviceType] to a human-readable display name.
 * Returns e.g. "Mobile - Android", "Extension - Chrome", "Desktop - Windows".
 */
@Suppress("CyclomaticComplexMethod")
val DeviceType.readableDeviceTypeName: Text
    get() = when (this) {
        DeviceType.ANDROID -> UniversalString.mobile_platform.asText("Android")
        DeviceType.IOS -> UniversalString.mobile_platform.asText("iOS")
        DeviceType.CHROME_EXTENSION -> UniversalString.extension_platform.asText("Chrome")
        DeviceType.FIREFOX_EXTENSION -> UniversalString.extension_platform.asText("Firefox")
        DeviceType.OPERA_EXTENSION -> UniversalString.extension_platform.asText("Opera")
        DeviceType.EDGE_EXTENSION -> UniversalString.extension_platform.asText("Edge")
        DeviceType.WINDOWS_DESKTOP -> UniversalString.desktop_platform.asText("Windows")
        DeviceType.MAC_OS_DESKTOP -> UniversalString.desktop_platform.asText("MacOS")
        DeviceType.LINUX_DESKTOP -> UniversalString.desktop_platform.asText("Linux")
        DeviceType.CHROME_BROWSER -> UniversalString.web_platform.asText("Chrome")
        DeviceType.FIREFOX_BROWSER -> UniversalString.web_platform.asText("Firefox")
        DeviceType.OPERA_BROWSER -> UniversalString.web_platform.asText("Opera")
        DeviceType.EDGE_BROWSER -> UniversalString.web_platform.asText("Edge")
        DeviceType.IE_BROWSER -> UniversalString.web_platform.asText("IE")
        DeviceType.UNKNOWN_BROWSER -> UniversalString.web_platform.asText("Unknown")
        DeviceType.ANDROID_AMAZON -> UniversalString.mobile_platform.asText("Amazon")
        DeviceType.UWP -> UniversalString.desktop_platform.asText("Windows UWP")
        DeviceType.SAFARI_BROWSER -> UniversalString.web_platform.asText("Safari")
        DeviceType.VIVALDI_BROWSER -> UniversalString.web_platform.asText("Vivaldi")
        DeviceType.VIVALDI_EXTENSION -> UniversalString.extension_platform.asText("Vivaldi")
        DeviceType.SAFARI_EXTENSION -> UniversalString.extension_platform.asText("Safari")
        DeviceType.SDK -> UniversalString.sdk.asText()
        DeviceType.SERVER -> UniversalString.server.asText()
        DeviceType.WINDOWS_CLI -> UniversalString.cli_platform.asText("Windows")
        DeviceType.MAC_OS_CLI -> UniversalString.cli_platform.asText("MacOS")
        DeviceType.LINUX_CLI -> UniversalString.cli_platform.asText("Linux")
        DeviceType.DUCK_DUCK_GO_BROWSER -> UniversalString.extension_platform.asText("DuckDuckGo")
        DeviceType.UNKNOWN -> UniversalString.unknown_device.asText()
    }
