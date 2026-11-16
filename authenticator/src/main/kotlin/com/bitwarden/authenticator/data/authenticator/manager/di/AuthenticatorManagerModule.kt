package .authenticator.data.authenticator.manager.di

import .authenticator.data.authenticator.datasource.sdk.AuthenticatorSdkSource
import .authenticator.data.authenticator.manager.TotpCodeManager
import .authenticator.data.authenticator.manager.TotpCodeManagerImpl
import .core.data.manager.dispatcher.DispatcherManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides managers in the authenticator package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthenticatorManagerModule {

    @Provides
    @Singleton
    fun provideTotpCodeManager(
        authenticatorSdkSource: AuthenticatorSdkSource,
        clock: Clock,
        dispatcherManager: DispatcherManager,
    ): TotpCodeManager = TotpCodeManagerImpl(
        authenticatorSdkSource = authenticatorSdkSource,
        clock = clock,
        dispatcherManager = dispatcherManager,
    )
}
