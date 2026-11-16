package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.UriMatchType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UriMatchTypeExtensionsTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            UriMatchType.DOMAIN to Universal Password ManagerString.base_domain.asText(),
            UriMatchType.HOST to Universal Password ManagerString.host.asText(),
            UriMatchType.STARTS_WITH to Universal Password ManagerString.starts_with.asText(),
            UriMatchType.REGULAR_EXPRESSION to Universal Password ManagerString.reg_ex.asText(),
            UriMatchType.EXACT to Universal Password ManagerString.exact.asText(),
            UriMatchType.NEVER to Universal Password ManagerString.never.asText(),
        )
            .forEach { (type, label) ->
                assertEquals(
                    label,
                    type.displayLabel,
                )
            }
    }

    @Test
    fun `toSdkUriMatchType should return the correct value for each type`() {
        assertEquals(
            .vault.UriMatchType.DOMAIN,
            UriMatchType.DOMAIN.toSdkUriMatchType(),
        )
        assertEquals(
            .vault.UriMatchType.EXACT,
            UriMatchType.EXACT.toSdkUriMatchType(),
        )
        assertEquals(
            .vault.UriMatchType.HOST,
            UriMatchType.HOST.toSdkUriMatchType(),
        )
        assertEquals(
            .vault.UriMatchType.NEVER,
            UriMatchType.NEVER.toSdkUriMatchType(),
        )
        assertEquals(
            .vault.UriMatchType.REGULAR_EXPRESSION,
            UriMatchType.REGULAR_EXPRESSION.toSdkUriMatchType(),
        )
        assertEquals(
            .vault.UriMatchType.STARTS_WITH,
            UriMatchType.STARTS_WITH.toSdkUriMatchType(),
        )
    }

    @Test
    fun `isAdvancedMatching should return the correct value for each type`() {
        assertEquals(
            false,
            UriMatchType.DOMAIN.isAdvancedMatching(),
        )
        assertEquals(
            false,
            UriMatchType.EXACT.isAdvancedMatching(),
        )
        assertEquals(
            false,
            UriMatchType.HOST.isAdvancedMatching(),
        )
        assertEquals(
            false,
            UriMatchType.NEVER.isAdvancedMatching(),
        )
        assertEquals(
            true,
            UriMatchType.REGULAR_EXPRESSION.isAdvancedMatching(),
        )
        assertEquals(
            true,
            UriMatchType.STARTS_WITH.isAdvancedMatching(),
        )
    }
}
