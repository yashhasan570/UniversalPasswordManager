package com.universalpasswordmanager.ui.platform.feature.settings.autofill.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.UriMatchType

/**
 * Returns a human-readable display label for the given [UriMatchType].
 */
val UriMatchType.displayLabel: Text
    get() = when (this) {
        UriMatchType.DOMAIN -> UniversalString.base_domain
        UriMatchType.HOST -> UniversalString.host
        UriMatchType.STARTS_WITH -> UniversalString.starts_with
        UriMatchType.REGULAR_EXPRESSION -> UniversalString.reg_ex
        UriMatchType.EXACT -> UniversalString.exact
        UriMatchType.NEVER -> UniversalString.never
    }
        .asText()

/**
 * Convert this internal [UriMatchType] to the sdk model.
 */
fun UriMatchType.toSdkUriMatchType(): com.universalpasswordmanager.vault.UriMatchType =
    when (this) {
        UriMatchType.DOMAIN -> com.universalpasswordmanager.vault.UriMatchType.DOMAIN
        UriMatchType.EXACT -> com.universalpasswordmanager.vault.UriMatchType.EXACT
        UriMatchType.HOST -> com.universalpasswordmanager.vault.UriMatchType.HOST
        UriMatchType.NEVER -> com.universalpasswordmanager.vault.UriMatchType.NEVER
        UriMatchType.REGULAR_EXPRESSION -> com.universalpasswordmanager.vault.UriMatchType.REGULAR_EXPRESSION
        UriMatchType.STARTS_WITH -> com.universalpasswordmanager.vault.UriMatchType.STARTS_WITH
    }

/**
 * Checks if the [UriMatchType] is considered an advanced matching strategy.
 */
fun UriMatchType.isAdvancedMatching(): Boolean =
    when (this) {
        UriMatchType.REGULAR_EXPRESSION,
        UriMatchType.STARTS_WITH,
            -> true

        else -> false
    }
