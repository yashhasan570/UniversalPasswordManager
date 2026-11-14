package com.universalpasswordmanager.ui.vault.feature.itemlisting.util

import com.universalpasswordmanager.send.SendType
import com.universalpasswordmanager.ui.platform.components.icon.model.IconData
import com.universalpasswordmanager.ui.platform.resource.UniversalDrawable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.vault.CipherType
import com.universalpasswordmanager.ui.vault.feature.itemlisting.VaultItemListingState
import com.universalpasswordmanager.ui.vault.feature.itemlisting.model.ListingItemOverflowAction
import kotlinx.collections.immutable.persistentListOf

/**
 * Create a mock [VaultItemListingState.DisplayItem] with a given [number].
 */
@Suppress("LongParameterList")
fun createMockDisplayItemForCipher(
    number: Int,
    cipherType: CipherType = CipherType.LOGIN,
    subtitle: String? = "mockUsername-$number",
    subtitleTestTag: String = "CipherSubTitleLabel",
    secondSubtitle: String? = null,
    secondSubtitleTestTag: String? = null,
    requiresPasswordReprompt: Boolean = true,
    iconData: IconData = IconData.Network(
        uri = "https://icons.bitwarden.net/www.mockuri.com/icon.png",
        fallbackIconRes = UniversalDrawable.ic_globe,
    ),
    isAutofill: Boolean = false,
    isCredentialCreation: Boolean = false,
    shouldShowMasterPasswordReprompt: Boolean = false,
): VaultItemListingState.DisplayItem =
    when (cipherType) {
        CipherType.LOGIN -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = iconData,
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.CopyUsernameClick(
                        username = "mockUsername-$number",
                    ),
                    ListingItemOverflowAction.VaultAction.CopyPasswordClick(
                        requiresPasswordReprompt = requiresPasswordReprompt,
                        cipherId = "mockId-$number",
                    ),
                    ListingItemOverflowAction.VaultAction.CopyTotpClick(
                        cipherId = "mockId-$number",
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.LaunchClick(
                        url = "www.mockuri$number.com",
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = isAutofill,
                isCredentialCreation = isCredentialCreation,
                shouldShowMasterPasswordReprompt = shouldShowMasterPasswordReprompt,
                iconTestTag = "LoginCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.SECURE_NOTE -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_note),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.CopyNoteClick(
                        cipherId = "mockId-$number",
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = "SecureNoteCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.CARD -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_payment_card),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.CopyNumberClick(
                        cipherId = "mockId-$number",
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.CopySecurityCodeClick(
                        cipherId = "mockId-$number",
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = "CardCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.IDENTITY -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_id_card),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = isAutofill,
                isCredentialCreation = isCredentialCreation,
                shouldShowMasterPasswordReprompt = shouldShowMasterPasswordReprompt,
                iconTestTag = "IdentityCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.SSH_KEY -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_ssh_key),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = "SshKeyCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.BANK_ACCOUNT -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_payment_card),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = "BankAccountCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.DRIVERS_LICENSE -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_note),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = "BankAccountCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }

        CipherType.PASSPORT -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "CipherNameLabel",
                secondSubtitle = secondSubtitle,
                secondSubtitleTestTag = secondSubtitleTestTag,
                subtitle = subtitle,
                subtitleTestTag = subtitleTestTag,
                iconData = IconData.Local(UniversalDrawable.ic_note),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_shared_folder,
                        contentDescription = UniversalString.shared_folders.asText(),
                        testTag = "CipherInCollectionIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_paperclip,
                        contentDescription = UniversalString.attachments.asText(),
                        testTag = "CipherWithAttachmentsIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.VaultAction.ViewClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.EditClick(
                        cipherId = "mockId-$number",
                        cipherType = cipherType,
                        requiresPasswordReprompt = requiresPasswordReprompt,
                    ),
                    ListingItemOverflowAction.VaultAction.ArchiveClick(cipherId = "mockId-$number"),
                ),
                optionsTestTag = "CipherOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = "BankAccountCipherIcon",
                itemType = VaultItemListingState.DisplayItem.ItemType.Vault(type = cipherType),
            )
        }
    }

/**
 * Create a mock [VaultItemListingState.DisplayItem] with a given [number].
 */
