package com.x8bit.universalpasswordmanager.ui.platform.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.platform.feature.settings.exportvault.model.ExportVaultFormat

/**
 *  Provides a human-readable label for the export format.
 */
val ExportVaultFormat.displayLabel: Text
    get() = when (this) {
        ExportVaultFormat.JSON -> Universal Password ManagerString.json_extension.asText()
        ExportVaultFormat.CSV -> Universal Password ManagerString.csv_extension.asText()
        ExportVaultFormat.JSON_ENCRYPTED -> Universal Password ManagerString.json_extension_formatted.asText(
            Universal Password ManagerString.password_protected.asText(),
        )
    }

/**
 * Provides the file extension associated with the export format.
 */
val ExportVaultFormat.fileExtension: String
    get() = when (this) {
        ExportVaultFormat.JSON -> "json"
        ExportVaultFormat.CSV -> "csv"
        ExportVaultFormat.JSON_ENCRYPTED -> "json"
    }
