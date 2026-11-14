@file:OmitFromCoverage

package com.universalpasswordmanager.ui.vault.feature.itemlisting.model

import android.os.Parcelable
import com.universalpasswordmanager.annotation.OmitFromCoverage
import com.universalpasswordmanager.send.SendType
import com.universalpasswordmanager.ui.platform.components.dialog.model.UniversalTwoButtonDialogData
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.vault.CipherType
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
    abstract val speedBump: UniversalTwoButtonDialogData?

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
            override val title: Text get() = UniversalString.view.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the edit send overflow option.
         */
        @Parcelize
        data class EditClick(
            val sendId: String,
            val sendType: SendType,
        ) : SendAction() {
            override val title: Text get() = UniversalString.edit.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy send URL overflow option.
         */
        @Parcelize
        data class CopyUrlClick(val sendUrl: String) : SendAction() {
            override val title: Text get() = UniversalString.copy_link.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the share send URL overflow option.
         */
        @Parcelize
        data class ShareUrlClick(val sendUrl: String) : SendAction() {
            override val title: Text get() = UniversalString.share_link.asText()
            override val contentDescription: Text
                get() = UniversalString.external_link_format.asText(title)
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the remove password send overflow option.
         */
        @Parcelize
        data class RemovePasswordClick(val sendId: String) : SendAction() {
            override val title: Text get() = UniversalString.remove_password.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the delete send overflow option.
         */
        @Parcelize
        data class DeleteClick(val sendId: String) : SendAction() {
            override val title: Text get() = UniversalString.delete.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData
                get() = UniversalTwoButtonDialogData(
                    title = UniversalString.delete.asText(),
                    message = UniversalString.are_you_sure_delete_send.asText(),
                    confirmButtonText = UniversalString.yes.asText(),
                    dismissButtonText = UniversalString.cancel.asText(),
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
            override val title: Text get() = UniversalString.view.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
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
            override val title: Text get() = UniversalString.edit.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy username overflow option.
         */
        @Parcelize
        data class CopyUsernameClick(val username: String) : VaultAction() {
            override val title: Text get() = UniversalString.copy_username.asText()
            override val requiresPasswordReprompt: Boolean get() = false
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy password overflow option.
         */
        @Parcelize
        data class CopyPasswordClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_password.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy TOTP code overflow option.
         */
        @Parcelize
        data class CopyTotpClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_totp.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy number overflow option.
         */
        @Parcelize
        data class CopyNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy security code overflow option.
         */
        @Parcelize
        data class CopySecurityCodeClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_security_code.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy bank account number overflow option.
         */
        @Parcelize
        data class CopyAccountNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_account_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy bank routing number overflow option.
         */
        @Parcelize
        data class CopyRoutingNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_routing_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy license number overflow option.
         */
        @Parcelize
        data class CopyLicenseNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_license_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy passport number overflow option.
         */
        @Parcelize
        data class CopyPassportNumberClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_passport_number.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the copy secure note overflow option.
         */
        @Parcelize
        data class CopyNoteClick(
            val cipherId: String,
            override val requiresPasswordReprompt: Boolean,
        ) : VaultAction() {
            override val title: Text get() = UniversalString.copy_notes.asText()
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the launch overflow option.
         */
        @Parcelize
        data class LaunchClick(val url: String) : VaultAction() {
            override val title: Text get() = UniversalString.launch.asText()
            override val requiresPasswordReprompt: Boolean get() = false
            override val contentDescription: Text
                get() = UniversalString.external_link_format.asText(title)
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }

        /**
         * Click on the archive overflow option.
         */
        @Parcelize
        data class ArchiveClick(val cipherId: String) : VaultAction() {
            override val title: Text get() = UniversalString.archive_verb.asText()
            override val requiresPasswordReprompt: Boolean get() = true
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData
                get() = UniversalTwoButtonDialogData(
                    title = UniversalString.archive_item.asText(),
                    message = UniversalString.once_archived_this_item_will_be_excluded.asText(),
                    confirmButtonText = UniversalString.archive_verb.asText(),
                    dismissButtonText = UniversalString.cancel.asText(),
                )
        }

        /**
         * Click on the unarchive overflow option.
         */
        @Parcelize
        data class UnarchiveClick(val cipherId: String) : VaultAction() {
            override val title: Text get() = UniversalString.unarchive.asText()
            override val requiresPasswordReprompt: Boolean get() = true
            override val contentDescription: Text get() = title
            override val speedBump: UniversalTwoButtonDialogData? get() = null
        }
    }
}
