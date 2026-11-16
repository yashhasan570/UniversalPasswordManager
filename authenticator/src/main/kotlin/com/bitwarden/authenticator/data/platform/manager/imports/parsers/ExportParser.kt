package .authenticator.data.platform.manager.imports.parsers

import .authenticator.data.authenticator.datasource.disk.entity.AuthenticatorItemEntity
import .authenticator.data.platform.manager.imports.model.ExportParseResult
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import java.io.IOException

/**
 * Responsible for transforming exported authenticator data to a format consumable by this
 * application.
 */
abstract class ExportParser {
    /**
     * Converts the given [byteArray] content of a file to a collection of
     * [AuthenticatorItemEntity].
     */
    protected abstract fun parse(byteArray: ByteArray): ExportParseResult

    /**
     * Parses the given byte array into an [ExportParseResult].
     *
     * This method attempts to deserialize the input data and return a successful result.
     * If deserialization fails due to various exceptions, an appropriate error result
     * is returned instead.
     *
     * Exceptions handled include:
     * - [MissingFieldException]: If required fields are missing in the input data.
     * - [SerializationException]: If the input data cannot be processed due to invalid format.
     * - [IllegalArgumentException]: If an argument provided to a method is invalid.
     * - [IOException]: If an I/O error occurs during processing.
     *
     * @param byteArray The input data to be parsed as a [ByteArray].
     * @return [ExportParseResult] indicating success or a specific error result.
     */
    fun parseForResult(byteArray: ByteArray): ExportParseResult = try {
        parse(byteArray = byteArray)
    } catch (@OptIn(ExperimentalSerializationApi::class) _: MissingFieldException) {
        ExportParseResult.Error(
            title = Universal Password ManagerString.required_information_missing.asText(),
            message = Universal Password ManagerString.required_information_missing_message.asText(),
        )
    } catch (_: SerializationException) {
        ExportParseResult.Error(
            title = Universal Password ManagerString.file_could_not_be_processed.asText(),
            message = Universal Password ManagerString.file_could_not_be_processed_message.asText(),
        )
    } catch (error: IllegalArgumentException) {
        ExportParseResult.Error(message = error.message?.asText())
    } catch (error: IOException) {
        ExportParseResult.Error(message = error.message?.asText())
    }
}
