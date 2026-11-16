package com.x8bit.universalpasswordmanager.ui.vault.feature.vault.util

import .core.data.util.toFormattedDateStyle
import .core.data.util.toFormattedTimeStyle
import .data.datasource.disk.model.FlightRecorderDataSet
import .ui.platform.components.snackbar.model.Universal Password ManagerSnackbarData
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import java.time.Clock
import java.time.Instant
import java.time.format.FormatStyle

/**
 * Helper function to create a [Universal Password ManagerSnackbarData] representing the active flight recorder.
 */
fun FlightRecorderDataSet.toSnackbarData(
    clock: Clock,
): Universal Password ManagerSnackbarData? {
    val expirationTime = this
        .data
        .find { it.isActive && !it.isBannerDismissed }
        ?.let { Instant.ofEpochMilli(it.startTimeMs + it.durationMs) }
        ?: return null
    return Universal Password ManagerSnackbarData(
        message = Universal Password ManagerString.flight_recorder_banner_message.asText(
            expirationTime.toFormattedDateStyle(dateStyle = FormatStyle.SHORT, clock = clock),
            expirationTime.toFormattedTimeStyle(timeStyle = FormatStyle.SHORT, clock = clock),
        ),
        messageHeader = Universal Password ManagerString.flight_recorder_banner_title.asText(),
        actionLabel = Universal Password ManagerString.go_to_settings.asText(),
        withDismissAction = true,
    )
}
