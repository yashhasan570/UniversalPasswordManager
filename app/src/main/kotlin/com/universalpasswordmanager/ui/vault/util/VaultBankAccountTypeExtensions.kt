package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.model.VaultBankAccountType

/**
 * Helper that exposes the display name [Text] for a [VaultBankAccountType].
 */
val VaultBankAccountType.longName: Text
    get() = when (this) {
        VaultBankAccountType.SELECT -> UniversalString.select.asText()
        VaultBankAccountType.CHECKING -> UniversalString.checking.asText()
        VaultBankAccountType.SAVINGS -> UniversalString.savings.asText()
        VaultBankAccountType.CERTIFICATE_OF_DEPOSIT -> {
            UniversalString.certificate_of_deposit.asText()
        }

        VaultBankAccountType.LINE_OF_CREDIT -> UniversalString.line_of_credit.asText()
        VaultBankAccountType.INVESTMENT_BROKERAGE -> {
            UniversalString.investment_brokerage.asText()
        }

        VaultBankAccountType.MONEY_MARKET -> UniversalString.money_market.asText()
        VaultBankAccountType.OTHER -> UniversalString.other.asText()
    }
