package .authenticator.ui.platform.util

import .authenticator.ui.platform.feature.settings.data.model.DefaultSaveOption
import .ui.platform.resource.Universal Password ManagerString
import .ui.util.asText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DefaultSaveOptionExtensionsTest {

    @Test
    fun `displayLabel should map to correct labels`() {
        DefaultSaveOption.entries.forEach {
            val expected = when (it) {
                DefaultSaveOption.UNIVERSAL PASSWORD MANAGER_APP -> Universal Password ManagerString.save_to_universalpasswordmanager.asText()
                DefaultSaveOption.LOCAL -> Universal Password ManagerString.save_here.asText()
                DefaultSaveOption.NONE -> Universal Password ManagerString.none.asText()
            }
            assertEquals(
                expected,
                it.displayLabel,
            )
        }
    }
}
