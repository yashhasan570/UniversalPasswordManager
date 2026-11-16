package .authenticator.data.platform.manager.di

import android.content.Context
import .authenticator.data.auth.datasource.disk.AuthDiskSource
import .authenticator.data.authenticator.datasource.disk.AuthenticatorDiskSource
import .authenticator.data.platform.datasource.disk.SettingsDiskSource
import .authenticator.data.platform.manager.BiometricsEncryptionManager
import .authenticator.data.platform.manager.BiometricsEncryptionManagerImpl
import .authenticator.data.platform.manager.Universal Password ManagerEncodingManager
import .authenticator.data.platform.manager.Universal Password ManagerEncodingManagerImpl
import .authenticator.data.platform.manager.CrashLogsManager
import .authenticator.data.platform.manager.CrashLogsManagerImpl
import .authenticator.data.platform.manager.DebugMenuFeatureFlagManagerImpl
import .authenticator.data.platform.manager.FeatureFlagManager
import .authenticator.data.platform.manager.FeatureFlagManagerImpl
import .authenticator.data.platform.manager.SdkClientManager
import .authenticator.data.platform.manager.SdkClientManagerImpl
import .authenticator.data.platform.manager.clipboard.Universal Password ManagerClipboardManager
import .authenticator.data.platform.manager.clipboard.Universal Password ManagerClipboardManagerImpl
import .authenticator.data.platform.manager.imports.ImportManager
import .authenticator.data.platform.manager.imports.ImportManagerImpl
import .authenticator.data.platform.manager.lock.AppLockManager
import .authenticator.data.platform.manager.lock.AppLockManagerImpl
import .authenticator.data.platform.repository.DebugMenuRepository
import .authenticator.data.platform.repository.SettingsRepository
import .core.data.manager.UuidManager
import .core.data.manager.dispatcher.DispatcherManager
import .core.data.manager.dispatcher.DispatcherManagerImpl
import .core.data.manager.realtime.RealtimeManager
import .core.data.manager.realtime.RealtimeManagerImpl
import .core.data.manager.toast.ToastManager
import .core.data.manager.toast.ToastManagerImpl
import .data.manager.appstate.AppStateManager
import .data.repository.ServerConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides managers in the platform package.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlatformManagerModule {

    @Provides
    @Singleton
    fun provideAppLockManager(
        appStateManager: AppStateManager,
        realtimeManager: RealtimeManager,
        settingsRepository: SettingsRepository,
        authDiskSource: AuthDiskSource,
        settingsDiskSource: SettingsDiskSource,
        dispatcherManager: DispatcherManager,
        @ApplicationContext context: Context,
    ): AppLockManager = AppLockManagerImpl(
        appStateManager = appStateManager,
        realtimeManager = realtimeManager,
        settingsRepository = settingsRepository,
        authDiskSource = authDiskSource,
        settingsDiskSource = settingsDiskSource,
        dispatcherManager = dispatcherManager,
        context = context,
    )

    @Provides
    @Singleton
    fun provideUniversal Password ManagerClipboardManager(
        @ApplicationContext context: Context,
        toastManager: ToastManager,
    ): Universal Password ManagerClipboardManager = Universal Password ManagerClipboardManagerImpl(
        context = context,
        toastManager = toastManager,
    )

    @Provides
    @Singleton
    fun provideRealtimeManager(): RealtimeManager = RealtimeManagerImpl()

    @Provides
    @Singleton
    fun provideToastManager(
        @ApplicationContext context: Context,
    ): ToastManager = ToastManagerImpl(
        context = context,
    )

    @Provides
    @Singleton
    fun provideUniversal Password ManagerDispatchers(): DispatcherManager = DispatcherManagerImpl()

    @Provides
    @Singleton
    fun provideSdkClientManager(): SdkClientManager = SdkClientManagerImpl()

    @Provides
    @Singleton
    fun provideBiometricsEncryptionManager(
        authDiskSource: AuthDiskSource,
        settingsDiskSource: SettingsDiskSource,
    ): BiometricsEncryptionManager = BiometricsEncryptionManagerImpl(
        authDiskSource = authDiskSource,
        settingsDiskSource = settingsDiskSource,
    )

    @Provides
    @Singleton
    fun provideCrashLogsManager(settingsRepository: SettingsRepository): CrashLogsManager =
        CrashLogsManagerImpl(
            settingsRepository = settingsRepository,
        )

    @Provides
    @Singleton
    fun provideImportManager(
        authenticatorDiskSource: AuthenticatorDiskSource,
        uuidManager: UuidManager,
    ): ImportManager = ImportManagerImpl(
        authenticatorDiskSource = authenticatorDiskSource,
        uuidManager = uuidManager,
    )

    @Provides
    @Singleton
    fun provideEncodingManager(): Universal Password ManagerEncodingManager = Universal Password ManagerEncodingManagerImpl()

    @Provides
    @Singleton
    fun providesFeatureFlagManager(
        debugMenuRepository: DebugMenuRepository,
        serverConfigRepository: ServerConfigRepository,
    ): FeatureFlagManager = if (debugMenuRepository.isDebugMenuEnabled) {
        DebugMenuFeatureFlagManagerImpl(
            debugMenuRepository = debugMenuRepository,
            defaultFeatureFlagManager = FeatureFlagManagerImpl(
                serverConfigRepository = serverConfigRepository,
            ),
        )
    } else {
        FeatureFlagManagerImpl(
            serverConfigRepository = serverConfigRepository,
        )
    }
}
