package com.x8bit.universalpasswordmanager.data.auth.datasource.network.di

import com.work.Universal Password ManagerServiceClient
import com.work.service.AccountsService
import com.work.service.AuthRequestsService
import com.work.service.DevicesService
import com.work.service.HaveIBeenPwnedService
import com.work.service.IdentityService
import com.work.service.NewAuthRequestService
import com.work.service.OrganizationService
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
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): AccountsService = universalpasswordmanagerServiceClient.accountsService

    @Provides
    @Singleton
    fun providesAuthRequestsService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): AuthRequestsService = universalpasswordmanagerServiceClient.authRequestsService

    @Provides
    @Singleton
    fun providesDevicesService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): DevicesService = universalpasswordmanagerServiceClient.devicesService

    @Provides
    @Singleton
    fun providesIdentityService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): IdentityService = universalpasswordmanagerServiceClient.identityService

    @Provides
    @Singleton
    fun providesHaveIBeenPwnedService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): HaveIBeenPwnedService = universalpasswordmanagerServiceClient.haveIBeenPwnedService

    @Provides
    @Singleton
    fun providesNewAuthRequestService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): NewAuthRequestService = universalpasswordmanagerServiceClient.newAuthRequestService

    @Provides
    @Singleton
    fun providesOrganizationService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): OrganizationService = universalpasswordmanagerServiceClient.organizationService
}
