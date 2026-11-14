package com.universalpasswordmanager.data.vault.datasource.sdk.model

import com.universalpasswordmanager.exporters.Account

/**
 * Creates a mock [com.universalpasswordmanager.exporters.Account] for testing purposes
 */
fun createMockAccount(
    number: Int,
    email: String = "mockEmail-$number",
    name: String? = "mockName-$number",
): Account = Account(
    id = "mockId-$number",
    email = email,
    name = name,
)
