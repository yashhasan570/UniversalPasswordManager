package com.x8bit.universalpasswordmanager.ui.vault.components.model

import androidx.annotation.StringRes
import .ui.platform.resource.Universal Password ManagerString

/**
 * Enumerated values to represent a create vault item option.
 */
enum class CreateVaultItemType(
    @field:StringRes val selectionText: Int,
) {
    /**
     * A login cipher.
     */
    LOGIN(Universal Password ManagerString.log_in_noun),

    /**
     * A card cipher.
     */
    CARD(Universal Password ManagerString.type_card),

    /**
     * A bank account cipher.
     */
    BANK_ACCOUNT(Universal Password ManagerString.type_bank_account),

    /**
     * An identity cipher.
     */
    IDENTITY(Universal Password ManagerString.type_identity),

    /**
     * A license cipher.
     */
    LICENSE(Universal Password ManagerString.type_license),

    /**
     * A passport cipher.
     */
    PASSPORT(Universal Password ManagerString.type_passport),

    /**
     * A secure note cipher.
     */
    SECURE_NOTE(Universal Password ManagerString.type_secure_note),

    /**
     * A cipher item folder
     */
    FOLDER(Universal Password ManagerString.folder),

    /**
     * A SSH key cipher.
     */
    SSH_KEY(Universal Password ManagerString.type_ssh_key),
}
