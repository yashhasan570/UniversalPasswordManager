package com.universalpasswordmanager.data.billing.di

import android.content.Context
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.network.service.BillingService
import com.universalpasswordmanager.data.auth.datasource.disk.AuthDiskSource
import com.universalpasswordmanager.data.billing.manager.PlayBillingManager
import com.universalpasswordmanager.data.billing.manager.PlayBillingManagerImpl
import com.universalpasswordmanager.data.billing.manager.PremiumStateManager
import com.universalpasswordmanager.data.billing.manager.PremiumStateManagerImpl
import com.universalpasswordmanager.data.billing.repository.BillingRepository
import com.universalpasswordmanager.data.billing.repository.BillingRepositoryImpl
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.universalpasswordmanager.data.platform.manager.FeatureFlagManager
import com.universalpasswordmanager.data.platform.manager.PushManager
import com.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import com.universalpasswordmanager.data.vault.repository.VaultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides billing-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object BillingModule {

    @Provides
    @Singleton
    fun providePlayBillingManager(
        @ApplicationContext context: Context,
        dispatcherManager: DispatcherManager,
    ): PlayBillingManager = PlayBillingManagerImpl(
        context = context,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideBillingRepository(
        playBillingManager: PlayBillingManager,
        billingService: BillingService,
    ): BillingRepository = BillingRepositoryImpl(
        playBillingManager = playBillingManager,
        billingService = billingService,
    )

    @Provides
    @Singleton
    fun providePremiumStateManager(
        authDiskSource: AuthDiskSource,
        billingRepository: BillingRepository,
        settingsDiskSource: SettingsDiskSource,
        vaultRepository: VaultRepository,
        featureFlagManager: FeatureFlagManager,
        environmentRepository: EnvironmentRepository,
        pushManager: PushManager,
        clock: Clock,
        dispatcherManager: DispatcherManager,
    ): PremiumStateManager = PremiumStateManagerImpl(
        authDiskSource = authDiskSource,
        billingRepository = billingRepository,
        settingsDiskSource = settingsDiskSource,
        vaultRepository = vaultRepository,
        featureFlagManager = featureFlagManager,
        environmentRepository = environmentRepository,
        pushManager = pushManager,
        clock = clock,
        dispatcherManager = dispatcherManager,
    )
}
