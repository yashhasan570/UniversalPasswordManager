@file:OmitFromCoverage

package com.x8bit.universalpasswordmanager.ui.vault.feature.itemlisting.model

import android.os.Parcelable
import .annotation.OmitFromCoverage
import .send.SendType
import .ui.platform.components.dialog.model.Universal Password ManagerTwoButtonDialogData
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import .vault.CipherType
import kotlinx.parcelize.Parcelize

/**
 * Represents the actions for an individual item's overflow menu.
 */
sealed class ListingItemOverflowAction : Parcelable {

    /**
     * The display title of the option.
     */
    abstract val title: Text

    /**
     * The content description of the option.
     */
    abstract val contentDescription: Text

    /**
     * The data to be displayed for an optional speed bump dialog.
     */
    abstract val speedBump: Universal Password ManagerTwoButtonDialogData?

    /**
     * Represents the send actions.
     */
    sealed class SendAction : ListingItemOverflowAction() {
        /**
         * Click on the view send overflow option.
         */
        @Parcelize
        data class ViewClick(
            val sendId: String,
            val sendType: SendType,
        ) : SendAction() {
            override val title: Text get() = Universal Password ManagerString.view.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the edit send overflow option.
         */
        @Parcelize
        data class EditClick(
            val sendId: String,
            val sendType: SendType,
        ) : SendAction() {
            override val title: Text get() = Universal Password ManagerString.edit.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy send URL overflow option.
         */
        @Parcelize
        data class CopyUrlClick(val sendUrl: String) : SendAction() {
            override val title: Text get() = Universal Password ManagerString.copy_link.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the share send URL overflow option.
         */
        @Parcelize
        data class ShareUrlClick(val sendUrl: String) : SendAction() {
            override val title: Text get() = Universal Password ManagerString.share_link.asText()
            override val contentDescription: Text
                get() = Universal Password ManagerString.external_link_format.asText(title)
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the remove password send overflow option.
         */
        @Parcelize
        data class RemovePasswordClick(val sendId: String) : SendAction() {
            override val title: Text get() = Universal Password ManagerString.remove_password.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the delete send overflow option.
         */
        @Parcelize
        data class DeleteClick(val sendId: String) : SendAction() {
            override val title: Text get() = Universal Password ManagerString.delete.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData
                get() = Universal Password ManagerTwoButtonDialogData(
                    title = Universal Password ManagerString.delete.asText(),
                    message = Universal Password ManagerString.are_you_sure_delete_send.asText(),
                    confirmButtonText = Universal Password ManagerString.yes.asText(),
                    dismissButtonText = Universal Password ManagerString.cancel.asText(),
                )
        }
    }

    /**
     * Represents the vault actions.
     */
    sealed class VaultAction : ListingItemOverflowAction() {
        /**
         * Whether the action requires a master password re-prompt if that
         * setting is enabled for the selected item.
         */
        abstract val requiresPasswordReprompt: Boolean

        /**
         * Click on the view cipher overflow option.
         */
        @Parcelize
        data class ViewClick(
            val cipherId: String,
            val cipherType: CipherType,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.view.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the edit cipher overflow option.
         */
        @Parcelize
        data class EditClick(
            val cipherId: String,
            val cipherType: CipherType,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.edit.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy username overflow option.
         */
        @Parcelize
        data class CopyUsernameClick(val username: String) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_username.asText()
            override val requiresPasswordReprompt: Boolean get() = false
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy password overflow option.
         */
        @Parcelize
        data class CopyPasswordClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_password.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy TOTP code overflow option.
         */
        @Parcelize
        data class CopyTotpClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_totp.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy number overflow option.
         */
        @Parcelize
        data class CopyNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy security code overflow option.
         */
        @Parcelize
        data class CopySecurityCodeClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_security_code.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy bank account number overflow option.
         */
        @Parcelize
        data class CopyAccountNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_account_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy bank routing number overflow option.
         */
        @Parcelize
        data class CopyRoutingNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_routing_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy license number overflow option.
         */
        @Parcelize
        data class CopyLicenseNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_license_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy passport number overflow option.
         */
        @Parcelize
        data class CopyPassportNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_passport_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy secure note overflow option.
         */
        @Parcelize
        data class CopyNoteClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.copy_notes.asText()
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the launch overflow option.
         */
        @Parcelize
        data class LaunchClick(val url: String) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.launch.asText()
            override val requiresPasswordReprompt: Boolean get() = false
            override val contentDescription: Text
                get() = Universal Password ManagerString.external_link_format.asText(title)
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }

        /**
         * Click on the archive overflow option.
         */
        @Parcelize
        data class ArchiveClick(val cipherId: String) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.archive_verb.asText()
            override val requiresPasswordReprompt: Boolean get() = true
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData
                get() = Universal Password ManagerTwoButtonDialogData(
                    title = Universal Password ManagerString.archive_item.asText(),
                    message = Universal Password ManagerString.once_archived_this_item_will_be_excluded.asText(),
                    confirmButtonText = Universal Password ManagerString.archive_verb.asText(),
                    dismissButtonText = Universal Password ManagerString.cancel.asText(),
                )
        }

        /**
         * Click on the unarchive overflow option.
         */
        @Parcelize
        data class UnarchiveClick(val cipherId: String) : VaultAction() {
            override val title: Text get() = Universal Password ManagerString.unarchive.asText()
            override val requiresPasswordReprompt: Boolean get() = true
            override val contentDescription: Text get() = title
            override val speedBump: Universal Password ManagerTwoButtonDialogData? get() = null
        }
    }
}
