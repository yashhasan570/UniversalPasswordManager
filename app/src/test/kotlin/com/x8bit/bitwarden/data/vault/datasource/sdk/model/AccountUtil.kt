package com.x8bit.universalpasswordmanager.data.vault.datasource.sdk.model

import .exporters.Account

/**
 * Creates a mock [.exporters.Account] for testing purposes
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
