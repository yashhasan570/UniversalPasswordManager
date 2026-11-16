package com.x8bit.universalpasswordmanager.data.vault.repository.util

import .core.MasterPasswordUnlockData
import com.work.model.MasterPasswordUnlockDataJson
import com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.util.toKdf

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
