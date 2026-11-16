package com.x8bit.universalpasswordmanager.ui.platform.util

import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import com.x8bit.universalpasswordmanager.data.platform.repository.model.VaultTimeoutAction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultTimeoutActionExtensionsTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            VaultTimeoutAction.LOCK to Universal Password ManagerString.lock.asText(),
            VaultTimeoutAction.LOGOUT to Universal Password ManagerString.log_out.asText(),
        )
            .forEach { (type, label) ->
                assertEquals(
                    label,
                    type.displayLabel,
                )
            }
    }
}
