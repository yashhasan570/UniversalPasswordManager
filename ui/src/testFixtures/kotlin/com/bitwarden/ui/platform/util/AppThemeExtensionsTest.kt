package .ui.platform.util

import .ui.platform.feature.settings.appearance.model.AppTheme
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppThemeExtensionsTest {
    @Test
    fun `displayLabel should return the correct value for each type`() {
        mapOf(
            AppTheme.DEFAULT to Universal Password ManagerString.default_system.asText(),
            AppTheme.DARK to Universal Password ManagerString.dark.asText(),
            AppTheme.LIGHT to Universal Password ManagerString.light.asText(),
        )
            .forEach { (type, label) ->
                assertEquals(
                    label,
                    type.displayLabel,
                )
            }
    }
}
