package com.universalpasswordmanager.data.vault.repository.util

import com.universalpasswordmanager.exporters.Account
import com.universalpasswordmanager.data.auth.datasource.disk.model.AccountJson

/**
 * Converts a [AccountJson] to a [Account] for use in the SDK.
 */
fun AccountJson.toSdkAccount(): Account = Account(
    id = profile.userId,
    email = profile.email,
    name = profile.name,
)
