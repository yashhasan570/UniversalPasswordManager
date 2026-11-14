package com.universalpasswordmanager.data.manager.di

import android.app.Application
import android.content.Context
import com.universalpasswordmanager.core.data.manager.BuildInfoManager
import com.universalpasswordmanager.core.data.manager.UuidManager
import com.universalpasswordmanager.core.data.manager.dispatcher.DispatcherManager
import com.universalpasswordmanager.data.datasource.disk.ConfigDiskSource
import com.universalpasswordmanager.data.datasource.disk.FlightRecorderDiskSource
import com.universalpasswordmanager.data.manager.UniversalPackageManager
import com.universalpasswordmanager.data.manager.UniversalPackageManagerImpl
import com.universalpasswordmanager.data.manager.NativeLibraryManager
import com.universalpasswordmanager.data.manager.NativeLibraryManagerImpl
import com.universalpasswordmanager.data.manager.appstate.AppStateManager
import com.universalpasswordmanager.data.manager.appstate.AppStateManagerImpl
import com.universalpasswordmanager.data.manager.file.FileManager
import com.universalpasswordmanager.data.manager.file.FileManagerImpl
import com.universalpasswordmanager.data.manager.flightrecorder.FlightRecorderManager
import com.universalpasswordmanager.data.manager.flightrecorder.FlightRecorderManagerImpl
import com.universalpasswordmanager.data.manager.flightrecorder.FlightRecorderWriter
import com.universalpasswordmanager.data.manager.flightrecorder.FlightRecorderWriterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Provides managers in the data module.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataManagerModule {

    @Provides
    @Singleton
    fun provideAppStateManager(
        application: Application,
    ): AppStateManager = AppStateManagerImpl(application = application)

    @Provides
    @Singleton
    fun provideUniversalPackageManager(
        @ApplicationContext context: Context,
    ): UniversalPackageManager = UniversalPackageManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideFileManager(
        @ApplicationContext context: Context,
        uuidManager: UuidManager,
        dispatcherManager: DispatcherManager,
    ): FileManager = FileManagerImpl(
        context = context,
        uuidManager = uuidManager,
        dispatcherManager = dispatcherManager,
    )

    @Provides
    @Singleton
    fun provideFlightRecorderManager(
        @ApplicationContext context: Context,
        clock: Clock,
        dispatcherManager: DispatcherManager,
        flightRecorderDiskSource: FlightRecorderDiskSource,
        flightRecorderWriter: FlightRecorderWriter,
    ): FlightRecorderManager = FlightRecorderManagerImpl(
        context = context,
        clock = clock,
        dispatcherManager = dispatcherManager,
        flightRecorderDiskSource = flightRecorderDiskSource,
        flightRecorderWriter = flightRecorderWriter,
    )

    @Provides
    @Singleton
    fun provideFlightRecorderWriter(
        clock: Clock,
        fileManager: FileManager,
        dispatcherManager: DispatcherManager,
        buildInfoManager: BuildInfoManager,
        configDiskSource: ConfigDiskSource,
    ): FlightRecorderWriter = FlightRecorderWriterImpl(
        clock = clock,
        fileManager = fileManager,
        dispatcherManager = dispatcherManager,
        buildInfoManager = buildInfoManager,
        configDiskSource = configDiskSource,
    )

    @Provides
    @Singleton
    fun provideNativeLibraryManager(): NativeLibraryManager = NativeLibraryManagerImpl()
}
