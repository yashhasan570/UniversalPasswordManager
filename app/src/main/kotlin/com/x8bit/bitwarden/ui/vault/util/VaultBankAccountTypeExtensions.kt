package com.x8bit.universalpasswordmanager.ui.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultBankAccountType

/**
 * Helper that exposes the display name [Text] for a [VaultBankAccountType].
 */
val VaultBankAccountType.longName: Text
    get() = when (this) {
        VaultBankAccountType.SELECT -> Universal Password ManagerString.select.asText()
        VaultBankAccountType.CHECKING -> Universal Password ManagerString.checking.asText()
        VaultBankAccountType.SAVINGS -> Universal Password ManagerString.savings.asText()
        VaultBankAccountType.CERTIFICATE_OF_DEPOSIT -> {
            Universal Password ManagerString.certificate_of_deposit.asText()
        }

        VaultBankAccountType.LINE_OF_CREDIT -> Universal Password ManagerString.line_of_credit.asText()
        VaultBankAccountType.INVESTMENT_BROKERAGE -> {
            Universal Password ManagerString.investment_brokerage.asText()
        }

        VaultBankAccountType.MONEY_MARKET -> Universal Password ManagerString.money_market.asText()
        VaultBankAccountType.OTHER -> Universal Password ManagerString.other.asText()
    }
