package .cxf.parser

import .cxf.model.CredentialExchangePayload

/**
 * Parser for Credential Exchange Payload JSON strings.
 */
interface CredentialExchangePayloadParser {

    /**
     * Parses a Credential Exchange Payload JSON string into a [CredentialExchangePayload].
     */
    fun parse(payload: String): CredentialExchangePayload
}
