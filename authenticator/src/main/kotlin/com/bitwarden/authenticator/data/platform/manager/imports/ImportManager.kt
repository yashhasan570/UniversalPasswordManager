package .authenticator.data.platform.manager.imports

import .authenticator.data.platform.manager.imports.model.ImportDataResult
import .authenticator.data.platform.manager.imports.model.ImportFileFormat

/**
 * Responsible for managing import of files from various authenticator exports.
 */
interface ImportManager {

    /**
     * Imports the selected file.
     */
    suspend fun import(
        importFileFormat: ImportFileFormat,
        byteArray: ByteArray,
    ): ImportDataResult
}
