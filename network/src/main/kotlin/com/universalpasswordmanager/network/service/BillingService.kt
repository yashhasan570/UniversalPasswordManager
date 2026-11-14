package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.model.CheckoutSessionResponseJson
import com.universalpasswordmanager.network.model.GetSubscriptionResponse
import com.universalpasswordmanager.network.model.PortalUrlResponseJson
import com.universalpasswordmanager.network.model.PremiumPlanResponseJson

/**
 * Provides an API for interacting with the billing endpoints.
 */
interface BillingService {

    /**
     * Creates a Stripe checkout session for Premium upgrade.
     */
    suspend fun createCheckoutSession(): Result<CheckoutSessionResponseJson>

    /**
     * Creates a Stripe customer portal session for managing the Premium subscription.
     */
    suspend fun getPortalUrl(): Result<PortalUrlResponseJson>

    /**
     * Retrieves the premium plan pricing information.
     */
    suspend fun getPremiumPlan(): Result<PremiumPlanResponseJson>

    /**
     * Retrieves the user's premium subscription details.
     */
    suspend fun getSubscription(): Result<GetSubscriptionResponse>
}
