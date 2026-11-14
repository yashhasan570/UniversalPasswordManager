package com.universalpasswordmanager.ui.vault.feature.attachments.util

import com.universalpasswordmanager.vault.CipherView
import com.universalpasswordmanager.ui.vault.feature.attachments.AttachmentsState
import kotlinx.collections.immutable.toImmutableList

/**
 * Converts the [CipherView] into a [AttachmentsState.ViewState.Content].
 */
fun CipherView.toViewState(): AttachmentsState.ViewState.Content =
    AttachmentsState.ViewState.Content(
        originalCipher = this,
        attachments = this
            .attachments
            .orEmpty()
            .mapNotNull {
                val id = it.id ?: return@mapNotNull null
                AttachmentsState.AttachmentItem(
                    id = id,
                    title = it.fileName.orEmpty(),
                    displaySize = it.sizeName.orEmpty(),
                    isLargeFile = it.isLargeFile(),
                )
            }
            .toImmutableList(),
        newAttachment = null,
    )
