package com.x8bit.universalpasswordmanager.ui.tools.feature.send.util

import com.work.model.SendTypeJson
import .send.SendType
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.model.SendItemType

/**
 * Converts the [SendType] to its corresponding [SendItemType].
 */
fun SendType.toSendItemType(): SendItemType =
    when (this) {
        SendType.FILE -> SendItemType.FILE
        SendType.TEXT -> SendItemType.TEXT
        SendType.ITEM -> TODO("[PM-41095] Support Item SendType")
    }

/**
 * Converts the [SendTypeJson] to its corresponding [SendItemType].
 */
fun SendTypeJson.toSendItemType(): SendItemType =
    when (this) {
        SendTypeJson.FILE -> SendItemType.FILE
        SendTypeJson.TEXT -> SendItemType.TEXT
    }
