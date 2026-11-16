package com.x8bit.universalpasswordmanager.data.platform.manager

import .data.repository.model.Environment
import com.x8bit.universalpasswordmanager.BuildConfig
import com.x8bit.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacyAppCenterMigrator
import com.x8bit.universalpasswordmanager.data.platform.repository.SettingsRepository
import timber.log.Timber

/**
 * [LogsManager] implementation for F-droid flavor builds.
 */
class LogsManagerImpl(
    settingsRepository: SettingsRepository,
    legacyAppCenterMigrator: LegacyAppCenterMigrator,
) : LogsManager {
    init {
        if (BuildConfig.HAS_LOGS_ENABLED) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override var isEnabled: Boolean = false

    override fun setUserData(userId: String?, environmentType: Environment.Type) = Unit

    override fun trackNonFatalException(throwable: Throwable) = Unit
}
