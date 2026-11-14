package com.universalpasswordmanager.data.platform.datasource.disk.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.data.datasource.disk.FlightRecorderDiskSource
import com.universalpasswordmanager.data.datasource.disk.di.EncryptedPreferences
import com.universalpasswordmanager.data.datasource.disk.di.KeystoreEncryptedPreferences
import com.universalpasswordmanager.data.datasource.disk.di.UnencryptedPreferences
import com.universalpasswordmanager.data.manager.flightrecorder.FlightRecorderManager
import com.universalpasswordmanager.data.platform.datasource.disk.CookieDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.CookieDiskSourceImpl
import com.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.EnvironmentDiskSourceImpl
import com.universalpasswordmanager.data.platform.datasource.disk.EventDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.EventDiskSourceImpl
import com.universalpasswordmanager.data.platform.datasource.disk.FeatureFlagOverrideDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.FeatureFlagOverrideDiskSourceImpl
import com.universalpasswordmanager.data.platform.datasource.disk.PushDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.PushDiskSourceImpl
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSource
import com.universalpasswordmanager.data.platform.datasource.disk.SettingsDiskSourceImpl
import com.universalpasswordmanager.data.platform.datasource.disk.dao.OrganizationEventDao
import com.universalpasswordmanager.data.platform.datasource.disk.database.PlatformDatabase
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.DiskSourceMigrationLogger
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.DiskSourceMigrationLoggerImpl
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacyAppCenterMigrator
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacyAppCenterMigratorImpl
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacySecureStorage
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacySecureStorageImpl
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacySecureStorageMigrator
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacySecureStorageMigratorImpl
import com.universalpasswordmanager.data.platform.manager.DatabaseSchemeManager
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import com.universalpasswordmanager.data.vault.datasource.disk.callback.DatabaseSchemeCallback
import com.universalpasswordmanager.data.vault.datasource.disk.convertor.InstantTypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Provides persistence-related dependencies in the platform package.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlatformDiskModule {

    @Provides
    @Singleton
    fun provideEnvironmentDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
        json: Json,
    ): EnvironmentDiskSource =
        EnvironmentDiskSourceImpl(
            sharedPreferences = sharedPreferences,
            json = json,
        )

    @Provides
    @Singleton
    fun provideEventDatabase(
        app: Application,
        databaseSchemeManager: DatabaseSchemeManager,
    ): PlatformDatabase =
        Room
            .databaseBuilder<PlatformDatabase>(context = app, name = "platform_database")
            .fallbackToDestructiveMigration(dropAllTables = false)
            .addColumnTypeConverter(typeConverter = InstantTypeConverter())
            .addCallback(DatabaseSchemeCallback(databaseSchemeManager = databaseSchemeManager))
            .setDriver(driver = AndroidSQLiteDriver())
            .build()

    @Provides
    @Singleton
    fun provideOrganizationEventDao(
        database: PlatformDatabase,
    ): OrganizationEventDao = database.organizationEventDao()

    @Provides
    @Singleton
    fun provideEventDiskSource(
        organizationEventDao: OrganizationEventDao,
        dispatcherManager: DispatcherManager,
        json: Json,
    ): EventDiskSource =
        EventDiskSourceImpl(
            organizationEventDao = organizationEventDao,
            dispatcherManager = dispatcherManager,
            json = json,
        )

    @Provides
    @Singleton
    fun provideLegacySecureStorage(
        @ApplicationContext context: Context,
    ): LegacySecureStorage =
        LegacySecureStorageImpl(
            context = context,
        )

    @Provides
    @Singleton
    fun provideLegacySecureStorageMigrator(
        legacySecureStorage: LegacySecureStorage,
        @EncryptedPreferences encryptedSharedPreferences: SharedPreferences,
    ): LegacySecureStorageMigrator =
        LegacySecureStorageMigratorImpl(
            legacySecureStorage = legacySecureStorage,
            encryptedSharedPreferences = encryptedSharedPreferences,
        )

    @Provides
    @Singleton
    fun provideDiskSourceMigrationLogger(
        flightRecorderManager: FlightRecorderManager,
        @EncryptedPreferences encryptedPreferences: SharedPreferences,
        @KeystoreEncryptedPreferences keystoreEncryptedPreferences: SharedPreferences,
        dispatcherManager: DispatcherManager,
    ): DiskSourceMigrationLogger = DiskSourceMigrationLoggerImpl(
        flightRecorderManager = flightRecorderManager,
        encryptedPreferences = encryptedPreferences,
        keystoreEncryptedPreferences = keystoreEncryptedPreferences,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideLegacyAppCenterMigrator(
        application: Application,
        settingsRepository: SettingsRepository,
    ): LegacyAppCenterMigrator =
        LegacyAppCenterMigratorImpl(
            settingsRepository = settingsRepository,
            appCenterPreferences = application.getSharedPreferences(
                "AppCenter",
                Context.MODE_PRIVATE,
            ),
        )

    @Provides
    @Singleton
    fun providePushDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
    ): PushDiskSource =
        PushDiskSourceImpl(
            sharedPreferences = sharedPreferences,
        )

    @Provides
    @Singleton
    fun provideSettingsDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
        json: Json,
        flightRecorderDiskSource: FlightRecorderDiskSource,
    ): SettingsDiskSource =
        SettingsDiskSourceImpl(
            sharedPreferences = sharedPreferences,
            json = json,
            flightRecorderDiskSource = flightRecorderDiskSource,
        )

    @Provides
    @Singleton
    fun provideFeatureFlagOverrideDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
    ): FeatureFlagOverrideDiskSource = FeatureFlagOverrideDiskSourceImpl(
        sharedPreferences = sharedPreferences,
    )

    @Provides
    @Singleton
    fun provideCookieDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
        @KeystoreEncryptedPreferences keystoreEncryptedPreferences: SharedPreferences,
        @EncryptedPreferences encryptedSharedPreferences: SharedPreferences,
        json: Json,
    ): CookieDiskSource = CookieDiskSourceImpl(
        sharedPreferences = sharedPreferences,
        keystoreEncryptedPreferences = keystoreEncryptedPreferences,
        encryptedSharedPreferences = encryptedSharedPreferences,
        json = json,
    )
}
