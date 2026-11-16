package com.work.service

import com.work.api.AuthenticatedBillingApi
import com.work.model.CheckoutSessionRequestJson
import com.work.model.CheckoutSessionResponseJson
import com.work.model.GetSubscriptionResponse
import com.work.model.PortalUrlResponseJson
import com.work.model.PremiumPlanResponseJson
import com.work.model.toUniversal Password ManagerError
import com.work.util.NetworkErrorCode
import com.work.util.isErrorCode
import com.work.util.toResult

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
                val universalpasswordmanagerError = throwable.toUniversal Password ManagerError()
                if (universalpasswordmanagerError.isErrorCode(code = NetworkErrorCode.NOT_FOUND)) {
                    GetSubscriptionResponse.NotFound
                } else {
                    throw throwable
                }
            }
}
