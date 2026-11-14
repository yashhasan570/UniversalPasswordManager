package com.universalpasswordmanager.data.datasource.disk

import com.universalpasswordmanager.data.datasource.disk.model.FlightRecorderDataSet
import kotlinx.coroutines.flow.Flow

/**
 * Primary access point for flight recorder related disk information.
 */
interface FlightRecorderDiskSource {
    /**
     * The current status of whether the flight recorder is enabled.
     */
    var flightRecorderData: FlightRecorderDataSet?

    /**
     * Emits updates that track [flightRecorderData].
     */
    val flightRecorderDataFlow: Flow<FlightRecorderDataSet?>
}
