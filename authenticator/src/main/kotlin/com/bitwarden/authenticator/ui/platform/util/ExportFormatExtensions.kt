package .authenticator.ui.platform.util

import .authenticator.ui.platform.feature.settings.export.model.ExportVaultFormat
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 *  Provides a human-readable label for the export format.
 */
val ExportVaultFormat.displayLabel: Text
    get() = when (this) {
        ExportVaultFormat.JSON -> Universal Password ManagerString.json_extension.asText()
        ExportVaultFormat.CSV -> Universal Password ManagerString.csv_extension.asText()
    }

/**
 * Provides the file extension associated with the export format.
 */
val ExportVaultFormat.fileExtension: String
    get() = when (this) {
        ExportVaultFormat.JSON -> "json"
        ExportVaultFormat.CSV -> "csv"
    }
