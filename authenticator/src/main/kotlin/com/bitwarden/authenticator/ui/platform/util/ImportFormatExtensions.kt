package .authenticator.ui.platform.util

import .authenticator.data.platform.manager.imports.model.ImportFileFormat
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 *  Provides a human-readable label for the export format.
 */
val ImportFileFormat.displayLabel: Text
    get() = when (this) {
        ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON -> {
            Universal Password ManagerString.import_format_label_universalpasswordmanager_json.asText()
        }

        ImportFileFormat.TWO_FAS_JSON -> {
            Universal Password ManagerString.import_format_label_2fas_json.asText()
        }

        ImportFileFormat.LAST_PASS_JSON -> {
            Universal Password ManagerString.import_format_label_lastpass_json.asText()
        }

        ImportFileFormat.AEGIS -> {
            Universal Password ManagerString.import_format_label_aegis_json.asText()
        }
    }
