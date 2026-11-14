package com.universalpasswordmanager.ui.tools.feature.send.util

import com.universalpasswordmanager.core.data.util.toFormattedDateTimeStyle
import com.universalpasswordmanager.send.SendType
import com.universalpasswordmanager.send.SendView
import com.universalpasswordmanager.data.vault.repository.model.SendData
import com.universalpasswordmanager.ui.tools.feature.send.SendState
import java.time.Clock
import java.time.format.FormatStyle

/**
 * Transforms [SendData] into [SendState.ViewState].
 */
fun SendData.toViewState(
    baseWebSendUrl: String,
    clock: Clock = Clock.systemDefaultZone(),
): SendState.ViewState =
    this
        .sendViewList
        .takeUnless { it.isEmpty() }
        ?.toSendContent(baseWebSendUrl, clock)
        ?: SendState.ViewState.Empty

private fun List<SendView>.toSendContent(
    baseWebSendUrl: String,
    clock: Clock,
): SendState.ViewState.Content {
    return SendState.ViewState.Content(
        textTypeCount = this.count { it.type == SendType.TEXT },
        fileTypeCount = this.count { it.type == SendType.FILE },
        sendItems = this
            .map { sendView ->
                SendState.ViewState.Content.SendItem(
                    id = requireNotNull(sendView.id),
                    name = sendView.name,
                    deletionDate = sendView.deletionDate.toFormattedDateTimeStyle(
                        dateStyle = FormatStyle.MEDIUM,
                        timeStyle = FormatStyle.SHORT,
                        clock = clock,
                    ),
                    type = when (sendView.type) {
                        SendType.TEXT -> SendState.ViewState.Content.SendItem.Type.TEXT
                        SendType.FILE -> SendState.ViewState.Content.SendItem.Type.FILE
                        SendType.ITEM -> TODO("[PM-41095] Support Item SendType")
                    },
                    iconList = sendView.toLabelIcons(),
                    shareUrl = sendView.toSendUrl(baseWebSendUrl),
                    hasPassword = sendView.hasPassword,
                    overflowItems = sendView.toOverflowActions(baseWebSendUrl),
                )
            },
    )
}
