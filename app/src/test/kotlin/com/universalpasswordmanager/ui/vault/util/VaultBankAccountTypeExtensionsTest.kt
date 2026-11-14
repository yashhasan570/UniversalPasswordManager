package com.universalpasswordmanager.ui.vault.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.ui.vault.model.VaultBankAccountType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultBankAccountTypeExtensionsTest {

    @Test
    fun `longName should return the correct value for each VaultBankAccountType`() {
        mapOf(
            VaultBankAccountType.SELECT to UniversalString.select.asText(),
            VaultBankAccountType.CHECKING to UniversalString.checking.asText(),
            VaultBankAccountType.SAVINGS to UniversalString.savings.asText(),
            VaultBankAccountType.CERTIFICATE_OF_DEPOSIT to
                UniversalString.certificate_of_deposit.asText(),
            VaultBankAccountType.LINE_OF_CREDIT to UniversalString.line_of_credit.asText(),
            VaultBankAccountType.INVESTMENT_BROKERAGE to
                UniversalString.investment_brokerage.asText(),
            VaultBankAccountType.MONEY_MARKET to UniversalString.money_market.asText(),
            VaultBankAccountType.OTHER to UniversalString.other.asText(),
        )
            .forEach { (type, label) ->
                assertEquals(label, type.longName)
            }
    }
}
