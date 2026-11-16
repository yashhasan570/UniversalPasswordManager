@file:Suppress("TooManyFunctions")

package com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.util

import androidx.annotation.DrawableRes
import .collections.CollectionView
import .core.data.util.toFormattedDateTimeStyle
import .send.SendType
import .send.SendView
import .ui.platform.base.util.toHostOrPathOrNull
import .ui.platform.components.icon.model.IconData
import .ui.platform.model.TotpData
import .ui.platform.resource.Universal Password ManagerDrawable
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import .vault.CipherListView
import .vault.CipherListViewType
import .vault.CipherRepromptType
import .vault.CipherView
import .vault.FolderView
import com.x8bit.universalpasswordmanager.data.autofill.model.AutofillSelectionData
import com.x8bit.universalpasswordmanager.data.autofill.util.isActiveWithFido2Credentials
import com.x8bit.universalpasswordmanager.data.autofill.util.login
import com.x8bit.universalpasswordmanager.data.credentials.model.CreateCredentialRequest
import com.x8bit.universalpasswordmanager.data.platform.util.isActive
import com.x8bit.universalpasswordmanager.data.vault.repository.model.VaultData
import com.x8bit.universalpasswordmanager.data.vault.repository.util.toFailureCipherListView
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.util.toLabelIcons
import com.x8bit.universalpasswordmanager.ui.tools.feature.send.util.toOverflowActions
import com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.VaultItemListingState
import com.x8bit.universalpasswordmanager.ui.vault.feature.util.getCollections
import com.x8bit.universalpasswordmanager.ui.vault.feature.util.getFolders
import com.x8bit.universalpasswordmanager.ui.vault.feature.util.toCollectionDisplayName
import com.x8bit.universalpasswordmanager.ui.vault.feature.util.toFolderDisplayName
import com.x8bit.universalpasswordmanager.ui.vault.feature.util.toLabelIcons
import com.x8bit.universalpasswordmanager.ui.vault.feature.util.toOverflowActions
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.model.VaultFilterType
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.util.applyRestrictItemTypesPolicy
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.util.toFilteredList
import com.x8bit.universalpasswordmanager.ui.vault.feature.vault.util.toLoginIconData
import com.x8bit.universalpasswordmanager.ui.vault.util.toSdkCipherType
import java.time.Clock
import java.time.format.FormatStyle

/**
 * Determines a predicate to filter a list of [CipherView] based on the
 * [VaultItemListingState.ItemListingType].
 */
