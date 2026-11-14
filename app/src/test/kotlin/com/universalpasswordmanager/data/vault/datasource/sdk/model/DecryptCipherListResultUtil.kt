package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.vault.Cipher
import com.universalpasswordmanager.vault.CipherListView
import com.universalpasswordmanager.vault.DecryptCipherListResult

/**
 * Creates a mock [DecryptCipherListResult] for testing purposes.
 */
fun createMockDecryptCipherListResult(
    number: Int,
    successes: List<CipherListView> = listOf(createMockCipherListView(number)),
    failures: List<Cipher> = emptyList(),
): DecryptCipherListResult = DecryptCipherListResult(
    successes = successes,
    failures = failures,
)
