package com.universalpasswordmanager.ui.platform.feature.settings.flightrecorder.util

import com.universalpasswordmanager.data.manager.model.FlightRecorderDuration
import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.Text
import com.universalpasswordmanager.ui.util.asText

/**
 * A helper function to map the [FlightRecorderDuration] to a displayable label.
 */
val FlightRecorderDuration.displayText: Text
    get() = when (this) {
        FlightRecorderDuration.ONE_HOUR -> UniversalString.flight_recorder_one_hour.asText()
        FlightRecorderDuration.EIGHT_HOURS -> UniversalString.flight_recorder_eight_hours.asText()
        FlightRecorderDuration.TWENTY_FOUR_HOURS -> {
            UniversalString.flight_recorder_twenty_four_hours.asText()
        }

        FlightRecorderDuration.ONE_WEEK -> UniversalString.flight_recorder_one_week.asText()
    }
