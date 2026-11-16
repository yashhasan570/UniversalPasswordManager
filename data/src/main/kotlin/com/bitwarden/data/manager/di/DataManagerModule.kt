package .data.manager.di

import android.app.Application
import android.content.Context
import .core.data.manager.BuildInfoManager
import .core.data.manager.UuidManager
import .core.data.manager.dispatcher.DispatcherManager
import .data.datasource.disk.ConfigDiskSource
import .data.datasource.disk.FlightRecorderDiskSource
import .data.manager.Universal Password ManagerPackageManager
import .data.manager.Universal Password ManagerPackageManagerImpl
import .data.manager.NativeLibraryManager
import .data.manager.NativeLibraryManagerImpl
import .data.manager.appstate.AppStateManager
import .data.manager.appstate.AppStateManagerImpl
import .data.manager.file.FileManager
import .data.manager.file.FileManagerImpl
import .data.manager.flightrecorder.FlightRecorderManager
import .data.manager.flightrecorder.FlightRecorderManagerImpl
import .data.manager.flightrecorder.FlightRecorderWriter
import .data.manager.flightrecorder.FlightRecorderWriterImpl
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
    fun provideUniversal Password ManagerPackageManager(
        @ApplicationContext context: Context,
    ): Universal Password ManagerPackageManager = Universal Password ManagerPackageManagerImpl(context = context)

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
