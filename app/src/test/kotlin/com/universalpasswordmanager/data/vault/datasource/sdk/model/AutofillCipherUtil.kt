package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.data.autofill.model.AutofillCipher

/**
 * Create a mock [AutofillCipher.Login].
 */
fun createMockPasswordCredentialAutofillCipherLogin() = AutofillCipher.Login(
    cipherId = "mockCipherId",
    name = "Cipher One",
    isTotpEnabled = false,
    password = "mock-password",
    username = "mock-username",
    subtitle = "Subtitle",
    website = "website",
)
