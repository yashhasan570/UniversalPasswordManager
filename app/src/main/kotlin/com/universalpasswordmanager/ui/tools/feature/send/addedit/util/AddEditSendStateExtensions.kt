package com.universalpasswordmanager.ui.tools.feature.send.addedit.util

import com.universalpasswordmanager.send.AuthType
import com.universalpasswordmanager.send.SendFileView
import com.universalpasswordmanager.send.SendTextView
import com.universalpasswordmanager.send.SendType
import com.universalpasswordmanager.send.SendView
import com.universalpasswordmanager.ui.platform.base.util.orNullIfBlank
import com.universalpasswordmanager.ui.tools.feature.send.addedit.AddEditSendState
import com.universalpasswordmanager.ui.tools.feature.send.addedit.model.SendAuth
import java.time.Clock

/**
 * Transforms [AddEditSendState] into [SendView].
 */
fun AddEditSendState.ViewState.Content.toSendView(
    clock: Clock,
): SendView =
    SendView(
        id = common.originalSendView?.id,
        accessId = common.originalSendView?.accessId,
        name = common.name,
        notes = common.noteInput.orNullIfBlank(),
        key = common.originalSendView?.key,
        newPassword = common
            .passwordInput
            .takeIf { common.sendAuth is SendAuth.Password }
            .orNullIfBlank(),
        hasPassword = false,
        type = selectedType.toSendType(),
        file = toSendFileView(),
        text = toSendTextView(),
        // Item sends are not supported yet, see [PM-41095].
        data = null,
        maxAccessCount = common.maxAccessCount?.toUInt(),
        accessCount = 0U,
        disabled = common.isDeactivateChecked,
        hideEmail = common.isHideEmailChecked,
        revisionDate = clock.instant(),
        deletionDate = common.deletionDate,
        expirationDate = common.expirationDate?.let {
            // We no longer support expiration dates but is a send has one already,
            // we just update it to match the deletion date.
            common.deletionDate
        },
        emails = (common.sendAuth as? SendAuth.Email)
            ?.emails
            ?.map { it.value }
            ?.filter { it.isNotBlank() }
            .orEmpty(),
        authType = when (common.sendAuth) {
            is SendAuth.Password -> AuthType.PASSWORD
            is SendAuth.Email -> AuthType.EMAIL
            is SendAuth.None -> AuthType.NONE
        },
    )

private fun AddEditSendState.ViewState.Content.SendType.toSendType(): SendType =
    when (this) {
        is AddEditSendState.ViewState.Content.SendType.File -> SendType.FILE
        is AddEditSendState.ViewState.Content.SendType.Text -> SendType.TEXT
    }

private fun AddEditSendState.ViewState.Content.toSendFileView(): SendFileView? =
    (this.selectedType as? AddEditSendState.ViewState.Content.SendType.File)?.let {
        SendFileView(
            id = null,
            fileName = it.name.orEmpty(),
            size = null,
            sizeName = null,
        )
    }

private fun AddEditSendState.ViewState.Content.toSendTextView(): SendTextView? =
    (this.selectedType as? AddEditSendState.ViewState.Content.SendType.Text)?.let {
        SendTextView(
            text = it.input,
            hidden = it.isHideByDefaultChecked,
        )
    }
