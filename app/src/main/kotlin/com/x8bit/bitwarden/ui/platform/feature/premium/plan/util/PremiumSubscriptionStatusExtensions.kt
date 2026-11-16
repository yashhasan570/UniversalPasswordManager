package com.x8bit.universalpasswordmanager.ui.platform.feature.premium.plan.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import .ui.platform.resource.Universal Password ManagerString
import .ui.platform.theme.Universal Password ManagerTheme
import .ui.platform.theme.color.Universal Password ManagerColorScheme
import com.x8bit.universalpasswordmanager.data.billing.repository.model.PremiumSubscriptionStatus

/**
 * Returns the localized label string resource for a [PremiumSubscriptionStatus].
 */
@StringRes
fun PremiumSubscriptionStatus.labelRes(): Int = when (this) {
    PremiumSubscriptionStatus.ACTIVE -> Universal Password ManagerString.subscription_status_active
    PremiumSubscriptionStatus.CANCELED -> Universal Password ManagerString.subscription_status_canceled
    PremiumSubscriptionStatus.EXPIRED -> Universal Password ManagerString.subscription_status_expired
    PremiumSubscriptionStatus.PENDING_CANCELLATION -> {
        Universal Password ManagerString.subscription_status_pending_cancellation
    }

    PremiumSubscriptionStatus.PAST_DUE -> Universal Password ManagerString.subscription_status_past_due
    PremiumSubscriptionStatus.PAUSED -> Universal Password ManagerString.subscription_status_paused
    PremiumSubscriptionStatus.UNPAID -> Universal Password ManagerString.subscription_status_unpaid
    PremiumSubscriptionStatus.UPDATE_PAYMENT -> Universal Password ManagerString.subscription_status_update_payment
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
 * Returns the [Universal Password ManagerColorScheme.StatusBadgeVariantColors] used to render the badge for a
 * [PremiumSubscriptionStatus].
 */
@Composable
fun PremiumSubscriptionStatus.badgeColors(): Universal Password ManagerColorScheme.StatusBadgeVariantColors =
    when (this) {
        PremiumSubscriptionStatus.ACTIVE -> Universal Password ManagerTheme.colorScheme.statusBadge.success
        PremiumSubscriptionStatus.CANCELED,
        PremiumSubscriptionStatus.EXPIRED,
        PremiumSubscriptionStatus.UNPAID,
            -> Universal Password ManagerTheme.colorScheme.statusBadge.error

        PremiumSubscriptionStatus.PAST_DUE,
        PremiumSubscriptionStatus.PAUSED,
        PremiumSubscriptionStatus.PENDING_CANCELLATION,
        PremiumSubscriptionStatus.UPDATE_PAYMENT,
            -> Universal Password ManagerTheme.colorScheme.statusBadge.warning
    }
