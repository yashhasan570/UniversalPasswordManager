package com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.di

import .core.data.manager.dispatcher.DispatcherManager
import com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.AuthSdkSource
import com.x8bit.universalpasswordmanager.data.auth.datasource.sdk.AuthSdkSourceImpl
import com.x8bit.universalpasswordmanager.data.platform.manager.SdkClientManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides SDK-related dependencies for the auth package.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthSdkModule {

    @Provides
    @Singleton
    fun provideAuthSdkSource(
        dispatcherManager: DispatcherManager,
        sdkClientManager: SdkClientManager,
    ): AuthSdkSource = AuthSdkSourceImpl(
        dispatcherManager = dispatcherManager,
        sdkClientManager = sdkClientManager,
    )
}
