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
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): CiphersService = universalpasswordmanagerServiceClient.ciphersService

    @Provides
    @Singleton
    fun providesFolderService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): FolderService = universalpasswordmanagerServiceClient.folderService

    @Provides
    @Singleton
    fun provideSendsService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): SendsService = universalpasswordmanagerServiceClient.sendsService

    @Provides
    @Singleton
    fun provideSyncService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): SyncService = universalpasswordmanagerServiceClient.syncService

    @Provides
    @Singleton
    fun provideDownloadService(
        universalpasswordmanagerServiceClient: UniversalServiceClient,
    ): DownloadService = universalpasswordmanagerServiceClient.downloadService
}
