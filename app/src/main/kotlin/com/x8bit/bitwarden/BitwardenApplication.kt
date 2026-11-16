package com.x8bit.universalpasswordmanager

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import .annotation.OmitFromCoverage
import .data.manager.flightrecorder.FlightRecorderManager
import com.x8bit.universalpasswordmanager.data.auth.manager.AuthRequestNotificationManager
import com.x8bit.universalpasswordmanager.data.autofill.manager.FillAssistManager
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.legacy.DiskSourceMigrationLogger
import com.x8bit.universalpasswordmanager.data.platform.manager.LogsManager
import com.x8bit.universalpasswordmanager.data.platform.manager.event.OrganizationEventManager
import com.x8bit.universalpasswordmanager.data.platform.manager.network.NetworkConfigManager
import com.x8bit.universalpasswordmanager.data.platform.manager.restriction.RestrictionManager
import com.x8bit.universalpasswordmanager.data.platform.repository.EnvironmentRepository
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Custom application class.
 */
@OmitFromCoverage
@HiltAndroidApp
class Universal Password ManagerApplication : Application(), SingletonImageLoader.Factory {
    // Inject classes here that must be triggered on startup but are not otherwise consumed by
    // other callers.
    @Inject
    lateinit var flightRecorder: FlightRecorderManager

    @Inject
    lateinit var logsManager: LogsManager

    @Inject
    lateinit var diskSourceMigrationLogger: DiskSourceMigrationLogger

    @Inject
    lateinit var fillAssistManager: FillAssistManager

    @Inject
    lateinit var networkConfigManager: NetworkConfigManager

    @Inject
    lateinit var authRequestNotificationManager: AuthRequestNotificationManager

    @Inject
    lateinit var organizationEventManager: OrganizationEventManager

    @Inject
    lateinit var restrictionManager: RestrictionManager

    @Inject
    lateinit var environmentRepository: EnvironmentRepository

    @Inject
    lateinit var imageLoader: Lazy<ImageLoader>

    override fun onCreate() {
        super.onCreate()
        // These must be initialized in order to ensure that the restrictionManager does not
        // override the environmentRepository values.
        restrictionManager.initialize()
        environmentRepository.initialize()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Timber.w("onLowMemory")
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()
}
