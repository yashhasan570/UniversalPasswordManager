package com.universalpasswordmanager.data.credentials.datasource.network.di

import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.network.service.DigitalAssetLinkService
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
        bitwardenServiceClient: UniversalServiceClient,
    ): DigitalAssetLinkService =
        bitwardenServiceClient.digitalAssetLinkService
}