fun CipherListView.determineListingPredicate(
    itemListingType: VaultItemListingState.ItemListingType.Vault,
): Boolean =
    when (itemListingType) {
        is VaultItemListingState.ItemListingType.Vault.Card -> {
            type is CipherListViewType.Card && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.Collection -> {
            itemListingType.collectionId in this.collectionIds && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.Folder -> {
            folderId == itemListingType.folderId && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.Identity -> {
            type is CipherListViewType.Identity && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.Login -> {
            type is CipherListViewType.Login && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.SecureNote -> {
            type is CipherListViewType.SecureNote && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.SshKey -> {
            type is CipherListViewType.SshKey && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.BankAccount -> {
            type is CipherListViewType.BankAccount && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.License -> {
            type is CipherListViewType.DriversLicense && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.Passport -> {
            type is CipherListViewType.Passport && isActive
        }

        is VaultItemListingState.ItemListingType.Vault.Trash -> {
            deletedDate != null
        }

        is VaultItemListingState.ItemListingType.Vault.Archive -> {
            archivedDate != null && deletedDate == null
        }
    }

/**
 * Determines a predicate to filter a list of [CipherView] based on the
 * [VaultItemListingState.ItemListingType].
 */
fun SendView.determineListingPredicate(
    itemListingType: VaultItemListingState.ItemListingType.Send,
): Boolean =
    when (itemListingType) {
        is VaultItemListingState.ItemListingType.Send.SendFile -> {
            type == SendType.FILE
        }

        is VaultItemListingState.ItemListingType.Send.SendText -> {
            type == SendType.TEXT
        }
    }

/**
 * Transforms a list of [CipherListView] into [VaultItemListingState.ViewState].
 */
@Suppress("CyclomaticComplexMethod", "LongMethod", "LongParameterList", "NestedBlockDepth")
fun VaultData.toViewState(
    itemListingType: VaultItemListingState.ItemListingType.Vault,
    vaultFilterType: VaultFilterType,
    hasMasterPassword: Boolean,
    baseIconUrl: String,
    isIconLoadingDisabled: Boolean,
    autofillSelectionData: AutofillSelectionData?,
    createCredentialRequestData: CreateCredentialRequest?,
    totpData: TotpData?,
    isPremiumUser: Boolean,
    restrictItemTypesPolicyOrgIds: List<String>,
): VaultItemListingState.ViewState {
    val filteredCipherViewList = decryptCipherListResult
        .successes
        .applyFilters(
            itemListingType = itemListingType,
            vaultFilterType = vaultFilterType,
            restrictItemTypesPolicyOrgIds = restrictItemTypesPolicyOrgIds,
        )

    val failuresCipherViewList = decryptCipherListResult
        .failures
        .map { cipher ->
            cipher.toFailureCipherListView()
        }

    val filteredFailuresCipherViewList = failuresCipherViewList
        .applyFilters(
            itemListingType = itemListingType,
            vaultFilterType = vaultFilterType,
            restrictItemTypesPolicyOrgIds = restrictItemTypesPolicyOrgIds,
        )

    val allFilteredCipherViewList = filteredFailuresCipherViewList
        .plus(filteredCipherViewList)

    val folderList =
        (itemListingType as? VaultItemListingState.ItemListingType.Vault.Folder)
            ?.folderId
            ?.let { folderViewList.getFolders(it) }
            .orEmpty()

    val collectionList =
        (itemListingType as? VaultItemListingState.ItemListingType.Vault.Collection)
            ?.let { collectionViewList.getCollections(it.collectionId) }
            .orEmpty()

    // Nested collection counts cannot use the parent-scoped list, since ciphers in a nested
    // collection are not necessarily in the parent collection.
    val nestedCollectionCipherViewList = if (collectionList.isEmpty()) {
        emptyList()
    } else {
        decryptCipherListResult
            .successes
            .plus(failuresCipherViewList)
            .filter { it.isActive }
            .applyRestrictItemTypesPolicy(restrictItemTypesPolicyOrgIds)
            .toFilteredList(vaultFilterType)
    }

    return if (folderList.isNotEmpty() || allFilteredCipherViewList.isNotEmpty() ||
        collectionList.isNotEmpty()
    ) {
        VaultItemListingState.ViewState.Content(
            displayItemList = filteredFailuresCipherViewList
                .toDisplayItemListDecryptionError()
                .plus(
                    filteredCipherViewList.toDisplayItemList(
                        baseIconUrl = baseIconUrl,
                        hasMasterPassword = hasMasterPassword,
                        isIconLoadingDisabled = isIconLoadingDisabled,
                        isAutofill = autofillSelectionData != null,
                        isFido2Creation = createCredentialRequestData != null,
                        isPremiumUser = isPremiumUser,
                    ),
                ),
            displayFolderList = folderList.map { folderView ->
                VaultItemListingState.FolderDisplayItem(
                    id = requireNotNull(folderView.id),
                    name = folderView.name,
                    count = decryptCipherListResult
                        .successes
                        .count {
                            it.deletedDate == null &&
                                !it.id.isNullOrBlank() &&
                                folderView.id == it.folderId
                        },
                )
            },
            displayCollectionList = collectionList.map { collectionView ->
                VaultItemListingState.CollectionDisplayItem(
                    id = requireNotNull(collectionView.id),
                    name = collectionView.name,
                    count = nestedCollectionCipherViewList
                        .count { collectionView.id in it.collectionIds },
                )
            },
        )
    } else {
        // Use the autofill empty message if necessary, otherwise use normal type-specific message
        val message = autofillSelectionData
            ?.uri
            ?.toHostOrPathOrNull()
            ?.let { Universal Password ManagerString.no_items_for_uri.asText(it) }
            ?: createCredentialRequestData
                ?.relyingPartyIdOrNull
                ?.toHostOrPathOrNull()
                ?.let { Universal Password ManagerString.no_items_for_uri.asText(it) }
            ?: totpData?.let { Universal Password ManagerString.search_for_a_login_or_add_a_new_login.asText() }
            ?: run {
                when (itemListingType) {
                    is VaultItemListingState.ItemListingType.Vault.Folder -> {
                        Universal Password ManagerString.no_items_folder
                    }

                    is VaultItemListingState.ItemListingType.Vault.Collection -> {
                        Universal Password ManagerString.there_are_no_items_in_this_shared_folder
                    }

                    VaultItemListingState.ItemListingType.Vault.Trash -> {
                        Universal Password ManagerString.no_items_trash
                    }

                    VaultItemListingState.ItemListingType.Vault.Card -> {
                        Universal Password ManagerString.no_cards
                    }

                    VaultItemListingState.ItemListingType.Vault.Identity -> {
                        Universal Password ManagerString.no_identities
                    }

                    VaultItemListingState.ItemListingType.Vault.Login -> {
                        Universal Password ManagerString.no_logins
                    }

                    VaultItemListingState.ItemListingType.Vault.SecureNote -> {
                        Universal Password ManagerString.no_notes
                    }

                    VaultItemListingState.ItemListingType.Vault.SshKey -> {
                        Universal Password ManagerString.no_ssh_keys
                    }

                    VaultItemListingState.ItemListingType.Vault.BankAccount -> {
                        Universal Password ManagerString.no_bank_accounts
                    }

                    VaultItemListingState.ItemListingType.Vault.License -> {
                        Universal Password ManagerString.no_licenses
                    }

                    VaultItemListingState.ItemListingType.Vault.Passport -> {
                        Universal Password ManagerString.no_passports
                    }

                    VaultItemListingState.ItemListingType.Vault.Archive -> {
                        Universal Password ManagerString.no_archives_message
                    }
                }
                    .asText()
            }

        val restrictItemTypePolicyEnabled = restrictItemTypesPolicyOrgIds.isNotEmpty() &&
            itemListingType == VaultItemListingState.ItemListingType.Vault.Card

        val shouldShowAddButton = !restrictItemTypePolicyEnabled && itemListingType.hasFab

        VaultItemListingState.ViewState.NoItems(
            header = totpData
                ?.let {
                    Universal Password ManagerString.no_items_for_vault
                        .asText(it.issuer ?: it.accountName ?: "--")
                }
                ?: run {
                    when (itemListingType) {
                        is VaultItemListingState.ItemListingType.Vault.Folder,
                        is VaultItemListingState.ItemListingType.Vault.Collection,
                        VaultItemListingState.ItemListingType.Vault.Trash,
                        VaultItemListingState.ItemListingType.Vault.Card,
                        VaultItemListingState.ItemListingType.Vault.Identity,
                        VaultItemListingState.ItemListingType.Vault.Login,
                        VaultItemListingState.ItemListingType.Vault.SecureNote,
                        VaultItemListingState.ItemListingType.Vault.SshKey,
                        VaultItemListingState.ItemListingType.Vault.BankAccount,
                        VaultItemListingState.ItemListingType.Vault.License,
                        VaultItemListingState.ItemListingType.Vault.Passport,
                            -> null

                        VaultItemListingState.ItemListingType.Vault.Archive -> {
                            Universal Password ManagerString.no_archives_title.asText()
                        }
                    }
                },
            message = message,
            shouldShowAddButton = shouldShowAddButton,
            buttonText = createCredentialRequestData
                ?.let { Universal Password ManagerString.save_passkey_as_new_login.asText() }
                ?: run {
                    when (itemListingType) {
                        VaultItemListingState.ItemListingType.Vault.Card -> {
                            Universal Password ManagerString.add_card
                        }

                        VaultItemListingState.ItemListingType.Vault.Identity -> {
                            Universal Password ManagerString.add_identity
                        }

                        VaultItemListingState.ItemListingType.Vault.Login -> {
                            Universal Password ManagerString.add_login
                        }

                        VaultItemListingState.ItemListingType.Vault.SecureNote -> {
                            Universal Password ManagerString.add_note
                        }

                        VaultItemListingState.ItemListingType.Vault.SshKey -> {
                            Universal Password ManagerString.add_ssh_key
                        }

                        VaultItemListingState.ItemListingType.Vault.BankAccount -> {
                            Universal Password ManagerString.add_bank_account
                        }

                        VaultItemListingState.ItemListingType.Vault.License -> {
                            Universal Password ManagerString.add_license
                        }

                        VaultItemListingState.ItemListingType.Vault.Passport -> {
                            Universal Password ManagerString.add_passport
                        }

                        else -> Universal Password ManagerString.add_item
                    }
                        .asText()
                },
            vectorRes = totpData
                ?.let { Universal Password ManagerDrawable.ill_folder_question }
                ?: run {
                    when (itemListingType) {
                        is VaultItemListingState.ItemListingType.Vault.Folder,
                        is VaultItemListingState.ItemListingType.Vault.Collection,
                        VaultItemListingState.ItemListingType.Vault.Trash,
                        VaultItemListingState.ItemListingType.Vault.Card,
                        VaultItemListingState.ItemListingType.Vault.Identity,
                        VaultItemListingState.ItemListingType.Vault.Login,
                        VaultItemListingState.ItemListingType.Vault.SecureNote,
                        VaultItemListingState.ItemListingType.Vault.SshKey,
                        VaultItemListingState.ItemListingType.Vault.BankAccount,
                        VaultItemListingState.ItemListingType.Vault.License,
                        VaultItemListingState.ItemListingType.Vault.Passport,
                            -> null

                        VaultItemListingState.ItemListingType.Vault.Archive -> {
                            Universal Password ManagerDrawable.ill_open_source
                        }
                    }
                },
        )
    }
}

/**
 * Transforms a list of [CipherView] into [VaultItemListingState.ViewState].
 */
fun List<SendView>.toViewState(
    itemListingType: VaultItemListingState.ItemListingType.Send,
    baseWebSendUrl: String,
    clock: Clock,
): VaultItemListingState.ViewState =
    if (isNotEmpty()) {
        VaultItemListingState.ViewState.Content(
            displayItemList = toDisplayItemList(
                baseWebSendUrl = baseWebSendUrl,
                clock = clock,
            ),
            displayFolderList = emptyList(),
            displayCollectionList = emptyList(),
        )
    } else {
        VaultItemListingState.ViewState.NoItems(
            message = when (itemListingType) {
                VaultItemListingState.ItemListingType.Send.SendFile -> Universal Password ManagerString.no_file_sends
                VaultItemListingState.ItemListingType.Send.SendText -> Universal Password ManagerString.no_text_sends
            }
                .asText(),
            shouldShowAddButton = true,
            buttonText = when (itemListingType) {
                VaultItemListingState.ItemListingType.Send.SendFile -> Universal Password ManagerString.new_file_send
                VaultItemListingState.ItemListingType.Send.SendText -> Universal Password ManagerString.new_text_send
            }
                .asText(),
        )
    }

/**
 * Updates a [VaultItemListingState.ItemListingType] with the given data if necessary.
 */
fun VaultItemListingState.ItemListingType.updateWithAdditionalDataIfNecessary(
    folderList: List<FolderView>,
    collectionList: List<CollectionView>,
): VaultItemListingState.ItemListingType =
    when (this) {
        is VaultItemListingState.ItemListingType.Vault.Card -> this
        is VaultItemListingState.ItemListingType.Vault.Collection -> copy(
            collectionName = collectionList
                .find { it.id == collectionId }
                ?.name
                ?.toCollectionDisplayName(collectionList)
                .orEmpty(),
        )

        is VaultItemListingState.ItemListingType.Vault.Folder -> {
            val fullyQualifiedName = folderList
                .find { it.id == folderId }
                ?.name
            copy(
                folderName = fullyQualifiedName
                    ?.toFolderDisplayName()
                    .orEmpty(),
                fullyQualifiedName = fullyQualifiedName.orEmpty(),
            )
        }

        is VaultItemListingState.ItemListingType.Vault.Identity -> this
        is VaultItemListingState.ItemListingType.Vault.Login -> this
        is VaultItemListingState.ItemListingType.Vault.SecureNote -> this
        is VaultItemListingState.ItemListingType.Vault.Trash -> this
        is VaultItemListingState.ItemListingType.Send.SendFile -> this
        is VaultItemListingState.ItemListingType.Send.SendText -> this
        is VaultItemListingState.ItemListingType.Vault.SshKey -> this
        is VaultItemListingState.ItemListingType.Vault.BankAccount -> this
        is VaultItemListingState.ItemListingType.Vault.License -> this
        is VaultItemListingState.ItemListingType.Vault.Passport -> this
        is VaultItemListingState.ItemListingType.Vault.Archive -> this
    }

@Suppress("LongParameterList")
private fun List<CipherListView>.toDisplayItemList(
    baseIconUrl: String,
    hasMasterPassword: Boolean,
    isIconLoadingDisabled: Boolean,
    isAutofill: Boolean,
    isFido2Creation: Boolean,
    isPremiumUser: Boolean,
): List<VaultItemListingState.DisplayItem> =
    this.map {
        it.toDisplayItem(
            baseIconUrl = baseIconUrl,
            hasMasterPassword = hasMasterPassword,
            isIconLoadingDisabled = isIconLoadingDisabled,
            isAutofill = isAutofill,
            isFido2Creation = isFido2Creation,
            isPremiumUser = isPremiumUser,
        )
    }

private fun List<SendView>.toDisplayItemList(
    baseWebSendUrl: String,
    clock: Clock,
): List<VaultItemListingState.DisplayItem> =
    this.map {
        it.toDisplayItem(
            baseWebSendUrl = baseWebSendUrl,
            clock = clock,
        )
    }

@Suppress("LongParameterList")
private fun CipherListView.toDisplayItem(
    baseIconUrl: String,
    hasMasterPassword: Boolean,
    isIconLoadingDisabled: Boolean,
    isAutofill: Boolean,
    isFido2Creation: Boolean,
    isPremiumUser: Boolean,
): VaultItemListingState.DisplayItem =
    VaultItemListingState.DisplayItem(
        id = id.orEmpty(),
        title = name.asText(),
        titleTestTag = "CipherNameLabel",
        secondSubtitle = this.toSecondSubtitle(
            fido2CredentialRpId = login?.fido2Credentials?.firstOrNull()?.rpId,
        ),
        secondSubtitleTestTag = "PasskeySite",
        subtitle = this.subtitle,
        subtitleTestTag = this.toSubtitleTestTag(
            isAutofill = isAutofill,
            isFido2Creation = isFido2Creation,
        ),
        iconData = this.toIconData(
            baseIconUrl = baseIconUrl,
            isIconLoadingDisabled = isIconLoadingDisabled,
            usePasskeyDefaultIcon = (isAutofill || isFido2Creation) &&
                this.isActiveWithFido2Credentials,
        ),
        iconTestTag = this.toIconTestTag(),
        extraIconList = this.toLabelIcons(),
        overflowOptions = this.toOverflowActions(
            hasMasterPassword = hasMasterPassword,
            isPremiumUser = isPremiumUser,
        ),
        optionsTestTag = "CipherOptionsButton",
        isAutofill = isAutofill,
        isCredentialCreation = isFido2Creation,
        shouldShowMasterPasswordReprompt = (reprompt == CipherRepromptType.PASSWORD) &&
            hasMasterPassword,
        itemType = VaultItemListingState
            .DisplayItem
            .ItemType
            .Vault(
                type = this.type.toSdkCipherType(),
            ),
    )

@Suppress("MaxLineLength")
private fun List<CipherListView>.toDisplayItemListDecryptionError(): List<VaultItemListingState.DisplayItem> =
    this.map { it.toDisplayItemDecryptionError() }

private fun CipherListView.toDisplayItemDecryptionError(): VaultItemListingState.DisplayItem =
    VaultItemListingState.DisplayItem(
        id = id.orEmpty(),
        title = Universal Password ManagerString.error_cannot_decrypt.asText(),
        titleTestTag = "CipherNameLabel",
        secondSubtitle = null,
        secondSubtitleTestTag = null,
        subtitle = null,
        subtitleTestTag = "",
        iconData = IconData.Local(iconRes = Universal Password ManagerDrawable.ic_globe),
        iconTestTag = this.toIconTestTag(),
        extraIconList = this.toLabelIcons(),
        overflowOptions = emptyList(),
        optionsTestTag = "CipherOptionsButton",
        isAutofill = false,
        isCredentialCreation = false,
        shouldShowMasterPasswordReprompt = false,
        itemType = VaultItemListingState.DisplayItem.ItemType.DecryptionError,
    )

private fun CipherListView.toSecondSubtitle(fido2CredentialRpId: String?): String? =
    fido2CredentialRpId
        ?.takeIf { this.type is CipherListViewType.Login && it.isNotEmpty() && it != this.name }

private fun CipherListView.toSubtitleTestTag(
    isAutofill: Boolean,
    isFido2Creation: Boolean,
): String =
    if ((isAutofill || isFido2Creation)) {
        if (this.isActiveWithFido2Credentials) "PasskeyName" else "PasswordName"
    } else {
        "CipherSubTitleLabel"
    }

private fun CipherListView.toIconTestTag(): String =
    when (type) {
        is CipherListViewType.Login -> "LoginCipherIcon"
        CipherListViewType.SecureNote -> "SecureNoteCipherIcon"
        is CipherListViewType.Card -> "CardCipherIcon"
        CipherListViewType.Identity -> "IdentityCipherIcon"
        CipherListViewType.SshKey -> "SshKeyCipherIcon"
        is CipherListViewType.BankAccount -> "BankAccountCipherIcon"
        CipherListViewType.DriversLicense -> "LicenseCipherIcon"
        CipherListViewType.Passport -> "PassportCipherIcon"
    }

private fun CipherListView.toIconData(
    baseIconUrl: String,
    isIconLoadingDisabled: Boolean,
    usePasskeyDefaultIcon: Boolean,
): IconData {
    return when (this.type) {
        is CipherListViewType.Login -> {
            login?.uris.toLoginIconData(
                baseIconUrl = baseIconUrl,
                isIconLoadingDisabled = isIconLoadingDisabled,
                usePasskeyDefaultIcon = usePasskeyDefaultIcon,
            )
        }

        else -> {
            IconData.Local(iconRes = this.type.iconRes)
        }
    }
}

private fun SendView.toDisplayItem(
    baseWebSendUrl: String,
    clock: Clock,
): VaultItemListingState.DisplayItem =
    VaultItemListingState.DisplayItem(
        id = id.orEmpty(),
        title = name.asText(),
        titleTestTag = "SendNameLabel",
        secondSubtitle = null,
        secondSubtitleTestTag = null,
        subtitle = deletionDate.toFormattedDateTimeStyle(
            dateStyle = FormatStyle.MEDIUM,
            timeStyle = FormatStyle.SHORT,
            clock = clock,
        ),
        subtitleTestTag = "SendDateLabel",
        iconData = IconData.Local(
            iconRes = when (type) {
                SendType.TEXT -> Universal Password ManagerDrawable.ic_file_text
                SendType.FILE -> Universal Password ManagerDrawable.ic_file
                SendType.ITEM -> TODO("[PM-41095] Support Item SendType")
            },
        ),
        iconTestTag = null,
        extraIconList = toLabelIcons(clock = clock),
        overflowOptions = toOverflowActions(baseWebSendUrl = baseWebSendUrl),
        optionsTestTag = "SendOptionsButton",
        isAutofill = false,
        shouldShowMasterPasswordReprompt = false,
        isCredentialCreation = false,
        itemType = VaultItemListingState.DisplayItem.ItemType.Sends(type = this.type),
    )

@get:DrawableRes
private val CipherListViewType.iconRes: Int
    get() = when (this) {
        is CipherListViewType.Login -> Universal Password ManagerDrawable.ic_globe
        CipherListViewType.SecureNote -> Universal Password ManagerDrawable.ic_note
        is CipherListViewType.Card -> Universal Password ManagerDrawable.ic_payment_card
        CipherListViewType.Identity -> Universal Password ManagerDrawable.ic_id_card
        CipherListViewType.SshKey -> Universal Password ManagerDrawable.ic_ssh_key
        is CipherListViewType.BankAccount -> Universal Password ManagerDrawable.ic_payment_card
        CipherListViewType.DriversLicense -> Universal Password ManagerDrawable.ic_note
        CipherListViewType.Passport -> Universal Password ManagerDrawable.ic_passport
    }

private fun List<CipherListView>.applyFilters(
    itemListingType: VaultItemListingState.ItemListingType.Vault,
    vaultFilterType: VaultFilterType,
    restrictItemTypesPolicyOrgIds: List<String>,
): List<CipherListView> = this
    .filter { it.determineListingPredicate(itemListingType) }
    .applyRestrictItemTypesPolicy(restrictItemTypesPolicyOrgIds)
    .toFilteredList(vaultFilterType)
