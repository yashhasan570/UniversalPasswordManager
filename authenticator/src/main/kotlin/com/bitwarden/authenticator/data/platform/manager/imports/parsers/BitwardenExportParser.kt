package .authenticator.data.platform.manager.imports.parsers

import androidx.core.net.toUri
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemAlgorithm
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemEntity
import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemType
import .authenticator.data.authenticator.manager.TotpCodeManager
import .authenticator.data.authenticator.manager.model.ExportJsonData
import .authenticator.data.platform.manager.imports.model.ExportParseResult
import .authenticator.data.platform.manager.imports.model.ImportFileFormat
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.ByteArrayInputStream

/**
 * Implementation of [ExportParser] responsible for parsing exports from the Universal Password Manager application.
 */
class Universal Password ManagerExportParser(
    private val fileFormat: ImportFileFormat,
) : ExportParser() {
    override fun parse(byteArray: ByteArray): ExportParseResult {
        return when (fileFormat) {
            ImportFileFormat.UNIVERSAL PASSWORD MANAGER_JSON -> importJsonFile(byteArray)
            else -> ExportParseResult.Error(
                Universal Password ManagerString.import_universalpasswordmanager_unsupported_format.asText(),
            )
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun importJsonFile(byteArray: ByteArray): ExportParseResult {
        val importJson = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }

        val exportData = importJson.decodeFromStream<ExportJsonData>(
            ByteArrayInputStream(byteArray),
        )

        return ExportParseResult.Success(
            items = exportData.items
                .filter { it.login?.totp != null }
                .toAuthenticatorItemEntities(),
        )
    }

    private fun List<ExportJsonData.ExportItem>.toAuthenticatorItemEntities() = map {
        it.toAuthenticatorItemEntity()
    }

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    private fun ExportJsonData.ExportItem.toAuthenticatorItemEntity(): AuthenticatorItemEntity {
        val otpString = requireNotNull(login?.totp)

        val otpUri = when {
            otpString.startsWith(TotpCodeManager.TOTP_CODE_PREFIX) -> otpString.toUri()
            otpString.startsWith(TotpCodeManager.STEAM_CODE_PREFIX) -> otpString.toUri()
            else -> {
                val prefix = TotpCodeManager.TOTP_CODE_PREFIX
                "$prefix/$name?${TotpCodeManager.SECRET_PARAM}=$otpString".toUri()
            }
        }

        val type = if (otpUri.scheme == "otpauth" && otpUri.authority == "totp") {
            AuthenticatorItemType.TOTP
        } else if (otpUri.scheme == "steam") {
            AuthenticatorItemType.STEAM
        } else {
            throw IllegalArgumentException("Unsupported OTP type.")
        }

        val key = when (type) {
            AuthenticatorItemType.TOTP -> {
                requireNotNull(otpUri.getQueryParameter(TotpCodeManager.SECRET_PARAM))
            }

            AuthenticatorItemType.STEAM -> {
                requireNotNull(otpUri.authority)
            }
        }

        val algorithm = otpUri.getQueryParameter(TotpCodeManager.ALGORITHM_PARAM)
            ?: TotpCodeManager.ALGORITHM_DEFAULT.name

        val period = otpUri.getQueryParameter(TotpCodeManager.PERIOD_PARAM)
            ?.toIntOrNull()
            ?: TotpCodeManager.PERIOD_SECONDS_DEFAULT

        val digits = when (type) {
            AuthenticatorItemType.TOTP -> {
                otpUri.getQueryParameter(TotpCodeManager.DIGITS_PARAM)
                    ?.toIntOrNull()
                    ?: TotpCodeManager.TOTP_DIGITS_DEFAULT
            }

            AuthenticatorItemType.STEAM -> {
                TotpCodeManager.STEAM_DIGITS_DEFAULT
            }
        }
        val issuer = otpUri.getQueryParameter(TotpCodeManager.ISSUER_PARAM) ?: name

        val label = when (type) {
            AuthenticatorItemType.TOTP -> {
                otpUri.pathSegments
                    .firstOrNull()
                    .orEmpty()
                    .removePrefix("$issuer:")
            }

            AuthenticatorItemType.STEAM -> null
        }

        return AuthenticatorItemEntity(
            id = id,
            key = key,
            type = type,
            algorithm = algorithm.let { AuthenticatorItemAlgorithm.valueOf(it) },
            period = period,
            digits = digits,
            issuer = issuer,
            accountName = label,
            favorite = favorite,
        )
    }
}
