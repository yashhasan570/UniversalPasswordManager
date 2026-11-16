package com.x8bit.universalpasswordmanager.data.vault.datasource.network.di

import com.work.Universal Password ManagerServiceClient
import com.work.service.CiphersService
import com.work.service.DownloadService
import com.work.service.FolderService
import com.work.service.SendsService
import com.work.service.SyncService
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
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): CiphersService = universalpasswordmanagerServiceClient.ciphersService

    @Provides
    @Singleton
    fun providesFolderService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): FolderService = universalpasswordmanagerServiceClient.folderService

    @Provides
    @Singleton
    fun provideSendsService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): SendsService = universalpasswordmanagerServiceClient.sendsService

    @Provides
    @Singleton
    fun provideSyncService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): SyncService = universalpasswordmanagerServiceClient.syncService

    @Provides
    @Singleton
    fun provideDownloadService(
        universalpasswordmanagerServiceClient: Universal Password ManagerServiceClient,
    ): DownloadService = universalpasswordmanagerServiceClient.downloadService
}
