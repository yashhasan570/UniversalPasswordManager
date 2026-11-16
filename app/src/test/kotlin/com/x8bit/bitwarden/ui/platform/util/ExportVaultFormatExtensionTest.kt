package com.x8bit.universalpasswordmanager.ui.platform.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.exportvault.model.ExportVaultFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExportVaultFormatExtensionTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            ExportVaultFormat.JSON to Universal Password ManagerString.json_extension.asText(),
            ExportVaultFormat.CSV to Universal Password ManagerString.csv_extension.asText(),
            ExportVaultFormat.JSON_ENCRYPTED to Universal Password ManagerString.json_extension_formatted.asText(
                Universal Password ManagerString.password_protected.asText(),
            ),
        )
            .forEach { (type, label) ->
                assertEquals(
                    label,
                    type.displayLabel,
                )
            }
    }

    @Test
    fun `fileExtension should return the correct value for each type`() {
        mapOf(
            ExportVaultFormat.JSON to "json",
            ExportVaultFormat.CSV to "csv",
            ExportVaultFormat.JSON_ENCRYPTED to "json",
        )
            .forEach { (type, label) ->
                assertEquals(
                    label,
                    type.fileExtension,
                )
            }
    }
}
