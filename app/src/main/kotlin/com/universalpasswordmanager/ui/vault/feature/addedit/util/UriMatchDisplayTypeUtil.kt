package com.universalpasswordmanager.ui.vault.feature.addedit.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.vault.UriMatchType
import com.universalpasswordmanager.ui.vault.feature.addedit.model.UriMatchDisplayType

/**
 * Method to convert the SDK match type for display to the user.
 */
fun UriMatchType?.toDisplayMatchType(): UriMatchDisplayType =
    when (this) {
        UriMatchType.DOMAIN -> UriMatchDisplayType.BASE_DOMAIN
        UriMatchType.EXACT -> UriMatchDisplayType.EXACT
        UriMatchType.HOST -> UriMatchDisplayType.HOST
        UriMatchType.NEVER -> UriMatchDisplayType.NEVER
        UriMatchType.REGULAR_EXPRESSION -> UriMatchDisplayType.REGULAR_EXPRESSION
        UriMatchType.STARTS_WITH -> UriMatchDisplayType.STARTS_WITH
        null -> UriMatchDisplayType.DEFAULT
    }

/**
 * Method to convert the match display type over to the SDK match type.
 */
fun UriMatchDisplayType.toUriMatchType(): UriMatchType? =
    when (this) {
        UriMatchDisplayType.DEFAULT -> null
        UriMatchDisplayType.BASE_DOMAIN -> UriMatchType.DOMAIN
        UriMatchDisplayType.HOST -> UriMatchType.HOST
        UriMatchDisplayType.STARTS_WITH -> UriMatchType.STARTS_WITH
        UriMatchDisplayType.REGULAR_EXPRESSION -> UriMatchType.REGULAR_EXPRESSION
        UriMatchDisplayType.EXACT -> UriMatchType.EXACT
        UriMatchDisplayType.NEVER -> UriMatchType.NEVER
    }

/**
 * Checks if the [UriMatchDisplayType] is considered an advanced matching strategy.
 */
fun UriMatchDisplayType.isAdvancedMatching(): Boolean =
    when (this) {
        UriMatchDisplayType.REGULAR_EXPRESSION,
        UriMatchDisplayType.STARTS_WITH,
            -> true

        else -> false
    }

/**
 * Returns a human-readable display label for the given [UriMatchType].
 */
fun UriMatchDisplayType.displayLabel(defaultUriOption: String): Text {
    return when (this) {
        UriMatchDisplayType.DEFAULT -> UniversalString.default_text.asText(defaultUriOption)
        UriMatchDisplayType.BASE_DOMAIN -> UniversalString.base_domain.asText()
        UriMatchDisplayType.HOST -> UniversalString.host.asText()
        UriMatchDisplayType.STARTS_WITH -> UniversalString.starts_with.asText()
        UriMatchDisplayType.REGULAR_EXPRESSION -> UniversalString.reg_ex.asText()
        UriMatchDisplayType.EXACT -> UniversalString.exact.asText()
        UriMatchDisplayType.NEVER -> UniversalString.never.asText()
    }
}
