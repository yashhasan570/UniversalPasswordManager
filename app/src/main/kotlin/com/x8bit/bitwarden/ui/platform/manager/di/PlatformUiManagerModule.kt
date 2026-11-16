package com.x8bit.universalpasswordmanager.ui.platform.manager.di

import android.content.Context
import .core.data.manager.BuildInfoManager
import .core.data.manager.dispatcher.DispatcherManager
import .ui.platform.manager.share.ShareManager
import .ui.platform.manager.share.ShareManagerImpl
import .ui.platform.manager.snackbar.SnackbarRelayManager
import .ui.platform.manager.snackbar.SnackbarRelayManagerImpl
import com.x8bit.universalpasswordmanager.ui.platform.manager.Universal Password ManagerBuildInfoManagerImpl
import com.x8bit.universalpasswordmanager.ui.platform.manager.resource.ResourceManager
import com.x8bit.universalpasswordmanager.ui.platform.manager.resource.ResourceManagerImpl
import com.x8bit.universalpasswordmanager.ui.platform.model.SnackbarRelay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides UI-based managers in the platform package.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlatformUiManagerModule {

    @Provides
    @Singleton
    fun provideBuildInfoManager(): BuildInfoManager = Universal Password ManagerBuildInfoManagerImpl()

    @Provides
    @Singleton
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager =
        ResourceManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideSnackbarRelayManager(
        dispatcherManager: DispatcherManager,
    ): SnackbarRelayManager<SnackbarRelay> = SnackbarRelayManagerImpl(
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideShareManager(
        @ApplicationContext context: Context,
        buildInfoManager: BuildInfoManager,
    ): ShareManager = ShareManagerImpl(
        context = context,
        buildInfoManager = buildInfoManager,
    )
}
