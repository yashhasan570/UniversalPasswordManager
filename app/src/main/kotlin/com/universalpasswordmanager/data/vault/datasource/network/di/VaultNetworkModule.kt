package com.universalpasswordmanager.data.vault.datasource.network.di

import com.universalpasswordmanager.network.UniversalServiceClient
import com.universalpasswordmanager.network.service.CiphersService
import com.universalpasswordmanager.network.service.DownloadService
import com.universalpasswordmanager.network.service.FolderService
import com.universalpasswordmanager.network.service.SendsService
import com.universalpasswordmanager.network.service.SyncService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides network dependencies in the vault package.
 */
@Module
@InstallIn(SingletonComponent::class)
object VaultNetworkModule {

    @Provides
    @Singleton
    fun provideCiphersService(
        bitwardenServiceClient: UniversalServiceClient,
    ): CiphersService = bitwardenServiceClient.ciphersService

    @Provides
    @Singleton
    fun providesFolderService(
        bitwardenServiceClient: UniversalServiceClient,
    ): FolderService = bitwardenServiceClient.folderService

    @Provides
    @Singleton
    fun provideSendsService(
        bitwardenServiceClient: UniversalServiceClient,
    ): SendsService = bitwardenServiceClient.sendsService

    @Provides
    @Singleton
    fun provideSyncService(
        bitwardenServiceClient: UniversalServiceClient,
    ): SyncService = bitwardenServiceClient.syncService

    @Provides
    @Singleton
    fun provideDownloadService(
        bitwardenServiceClient: UniversalServiceClient,
    ): DownloadService = bitwardenServiceClient.downloadService
}
