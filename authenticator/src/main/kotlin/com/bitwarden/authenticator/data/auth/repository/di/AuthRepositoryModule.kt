package .authenticator.data.auth.repository.di

import .authenticator.data.auth.datasource.disk.AuthDiskSource
import .authenticator.data.auth.repository.AuthRepository
import .authenticator.data.auth.repository.AuthRepositoryImpl
import .authenticator.data.authenticator.datasource.sdk.AuthenticatorSdkSource
import .authenticator.data.platform.manager.BiometricsEncryptionManager
import .authenticator.data.platform.manager.lock.AppLockManager
import .authenticator.data.platform.repository.SettingsRepository
import .core.data.manager.dispatcher.DispatcherManager
import .core.data.manager.realtime.RealtimeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides repositories in the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    fun provideAuthRepository(
        authDiskSource: AuthDiskSource,
        settingsRepository: SettingsRepository,
        authenticatorSdkSource: AuthenticatorSdkSource,
        biometricsEncryptionManager: BiometricsEncryptionManager,
        realtimeManager: RealtimeManager,
        dispatcherManager: DispatcherManager,
        appLockManager: AppLockManager,
    ): AuthRepository = AuthRepositoryImpl(
        authDiskSource = authDiskSource,
        settingsRepository = settingsRepository,
        authenticatorSdkSource = authenticatorSdkSource,
        biometricsEncryptionManager = biometricsEncryptionManager,
        realtimeManager = realtimeManager,
        dispatcherManager = dispatcherManager,
        appLockManager = appLockManager,
    )
}
