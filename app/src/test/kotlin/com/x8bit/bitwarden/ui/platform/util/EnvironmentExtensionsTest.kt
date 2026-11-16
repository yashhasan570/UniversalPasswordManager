package com.x8bit.universalpasswordmanager.ui.platform.util

import .data.repository.model.Environment
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EnvironmentExtensionsTest {
    @Test
    fun `displayLabel for US type should return the correct value`() {
        assertEquals(
            "".asText(),
            Environment.Type.US.displayLabel,
        )
    }

    @Test
    fun `displayLabel for EU type should return the correct value`() {
        assertEquals(
            "".asText(),
            Environment.Type.EU.displayLabel,
        )
    }

    @Test
    fun `displayLabel for FED_RAMP type should return the correct value`() {
        assertEquals(
            "".asText(),
            Environment.Type.FED_RAMP.displayLabel,
        )
    }

    @Test
    fun `displayLabel for SELF_HOSTED type should return the correct value`() {
        assertEquals(
            Universal Password ManagerString.self_hosted.asText(),
            Environment.Type.SELF_HOSTED.displayLabel,
        )
    }
}
