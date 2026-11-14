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
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): AccountsService = universalpasswordmanagerServiceClient.accountsService

    @Provides
    @Singleton
    fun providesAuthRequestsService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): AuthRequestsService = universalpasswordmanagerServiceClient.authRequestsService

    @Provides
    @Singleton
    fun providesDevicesService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): DevicesService = universalpasswordmanagerServiceClient.devicesService

    @Provides
    @Singleton
    fun providesIdentityService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): IdentityService = universalpasswordmanagerServiceClient.identityService

    @Provides
    @Singleton
    fun providesHaveIBeenPwnedService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): HaveIBeenPwnedService = universalpasswordmanagerServiceClient.haveIBeenPwnedService

    @Provides
    @Singleton
    fun providesNewAuthRequestService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): NewAuthRequestService = universalpasswordmanagerServiceClient.newAuthRequestService

    @Provides
    @Singleton
    fun providesOrganizationService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): OrganizationService = universalpasswordmanagerServiceClient.organizationService
}
