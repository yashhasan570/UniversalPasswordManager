package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.platform.feature.settings.exportvault.model.ExportVaultFormat

/**
 *  Provides a human-readable label for the export format.
 */
val ExportVaultFormat.displayLabel: Text
    get() = when (this) {
        ExportVaultFormat.JSON -> UniversalString.json_extension.asText()
        ExportVaultFormat.CSV -> UniversalString.csv_extension.asText()
        ExportVaultFormat.JSON_ENCRYPTED -> UniversalString.json_extension_formatted.asText(
            UniversalString.password_protected.asText(),
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
