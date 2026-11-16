package .authenticator.data.platform.manager.imports

import .authenticator.data.authenticator.datasource.disk.AuthenticatorDiskSource
import .authenticator.data.platform.manager.imports.model.ExportParseResult
import .authenticator.data.platform.manager.imports.model.ImportDataResult
import .authenticator.data.platform.manager.imports.model.ImportFileFormat
import .authenticator.data.platform.manager.imports.parsers.AegisExportParser
import .authenticator.data.platform.manager.imports.parsers.Universal Password ManagerExportParser
import .authenticator.data.platform.manager.imports.parsers.ExportParser
import .authenticator.data.platform.manager.imports.parsers.LastPassExportParser
import .authenticator.data.platform.manager.imports.parsers.TwoFasExportParser
import .core.data.manager.UuidManager

/**
 * Default implementation of [ImportManager] for managing importing files exported by various
 * authenticator applications.
 */
class ImportManagerImpl(
    private val authenticatorDiskSource: AuthenticatorDiskSource,
    private val uuidManager: UuidManager,
) : ImportManager {
    override suspend fun import(
        importFileFormat: ImportFileFormat,
        byteArray: ByteArray,
    ): ImportDataResult {
        val parser = createParser(importFileFormat)
        return processParseResult(parser.parseForResult(byteArray))
    }

    private fun createParser(
        importFileFormat: ImportFileFormat,
    ): ExportParser = when (importFileFormat) {
        ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON -> Universal Password ManagerExportParser(importFileFormat)
        ImportFileFormat.TWO_FAS_JSON -> TwoFasExportParser(uuidManager)
        ImportFileFormat.LAST_PASS_JSON -> LastPassExportParser(uuidManager)
        ImportFileFormat.AEGIS -> AegisExportParser(uuidManager)
    }

    private suspend fun processParseResult(
        parseResult: ExportParseResult,
    ): ImportDataResult = when (parseResult) {
        is ExportParseResult.Error -> {
            ImportDataResult.Error(
                title = parseResult.title,
                message = parseResult.message,
            )
        }

        is ExportParseResult.Success -> {
            val items = parseResult.items.toTypedArray()
            authenticatorDiskSource.saveItem(*items)
            ImportDataResult.Success
        }
    }
}
