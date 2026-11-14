package com.universalpasswordmanager.network.service

import com.universalpasswordmanager.network.api.AuthenticatedBillingApi
import com.universalpasswordmanager.network.model.CheckoutSessionRequestJson
import com.universalpasswordmanager.network.model.CheckoutSessionResponseJson
import com.universalpasswordmanager.network.model.GetSubscriptionResponse
import com.universalpasswordmanager.network.model.PortalUrlResponseJson
import com.universalpasswordmanager.network.model.PremiumPlanResponseJson
import com.universalpasswordmanager.network.model.toUniversalError
import com.universalpasswordmanager.network.util.NetworkErrorCode
import com.universalpasswordmanager.network.util.isErrorCode
import com.universalpasswordmanager.network.util.toResult

private const val PLATFORM = "android"

/**
 * The default implementation of the [BillingService].
 */
internal class BillingServiceImpl(
    private val authenticatedBillingApi: AuthenticatedBillingApi,
) : BillingService {

    override suspend fun createCheckoutSession(): Result<CheckoutSessionResponseJson> =
        authenticatedBillingApi
            .createCheckoutSession(
                body = CheckoutSessionRequestJson(platform = PLATFORM),
            )
            .toResult()

    override suspend fun getPortalUrl(): Result<PortalUrlResponseJson> =
        authenticatedBillingApi
            .getPortalUrl()
            .toResult()

    override suspend fun getPremiumPlan(): Result<PremiumPlanResponseJson> =
        authenticatedBillingApi
            .getPremiumPlan()
            .toResult()

    override suspend fun getSubscription(): Result<GetSubscriptionResponse> =
        authenticatedBillingApi
            .getSubscription()
            .toResult()
            .map { GetSubscriptionResponse.Success(subscription = it) }
            .recoverCatching { throwable ->
                val bitwardenError = throwable.toUniversalError()
                if (bitwardenError.isErrorCode(code = NetworkErrorCode.NOT_FOUND)) {
                    GetSubscriptionResponse.NotFound
                } else {
                    throw throwable
                }
            }
}
