@file:Suppress("TooManyFunctions")

package com.universalpasswordmanager.data.vault.repository.util

import com.universalpasswordmanager.core.data.repository.util.SpecialCharWithPrecedenceComparator
import com.universalpasswordmanager.network.model.SendAuthTypeJson
import com.universalpasswordmanager.network.model.SendJsonRequest
import com.universalpasswordmanager.network.model.SendTypeJson
import com.universalpasswordmanager.network.model.SyncResponseJson
import com.universalpasswordmanager.send.AuthType
import com.universalpasswordmanager.send.Send
import com.universalpasswordmanager.send.SendFile
import com.universalpasswordmanager.send.SendText
import com.universalpasswordmanager.send.SendType
import com.universalpasswordmanager.send.SendView

/**
 * Converts a Universal SDK [Send] object to a corresponding [SyncResponseJson.Send] object.
 */
fun Send.toEncryptedNetworkSend(fileLength: Long? = null): SendJsonRequest =
    SendJsonRequest(
        type = type.toNetworkSendType(),
        name = name,
        notes = notes,
        key = key,
        maxAccessCount = maxAccessCount?.toInt(),
        expirationDate = expirationDate,
        deletionDate = deletionDate,
        fileLength = fileLength,
        file = file?.toNetworkSendFile(),
        text = text?.toNetworkSendText(),
        password = password,
        isDisabled = disabled,
        shouldHideEmail = hideEmail,
        authType = authType.toNetworkSendAuthType(),
        emails = emails,
    )

/**
 * Converts a Universal SDK [SendFile] object to a corresponding [SyncResponseJson.Send.File]
 * object.
 */
private fun SendFile.toNetworkSendFile(): SyncResponseJson.Send.File =
    SyncResponseJson.Send.File(
        fileName = fileName,
        size = size?.toInt(),
        sizeName = sizeName,
        id = id,
    )

/**
 * Converts a Universal SDK [SendText] object to a corresponding [SyncResponseJson.Send.Text]
 * object.
 */
private fun SendText.toNetworkSendText(): SyncResponseJson.Send.Text =
    SyncResponseJson.Send.Text(
        isHidden = hidden,
        text = text,
    )

/**
 * Converts a Universal SDK [SendType] object to a corresponding [SendTypeJson] object.
 */
private fun SendType.toNetworkSendType(): SendTypeJson =
    when (this) {
        SendType.TEXT -> SendTypeJson.TEXT
        SendType.FILE -> SendTypeJson.FILE
        SendType.ITEM -> TODO("[PM-41095] Support Item SendType")
    }

/**
 * Converts a list of [SyncResponseJson.Send] objects to a list of corresponding
 * Universal SDK [Send] objects.
 */
fun List<SyncResponseJson.Send>.toEncryptedSdkSendList(): List<Send> =
    map { it.toEncryptedSdkSend() }

/**
 * Converts a [SyncResponseJson.Send] object to a corresponding
 * Universal SDK [Send] object.
 */
fun SyncResponseJson.Send.toEncryptedSdkSend(): Send =
    Send(
        id = id,
        accessId = accessId.toString(),
        name = name.toString(),
        notes = notes,
        key = key.toString(),
        password = password,
        type = type.toSdkSendType(),
        file = file?.toEncryptedSdkFile(),
        text = text?.toEncryptedSdkText(),
        // Item sends are not supported yet, see [PM-41095].
        data = null,
        maxAccessCount = maxAccessCount?.toUInt(),
        accessCount = accessCount.toUInt(),
        disabled = isDisabled,
        hideEmail = shouldHideEmail,
        revisionDate = revisionDate,
        deletionDate = deletionDate,
        expirationDate = expirationDate,
        emails = emails,
        authType = authType?.toSdkAuthType() ?: AuthType.NONE,
    )

/**
 * Converts a Universal SDK [AuthType] object to a corresponding [SendAuthTypeJson] object.
 */
private fun AuthType.toNetworkSendAuthType(): SendAuthTypeJson =
    when (this) {
        AuthType.EMAIL -> SendAuthTypeJson.EMAIL
        AuthType.PASSWORD -> SendAuthTypeJson.PASSWORD
        AuthType.NONE -> SendAuthTypeJson.NONE
    }

/**
 * Converts a [SendAuthTypeJson] objects to a corresponding
 * Universal SDK [AuthType].
 */
private fun SendAuthTypeJson.toSdkAuthType(): AuthType =
    when (this) {
        SendAuthTypeJson.PASSWORD -> AuthType.PASSWORD
        SendAuthTypeJson.EMAIL -> AuthType.EMAIL
        SendAuthTypeJson.NONE -> AuthType.NONE
    }

/**
 * Converts a [SyncResponseJson.Send.Text] object to a corresponding
 * Universal SDK [SendText] object.
 */
private fun SyncResponseJson.Send.Text.toEncryptedSdkText(): SendText =
    SendText(
        text = text,
        hidden = isHidden,
    )

/**
 * Converts a [SyncResponseJson.Send.File] objects to a corresponding
 * Universal SDK [SendFile] object.
 */
private fun SyncResponseJson.Send.File.toEncryptedSdkFile(): SendFile =
    SendFile(
        id = id.toString(),
        fileName = fileName.toString(),
        size = size.toString(),
        sizeName = sizeName.toString(),
    )

/**
 * Converts a [SendTypeJson] objects to a corresponding
 * Universal SDK [SendType].
 */
private fun SendTypeJson.toSdkSendType(): SendType =
    when (this) {
        SendTypeJson.TEXT -> SendType.TEXT
        SendTypeJson.FILE -> SendType.FILE
    }

/**
 * Sorts the data in alphabetical order by name.
 */
@JvmName("toAlphabeticallySortedSendList")
fun List<SendView>.sortAlphabetically(): List<SendView> {
    return this.sortedWith(
        comparator = { send1, send2 ->
            SpecialCharWithPrecedenceComparator.compare(send1.name, send2.name)
        },
    )
}
