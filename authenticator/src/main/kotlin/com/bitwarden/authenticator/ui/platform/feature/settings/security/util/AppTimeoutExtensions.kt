package .authenticator.ui.platform.feature.settings.security.util

import .authenticator.data.platform.manager.lock.model.AppTimeout
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText

/**
 * Provides a human-readable display label for the given [AppTimeout.Type].
 */
val AppTimeout.Type.displayLabel: Text
    get() = when (this) {
        AppTimeout.Type.IMMEDIATELY -> Universal Password ManagerString.immediately
        AppTimeout.Type.ONE_MINUTE -> Universal Password ManagerString.one_minute
        AppTimeout.Type.FIVE_MINUTES -> Universal Password ManagerString.five_minutes
        AppTimeout.Type.FIFTEEN_MINUTES -> Universal Password ManagerString.fifteen_minutes
        AppTimeout.Type.THIRTY_MINUTES -> Universal Password ManagerString.thirty_minutes
        AppTimeout.Type.ONE_HOUR -> Universal Password ManagerString.one_hour
        AppTimeout.Type.FOUR_HOURS -> Universal Password ManagerString.four_hours
        AppTimeout.Type.ON_APP_RESTART -> Universal Password ManagerString.on_restart
        AppTimeout.Type.NEVER -> Universal Password ManagerString.never
    }
        .asText()
