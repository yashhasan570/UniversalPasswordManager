package com.universalpasswordmanager.data.platform.manager

import com.universalpasswordmanager.MainActivity
import com.universalpasswordmanager.data.platform.manager.model.SpecialCircumstance
import kotlinx.coroutines.flow.StateFlow

/**
 * Tracks any [SpecialCircumstance] that may be present.
 *
 * Note that this will be scoped to the current "retained Activity": if there are multiple tasks
 * that each have a [MainActivity], they can each have a separate [SpecialCircumstance] associated
 * with them.
 */
interface SpecialCircumstanceManager {
    /**
     * Gets the current [SpecialCircumstance] if any.
     */
    var specialCircumstance: SpecialCircumstance?

    /**
     * Emits updates that track changes to [specialCircumstance].
     */
    val specialCircumstanceStateFlow: StateFlow<SpecialCircumstance?>
}
