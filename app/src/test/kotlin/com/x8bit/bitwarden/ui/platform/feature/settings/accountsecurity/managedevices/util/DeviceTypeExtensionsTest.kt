package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices.util

import com.work.model.DeviceType
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class DeviceTypeExtensionsTest {
    @TestFactory
    fun `readableDeviceTypeName returns correct value for each known device type`() =
        listOf(
            DeviceType.ANDROID to Universal Password ManagerString.mobile_platform.asText("Android"),
            DeviceType.IOS to Universal Password ManagerString.mobile_platform.asText("iOS"),
            DeviceType.CHROME_EXTENSION to Universal Password ManagerString.extension_platform.asText("Chrome"),
            DeviceType.FIREFOX_EXTENSION to Universal Password ManagerString.extension_platform.asText("Firefox"),
            DeviceType.OPERA_EXTENSION to Universal Password ManagerString.extension_platform.asText("Opera"),
            DeviceType.EDGE_EXTENSION to Universal Password ManagerString.extension_platform.asText("Edge"),
            DeviceType.WINDOWS_DESKTOP to Universal Password ManagerString.desktop_platform.asText("Windows"),
            DeviceType.MAC_OS_DESKTOP to Universal Password ManagerString.desktop_platform.asText("MacOS"),
            DeviceType.LINUX_DESKTOP to Universal Password ManagerString.desktop_platform.asText("Linux"),
            DeviceType.CHROME_BROWSER to Universal Password ManagerString.web_platform.asText("Chrome"),
            DeviceType.FIREFOX_BROWSER to Universal Password ManagerString.web_platform.asText("Firefox"),
            DeviceType.OPERA_BROWSER to Universal Password ManagerString.web_platform.asText("Opera"),
            DeviceType.EDGE_BROWSER to Universal Password ManagerString.web_platform.asText("Edge"),
            DeviceType.IE_BROWSER to Universal Password ManagerString.web_platform.asText("IE"),
            DeviceType.UNKNOWN_BROWSER to Universal Password ManagerString.web_platform.asText("Unknown"),
            DeviceType.ANDROID_AMAZON to Universal Password ManagerString.mobile_platform.asText("Amazon"),
            DeviceType.UWP to Universal Password ManagerString.desktop_platform.asText("Windows UWP"),
            DeviceType.SAFARI_BROWSER to Universal Password ManagerString.web_platform.asText("Safari"),
            DeviceType.VIVALDI_BROWSER to Universal Password ManagerString.web_platform.asText("Vivaldi"),
            DeviceType.VIVALDI_EXTENSION to Universal Password ManagerString.extension_platform.asText("Vivaldi"),
            DeviceType.SAFARI_EXTENSION to Universal Password ManagerString.extension_platform.asText("Safari"),
            DeviceType.SDK to Universal Password ManagerString.sdk.asText(),
            DeviceType.SERVER to Universal Password ManagerString.server.asText(),
            DeviceType.WINDOWS_CLI to Universal Password ManagerString.cli_platform.asText("Windows"),
            DeviceType.MAC_OS_CLI to Universal Password ManagerString.cli_platform.asText("MacOS"),
            DeviceType.LINUX_CLI to Universal Password ManagerString.cli_platform.asText("Linux"),
            DeviceType.DUCK_DUCK_GO_BROWSER to
                Universal Password ManagerString.extension_platform.asText("DuckDuckGo"),
            DeviceType.UNKNOWN to Universal Password ManagerString.unknown_device.asText(),
        ).map { (type, expected) ->
            dynamicTest("$type should return $expected") {
                assertEquals(expected, type.readableDeviceTypeName)
            }
        }
}
