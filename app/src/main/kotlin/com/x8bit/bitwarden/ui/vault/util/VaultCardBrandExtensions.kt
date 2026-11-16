package com.x8bit.universalpasswordmanager.ui.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultCardBrand

/**
 * Helper that exposes the long name Text for a [VaultCardBrand].
 */
val VaultCardBrand.longName: Text
    get() = when (this) {
        VaultCardBrand.SELECT -> Universal Password ManagerString.select.asText()
        VaultCardBrand.VISA -> "Visa".asText()
        VaultCardBrand.MASTERCARD -> "Mastercard".asText()
        VaultCardBrand.AMEX -> "American Express".asText()
        VaultCardBrand.DISCOVER -> "Discover".asText()
        VaultCardBrand.DINERS_CLUB -> "Diners Club".asText()
        VaultCardBrand.JCB -> "JCB".asText()
        VaultCardBrand.MAESTRO -> "Maestro".asText()
        VaultCardBrand.UNIONPAY -> "UnionPay".asText()
        VaultCardBrand.RUPAY -> "RuPay".asText()
        VaultCardBrand.OTHER -> Universal Password ManagerString.other.asText()
    }

/**
 * Helper that exposes the short name Text for a [VaultCardBrand].
 */
val VaultCardBrand.shortName: Text
    get() = when (this) {
        VaultCardBrand.SELECT -> Universal Password ManagerString.select.asText()
        VaultCardBrand.VISA -> "Visa".asText()
        VaultCardBrand.MASTERCARD -> "Mastercard".asText()
        VaultCardBrand.AMEX -> "Amex".asText()
        VaultCardBrand.DISCOVER -> "Discover".asText()
        VaultCardBrand.DINERS_CLUB -> "Diners Club".asText()
        VaultCardBrand.JCB -> "JCB".asText()
        VaultCardBrand.MAESTRO -> "Maestro".asText()
        VaultCardBrand.UNIONPAY -> "UnionPay".asText()
        VaultCardBrand.RUPAY -> "RuPay".asText()
        VaultCardBrand.OTHER -> Universal Password ManagerString.other.asText()
    }

/**
 * Helper that exposes the long name string or null for a [VaultCardBrand].
 */
val VaultCardBrand.stringLongNameOrNull: String?
    get() = when (this) {
        VaultCardBrand.SELECT -> null
        VaultCardBrand.VISA -> "Visa"
        VaultCardBrand.MASTERCARD -> "Mastercard"
        VaultCardBrand.AMEX -> "Amex"
        VaultCardBrand.DISCOVER -> "Discover"
        VaultCardBrand.DINERS_CLUB -> "Diners Club"
        VaultCardBrand.JCB -> "JCB"
        VaultCardBrand.MAESTRO -> "Maestro"
        VaultCardBrand.UNIONPAY -> "UnionPay"
        VaultCardBrand.RUPAY -> "RuPay"
        VaultCardBrand.OTHER -> "Other"
    }
