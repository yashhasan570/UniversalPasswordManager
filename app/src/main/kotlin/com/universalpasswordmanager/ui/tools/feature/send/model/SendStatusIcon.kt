package com.universalpasswordmanager.ui.tools.feature.send.model

import androidx.annotation.DrawableRes
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * Represents the types of icons to be displayed with the send.
 */
enum class SendStatusIcon(
    @field:DrawableRes val iconRes: Int,
    val contentDescription: Text,
    val testTag: String,
) {
    DISABLED(
        iconRes = UniversalDrawable.ic_send_disabled,
        contentDescription = UniversalString.disabled.asText(),
        testTag = "DisabledSendIcon",
    ),
    PASSWORD(
        iconRes = UniversalDrawable.ic_key,
        contentDescription = UniversalString.password.asText(),
        testTag = "PasswordProtectedSendIcon",
    ),
    EXPIRED(
        iconRes = UniversalDrawable.ic_send_expired,
        contentDescription = UniversalString.expired.asText(),
        testTag = "ExpiredSendIcon",
    ),
    MAX_ACCESS_COUNT_REACHED(
        iconRes = UniversalDrawable.ic_send_max_access_count_reached,
        contentDescription = UniversalString.maximum_access_count_reached.asText(),
        testTag = "MaxAccessSendIcon",
    ),
    PENDING_DELETE(
        iconRes = UniversalDrawable.ic_send_pending_delete,
        contentDescription = UniversalString.pending_delete.asText(),
        testTag = "PendingDeletionSendIcon",
    ),
}
