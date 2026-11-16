package com.x8bit.universalpasswordmanager.ui.vault.model

import androidx.annotation.DrawableRes
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Represents the icons displayed after the cipher name.
 */
enum class VaultTrailingIcon(
    @field:DrawableRes val iconRes: Int,
    val contentDescription: Text,
    val testTag: String,
) {
    COLLECTION(
        iconRes = Universal Password ManagerDrawable.ic_shared_folder,
        contentDescription = Universal Password ManagerString.shared_folders.asText(),
        testTag = "CipherInCollectionIcon",
    ),
    ATTACHMENT(
        iconRes = Universal Password ManagerDrawable.ic_paperclip,
        contentDescription = Universal Password ManagerString.attachments.asText(),
        testTag = "CipherWithAttachmentsIcon",
    ),
}
