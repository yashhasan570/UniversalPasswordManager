package com.universalpasswordmanager.ui.vault.feature.vault.util

import com.universalpasswordmanager.core.data.util.toFormattedDateStyle
import com.universalpasswordmanager.core.data.util.toFormattedTimeStyle
import com.universalpasswordmanager.data.datasource.disk.model.FlightRecorderDataSet
import com.universalpasswordmanager.ui.platform.components.snackbar.model.UniversalSnackbarData
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import java.time.Clock
import java.time.Instant
import java.time.format.FormatStyle

/**
 * Helper function to create a [UniversalSnackbarData] representing the active flight recorder.
 */
fun FlightRecorderDataSet.toSnackbarData(
    clock: Clock,
): UniversalSnackbarData? {
    val expirationTime = this
        .data
        .find { it.isActive && !it.isBannerDismissed }
        ?.let { Instant.ofEpochMilli(it.startTimeMs + it.durationMs) }
        ?: return null
    return UniversalSnackbarData(
        message = UniversalString.flight_recorder_banner_message.asText(
            expirationTime.toFormattedDateStyle(dateStyle = FormatStyle.SHORT, clock = clock),
            expirationTime.toFormattedTimeStyle(timeStyle = FormatStyle.SHORT, clock = clock),
        ),
        messageHeader = UniversalString.flight_recorder_banner_title.asText(),
        actionLabel = UniversalString.go_to_settings.asText(),
        withDismissAction = true,
    )
}
