package com.x8bit.universalpasswordmanager.ui.platform.feature.settings.about.util

import .core.data.util.toFormattedDateStyle
import .core.data.util.toFormattedTimeStyle
import .data.datasource.disk.model.FlightRecorderDataSet
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.Text
import .ui.util.asText
import java.time.Clock
import java.time.Instant
import java.time.format.FormatStyle

/**
 * Creates a properly formatted string indicating when the logging will stop for the active log or
 * null if there is no active log.
 */
fun FlightRecorderDataSet.getStopsLoggingStringForActiveLog(
    clock: Clock = Clock.systemDefaultZone(),
): Text? = this.activeFlightRecorderData?.getStopsLoggingString(clock = clock)

/**
 * Creates a properly formatted string indicating when the logging will stop.
 */
private fun FlightRecorderDataSet.FlightRecorderData.getStopsLoggingString(
    clock: Clock,
): Text {
    val completionInstant = Instant.ofEpochMilli(this.startTimeMs + this.durationMs)
    val completionDate = completionInstant.toFormattedDateStyle(
        dateStyle = FormatStyle.SHORT,
        clock = clock,
    )
    val completionTime = completionInstant.toFormattedTimeStyle(
        timeStyle = FormatStyle.SHORT,
        clock = clock,
    )
    return Universal Password ManagerString.stops_logging_on.asText(completionDate, completionTime)
}
