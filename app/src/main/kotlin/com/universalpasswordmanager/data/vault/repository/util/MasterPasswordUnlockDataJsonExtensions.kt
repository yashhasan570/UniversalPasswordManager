package com.universalpasswordmanager.data.vault.repository.util

import com.universalpasswordmanager.core.MasterPasswordUnlockData
import com.universalpasswordmanager.network.model.MasterPasswordUnlockDataJson
import com.universalpasswordmanager.data.auth.datasource.sdk.util.toKdf

/**
 * Converts [MasterPasswordUnlockDataJson] to [MasterPasswordUnlockData]
 */
fun MasterPasswordUnlockDataJson.toSdkMasterPasswordUnlock(): MasterPasswordUnlockData =
    MasterPasswordUnlockData(
        kdf = kdf.toKdf(),
        masterKeyWrappedUserKey = masterKeyWrappedUserKey,
        salt = salt,
        containedKeyId = containedKeyId,
    )
