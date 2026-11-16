package com.work.api

import com.work.model.Universal Password ManagerSubscriptionResponseJson
import com.work.model.CheckoutSessionRequestJson
import com.work.model.CheckoutSessionResponseJson
import com.work.model.NetworkResult
import com.work.model.PortalUrlResponseJson
import com.work.model.PremiumPlanResponseJson
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
    suspend fun getSubscription(): NetworkResult<Universal Password ManagerSubscriptionResponseJson>
}
