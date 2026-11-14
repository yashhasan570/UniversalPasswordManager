package com.universalpasswordmanager.network.api

import com.universalpasswordmanager.network.model.UniversalSubscriptionResponseJson
import com.universalpasswordmanager.network.model.CheckoutSessionRequestJson
import com.universalpasswordmanager.network.model.CheckoutSessionResponseJson
import com.universalpasswordmanager.network.model.NetworkResult
import com.universalpasswordmanager.network.model.PortalUrlResponseJson
import com.universalpasswordmanager.network.model.PremiumPlanResponseJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Defines raw calls under the /account/billing API with authentication applied.
 */
internal interface AuthenticatedBillingApi {

    /**
     * Creates a Stripe checkout session for Premium upgrade.
     */
    @POST("/account/billing/vnext/premium/checkout")
    suspend fun createCheckoutSession(
        @Body body: CheckoutSessionRequestJson,
    ): NetworkResult<CheckoutSessionResponseJson>

    /**
     * Creates a Stripe customer portal session for managing the Premium subscription.
     */
    @POST("/account/billing/vnext/portal-session")
    suspend fun getPortalUrl(): NetworkResult<PortalUrlResponseJson>

    /**
     * Retrieves the premium plan pricing information.
     */
    @GET("/plans/premium")
    suspend fun getPremiumPlan(): NetworkResult<PremiumPlanResponseJson>

    /**
     * Retrieves the user's premium subscription details.
     */
    @GET("/account/billing/vnext/subscription")
    suspend fun getSubscription(): NetworkResult<UniversalSubscriptionResponseJson>
}
