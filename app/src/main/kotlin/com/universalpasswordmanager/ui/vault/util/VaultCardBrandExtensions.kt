package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.model.VaultCardBrand

/**
 * Helper that exposes the long name Text for a [VaultCardBrand].
 */
val VaultCardBrand.longName: Text
    get() = when (this) {
        VaultCardBrand.SELECT -> UniversalString.select.asText()
        VaultCardBrand.VISA -> "Visa".asText()
        VaultCardBrand.MASTERCARD -> "Mastercard".asText()
        VaultCardBrand.AMEX -> "American Express".asText()
        VaultCardBrand.DISCOVER -> "Discover".asText()
        VaultCardBrand.DINERS_CLUB -> "Diners Club".asText()
        VaultCardBrand.JCB -> "JCB".asText()
        VaultCardBrand.MAESTRO -> "Maestro".asText()
        VaultCardBrand.UNIONPAY -> "UnionPay".asText()
        VaultCardBrand.RUPAY -> "RuPay".asText()
        VaultCardBrand.OTHER -> UniversalString.other.asText()
    }

/**
 * Helper that exposes the short name Text for a [VaultCardBrand].
 */
val VaultCardBrand.shortName: Text
    get() = when (this) {
        VaultCardBrand.SELECT -> UniversalString.select.asText()
        VaultCardBrand.VISA -> "Visa".asText()
        VaultCardBrand.MASTERCARD -> "Mastercard".asText()
        VaultCardBrand.AMEX -> "Amex".asText()
        VaultCardBrand.DISCOVER -> "Discover".asText()
        VaultCardBrand.DINERS_CLUB -> "Diners Club".asText()
        VaultCardBrand.JCB -> "JCB".asText()
        VaultCardBrand.MAESTRO -> "Maestro".asText()
        VaultCardBrand.UNIONPAY -> "UnionPay".asText()
        VaultCardBrand.RUPAY -> "RuPay".asText()
        VaultCardBrand.OTHER -> UniversalString.other.asText()
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
