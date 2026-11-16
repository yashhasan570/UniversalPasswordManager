package com.x8bit.universalpasswordmanager.data.platform.manager.model

/**
 * Required data for sync cipher delete operations.
 */
data class SyncCipherDeleteData(
    val userId: String,
    val cipherId: String,
)
