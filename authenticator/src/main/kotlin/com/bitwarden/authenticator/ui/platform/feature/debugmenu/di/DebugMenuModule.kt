package .authenticator.ui.platform.feature.debugmenu.di

import .authenticator.data.platform.repository.DebugMenuRepository
import .authenticator.ui.platform.feature.debugmenu.manager.DebugLaunchManagerImpl
import .authenticator.ui.platform.feature.debugmenu.manager.DebugMenuLaunchManager
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
