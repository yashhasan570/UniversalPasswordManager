package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.fido.AuthenticatorAttestationResponse
import com.universalpasswordmanager.fido.ClientExtensionResults
import com.universalpasswordmanager.fido.CredPropsResult
import com.universalpasswordmanager.fido.PublicKeyCredentialAuthenticatorAttestationResponse
import com.universalpasswordmanager.fido.SelectedCredential

/**
 * Creates a mock [PublicKeyCredentialAuthenticatorAttestationResponse] for testing.
 */
fun createMockPublicKeyAttestationResponse(number: Int) =
    PublicKeyCredentialAuthenticatorAttestationResponse(
        id = "mockId",
        rawId = "0987654321".toByteArray(),
        ty = "mockTy",
        authenticatorAttachment = "mockAuthenticatorAttachment",
        clientExtensionResults = ClientExtensionResults(
            credProps = CredPropsResult(
                rk = true,
            ),
        ),
        response = AuthenticatorAttestationResponse(
            clientDataJson = "mockClientDataJson".toByteArray(),
            authenticatorData = "mockAuthenticatorData".toByteArray(),
            publicKey = "mockPublicKey".toByteArray(),
            publicKeyAlgorithm = 0L,
            attestationObject = "mockAttestationObject".toByteArray(),
            transports = emptyList(),
        ),
        selectedCredential = SelectedCredential(
            createMockCipherView(number),
            createMockFido2CredentialView(number),
        ),
    )
