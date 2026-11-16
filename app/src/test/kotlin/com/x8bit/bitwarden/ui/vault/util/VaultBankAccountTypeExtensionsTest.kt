package com.x8bit.universalpasswordmanager.ui.vault.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.ui.vault.model.VaultBankAccountType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultBankAccountTypeExtensionsTest {

    @Test
    fun `longName should return the correct value for each VaultBankAccountType`() {
        mapOf(
            VaultBankAccountType.SELECT to Universal Password ManagerString.select.asText(),
            VaultBankAccountType.CHECKING to Universal Password ManagerString.checking.asText(),
            VaultBankAccountType.SAVINGS to Universal Password ManagerString.savings.asText(),
            VaultBankAccountType.CERTIFICATE_OF_DEPOSIT to
                Universal Password ManagerString.certificate_of_deposit.asText(),
            VaultBankAccountType.LINE_OF_CREDIT to Universal Password ManagerString.line_of_credit.asText(),
            VaultBankAccountType.INVESTMENT_BROKERAGE to
                Universal Password ManagerString.investment_brokerage.asText(),
            VaultBankAccountType.MONEY_MARKET to Universal Password ManagerString.money_market.asText(),
            VaultBankAccountType.OTHER to Universal Password ManagerString.other.asText(),
        )
            .forEach { (type, label) ->
                assertEquals(label, type.longName)
            }
    }
}
