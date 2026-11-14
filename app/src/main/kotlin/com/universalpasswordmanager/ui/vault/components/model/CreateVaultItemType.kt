package com.universalpasswordmanager.ui.vault.components.model

import androidx.annotation.StringRes
import com.universalpasswordmanager.ui.platform.resource.UniversalString

/**
 * Enumerated values to represent a create vault item option.
 */
enum class CreateVaultItemType(
    @field:StringRes val selectionText: Int,
) {
    /**
     * A login cipher.
     */
    LOGIN(UniversalString.log_in_noun),

    /**
     * A card cipher.
     */
    CARD(UniversalString.type_card),

    /**
     * A bank account cipher.
     */
    BANK_ACCOUNT(UniversalString.type_bank_account),

    /**
     * An identity cipher.
     */
    IDENTITY(UniversalString.type_identity),

    /**
     * A license cipher.
     */
    LICENSE(UniversalString.type_license),

    /**
     * A passport cipher.
     */
    PASSPORT(UniversalString.type_passport),

    /**
     * A secure note cipher.
     */
    SECURE_NOTE(UniversalString.type_secure_note),

    /**
     * A cipher item folder
     */
    FOLDER(UniversalString.folder),

    /**
     * A SSH key cipher.
     */
    SSH_KEY(UniversalString.type_ssh_key),
}
