package com.universalpasswordmanager.data.auth.datasource.network.di

import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.network.service.AccountsService
import com.universalpasswordmanager.network.service.AuthRequestsService
import com.universalpasswordmanager.network.service.DevicesService
import com.universalpasswordmanager.network.service.HaveIBeenPwnedService
import com.universalpasswordmanager.network.service.IdentityService
import com.universalpasswordmanager.network.service.NewAuthRequestService
import com.universalpasswordmanager.network.service.OrganizationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides network dependencies in the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun providesAccountService(
        bitwardenServiceClient: UniversalServiceClient,
    ): AccountsService = bitwardenServiceClient.accountsService

    @Provides
    @Singleton
    fun providesAuthRequestsService(
        bitwardenServiceClient: UniversalServiceClient,
    ): AuthRequestsService = bitwardenServiceClient.authRequestsService

    @Provides
    @Singleton
    fun providesDevicesService(
        bitwardenServiceClient: UniversalServiceClient,
    ): DevicesService = bitwardenServiceClient.devicesService

    @Provides
    @Singleton
    fun providesIdentityService(
        bitwardenServiceClient: UniversalServiceClient,
    ): IdentityService = bitwardenServiceClient.identityService

    @Provides
    @Singleton
    fun providesHaveIBeenPwnedService(
        bitwardenServiceClient: UniversalServiceClient,
    ): HaveIBeenPwnedService = bitwardenServiceClient.haveIBeenPwnedService

    @Provides
    @Singleton
    fun providesNewAuthRequestService(
        bitwardenServiceClient: UniversalServiceClient,
    ): NewAuthRequestService = bitwardenServiceClient.newAuthRequestService

    @Provides
    @Singleton
    fun providesOrganizationService(
        bitwardenServiceClient: UniversalServiceClient,
    ): OrganizationService = bitwardenServiceClient.organizationService
}
