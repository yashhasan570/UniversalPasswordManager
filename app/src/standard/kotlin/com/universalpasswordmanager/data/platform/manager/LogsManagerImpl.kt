package com.universalpasswordmanager.data.platform.manager

import com.universalpasswordmanager.data.repository.model.Environment
import com.universalpasswordmanager.BuildConfig
import com.universalpasswordmanager.data.platform.datasource.disk.legacy.LegacyAppCenterMigrator
import com.universalpasswordmanager.data.platform.repository.SettingsRepository
import timber.log.Timber

/**
 * [LogsManager] implementation for standard flavor builds (no crash logging).
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
