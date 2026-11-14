package com.universalpasswordmanager.ui.platform.feature.settings.autofill.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.UriMatchType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UriMatchTypeExtensionsTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            UriMatchType.DOMAIN to UniversalString.base_domain.asText(),
            UriMatchType.HOST to UniversalString.host.asText(),
            UriMatchType.STARTS_WITH to UniversalString.starts_with.asText(),
            UriMatchType.REGULAR_EXPRESSION to UniversalString.reg_ex.asText(),
            UriMatchType.EXACT to UniversalString.exact.asText(),
            UriMatchType.NEVER to UniversalString.never.asText(),
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
            com.universalpasswordmanager.vault.UriMatchType.DOMAIN,
            UriMatchType.DOMAIN.toSdkUriMatchType(),
        )
        assertEquals(
            com.universalpasswordmanager.vault.UriMatchType.EXACT,
            UriMatchType.EXACT.toSdkUriMatchType(),
        )
        assertEquals(
            com.universalpasswordmanager.vault.UriMatchType.HOST,
            UriMatchType.HOST.toSdkUriMatchType(),
        )
        assertEquals(
            com.universalpasswordmanager.vault.UriMatchType.NEVER,
            UriMatchType.NEVER.toSdkUriMatchType(),
        )
        assertEquals(
            com.universalpasswordmanager.vault.UriMatchType.REGULAR_EXPRESSION,
            UriMatchType.REGULAR_EXPRESSION.toSdkUriMatchType(),
        )
        assertEquals(
            com.universalpasswordmanager.vault.UriMatchType.STARTS_WITH,
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
