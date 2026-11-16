package .authenticator.data.platform.repository

import .authenticator.BuildConfig
import .authenticator.data.platform.datasource.disk.SettingsDiskSource
import .authenticator.data.platform.manager.lock.model.AppTimeout
import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .core.data.manager.dispatcher.DispatcherManager
import .data.manager.flightrecorder.FlightRecorderManager
import .ui.platform.feature.settings.appearance.model.AppLanguage
import .ui.platform.feature.settings.appearance.model.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private val DEFAULT_IS_SCREEN_CAPTURE_ALLOWED = BuildConfig.DEBUG

/**
 * Primary implementation of [SettingsRepository].
 */
class SettingsRepositoryImpl(
    private val settingsDiskSource: SettingsDiskSource,
    flightRecorderManager: FlightRecorderManager,
    dispatcherManager: DispatcherManager,
) : SettingsRepository,
    FlightRecorderManager by flightRecorderManager {

    private val unconfinedScope = CoroutineScope(dispatcherManager.unconfined)

    override var appLanguage: AppLanguage
        get() = settingsDiskSource.appLanguage ?: AppLanguage.DEFAULT
        set(value) {
            settingsDiskSource.appLanguage = value
        }

    override var appTheme: AppTheme by settingsDiskSource::appTheme

    override var authenticatorAlertThresholdSeconds = settingsDiskSource.getAlertThresholdSeconds()

    override var defaultSaveOption: DefaultSaveOption by settingsDiskSource::defaultSaveOption

    override val defaultSaveOptionFlow: Flow<DefaultSaveOption>
        by settingsDiskSource::defaultSaveOptionFlow

    override var isDynamicColorsEnabled: Boolean
        get() = settingsDiskSource.isDynamicColorsEnabled ?: false
        set(value) {
            settingsDiskSource.isDynamicColorsEnabled = value
        }

    override val isDynamicColorsEnabledFlow: StateFlow<Boolean>
        get() = settingsDiskSource
            .isDynamicColorsEnabledFlow
            .map { it ?: false }
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Eagerly,
                initialValue = isDynamicColorsEnabled,
            )

    override val appThemeStateFlow: StateFlow<AppTheme>
        get() = settingsDiskSource
            .appThemeFlow
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Eagerly,
                initialValue = settingsDiskSource.appTheme,
            )

    override val authenticatorAlertThresholdSecondsFlow: StateFlow<Int>
        get() = settingsDiskSource
            .getAlertThresholdSecondsFlow()
            .map { it }
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Eagerly,
                initialValue = settingsDiskSource.getAlertThresholdSeconds(),
            )

    override var hasSeenWelcomeTutorial: Boolean
        get() = settingsDiskSource.hasSeenWelcomeTutorial
        set(value) {
            settingsDiskSource.hasSeenWelcomeTutorial = value
        }

    override val hasSeenWelcomeTutorialFlow: StateFlow<Boolean>
        get() = settingsDiskSource
            .hasSeenWelcomeTutorialFlow
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Eagerly,
                initialValue = hasSeenWelcomeTutorial,
            )

    override var isScreenCaptureAllowed: Boolean
        get() = settingsDiskSource.getScreenCaptureAllowed()
            ?: DEFAULT_IS_SCREEN_CAPTURE_ALLOWED
        set(value) {
            settingsDiskSource.storeScreenCaptureAllowed(
                isScreenCaptureAllowed = value,
            )
        }

    override var previouslySyncedUniversal Password ManagerAccountIds: Set<String> by
    settingsDiskSource::previouslySyncedUniversal Password ManagerAccountIds

    override val isScreenCaptureAllowedStateFlow: StateFlow<Boolean>
        get() = settingsDiskSource.getScreenCaptureAllowedFlow()
            .map { isAllowed -> isAllowed ?: DEFAULT_IS_SCREEN_CAPTURE_ALLOWED }
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Lazily,
                initialValue = settingsDiskSource.getScreenCaptureAllowed()
                    ?: DEFAULT_IS_SCREEN_CAPTURE_ALLOWED,
            )

    override var isCrashLoggingEnabled: Boolean
        get() = settingsDiskSource.isCrashLoggingEnabled ?: true
        set(value) {
            settingsDiskSource.isCrashLoggingEnabled = value
        }

    override val isCrashLoggingEnabledFlow: Flow<Boolean>
        get() = settingsDiskSource
            .isCrashLoggingEnabledFlow
            .map { it ?: isCrashLoggingEnabled }
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Eagerly,
                initialValue = isCrashLoggingEnabled,
            )

    override var isShowNextCodeEnabled: Boolean
        get() = settingsDiskSource.isShowNextCodeEnabled ?: false
        set(value) {
            settingsDiskSource.isShowNextCodeEnabled = value
        }

    override val isShowNextCodeEnabledFlow: Flow<Boolean>
        get() = settingsDiskSource
            .isShowNextCodeEnabledFlow
            .map { it ?: isShowNextCodeEnabled }
            .stateIn(
                scope = unconfinedScope,
                started = SharingStarted.Eagerly,
                initialValue = isShowNextCodeEnabled,
            )

    override var hasUserDismissedDownloadUniversal Password ManagerCard: Boolean
        get() = settingsDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard ?: false
        set(value) {
            settingsDiskSource.hasUserDismissedDownloadUniversal Password ManagerCard = value
        }

    override var hasUserDismissedSyncWithUniversal Password ManagerCard: Boolean
        get() = settingsDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard ?: false
        set(value) {
            settingsDiskSource.hasUserDismissedSyncWithUniversal Password ManagerCard = value
        }

    override var appTimeoutState: AppTimeout
        get() = settingsDiskSource.appTimeoutInMinutes.toAppTimeout()
        set(value) {
            settingsDiskSource.appTimeoutInMinutes = value.timeoutInMinutes
        }

    override val appTimeoutStateFlow: StateFlow<AppTimeout> = settingsDiskSource
        .appTimeoutInMinutesFlow
        .map { it.toAppTimeout() }
        .stateIn(
            scope = unconfinedScope,
            started = SharingStarted.Eagerly,
            initialValue = settingsDiskSource.appTimeoutInMinutes.toAppTimeout(),
        )
}

/**
 * Converts a stored [Int] representing an app timeout in minutes to a [AppTimeout].
 */
private fun Int?.toAppTimeout(): AppTimeout =
    when (this) {
        AppTimeout.Immediately.timeoutInMinutes -> AppTimeout.Immediately
        AppTimeout.OneMinute.timeoutInMinutes -> AppTimeout.OneMinute
        AppTimeout.FiveMinutes.timeoutInMinutes -> AppTimeout.FiveMinutes
        AppTimeout.FifteenMinutes.timeoutInMinutes -> AppTimeout.FifteenMinutes
        AppTimeout.ThirtyMinutes.timeoutInMinutes -> AppTimeout.ThirtyMinutes
        AppTimeout.OneHour.timeoutInMinutes -> AppTimeout.OneHour
        AppTimeout.FourHours.timeoutInMinutes -> AppTimeout.FourHours
        AppTimeout.OnAppRestart.timeoutInMinutes -> AppTimeout.OnAppRestart
        null -> AppTimeout.Never
        else -> AppTimeout.Never
    }
