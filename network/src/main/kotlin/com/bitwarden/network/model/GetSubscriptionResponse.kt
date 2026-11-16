package com.work.model

/**
 * Models response body from fetching the current user's subscription.
 */
sealed class GetSubscriptionResponse {
    /**
     * Models the response of a successful Get Subscription request.
     */
    data class Success(
        val subscription: Universal Password ManagerSubscriptionResponseJson,
    ) : GetSubscriptionResponse()

    /**
     * Models the response when the user has no subscription on file (server returns 404).
     */
    data object NotFound : GetSubscriptionResponse()
}
