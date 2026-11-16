package com.x8bit.universalpasswordmanager.data.auth.repository.model

import .core.WrappedAccountCryptographicState

/**
 * Create a mock [WrappedAccountCryptographicState] with a given number.
 */
fun createMockWrappedAccountCryptographicState(
    number: Int,
    signedPublicKey: String = "mockSignedPublicKey-$number",
    privateKey: String = "mockWrappedPrivateKey-$number",
    signingKey: String = "mockWrappedSigningKey-$number",
    securityState: String = "mockSecurityState-$number",
): WrappedAccountCryptographicState =
    WrappedAccountCryptographicState.V2(
        signedPublicKey = signedPublicKey,
        privateKey = privateKey,
        signingKey = signingKey,
        securityState = securityState,
    )
