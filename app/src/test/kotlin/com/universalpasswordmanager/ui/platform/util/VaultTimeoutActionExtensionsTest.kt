package com.universalpasswordmanager.ui.platform.util

import com.universalpasswordmanager.ui.platform.resource.UniversalString
import com.universalpasswordmanager.ui.util.asText
import com.universalpasswordmanager.data.platform.repository.model.VaultTimeoutAction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VaultTimeoutActionExtensionsTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            VaultTimeoutAction.LOCK to UniversalString.lock.asText(),
            VaultTimeoutAction.LOGOUT to UniversalString.log_out.asText(),
        )
            .forEach { (type, label) ->
                assertEquals(
                    label,
                    type.displayLabel,
                )
            }
    }
}
