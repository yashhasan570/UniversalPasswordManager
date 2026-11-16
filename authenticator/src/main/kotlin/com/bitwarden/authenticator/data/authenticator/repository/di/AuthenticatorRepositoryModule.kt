package .authenticator.data.authenticator.repository.di

import .authenticator.data.authenticator.datasource.disk.AuthenticatorDiskSource
import .authenticator.data.authenticator.manager.TotpCodeManager
import .authenticator.data.authenticator.repository.AuthenticatorRepository
import .authenticator.data.authenticator.repository.AuthenticatorRepositoryImpl
import .authenticator.data.platform.manager.imports.ImportManager
import .authenticator.data.platform.repository.SettingsRepository
import .authenticatorbridge.manager.AuthenticatorBridgeManager
import .core.data.manager.dispatcher.DispatcherManager
import .data.manager.file.FileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides repositories in the authenticator package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthenticatorRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthenticatorRepository(
        authenticatorBridgeManager: AuthenticatorBridgeManager,
        authenticatorDiskSource: AuthenticatorDiskSource,
        dispatcherManager: DispatcherManager,
        fileManager: FileManager,
        importManager: ImportManager,
        totpCodeManager: TotpCodeManager,
        settingsRepository: SettingsRepository,
    ): AuthenticatorRepository = AuthenticatorRepositoryImpl(
        authenticatorBridgeManager = authenticatorBridgeManager,
        authenticatorDiskSource = authenticatorDiskSource,
        dispatcherManager = dispatcherManager,
        fileManager = fileManager,
        importManager = importManager,
        totpCodeManager = totpCodeManager,
        settingRepository = settingsRepository,
    )
}
