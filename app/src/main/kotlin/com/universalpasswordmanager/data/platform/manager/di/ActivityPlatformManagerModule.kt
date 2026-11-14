package com.universalpasswordmanager.data.platform.manager.di

import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.MainActivity
import com.universalpasswordmanager.data.auth.repository.AuthRepository
import com.universalpasswordmanager.data.platform.manager.SpecialCircumstanceManager
import com.universalpasswordmanager.data.platform.manager.SpecialCircumstanceManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

/**
 * Provides managers in the platform package that must be scoped to a retained Activity. These are
 * for dependencies that must operate independently in different application tasks that contain
 * unique [MainActivity] instances.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityPlatformManagerModule {

    @Provides
    @ActivityRetainedScoped
    fun provideActivityScopedSpecialCircumstanceRepository(
        authRepository: AuthRepository,
        dispatcher: DispatcherManager,
    ): SpecialCircumstanceManager =
        SpecialCircumstanceManagerImpl(
            authRepository = authRepository,
            dispatcherManager = dispatcher,
        )
}
