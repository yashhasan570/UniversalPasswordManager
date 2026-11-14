package com.universalpasswordmanager.ui.platform.feature.debugmenu.di

import com.universalpasswordmanager.data.platform.repository.DebugMenuRepository
import com.universalpasswordmanager.ui.platform.feature.debugmenu.manager.DebugLaunchManagerImpl
import com.universalpasswordmanager.ui.platform.feature.debugmenu.manager.DebugMenuLaunchManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides dependencies for the debug menu.
 */
@Module
@InstallIn(SingletonComponent::class)
class DebugMenuModule {

    @Provides
    fun provideDebugMenuLaunchManager(
        debugMenuRepository: DebugMenuRepository,
    ): DebugMenuLaunchManager = DebugLaunchManagerImpl(debugMenuRepository = debugMenuRepository)
}
