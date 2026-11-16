package com.x8bit.universalpasswordmanager.data.credentials.datasource.network.di

import com.work.Universal Password ManagerServiceClient
import com.work.service.DigitalAssetLinkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides network dependencies in the fido2 package.
 */
@Module
@InstallIn(SingletonComponent::class)
object CredentialProviderNetworkModule {

    @Provides
    @Singleton
    fun provideDigitalAssetLinkService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): DigitalAssetLinkService =
        universalpasswordmanagerServiceClient.digitalAssetLinkService
}
