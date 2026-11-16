package com.x8bit.universalpasswordmanager.data.vault.repository.model

import .collections.CollectionView
import .send.SendView
import .vault.DecryptCipherListResult
import .vault.FolderView

/**
 * Represents decrypted vault data.
 *
 * @param decryptCipherListResult Contains the result of decrypting ciphers for display in a list.
 * @param collectionViewList List of decrypted collections.
 * @param folderViewList List of decrypted folders.
 * @param sendViewList List of decrypted sends.
 */
data class VaultData(
    val decryptCipherListResult: DecryptCipherListResult,
    val collectionViewList: List<CollectionView>,
    val folderViewList: List<FolderView>,
    val sendViewList: List<SendView>,
)
