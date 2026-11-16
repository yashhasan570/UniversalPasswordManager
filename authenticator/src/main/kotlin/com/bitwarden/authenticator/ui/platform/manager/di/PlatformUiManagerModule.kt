package .authenticator.ui.platform.manager.di

import .authenticator.ui.platform.manager.AuthenticatorBuildInfoManagerImpl
import .authenticator.ui.platform.model.SnackbarRelay
import .core.data.manager.BuildInfoManager
import .core.data.manager.dispatcher.DispatcherManager
import .ui.platform.manager.snackbar.SnackbarRelayManager
import .ui.platform.manager.snackbar.SnackbarRelayManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides UI-based managers in the platform package.
 */
@Module
@InstallIn(SingletonComponent::class)
class PlatformUiManagerModule {
    @Provides
    @Singleton
    fun provideBuildInfoManager(): BuildInfoManager = AuthenticatorBuildInfoManagerImpl()

    @Provides
    @Singleton
    fun provideSnackbarRelayManager(
        dispatcherManager: DispatcherManager,
    ): SnackbarRelayManager<SnackbarRelay> = SnackbarRelayManagerImpl(
        dispatcherManager = dispatcherManager,
    )
}
