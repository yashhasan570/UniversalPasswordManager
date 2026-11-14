package com.universalpasswordmanager.ui.platform.feature.premium.plan.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.platform.theme.UniversalTheme
import com.universalpasswordmanager.ui.platform.theme.color.UniversalColorScheme
import com.universalpasswordmanager.data.billing.repository.model.PremiumSubscriptionStatus

/**
 * Returns the localized label string resource for a [PremiumSubscriptionStatus].
 */
@StringRes
fun PremiumSubscriptionStatus.labelRes(): Int = when (this) {
    PremiumSubscriptionStatus.ACTIVE -> UniversalString.subscription_status_active
    PremiumSubscriptionStatus.CANCELED -> UniversalString.subscription_status_canceled
    PremiumSubscriptionStatus.EXPIRED -> UniversalString.subscription_status_expired
    PremiumSubscriptionStatus.PENDING_CANCELLATION -> {
        UniversalString.subscription_status_pending_cancellation
    }

    PremiumSubscriptionStatus.PAST_DUE -> UniversalString.subscription_status_past_due
    PremiumSubscriptionStatus.PAUSED -> UniversalString.subscription_status_paused
    PremiumSubscriptionStatus.UNPAID -> UniversalString.subscription_status_unpaid
    PremiumSubscriptionStatus.UPDATE_PAYMENT -> UniversalString.subscription_status_update_payment
}

/**
 * Returns `true` when the Premium plan card should replace its billing line items with the
 * premium feature list. Reserved for terminal states where line items carry no actionable
 * information and the user's path forward is to resubscribe.
 */
fun PremiumSubscriptionStatus.showsFeatureList(): Boolean = when (this) {
    PremiumSubscriptionStatus.CANCELED,
    PremiumSubscriptionStatus.EXPIRED,
        -> true

    PremiumSubscriptionStatus.ACTIVE,
    PremiumSubscriptionStatus.PAST_DUE,
    PremiumSubscriptionStatus.PAUSED,
    PremiumSubscriptionStatus.PENDING_CANCELLATION,
    PremiumSubscriptionStatus.UNPAID,
    PremiumSubscriptionStatus.UPDATE_PAYMENT,
        -> false
}

/**
 * Returns the [UniversalColorScheme.StatusBadgeVariantColors] used to render the badge for a
 * [PremiumSubscriptionStatus].
 */
@Composable
fun PremiumSubscriptionStatus.badgeColors(): UniversalColorScheme.StatusBadgeVariantColors =
    when (this) {
        PremiumSubscriptionStatus.ACTIVE -> UniversalTheme.colorScheme.statusBadge.success
        PremiumSubscriptionStatus.CANCELED,
        PremiumSubscriptionStatus.EXPIRED,
        PremiumSubscriptionStatus.UNPAID,
            -> UniversalTheme.colorScheme.statusBadge.error

        PremiumSubscriptionStatus.PAST_DUE,
        PremiumSubscriptionStatus.PAUSED,
        PremiumSubscriptionStatus.PENDING_CANCELLATION,
        PremiumSubscriptionStatus.UPDATE_PAYMENT,
            -> UniversalTheme.colorScheme.statusBadge.warning
    }
