package com.universalpasswordmanager.ui.vault.model

import androidx.annotation.DrawableRes
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Represents the icons displayed after the cipher name.
 */
enum class VaultTrailingIcon(
    @field:DrawableRes val iconRes: Int,
    val contentDescription: Text,
    val testTag: String,
) {
    COLLECTION(
        iconRes = UniversalDrawable.ic_shared_folder,
        contentDescription = UniversalString.shared_folders.asText(),
        testTag = "CipherInCollectionIcon",
    ),
    ATTACHMENT(
        iconRes = UniversalDrawable.ic_paperclip,
        contentDescription = UniversalString.attachments.asText(),
        testTag = "CipherWithAttachmentsIcon",
    ),
}
