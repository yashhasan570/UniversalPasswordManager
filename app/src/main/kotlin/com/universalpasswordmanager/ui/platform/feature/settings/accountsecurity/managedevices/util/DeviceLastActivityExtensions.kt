package com.universalpasswordmanager.ui.platform.feature.settings.accountsecurity.managedevices.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText
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
        daysAgo <= 0 -> UniversalString.today
        daysAgo < 7 -> UniversalString.past_seven_days
        daysAgo < 14 -> UniversalString.past_fourteen_days
        daysAgo < 30 -> UniversalString.past_thirty_days
        else -> UniversalString.over_thirty_days_ago
    }
    return resId.asText()
}
