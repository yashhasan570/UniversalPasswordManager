package com.x8bit.universalpasswordmanager.data.billing.datasource.network.di

import com.work.Universal Password ManagerServiceClient
import com.work.service.BillingService
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
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): BillingService = universalpasswordmanagerServiceClient.billingService
}
