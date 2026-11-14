package com.universalpasswordmanager.ui.platform.feature.premium.plan.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.billing.repository.model.PlanCadence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class BigDecimalExtensionsTest {
    private val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    @Test
    fun `toBillingAmountText returns the per-year rate for an annual cadence`() {
        assertEquals(
            UniversalString.billing_rate_per_year.asText("$10.00"),
            BigDecimal("10").toBillingAmountText(PlanCadence.ANNUALLY, currencyFormatter),
        )
    }

    @Test
    fun `toBillingAmountText returns the per-month rate for a monthly cadence`() {
        assertEquals(
            UniversalString.billing_rate_per_month.asText("$10.00"),
            BigDecimal("10").toBillingAmountText(PlanCadence.MONTHLY, currencyFormatter),
        )
    }

    @Test
    fun `toRequiredMoneyText coerces null to a formatted zero`() {
        assertEquals("$0.00", null.toRequiredMoneyText(currencyFormatter))
    }

    @Test
    fun `toRequiredMoneyText formats zero and positive amounts`() {
        assertEquals("$0.00", BigDecimal.ZERO.toRequiredMoneyText(currencyFormatter))
        assertEquals("$10.00", BigDecimal("10").toRequiredMoneyText(currencyFormatter))
    }

    @Test
    fun `toPresentMoneyText returns null only when the amount is null`() {
        assertNull(null.toPresentMoneyText(currencyFormatter))
    }

    @Test
    fun `toPresentMoneyText renders zero and positive amounts`() {
        assertEquals("$0.00", BigDecimal.ZERO.toPresentMoneyText(currencyFormatter))
        assertEquals("$10.00", BigDecimal("10").toPresentMoneyText(currencyFormatter))
    }

    @Test
    fun `toDiscountMoneyText returns null when the amount is null or non-positive`() {
        assertNull(null.toDiscountMoneyText(currencyFormatter))
        assertNull(BigDecimal.ZERO.toDiscountMoneyText(currencyFormatter))
        assertNull(BigDecimal("-5").toDiscountMoneyText(currencyFormatter))
    }

    @Test
    fun `toDiscountMoneyText formats a positive amount as a negative money string`() {
        assertEquals("\u2212$5.00", BigDecimal("5").toDiscountMoneyText(currencyFormatter))
    }
}
