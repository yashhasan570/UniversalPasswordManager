package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.platform.feature.settings.exportvault.model.ExportVaultFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExportVaultFormatExtensionTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            ExportVaultFormat.JSON to UniversalString.json_extension.asText(),
            ExportVaultFormat.CSV to UniversalString.csv_extension.asText(),
            ExportVaultFormat.JSON_ENCRYPTED to UniversalString.json_extension_formatted.asText(
                UniversalString.password_protected.asText(),
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
