package com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.model

import .fido.AuthenticatorAttestationResponse
import .fido.ClientExtensionResults
import .fido.CredPropsResult
import .fido.PublicKeyCredentialAuthenticatorAttestationResponse
import .fido.SelectedCredential

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
