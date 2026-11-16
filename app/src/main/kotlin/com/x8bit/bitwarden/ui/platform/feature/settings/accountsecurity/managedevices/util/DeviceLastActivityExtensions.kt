package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Returns a localized string describing how recently this device was active,
 * or null if no activity date is available.
 *
 * Buckets are based on calendar days in the device's local timezone, matching
 * the web client behaviour. Using [java.time.LocalDate] comparison makes this DST-safe without
 * requiring rounding (unlike the JavaScript equivalent).
 */
@Suppress("MagicNumber")
fun Instant.toLastActivityLabel(clock: Clock): Text {
    val nowDate = clock.instant().atZone(clock.zone).toLocalDate()
    val activityDate = this.atZone(clock.zone).toLocalDate()
    val daysAgo = ChronoUnit.DAYS.between(activityDate, nowDate)
    val resId = when {
        daysAgo <= 0 -> Universal Password ManagerString.today
        daysAgo < 7 -> Universal Password ManagerString.past_seven_days
        daysAgo < 14 -> Universal Password ManagerString.past_fourteen_days
        daysAgo < 30 -> Universal Password ManagerString.past_thirty_days
        else -> Universal Password ManagerString.over_thirty_days_ago
    }
    return resId.asText()
}