@Suppress("MaxLineLength")
fun createMockDisplayItemForSend(
    number: Int,
    sendType: SendType = SendType.FILE,
): VaultItemListingState.DisplayItem =
    when (sendType) {
        SendType.FILE -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "SendNameLabel",
                secondSubtitle = null,
                secondSubtitleTestTag = null,
                subtitle = "Oct 27, 2023, 12:00 PM",
                subtitleTestTag = "SendDateLabel",
                iconData = IconData.Local(UniversalDrawable.ic_file),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_key,
                        contentDescription = UniversalString.password.asText(),
                        testTag = "PasswordProtectedSendIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_send_max_access_count_reached,
                        contentDescription = UniversalString.maximum_access_count_reached.asText(),
                        testTag = "MaxAccessSendIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.SendAction.CopyUrlClick(
                        sendUrl = "https://send.bitwarden.com/#mockAccessId-$number/mockKey-$number",
                    ),
                    ListingItemOverflowAction.SendAction.ShareUrlClick(
                        sendUrl = "https://send.bitwarden.com/#mockAccessId-$number/mockKey-$number",
                    ),
                    ListingItemOverflowAction.SendAction.ViewClick(
                        sendId = "mockId-$number",
                        sendType = sendType,
                    ),
                    ListingItemOverflowAction.SendAction.EditClick(
                        sendId = "mockId-$number",
                        sendType = sendType,
                    ),
                    ListingItemOverflowAction.SendAction.RemovePasswordClick(sendId = "mockId-$number"),
                    ListingItemOverflowAction.SendAction.DeleteClick(sendId = "mockId-$number"),
                ),
                optionsTestTag = "SendOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = null,
                itemType = VaultItemListingState.DisplayItem.ItemType.Sends(type = sendType),
            )
        }

        SendType.TEXT -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "SendNameLabel",
                secondSubtitle = null,
                secondSubtitleTestTag = null,
                subtitle = "Oct 27, 2023, 12:00 PM",
                subtitleTestTag = "SendDateLabel",
                iconData = IconData.Local(UniversalDrawable.ic_file_text),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_key,
                        contentDescription = UniversalString.password.asText(),
                        testTag = "PasswordProtectedSendIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_send_max_access_count_reached,
                        contentDescription = UniversalString.maximum_access_count_reached.asText(),
                        testTag = "MaxAccessSendIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.SendAction.CopyUrlClick(
                        sendUrl = "https://send.bitwarden.com/#mockAccessId-$number/mockKey-$number",
                    ),
                    ListingItemOverflowAction.SendAction.ShareUrlClick(
                        sendUrl = "https://send.bitwarden.com/#mockAccessId-$number/mockKey-$number",
                    ),
                    ListingItemOverflowAction.SendAction.ViewClick(
                        sendId = "mockId-$number",
                        sendType = sendType,
                    ),
                    ListingItemOverflowAction.SendAction.EditClick(
                        sendId = "mockId-$number",
                        sendType = sendType,
                    ),
                    ListingItemOverflowAction.SendAction.RemovePasswordClick(sendId = "mockId-$number"),
                    ListingItemOverflowAction.SendAction.DeleteClick(sendId = "mockId-$number"),
                ),
                optionsTestTag = "SendOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = null,
                itemType = VaultItemListingState.DisplayItem.ItemType.Sends(type = sendType),
            )
        }

        SendType.ITEM -> {
            VaultItemListingState.DisplayItem(
                id = "mockId-$number",
                title = "mockName-$number".asText(),
                titleTestTag = "SendNameLabel",
                secondSubtitle = null,
                secondSubtitleTestTag = null,
                subtitle = "Oct 27, 2023, 12:00 PM",
                subtitleTestTag = "SendDateLabel",
                iconData = IconData.Local(UniversalDrawable.ic_file_text),
                extraIconList = persistentListOf(
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_key,
                        contentDescription = UniversalString.password.asText(),
                        testTag = "PasswordProtectedSendIcon",
                    ),
                    IconData.Local(
                        iconRes = UniversalDrawable.ic_send_max_access_count_reached,
                        contentDescription = UniversalString.maximum_access_count_reached.asText(),
                        testTag = "MaxAccessSendIcon",
                    ),
                ),
                overflowOptions = listOf(
                    ListingItemOverflowAction.SendAction.CopyUrlClick(
                        sendUrl = "https://send.bitwarden.com/#mockAccessId-$number/mockKey-$number",
                    ),
                    ListingItemOverflowAction.SendAction.ShareUrlClick(
                        sendUrl = "https://send.bitwarden.com/#mockAccessId-$number/mockKey-$number",
                    ),
                    ListingItemOverflowAction.SendAction.ViewClick(
                        sendId = "mockId-$number",
                        sendType = sendType,
                    ),
                    ListingItemOverflowAction.SendAction.EditClick(
                        sendId = "mockId-$number",
                        sendType = sendType,
                    ),
                    ListingItemOverflowAction.SendAction.RemovePasswordClick(sendId = "mockId-$number"),
                    ListingItemOverflowAction.SendAction.DeleteClick(sendId = "mockId-$number"),
                ),
                optionsTestTag = "SendOptionsButton",
                isAutofill = false,
                isCredentialCreation = false,
                shouldShowMasterPasswordReprompt = false,
                iconTestTag = null,
                itemType = VaultItemListingState.DisplayItem.ItemType.Sends(type = sendType),
            )
        }
    }

/**
 * Create a mock [VaultItemListingState.DisplayItem] for a decryption error.
 */
fun createMockDisplayItemForDecryptionError(
    number: Int,
): VaultItemListingState.DisplayItem = VaultItemListingState.DisplayItem(
    id = "mockId-$number",
    title = UniversalString.error_cannot_decrypt.asText(),
    titleTestTag = "CipherNameLabel",
    secondSubtitle = null,
    secondSubtitleTestTag = null,
    subtitle = null,
    subtitleTestTag = "",
    iconData = IconData.Local(iconRes = UniversalDrawable.ic_globe),
    iconTestTag = "LoginCipherIcon",
    extraIconList = persistentListOf(
        IconData.Local(
            iconRes = UniversalDrawable.ic_shared_folder,
            contentDescription = UniversalString.shared_folders.asText(),
            testTag = "CipherInCollectionIcon",
        ),
    ),
    overflowOptions = emptyList(),
    optionsTestTag = "CipherOptionsButton",
    isAutofill = false,
    isCredentialCreation = false,
    shouldShowMasterPasswordReprompt = false,
    itemType = VaultItemListingState.DisplayItem.ItemType.DecryptionError,
)
