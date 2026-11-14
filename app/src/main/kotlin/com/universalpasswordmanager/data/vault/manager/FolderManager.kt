package com.universalpasswordmanager.data.vault.manager

import com.universalpasswordmanager.vault.FolderView
import com.universalpasswordmanager.data.vault.repository.model.CreateFolderResult
import com.universalpasswordmanager.data.vault.repository.model.DeleteFolderResult
import com.universalpasswordmanager.data.vault.repository.model.UpdateFolderResult

/**
 * Manages the creating, updating, and deleting folders.
 */
interface FolderManager {
    /**
     * Attempt to create a folder.
     */
    suspend fun createFolder(folderView: FolderView): CreateFolderResult

    /**
     * Attempt to delete a folder.
     */
    suspend fun deleteFolder(folderId: String): DeleteFolderResult

    /**
     * Attempt to update a folder.
     */
    suspend fun updateFolder(folderId: String, folderView: FolderView): UpdateFolderResult
}
