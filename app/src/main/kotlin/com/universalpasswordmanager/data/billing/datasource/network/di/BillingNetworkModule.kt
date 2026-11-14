package com.universalpasswordmanager.data.billing.datasource.network.di

import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.network.service.BillingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides network dependencies in the billing package.
 */
@Module
@InstallIn(SingletonComponent::class)
object BillingNetworkModule {

    @Provides
    @Singleton
    fun provideBillingService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): BillingService = universalpasswordmanagerServiceClient.billingService
}
