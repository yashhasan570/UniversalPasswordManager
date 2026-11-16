package com.x8bit.universalpasswordmanager.ui.tools.feature.send.model

import androidx.annotation.DrawableRes
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Represents the types of icons to be displayed with the send.
 */
enum class SendStatusIcon(
    @field:DrawableRes val iconRes: Int,
    val contentDescription: Text,
    val testTag: String,
) {
    DISABLED(
        iconRes = Universal Password ManagerDrawable.ic_send_disabled,
        contentDescription = Universal Password ManagerString.disabled.asText(),
        testTag = "DisabledSendIcon",
    ),
    PASSWORD(
        iconRes = Universal Password ManagerDrawable.ic_key,
        contentDescription = Universal Password ManagerString.password.asText(),
        testTag = "PasswordProtectedSendIcon",
    ),
    EXPIRED(
        iconRes = Universal Password ManagerDrawable.ic_send_expired,
        contentDescription = Universal Password ManagerString.expired.asText(),
        testTag = "ExpiredSendIcon",
    ),
    MAX_ACCESS_COUNT_REACHED(
        iconRes = Universal Password ManagerDrawable.ic_send_max_access_count_reached,
        contentDescription = Universal Password ManagerString.maximum_access_count_reached.asText(),
        testTag = "MaxAccessSendIcon",
    ),
    PENDING_DELETE(
        iconRes = Universal Password ManagerDrawable.ic_send_pending_delete,
        contentDescription = Universal Password ManagerString.pending_delete.asText(),
        testTag = "PendingDeletionSendIcon",
    ),
}
