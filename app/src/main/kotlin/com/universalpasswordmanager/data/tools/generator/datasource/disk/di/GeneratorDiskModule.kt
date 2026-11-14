package com.universalpasswordmanager.data.tools.generator.datasource.disk.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.universalpasswordmanager.data.datasource.disk.di.UnencryptedPreferences
import com.universalpasswordmanager.data.platform.manager.DatabaseSchemeManager
import com.universalpasswordmanager.data.tools.generator.datasource.disk.GeneratorDiskSource
import com.universalpasswordmanager.data.tools.generator.datasource.disk.GeneratorDiskSourceImpl
import com.universalpasswordmanager.data.tools.generator.datasource.disk.PasswordHistoryDiskSource
import com.universalpasswordmanager.data.tools.generator.datasource.disk.PasswordHistoryDiskSourceImpl
import com.universalpasswordmanager.data.tools.generator.datasource.disk.dao.PasswordHistoryDao
import com.universalpasswordmanager.data.tools.generator.datasource.disk.database.PasswordHistoryDatabase
import com.universalpasswordmanager.data.vault.datasource.disk.callback.DatabaseSchemeCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Provides persistence-related dependencies for the generator package.
 */
@Module
@InstallIn(SingletonComponent::class)
object GeneratorDiskModule {

    @Provides
    @Singleton
    fun provideGeneratorDiskSource(
        @UnencryptedPreferences sharedPreferences: SharedPreferences,
        json: Json,
    ): GeneratorDiskSource =
        GeneratorDiskSourceImpl(
            sharedPreferences = sharedPreferences,
            json = json,
        )

    @Provides
    @Singleton
    fun providePasswordHistoryDiskSource(
        passwordHistoryDao: PasswordHistoryDao,
    ): PasswordHistoryDiskSource = PasswordHistoryDiskSourceImpl(
        passwordHistoryDao = passwordHistoryDao,
    )

    @Provides
    @Singleton
    fun providePasswordHistoryDatabase(
        app: Application,
        databaseSchemeManager: DatabaseSchemeManager,
    ): PasswordHistoryDatabase =
        Room
            .databaseBuilder<PasswordHistoryDatabase>(
                context = app,
                name = "passcode_history_database",
            )
            .addCallback(DatabaseSchemeCallback(databaseSchemeManager = databaseSchemeManager))
            .setDriver(driver = AndroidSQLiteDriver())
            .build()

    @Provides
    @Singleton
    fun providePasswordHistoryDao(database: PasswordHistoryDatabase): PasswordHistoryDao {
        return database.passwordHistoryDao()
    }
}
