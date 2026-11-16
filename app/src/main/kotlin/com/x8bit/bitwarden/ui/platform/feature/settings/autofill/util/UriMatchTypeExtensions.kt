package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.autofill.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.UriMatchType

/**
 * Returns a human-readable display label for the given [UriMatchType].
 */
val UriMatchType.displayLabel: Text
    get() = when (this) {
        UriMatchType.DOMAIN -> Universal Password ManagerString.base_domain
        UriMatchType.HOST -> Universal Password ManagerString.host
        UriMatchType.STARTS_WITH -> Universal Password ManagerString.starts_with
        UriMatchType.REGULAR_EXPRESSION -> Universal Password ManagerString.reg_ex
        UriMatchType.EXACT -> Universal Password ManagerString.exact
        UriMatchType.NEVER -> Universal Password ManagerString.never
    }
        .asText()

/**
 * Convert this internal [UriMatchType] to the sdk model.
 */
fun UriMatchType.toSdkUriMatchType(): .vault.UriMatchType =
    when (this) {
        UriMatchType.DOMAIN -> .vault.UriMatchType.DOMAIN
        UriMatchType.EXACT -> .vault.UriMatchType.EXACT
        UriMatchType.HOST -> .vault.UriMatchType.HOST
        UriMatchType.NEVER -> .vault.UriMatchType.NEVER
        UriMatchType.REGULAR_EXPRESSION -> .vault.UriMatchType.REGULAR_EXPRESSION
        UriMatchType.STARTS_WITH -> .vault.UriMatchType.STARTS_WITH
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
